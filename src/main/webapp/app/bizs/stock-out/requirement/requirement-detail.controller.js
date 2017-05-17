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
        vm.requirement = entity;

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
            _fnQuerySampleType();
            _fuQueryDelegates();
            _fuQueryFrozenTubeType();
        }
        _initData();
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
            BioBankBlockUi.blockUiStart();
            RequirementService.saveRequirement(vm.requirement).success(function (data) {
                BioBankBlockUi.blockUiStop();
                toastr.success("保存申请记录成功！");
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
                if(vm.sampleClassOptions.length){
                    vm.sampleRequirement.sampleClassId = vm.sampleClassOptions[0].sampleClassificationId;
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
        //上传
        vm.submit = function () {
            vm.upload(vm.file);
            console.log(JSON.stringify(vm.file))
        };
        vm.upload = function(file) {
            console.log(file);
            file.upload = Upload.upload({
                url: 'https://angular-file-upload-cors-srv.appspot.com/upload',
                data: {file: file},
            });

            file.upload.then(function (response) {
                $timeout(function () {
                    file.result = response.data;
                });
            }, function (response) {
                if (response.status > 0)
                    vm.errorMsg = response.status + ': ' + response.data;
            }, function (evt) {
                // Math.min is to fix IE which reports 200% sometimes
                file.progress = Math.min(100, parseInt(100.0 * evt.loaded / evt.total));
            });
        };

        //---------------------------样本需求列表--------------------------
        vm.dtOptions = DTOptionsBuilder.newOptions()
            .withOption('processing',true)
            .withOption('serverSide',true)
            .withFnServerData(function ( sSource, aoData, fnCallback, oSettings ) {
                var data = {};
                for(var i=0; aoData && i<aoData.length; ++i){
                    var oData = aoData[i];
                    data[oData.name] = oData.value;
                }
                var jqDt = this;
                RequirementService.queryDemo(data, oSettings).then(function (res){
                    var json = res.data;
                    var error = json.error || json.sError;
                    if ( error ) {
                        jqDt._fnLog( oSettings, 0, error );
                    }
                    oSettings.json = json;
                    fnCallback( json );
                }).catch(function(res){
                    console.log(res);

                    var ret = jqDt._fnCallbackFire( oSettings, null, 'xhr', [oSettings, null, oSettings.jqXHR] );

                    if ( $.inArray( true, ret ) === -1 ) {
                        if ( error == "parsererror" ) {
                            jqDt._fnLog( oSettings, 0, 'Invalid JSON response', 1 );
                        }
                        else if ( res.readyState === 4 ) {
                            jqDt._fnLog( oSettings, 0, 'Ajax error', 7 );
                        }
                    }

                    jqDt._fnProcessingDisplay( oSettings, false );
                });
            })
            .withPaginationType('full_numbers')
            .withOption('createdRow', createdRow)
            .withColumnFilter({
                aoColumns: [{
                    type: 'text',
                    width:50,
                    iFilterLength:3
                }, {
                    type: 'text',
                    bRegex: true,
                    bSmart: true,
                    iFilterLength:3
                }, {
                    type: 'Datepicker',
                    bRegex: true,
                    bSmart: true
                }, {
                    type: 'text',
                    bRegex: true,
                    bSmart: true
                }, {
                    type: 'Datepicker',
                    bRegex: true,
                    bSmart: true
                }, {
                    type: 'select',
                    bRegex: false,
                    width:50,
                    values: [
                        {value:"10",label:"非常满意"},
                        {value:"9",label:"较满意"},
                        {value:"8",label:"满意"},
                        {value:"7",label:"有少量空管"},
                        {value:"6",label:"有许多空管"},
                        {value:"5",label:"有大量空管"},
                        {value:"4",label:"有少量空孔"},
                        {value:"3",label:"有少量错位"},
                        {value:"2",label:"有大量错位"},
                        {value:"1",label:"非常不满意"}
                    ]
                }, {
                    type: 'select',
                    bRegex: true,
                    width:50,
                    values: [
                        {value:'1001',label:"进行中"},
                        {value:"1002",label:"待入库"},
                        {value:"1003",label:"已入库"},
                        {value:"1004",label:"已作废"}
                    ]
                }]
            });

        vm.dtColumns = [
            DTColumnBuilder.newColumn('projectSiteCode').withTitle('项目点'),
            DTColumnBuilder.newColumn('projectCode').withTitle('项目编号'),
            DTColumnBuilder.newColumn('transhipDate').withTitle('转运日期'),
            DTColumnBuilder.newColumn('receiver').withTitle('接收人'),
            DTColumnBuilder.newColumn('receiveDate').withTitle('接收日期'),
            DTColumnBuilder.newColumn('sampleSatisfaction').withTitle('满意度'),
            DTColumnBuilder.newColumn('transhipState').withTitle('状态'),
            DTColumnBuilder.newColumn("").withTitle('操作').notSortable().renderWith(actionsHtml)
        ];
        function createdRow(row, data, dataIndex) {
            var transhipState = '';
            var sampleSatisfaction = '';
            switch (data.transhipState){
                case '1001': transhipState = '进行中';break;
                case '1002': transhipState = '待入库';break;
                case '1003': transhipState = '已入库';break;
                case '1004': transhipState = '已作废';break;
            }
            switch (data.sampleSatisfaction){
                case 1: sampleSatisfaction = '非常不满意';break;
                case 2: sampleSatisfaction = '有大量错位';break;
                case 3: sampleSatisfaction = '有少量错位';break;
                case 4: sampleSatisfaction = '有少量空孔';break;
                case 5: sampleSatisfaction = '有大量空管';break;
                case 6: sampleSatisfaction = '有许多空管';break;
                case 7: sampleSatisfaction = '有少量空管';break;
                case 8: sampleSatisfaction = '满意';break;
                case 9: sampleSatisfaction = '较满意';break;
                case 10: sampleSatisfaction = '非常满意';break;
            }
            $('td:eq(5)', row).html(sampleSatisfaction);
            $('td:eq(6)', row).html(transhipState);
            $compile(angular.element(row).contents())($scope);
        }
        function actionsHtml(data, type, full, meta) {
            return '<button type="button" class="btn btn-warning" ui-sref="transport-record-edit({id:'+ full.id +'})">' +
                '   <i class="fa fa-edit"></i>' +
                '</button>&nbsp;'
        }

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
