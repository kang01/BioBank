/**
 * Created by gaokangkang on 2017/5/12.
 * 出库申请详情页面
 */
(function() {
    'use strict';

    angular
        .module('bioBankApp')
        .controller('RequirementDetailController', RequirementDetailController);

    RequirementDetailController.$inject = ['$scope','$stateParams','$compile','entity','$uibModal','$timeout','Upload','toastr','DTColumnBuilder','DTOptionsBuilder','RequirementService','MasterData','SampleTypeService','SampleUserService','BioBankBlockUi','ProjectService'];

    function RequirementDetailController($scope,$stateParams,$compile,entity,$uibModal,$timeout,Upload,toastr,DTColumnBuilder,DTOptionsBuilder,RequirementService,MasterData,SampleTypeService,SampleUserService,BioBankBlockUi,ProjectService) {
        var vm = this;
        var modalInstance;
        vm.dtInstance = {};
        vm.requirement = entity;
        //判断是否需求保存
        vm.sampleflag = false;

        //批准
        vm.approvalModal = _fnApprovalModal;
        //样本库存详情
        vm.sampleDescModal = _fnSampleDescModal;
        //保存申请
        vm.saveRequirement = _fnSaveRequirement;

        vm.datePickerOpenStatus = {};
        vm.openCalendar = openCalendar; //时间

        function openCalendar (date) {
            vm.datePickerOpenStatus[date] = true;
        }
        if($stateParams.applyId){
            vm.requirement.id = $stateParams.applyId;
        }
        if($stateParams.applyCode){
            vm.requirement.applyCode = $stateParams.applyCode;
        }
        //初始化数据
        function _initData() {
            _requirementInfo();
            _fnQuerySampleType();
            _fuQueryDelegates();
            _fuQueryFrozenTubeType();
        }
        _initData();
        function _requirementInfo() {
            if(vm.requirement.startTime){
                vm.requirement.startTime = new Date(vm.requirement.startTime)
            }
            if(vm.requirement.endTime){
                vm.requirement.endTime = new Date(vm.requirement.endTime)
            }
            if(vm.requirement.recordTime){
                vm.requirement.recordTime = new Date(vm.requirement.recordTime)
            }
            if(vm.requirement.projectIds){
                vm.projectIds = _.join(vm.requirement.projectIds, ',');
            }
            setTimeout(function () {
                vm.dtOptions.withOption('data', vm.requirement.stockOutRequirement);
            },100)
        }
        //委托方查询
        function _fuQueryDelegates() {
            RequirementService.queryDelegates().success(function (data) {
                vm.delegatesOptions = data;
                if(!vm.requirement.delegateId){
                    vm.requirement.delegateId = vm.delegatesOptions[0].id;
                }
            })
        }
        vm.delegatesConfig = {
            valueField:'id',
            labelField:'delegateName',
            maxItems: 1,
            onChange:function (value) {
            }
        };
        //委托人
        SampleUserService.query({},onRecorderSuccess, onError);
        function onRecorderSuccess(data) {
            vm.recorderOptions = data;
        }
        vm.recorderConfig = {
            valueField:'id',
            labelField:'userName',
            maxItems: 1

        };
        //状态
        vm.statusOptions = [
            {id:"1101",name:"进行中"},
            {id:"1102",name:"待批准"},
            {id:"1103",name:"已批准"},
            {id:"1104",name:"已作废"}
        ];
        vm.statusConfig = {
            valueField:'id',
            labelField:'name',
            maxItems: 1,
            onChange:function (value) {
            }
        };
        if(!vm.status){
            vm.status = vm.statusOptions[0].id;
        }
        //获取项目
        ProjectService.query({},onProjectSuccess, onError);
        function onProjectSuccess(data) {
            vm.projectOptions = data;
        }
        vm.projectConfig = {
            valueField:'id',
            labelField:'projectName',
            onChange:function(value){
                vm.projectIds = _.join(value, ',');

                if(vm.sampleRequirement.sampleTypeId && vm.projectIds){
                    _fuQuerySampleClass(vm.projectIds,vm.sampleRequirement.sampleTypeId);
                }else{
                    vm.sampleClassOptions = [];
                    vm.sampleRequirement.sampleClassId = "";
                    $scope.$apply();
                }
            }
        };
        //保存申请记录
        function _fnSaveRequirement() {
            delete vm.requirement.stockOutRequirement;
            BioBankBlockUi.blockUiStart();
            RequirementService.saveRequirementInfo(vm.requirement).success(function (data) {
                BioBankBlockUi.blockUiStop();
                if(!vm.sampleflag){
                    toastr.success("保存申请记录成功！");
                }
                if(vm.sampleRequirement.id){
                    if(vm.sampleRequirement.status){
                        delete  vm.sampleRequirement.status;
                    }
                    delete  vm.sampleRequirement.samples;
                    RequirementService.saveEditSampleRequirement(vm.requirement.id,vm.sampleRequirement).success(function (data) {
                        BioBankBlockUi.blockUiStop();
                        toastr.success("保存样本需求成功！");
                        vm.sampleRequirement.requirementName = '';
                        vm.sampleRequirement.countOfSample = '';
                        _loadRequirement(vm.requirement.id);
                    }).error(function (data) {
                        BioBankBlockUi.blockUiStop();
                    })

                }else{
                    RequirementService.saveSampleRequirement(vm.requirement.id,vm.sampleRequirement).success(function (data) {
                        BioBankBlockUi.blockUiStop();
                        toastr.success("保存样本需求成功！");
                        _loadRequirement(vm.requirement.id);
                    }).error(function (data) {
                        BioBankBlockUi.blockUiStop();
                    })
                }

            }).error(function (data) {
                BioBankBlockUi.blockUiStop();
                toastr.error("保存申请记录失败！");
            })
        }
        //---------------------------样本需求--------------------------
        vm.sampleRequirement = {};
        vm.sampleTypeConfig = {
            valueField:'id',
            labelField:'sampleTypeName',
            maxItems: 1,
            onChange:function (value) {
                vm.sampleRequirement.sampleTypeId = value;
                if(vm.sampleRequirement.sampleTypeId && vm.projectIds){
                    _fuQuerySampleClass(vm.projectIds,vm.sampleRequirement.sampleTypeId);
                }

            }
        };
        //根据项目和样本类型获取样本分类
        function _fuQuerySampleClass(projectIds,sampleTypeId) {
            RequirementService.queryRequirementSampleClasses(projectIds,sampleTypeId).success(function (data) {
                vm.sampleClassOptions = data;
                if(!vm.sampleRequirement.sampleClassificationId){
                    if(vm.sampleClassOptions.length){
                        vm.sampleRequirement.sampleClassificationId = vm.sampleClassOptions[0].sampleClassificationId;
                    }
                }

            })
        }

        //获取样本类型
        function _fnQuerySampleType() {
            SampleTypeService.querySampleType().success(function (data) {
                vm.sampleTypeOptions = _.orderBy(data, ['id'], ['esc']);
                vm.sampleTypeOptions.pop();
                if(!vm.sampleRequirement.sampleTypeId){
                    vm.sampleRequirement.sampleTypeId = vm.sampleTypeOptions[0].id;
                }
            });
        }
        //样本分类
        vm.sampleClassConfig = {
            valueField:'sampleClassificationId',
            labelField:'sampleClassificationName',
            maxItems: 1,
            onChange:function (value) {
            }
        };
        //获取冻存管类型
        function _fuQueryFrozenTubeType() {
            RequirementService.queryFrozenTubeType().success(function (data) {
                vm.frozenTubeTypeOptions = data;
                if(!vm.sampleRequirement.frozenTubeTypeId){
                    vm.sampleRequirement.frozenTubeTypeId = vm.frozenTubeTypeOptions[0].id;
                }
            })
        }
        vm.frozenTubeTypeConfig = {
            valueField:'id',
            labelField:'frozenTubeTypeName',
            maxItems: 1,
            onChange:function (value) {
                // console.log( _.filter(vm.sampleTypeOptions,{'id':+value})[0].isMixed);
            }
        };
        //性别
        vm.sexOptions = MasterData.sexDict;
        if(!vm.requirement.sex){
            vm.sampleRequirement.sex = vm.sexOptions[0].type;
        }
        vm.sexConfig = {
            valueField:'type',
            labelField:'name',
            maxItems: 1
        };
        //疾病类型
        vm.diseaseTypeOptions = MasterData.diseaseType;
        if(!vm.sampleRequirement.diseaseTypeId){
            vm.sampleRequirement.diseaseTypeId = vm.diseaseTypeOptions[0].id;
        }
        vm.diseaseTypeConfig = {
            valueField:'id',
            labelField:'name',
            maxItems: 1,
            onChange:function (value) {
            }
        };

        vm.saveSampleRequirement = function (file) {
            BioBankBlockUi.blockUiStart();
            console.log(file);
            var fb = new FormData();
            // fb.append('stockOutRequirement', JSON.stringify(vm.sampleRequirement));
            fb.append('file', file);
            //是否上传附件
            if(file){
                var obj = {};
                obj.requirementName = vm.sampleRequirement.requirementName;
                RequirementService.saveSampleRequirementOfUpload(vm.requirement.id,JSON.stringify(obj),fb).success(function (data) {
                    BioBankBlockUi.blockUiStop();
                    toastr.success("保存样本需求成功！");
                }).error(function (data) {
                    BioBankBlockUi.blockUiStop();
                })
            }else{
                vm.sampleflag = true;
                _fnSaveRequirement();
            }
        };
        function _loadRequirement(id) {
            RequirementService.queryRequirementDesc(id).then(function (data) {
                vm.requirement = data;
                vm.dtOptions.withOption('data', vm.requirement.stockOutRequirement);
                vm.dtInstance.rerender();
            });
        }
        //---------------------------样本需求列表--------------------------
        vm.dtOptions = DTOptionsBuilder.newOptions()
            .withPaginationType('full_numbers')
            .withOption('createdRow', createdRow);

        vm.dtColumns = [
            DTColumnBuilder.newColumn('id').notVisible(),
            DTColumnBuilder.newColumn('countOfSample').withTitle('样本量'),
            DTColumnBuilder.newColumn('sampleTypeName').withTitle('样本类型'),
            DTColumnBuilder.newColumn('frozenTubeTypeName').withTitle('管类型'),
            DTColumnBuilder.newColumn('sex').withTitle('性别'),
            DTColumnBuilder.newColumn('diseaseTypeId').withTitle('疾病'),
            DTColumnBuilder.newColumn('samples').withTitle('指定样本编码').withClass('text-ellipsis'),
            DTColumnBuilder.newColumn('status').withTitle('状态'),
            DTColumnBuilder.newColumn("").withTitle('操作').notSortable().renderWith(actionsHtml)
        ];
        function createdRow(row, data, dataIndex) {
            var status = '';
            var diseaseType = '';
            switch (data.status){
                case '1201': status = '待核对';break;
                case '1202': status = '库存不足';break;
                case '1203': status = '库存满足';break;
            }
            switch (data.diseaseTypeId){
                case 1: diseaseType = 'AMI';break;
                case 2: diseaseType = 'PCI';break;
                case 3: diseaseType = '不详';break;
            }
            if(data.isBloodLipid){
                diseaseType += "脂质血　"
            }
            if(data.isHemolysis){
                diseaseType += "溶血　"
            }
            $('td:eq(4)', row).html(diseaseType);
            $('td:eq(6)', row).html(status);
            $compile(angular.element(row).contents())($scope);
        }
        function actionsHtml(data, type, full, meta) {
            return '<a  ng-click="vm.sampleRequirementEdit('+full.id+')">修改</a>&nbsp;' +
                    '<a ng-click="vm.sampleRequirementCheck()">核对</a>&nbsp;'+
                    '<a ng-click="vm.sampleRequirementDel()">删除</a>&nbsp;'+
                    '<a ng-click="vm.sampleRequirementDescModel()">详情</a>'
        }
        vm.sampleRequirementEdit = function (sampleRequirementId) {
            RequirementService.querySampleRequirement(sampleRequirementId).success(function (data) {
                vm.sampleRequirement = data;
                _fuQuerySampleClass(vm.projectIds,data.sampleTypeId)

            }).error(function (data) {
            })
        };

        //---------------------------弹出框--------------------------
        //批准
        function _fnApprovalModal() {
            modalInstance = $uibModal.open({
                animation: true,
                templateUrl: 'app/bizs/stock-out/requirement/modal/requirement-approval-modal.html',
                controller: 'RequirementApprovalModalController',
                controllerAs:'vm',
                size:'lg',
                resolve: {
                    items: function () {
                        return {
                        }
                    }
                }
            });

            modalInstance.result.then(function (data) {

            });
        }
        //样本库存详情
        function _fnSampleDescModal() {
            modalInstance = $uibModal.open({
                animation: true,
                templateUrl: 'app/bizs/stock-out/requirement/modal/requirement-sample-desc-modal.html',
                controller: 'RequirementSampleDescModalController',
                controllerAs:'vm',
                size:'lg',
                resolve: {
                    items: function () {
                        return {
                        }
                    }
                }
            });

            modalInstance.result.then(function (data) {

            });
        }



        function onError(error) {
            BioBankBlockUi.blockUiStop();
            toastr.error(error.data.message);
        }

    }
})();
