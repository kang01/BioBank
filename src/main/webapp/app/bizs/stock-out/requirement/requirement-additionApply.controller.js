/**
 * Created by gaokangkang on 2017/6/5.
 * 申请页面
 */
(function() {
    'use strict';

    angular
        .module('bioBankApp')
        .controller('RequirementAdditionApplyController', RequirementAdditionApplyController);

    RequirementAdditionApplyController.$inject = ['$scope','$stateParams','$state','$compile','entity','$uibModal','toastr','DTColumnBuilder','DTOptionsBuilder','RequirementService','SampleUserService','BioBankBlockUi','ProjectService','MasterData','BioBankDataTable'];
    function RequirementAdditionApplyController($scope,$stateParams,$state,$compile,entity,$uibModal,toastr,DTColumnBuilder,DTOptionsBuilder,RequirementService,SampleUserService,BioBankBlockUi,ProjectService,MasterData,BioBankDataTable) {
        var vm = this;
        var modalInstance;
        vm.dtInstance = {};
        vm.requirement = entity;
        //批准
        vm.approvalModal = _fnApprovalModal;
        //附加功能
        vm.additionApply = _fnAdditionApply;
        //样本库存详情
        vm.sampleDescModal = _fnSampleDescModal;
        //打印申请
        vm.printRequirement = _fnPrintRequirement;
        //作废
        vm.cancellation = _fnCancellation;

        if($stateParams.applyId){
            vm.requirement.id = $stateParams.applyId;
        }
        if($stateParams.viewFlag){
            vm.viewFlag = $stateParams.viewFlag;
        }
        //初始化数据
        function _initData() {
            _requirementInfo();
        }
        _initData();
        function _requirementInfo() {
            if(vm.requirement.startTime){
                vm.requirement.startTime = moment(vm.requirement.startTime).format("YYYY-MM-DD");
            }
            if(vm.requirement.endTime){
                vm.requirement.endTime = moment(vm.requirement.endTime).format("YYYY-MM-DD");
            }
            if(vm.requirement.recordTime){
                vm.requirement.recordTime = moment(vm.requirement.recordTime).format("YYYY-MM-DD");
            }
            if(vm.requirement.projectIds){
                vm.projectIds = _.join(vm.requirement.projectIds, ',');
            }
            if(vm.requirement.status){
                var requirementStatus =  MasterData.requirementStatus;
                for(var i = 0; i < requirementStatus.length; i++){
                    if(vm.requirement.status == requirementStatus[i].id){
                        vm.statusName = requirementStatus[i].name;
                    }
                }

            }
            //附加申请 2：附加编辑 1：附加浏览 3.附加
            if(vm.viewFlag == '3'){
                vm.requirement.stockOutRequirement = [];
                setTimeout(function () {
                    vm.dtOptions.withOption('data', vm.requirement.stockOutRequirement);
                    vm.isApproval();
                },100);
            }
            //附加编辑
            if(vm.viewFlag != "3"){
                vm.sampleRequirementIds = _.join(_.map(vm.requirement.stockOutRequirement,'id'),',');
                setTimeout(function () {
                    vm.dtOptions.withOption('data', vm.requirement.stockOutRequirement);
                },100);
            }

        }
        //附加
        vm.applyFlag = false;
        function _fnAdditionApply() {
            modalInstance = $uibModal.open({
                animation: true,
                templateUrl: 'app/bizs/stock-out/requirement/modal/requirement-confirm-modal.html',
                controller: 'ConfirmModalController',
                controllerAs:'vm'
            });

            modalInstance.result.then(function (data) {
                RequirementService.addApplyRequirement(vm.requirement.id).success(function (data) {
                    vm.status = data.status;
                    vm.requirement.id = data.id;
                    $state.go("requirement-additionApply",{applyId:data.id,viewFlag:3});
                });
            });
            // RequirementService.addApplyRequirement(vm.requirement.id).success(function (data) {
            //     vm.status = data.status;
            //     vm.requirement.id = data.id;
            //     $state.go("requirement-additionApply",{applyId:vm.requirement.id});
            // });
        }
        //打印申请
        function _fnPrintRequirement() {
            window.open ('/api/stock-out-applies/print/' + vm.requirement.id);
        }
        //作废
        function _fnCancellation() {
            modalInstance = $uibModal.open({
                animation: true,
                templateUrl: 'app/bizs/stock-out/requirement/modal/requirement-cancellation-modal.html',
                controller: 'RequirementCancellationModalController',
                controllerAs:'vm',
                size:'lg'

            });

            modalInstance.result.then(function (reason) {
                var invalid = {};
                invalid.invalidReason = reason;
                RequirementService.invalidPlan(vm.requirement.id,invalid).success(function (data) {
                    toastr.success("作废成功!");
                    $state.go("requirement-list");
                }).error(function (res) {
                    toastr.error(res.message);
                });
            }, function () {

            });
        }
        //---------------------------样本需求--------------------------
        //批量核对
        vm.sampleRequirementListCheck = _fnSampleRequirementCheckList;
        //添加样本需求
        vm.addSampleModal = function (sampleRequirement) {
            modalInstance = $uibModal.open({
                animation: true,
                templateUrl: 'app/bizs/stock-out/requirement/modal/requirement-sample-edit-modal.html',
                controller: 'RequirementSampleEditModalController',
                controllerAs:'vm',
                size:'90',
                resolve: {
                    items: function () {
                        return {
                            projectIds:vm.projectIds,
                            requirementId:vm.requirement.id,
                            sampleRequirement:sampleRequirement || ""
                        };
                    }
                }
            });

            modalInstance.result.then(function (data) {
                _loadRequirement();
            });
        };
        // function _fnSaveRequirement() {
        //     delete vm.requirement.stockOutRequirement;
        //     BioBankBlockUi.blockUiStart();
        //     RequirementService.saveRequirementInfo(vm.requirement).success(function (data) {
        //         BioBankBlockUi.blockUiStop();
        //     }).error(function (data) {
        //         BioBankBlockUi.blockUiStop();
        //         toastr.error("保存申请记录失败！");
        //     })
        // }
        //批量核对
        function _fnSampleRequirementCheckList() {
            vm.sampleRequirementIds = _.join(_.map(vm.requirement.stockOutRequirement,'id'),',');
            RequirementService.checkSampleRequirementList(vm.sampleRequirementIds).success(function (data) {
                vm.requirementApplyFlag = true;
                _loadRequirement();
            }).error(function (data) {
            });
        }
        function _loadRequirement(success, error) {
            RequirementService.queryRequirementDesc(vm.requirement.id).then(function (data) {
                vm.requirement = data;
                vm.requirement.stockOutRequirement = data.stockOutRequirement;
                vm.sampleRequirementIds = _.join(_.map(vm.requirement.stockOutRequirement,'id'),',');
                vm.dtOptions.withOption('data', vm.requirement.stockOutRequirement);
                vm.dtInstance.rerender();
                vm.isApproval();

                if (typeof success === "function"){
                    success(data);
                }
            });
        }
        //---------------------------样本需求列表--------------------------
        //核对
        vm.sampleRequirementCheck = _fnSampleRequirementCheck;
        //删除
        vm.sampleRequirementDel = _fnSampleRequirementDel;
        //详情
        vm.sampleRequirementDescModel = _fnSampleRequirementDescModel;
        //修改
        vm.sampleRequirementEdit = _fnSampleRequirementEdit;
        //复原
        vm.sampleRequirementRevert = _fnSampleRequirementRevert;
        //判断是否都是核对完的列表
        vm.isApproval = _fnIsApproval;

        vm.dtOptions = BioBankDataTable.buildDTOption("BASIC",240)
            .withOption('createdRow', createdRow);

        vm.dtColumns = [
            DTColumnBuilder.newColumn('id').notVisible(),
            DTColumnBuilder.newColumn('requirementName').withTitle('需求名称'),
            DTColumnBuilder.newColumn('countOfSample').withTitle('样本量'),
            DTColumnBuilder.newColumn('sampleTypeName').withTitle('样本类型'),
            DTColumnBuilder.newColumn('frozenTubeTypeName').withTitle('管类型'),
            DTColumnBuilder.newColumn('sex').withTitle('性别'),
            DTColumnBuilder.newColumn('diseaseTypeId').withTitle('疾病'),
            DTColumnBuilder.newColumn('samples').withTitle('指定样本编码').withClass('text-ellipsis'),
            DTColumnBuilder.newColumn('status').withTitle('状态')

        ];
        if(vm.viewFlag != "1"){
            vm.dtColumns.push(DTColumnBuilder.newColumn("").withTitle('操作').withOption("width", "150").notSortable().renderWith(actionsHtml));
        }
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
                diseaseType += "脂质血　";
            }
            if(data.isHemolysis){
                diseaseType += "溶血　";
            }
            $('td:eq(5)', row).html(diseaseType);
            $('td:eq(7)', row).html(status);
            $compile(angular.element(row).contents())($scope);
        }
        function actionsHtml(data, type, full, meta) {
            return '<div ng-if="vm.requirement.status != 1103">'+
                '<a ng-if="'+full.status+'!== 1201" ng-click="vm.sampleRequirementRevert('+full.id+')">复原</a>&nbsp;' +
                '<a ng-if="'+full.status+'== 1201 || '+full.status+'== 1202" ng-click="vm.sampleRequirementCheck('+full.id+')">核对</a>&nbsp;' +
                '<a ng-click="vm.sampleRequirementEdit('+full.id+')">修改</a>&nbsp;'+
                '<a ng-click="vm.sampleRequirementDel('+full.id+')">删除</a>&nbsp;'+
                '<a ng-if="'+full.status+'!== 1201" ng-click="vm.sampleRequirementDescModel('+full.id+')">详情</a>'+
                '</div>';
        }
        //编辑
        function _fnSampleRequirementEdit(sampleRequirementId) {
            RequirementService.querySampleRequirement(sampleRequirementId).success(function (data) {
                vm.file = "";
                vm.sampleRequirement = data;
                vm.addSampleModal(vm.sampleRequirement);
                // if(vm.sampleRequirement.sampleTypeId && vm.projectIds){
                //     _fuQuerySampleClass(vm.projectIds,vm.sampleRequirement.sampleTypeId);
                // }
            }).error(function (data) {
            });
        }
        //核对
        function _fnSampleRequirementCheck(sampleRequirementId) {
            RequirementService.checkSampleRequirement(sampleRequirementId).success(function (data) {
                _loadRequirement();
            }).error(function (data) {
            });
        }
        //复原
        function _fnSampleRequirementRevert(sampleRequirementId) {
            RequirementService.revertSampleRequirement(sampleRequirementId).success(function (data) {
                _loadRequirement();
            }).error(function (data) {
            });
        }
        //删除
        function _fnSampleRequirementDel(sampleRequirementId) {
            modalInstance = $uibModal.open({
                animation: true,
                templateUrl: 'app/bizs/stock-out/requirement/modal/requirement-sample-delete-modal.html',
                controller: 'RequirementSampleDelModalController',
                controllerAs:'vm'
            });

            modalInstance.result.then(function (data) {
                RequirementService.delSampleRequirement(sampleRequirementId).success(function (data) {
                    toastr.success(data.message);
                    _loadRequirement();
                }).error(function (data) {
                    toastr.error(data.message);
                });
            });

        }
        //详情
        function _fnSampleRequirementDescModel(sampleRequirementId) {
            modalInstance = $uibModal.open({
                animation: true,
                templateUrl: 'app/bizs/stock-out/requirement/modal/requirement-sample-desc-modal.html',
                controller: 'RequirementSampleDescModalController',
                controllerAs:'vm',
                size:'lg',
                resolve: {
                    items: function () {
                        return {
                            sampleRequirementId:sampleRequirementId
                        };
                    }
                }
            });

            modalInstance.result.then(function (data) {

            });

        }
        //判断是否都已核对 1201：待核对，1202：库存不够，1203：库存满足
        vm.requirementApplyFlag = false;
        function _fnIsApproval() {
            if(vm.requirement.stockOutRequirement.length){
                var len =  _.filter(vm.requirement.stockOutRequirement,{status:'1201'}).length;
                if(len){
                    // 不能批准
                    vm.requirementApplyFlag = false;
                }else{
                    // 可以批准
                    vm.requirementApplyFlag = true;
                }
            }
        }
        _fnIsApproval();
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
                            requirement:vm.requirement
                        };
                    }
                }
            });

            modalInstance.result.then(function (data) {
                $state.go("requirement-list");
            });
        }
        vm.planSave = function () {
            var planId;
            RequirementService.savePlan(vm.requirement.id).then(function (res) {
                planId = res.data.id;
                $state.go('plan-edit',{planId:planId});
            }).then(function (data) {
                toastr.error(data.message);
            });


        };
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

                        };
                    }
                }
            });

            modalInstance.result.then(function (data) {

            });
        }
        //改变项目是，任务列表是否复原
        function _fnIsChangeProjectModal() {
            modalInstance = $uibModal.open({
                animation: true,
                templateUrl: 'app/bizs/stock-out/requirement/modal/requirement-apply-project-modal.html',
                controller: 'RequirementApplyProjectModalController',
                controllerAs:'vm'

            });

            return modalInstance.result;

        }

        function onError(error) {
            BioBankBlockUi.blockUiStop();
            toastr.error(error.data.message);
        }

    }
})();
