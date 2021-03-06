/**
 * Created by gaokangkang on 2017/5/12.
 * 出库申请详情页面
 */
(function() {
    'use strict';

    angular
        .module('bioBankApp')
        .controller('RequirementDetailController', RequirementDetailController)
        .controller('RequirementSampleDelModalController', RequirementSampleDelModalController)
        .controller('RequirementApplyProjectModalController', RequirementApplyProjectModalController)
        .controller('RequirementCancellationModalController', RequirementCancellationModalController);
    RequirementDetailController.$inject = ['$scope','$stateParams','$state','$compile','entity','MasterData','$uibModal','toastr','DTColumnBuilder','DTOptionsBuilder','RequirementService','SampleUserService','BioBankBlockUi','ProjectService','BioBankDataTable','Principal'];
    RequirementSampleDelModalController.$inject = ['$uibModalInstance'];
    RequirementApplyProjectModalController.$inject = ['$uibModalInstance'];
    RequirementCancellationModalController.$inject = ['$uibModalInstance'];
    function RequirementDetailController($scope,$stateParams,$state,$compile,entity,MasterData,$uibModal,toastr,DTColumnBuilder,DTOptionsBuilder,RequirementService,SampleUserService,BioBankBlockUi,ProjectService,BioBankDataTable,Principal) {
        var vm = this;
        var modalInstance;
        vm.dtInstance = {};
        vm.requirement = entity;
        //判断是否弹出需求保存提示信息
        vm.isRequirementSaveFlag = false;
        //是否可以核对
        vm.allowCheckFlag = false;

        //批准
        vm.approvalModal = _fnApprovalModal;
        //样本库存详情
        vm.sampleDescModal = _fnSampleDescModal;
        //保存申请
        vm.saveRequirement = _fnSaveRequirement;
        //附加功能
        vm.additionApply = _fnAdditionApply;
        //打印申请
        vm.printRequirement = _fnPrintRequirement;
        //作废
        vm.cancellation = _fnCancellation;

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
        //列表附加
        if($stateParams.addApplyFlag == 1){
            vm.additionApply();
        }
        //初始化数据
        function _initData() {
            _requirementInfo();
            _fuQueryDelegates();
            _fnQueryUser();
            _fnQueryCheckType();

        }
        function _fnQueryUser() {
            Principal.identity().then(function(account) {
                vm.account = account;
                if(!vm.requirement.recordId){
                    vm.requirement.recordId = vm.account.id;
                }

            });
        }
        _initData();
        function _requirementInfo() {
            if(vm.requirement.startTime){
                vm.requirement.startTime = new Date(vm.requirement.startTime);
            }
            if(!vm.requirement.startTime){
                vm.requirement.startTime = new Date();
            }
            if(vm.requirement.endTime){
                vm.requirement.endTime = new Date(vm.requirement.endTime);
            }
            if(!vm.requirement.endTime){
                vm.requirement.endTime = new Date();
            }
            if(vm.requirement.recordTime){
                vm.requirement.recordTime = new Date(vm.requirement.recordTime);
            }
            if(!vm.requirement.recordTime){
                vm.requirement.recordTime = new Date();
            }
            if(vm.requirement.projectIds){
                vm.projectIds = _.join(vm.requirement.projectIds, ',');
            }
            if(vm.requirement.status){
                vm.status = vm.requirement.status;
            }
            vm.sampleRequirementIds = _.join(_.map(vm.requirement.stockOutRequirement,'id'),',');
            //更新申请列表
            _updateRequirementTable();
        }
        //更新申请列表
        function _updateRequirementTable() {
            setTimeout(function () {
                vm.dtOptions.withOption('data', vm.requirement.stockOutRequirement);
                vm.sampleRequirementIds = _.join(_.map(vm.requirement.stockOutRequirement,'id'),',');
                //判断是否都是核对完的列表
                vm.isApproval();
            },100);
        }
        //委托方查询
        function _fuQueryDelegates() {
            RequirementService.queryDelegates().success(function (data) {
                vm.delegatesOptions = data;
                if(!vm.requirement.delegateId){
                    vm.requirement.delegateId = vm.delegatesOptions[0].id;
                }
            });
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
        //获取检测类型
        function _fnQueryCheckType() {
            RequirementService.queryCheckTypes().success(function (data) {
                vm.checkTypeOptions = data;
            });
        }
        vm.checkTypeConfig = {
            valueField:'id',
            labelField:'checkTypeName',
            maxItems: 1,
            onChange:function (value) {
            }
        };

        //状态
        // vm.statusOptions = MasterData.requirementStatus;
        // vm.statusConfig = {
        //     valueField:'id',
        //     labelField:'name',
        //     maxItems: 1,
        //     onChange:function (value) {
        //     }
        // };
        // if(!vm.status){
        //     vm.status = vm.statusOptions[0].id;
        // }
        //获取项目
        ProjectService.query({},onProjectSuccess, onError);
        function onProjectSuccess(data) {
            vm.projectOptions = data;
        }
        var selector = null;
        vm.projectConfig = {
            valueField:'id',
            labelField:'projectName',
            searchField:'projectName',
            onInitialize: function(){
                selector = arguments[0];
            },
            onFocus: function(){
                _fnIsApproval();
                // console.log('onFocus', vm.isVerifyFlag);
                // 有需求已经进行过核对
                if(vm.isVerifyFlag){
                    // 先组织本次的修改操作,拒绝响应Focus时间。
                    selector.disable();
                    // 弹窗询问
                    var result = _fnIsChangeProjectModal();
                    result.then(function () {
                        // 复原已核对需求
                        RequirementService.sampleRevert(vm.requirement.id).success(function (data) {
                            // 重新加载需求列表
                            _loadRequirement(function(){
                                // 开启授权项目选择框
                                selector.enable();
                            });
                        });
                    }, function () {
                        // 开启授权项目选择框, 继续接受编辑
                        selector.enable();
                    });
                }
            },
            onChange:function(value){
                vm.projectIds = _.join(value, ',');
                $scope.$apply();
            }
        };

        //附加
        vm.applyFlag = false;
        function _fnAdditionApply() {
            modalInstance = $uibModal.open({
                animation: true,
                size:"sm",
                backdrop:"static",
                templateUrl: 'app/bizs/stock-out/requirement/modal/requirement-confirm-modal.html',
                controller: 'ConfirmModalController',
                controllerAs:'vm'
            });

            modalInstance.result.then(function (data) {
                RequirementService.addApplyRequirement(vm.requirement.id).success(function (data) {
                    vm.status = data.status;
                    vm.requirement.id = data.id;
                    $state.go("requirement-additionApply",{applyId:data.id,viewFlag:3});
                }).error(function (data) {
                    toastr.error(data.message);
                });
            });
            // RequirementService.addApplyRequirement(vm.requirement.id).success(function (data) {
            //     vm.status = data.status;
            //     vm.requirement.id = data.id;
            //     $state.go("requirement-additionApply",{'applyId':vm.requirement.id,'viewFlag':3});
            // });
        }
        //打印申请
        function _fnPrintRequirement() {

            window.open ('/api/stock-out-applies/print/' + vm.requirement.id);
            setTimeout(function () {
                RequirementService.queryRequirementDesc(vm.requirement.id).then(function (data) {
                    vm.requirement = data;
                    _initData();
                });
            },100);

        }
        //作废
        function _fnCancellation() {
            modalInstance = $uibModal.open({
                backdrop:"static",
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
            vm.isRequirementSaveFlag = true;
            _fnSaveRequirement();
            modalInstance = $uibModal.open({
                animation: true,
                templateUrl: 'app/bizs/stock-out/requirement/modal/requirement-sample-edit-modal.html',
                controller: 'RequirementSampleEditModalController',
                controllerAs:'vm',
                backdrop:'static',
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
                _fnUpdateRequirementData(data);
                //更新申请列表
                _updateRequirementTable();
                // if(vm.requirement.id){
                //     _loadRequirement();
                // }else{
                //
                // }

            }, function () {
                vm.isRequirementSaveFlag = false;
            });
        };
        //更新需求的数据
        function _fnUpdateRequirementData(data) {
            var requirement = _.find(vm.requirement.stockOutRequirement,{id:data.id});
            if(requirement){
                _.assign(requirement,data);
            }else{
                vm.requirement.stockOutRequirement.push(data);
            }
        }
        //保存申请记录
        function _fnSaveRequirement(sampleRequirementId) {
            // delete vm.requirement.stockOutRequirement;
            BioBankBlockUi.blockUiStart();
            RequirementService.saveRequirementInfo(vm.requirement).success(function (data) {
                BioBankBlockUi.blockUiStop();
                if(!vm.isRequirementSaveFlag){
                    toastr.success("保存申请记录成功！");
                }
                //保存完了核对
                if(vm.allowCheckFlag){
                    RequirementService.checkSampleRequirement(sampleRequirementId).success(function (data) {
                        var requirement = _.find(vm.requirement.stockOutRequirement,{id:data.id});
                        requirement.status = data.status;
                        //更新申请列表
                        _updateRequirementTable();
                        vm.allowCheckFlag = false;
                        vm.isRequirementSaveFlag = false;
                    }).error(function (data) {
                        toastr.error(data.message);
                        vm.allowCheckFlag = false;
                        vm.isRequirementSaveFlag = false;
                    });
                }
            }).error(function (data) {
                BioBankBlockUi.blockUiStop();
                toastr.error("保存申请记录失败！");
            });
        }
        //批量核对
        function _fnSampleRequirementCheckList() {
            vm.sampleRequirementIds = _.join(_.map(vm.requirement.stockOutRequirement,'id'),',');
            RequirementService.checkSampleRequirementList(vm.sampleRequirementIds).success(function (data) {
                vm.requirementApplyFlag = true;
                _.forEach(data,function (requirement) {
                   var obj = _.find(vm.requirement.stockOutRequirement,{id:requirement.id});
                   obj.status = requirement.status;
                });
                //更新申请列表
                _updateRequirementTable();
                // _loadRequirement();
            }).error(function (data) {
            });
        }
        function _loadRequirement(success, error) {
            RequirementService.queryRequirementDesc(vm.requirement.id).then(function (data) {
                vm.requirement = data;
                vm.requirement.startTime = new Date(data.startTime);
                vm.requirement.endTime = new Date(data.endTime);
                vm.requirement.recordTime = new Date(data.recordTime);
                // vm.requirement.recordId = data.recordId;
                // vm.requirement.recordId = data.applyPersonName;
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
        //指定样本编码详情
        vm.appointDescSample = _fnAppointDescSample;

        vm.dtOptions = BioBankDataTable.buildDTOption("SORTING")
            .withOption('createdRow', createdRow);

        vm.dtColumns = [
            DTColumnBuilder.newColumn('id').notVisible(),
            DTColumnBuilder.newColumn('requirementName').withTitle('需求名称'),
            DTColumnBuilder.newColumn('countOfSample').withTitle('样本量'),
            DTColumnBuilder.newColumn('sampleTypeName').withTitle('样本类型'),
            DTColumnBuilder.newColumn('frozenTubeTypeName').withTitle('管类型'),
            DTColumnBuilder.newColumn('sex').withTitle('性别'),
            DTColumnBuilder.newColumn('diseaseTypeId').withTitle('疾病'),
            DTColumnBuilder.newColumn('samples').withTitle('指定样本编码').withClass('text-ellipsis').renderWith(sampleOperateHtml),
            DTColumnBuilder.newColumn('status').withTitle('状态'),
            DTColumnBuilder.newColumn("").withTitle('操作').withOption("width", "150").notSortable().renderWith(actionsHtml)
        ];
        function createdRow(row, data, dataIndex) {
            var status = '';
            var diseaseType = '';
            var sex = "";
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

            if(data.sex){
                switch (data.sex){
                    case 'M': sex = '男';break;
                    case '男': sex = '男';break;
                    case 'F': sex = '女';break;
                    case '女': sex = '女';break;
                    case 'null': sex = '不详';break;
                }
            }else{
                sex = "不详";
            }
            $('td:eq(4)', row).html(sex);
            $('td:eq(5)', row).html(diseaseType);
            $('td:eq(7)', row).html(status);
            $compile(angular.element(row).contents())($scope);
        }
        //指定样本编码
        function sampleOperateHtml(data, type, full, meta) {
            var html = '';
            if(full.samples){
                html =  '<a ng-click="vm.appointDescSample('+full.id+')">'+ full.samples +
                    '</a>';
            }

            return html;
        }
        //操作
        function actionsHtml(data, type, full, meta) {
            return '<div ng-if="vm.status != 1103">'+
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
            }).error(function (data) {
            });
        }
        //核对
        function _fnSampleRequirementCheck(sampleRequirementId) {
            //是否可以核对
            vm.allowCheckFlag = true;
            //
            vm.isRequirementSaveFlag = true;
            _fnSaveRequirement(sampleRequirementId);

        }
        //复原
        function _fnSampleRequirementRevert(sampleRequirementId) {
            RequirementService.revertSampleRequirement(sampleRequirementId).success(function (data) {
                _.each(vm.requirement.stockOutRequirement,function (requirement) {
                    if(requirement.id == data.id){
                        requirement.status = data.status;
                    }
                });
                //更新申请列表
                _updateRequirementTable();
            }).error(function (data) {
            });
        }
        //删除
        function _fnSampleRequirementDel(sampleRequirementId) {
            modalInstance = $uibModal.open({
                size:"sm",
                backdrop:"static",
                animation: true,
                templateUrl: 'app/bizs/stock-out/requirement/modal/requirement-sample-delete-modal.html',
                controller: 'RequirementSampleDelModalController',
                controllerAs:'vm'
            });

            modalInstance.result.then(function (data) {
                RequirementService.delSampleRequirement(sampleRequirementId).success(function (data) {
                    toastr.success("删除成功!");
                    _.remove(vm.requirement.stockOutRequirement,{id:sampleRequirementId});
                    //更新申请列表
                    _updateRequirementTable();
                }).error(function (data) {
                    toastr.error(data.message);
                });
            });

        }
        //核对详情
        function _fnSampleRequirementDescModel(sampleRequirementId) {
            modalInstance = $uibModal.open({
                animation: true,
                templateUrl: 'app/bizs/stock-out/requirement/modal/requirement-sample-desc-modal.html',
                controller: 'RequirementSampleDescModalController',
                controllerAs:'vm',
                backdrop:"static",
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
                if(len != vm.requirement.stockOutRequirement.length){
                    // 不能修改授权项目
                    vm.isVerifyFlag = true;
                }else{
                    // 可以修改授权项目
                    vm.isVerifyFlag = false;
                }
            }
        }
        //指定样本编码
        function _fnAppointDescSample(requirementId) {
            window.open ('api/stock-out-required-samples/requirement/'+requirementId);
            // modalInstance = $uibModal.open({
            //     animation: true,
            //     templateUrl: 'app/bizs/stock-out/requirement/modal/requirement-appoint-sample-desc-modal.html',
            //     controller: 'RequirementAppointSampleDescModalController',
            //     controllerAs:'vm',
            //     size:'lg',
            //     resolve: {
            //         items: function () {
            //             return {
            //                 sampleRequirementId:requirementId
            //             };
            //         }
            //     }
            // });
            //
            // modalInstance.result.then(function (data) {
            //
            // });
        }
        //---------------------------弹出框--------------------------

        //批准
        function _fnApprovalModal() {
            vm.isRequirementSaveFlag = true;
            _fnSaveRequirement();
            modalInstance = $uibModal.open({
                animation: true,
                templateUrl: 'app/bizs/stock-out/requirement/modal/requirement-approval-modal.html',
                controller: 'RequirementApprovalModalController',
                controllerAs:'vm',
                backdrop:'static',
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
            }, function () {
                vm.isRequirementSaveFlag = false;
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
                backdrop:"static",
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
                size:"sm",
                backdrop:"static",
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
    function RequirementSampleDelModalController($uibModalInstance) {
        var vm = this;
        vm.ok = function () {
            $uibModalInstance.close(true);
        };
        vm.cancel = function () {
            $uibModalInstance.dismiss('cancel');
        };
    }
    function RequirementApplyProjectModalController($uibModalInstance) {
        var vm = this;
        vm.ok = function () {
            $uibModalInstance.close(true);
        };
        vm.cancel = function () {
            // $uibModalInstance.close(false);
            $uibModalInstance.dismiss('cancel');
        };
    }
    function RequirementCancellationModalController($uibModalInstance) {
        var vm = this;
        vm.ok = function () {
            $uibModalInstance.close(vm.reason);
        };
        vm.cancel = function () {
            $uibModalInstance.dismiss('cancel');
        };
    }
})();
