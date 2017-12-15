/**
 * Created by gaokangkang on 2017/3/10.
 */
(function() {
    'use strict';

    angular
        .module('bioBankApp')
        .controller('TransportRecordController', TransportRecordController)
        .controller('StartStockInModalController', StartStockInModalController);

    TransportRecordController.$inject = ['$scope', '$compile', 'BioBankDataTable','Principal','$uibModal', 'TransportRecordService', 'TranshipStockInService', 'toastr', '$state', 'pagingParams', 'paginationConstants', 'JhiLanguageService','DTOptionsBuilder','DTColumnBuilder','TranshipNewEmptyService'];
    StartStockInModalController.$inject = ['$uibModalInstance'];
    function TransportRecordController($scope, $compile, BioBankDataTable,Principal, $uibModal,TransportRecordService, TranshipStockInService, toastr, $state, pagingParams, paginationConstants, JhiLanguageService,DTOptionsBuilder,DTColumnBuilder,TranshipNewEmptyService) {
        var vm = this;

        vm.authorities = ['ROLE_USER', 'ROLE_ADMIN'];
        vm.currentAccount = null;
        vm.users = [];
        vm.page = 1;
        vm.totalItems = null;
        vm.links = null;
        vm.predicate = pagingParams.predicate;
        vm.reverse = pagingParams.ascending;
        vm.itemsPerPage = paginationConstants.itemsPerPage;
        vm.datePickerOpenStatus = {};
        vm.openCalendar = openCalendar;

        vm.dtInstance = {};
        vm.persons = {};
        vm.message = '';
        vm.edit = edit;
        vm.add = add;
        vm.stockIn = _fnStockIn;
        var modalInstance;
        function add() {
            modalInstance = $uibModal.open({
                animation: true,
                templateUrl: 'app/bizs/transport-record/modal/transport-record-select-project-modal.html',
                controller: 'SelectProjectModalController',
                backdrop:'static',
                controllerAs: 'vm',
                // resolve:{
                //     items:function () {
                //         return{
                //             box:vm.box || {},
                //             receiverId:vm.transportRecord.receiverId,
                //             receiveDate: vm.transportRecord.receiveDate
                //         };
                //     }
                // }
            });
            modalInstance.result.then(function (project) {
                // TranshipNewEmptyService.save({},onTranshipNewEmptyService,onError);
                TranshipNewEmptyService.saveTransportEmpty(project.projectId,project.projectSiteId).success(function (data) {
                    $state.go('transport-record-edit',{
                        transhipId : data.id,
                        transhipCode : data.transhipCode
                    });
                });
            });


        }
        // function onTranshipNewEmptyService(data) {
        //     $state.go('transport-record-edit',{
        //         transhipId : data.id,
        //         transhipCode : data.transhipCode
        //     });
        // }
        //开始入库
        function _fnStockIn() {
            var codeArray = [];
            for(var code in vm.selected){
                if(vm.selected[code]){
                    codeArray.push(code)
                }
            }
            if(codeArray.length){
                var transhipCodes = _.join(codeArray, ',');
                var modalInstance = $uibModal.open({
                    templateUrl: 'startStockIn.html',
                    controller: 'StartStockInModalController',
                    controllerAs: 'vm'
                });
                modalInstance.result.then(function () {
                    TranshipStockInService.saveStockIn(transhipCodes).success(function (data) {
                        $state.go('stock-in-edit',{id:data.id});
                    }).error(function (data) {
                        toastr.error(data.message);
                    })
                }, function () {
                });
            }else{
                toastr.error("请选择转运完成的转运记录!");
                vm.selectAll = false;
            }
        }


        vm.selected = {};
        vm.selectAll = false;

        // 处理盒子选中状态
        vm.toggleAll = function (selectAll, selectedItems) {
            selectedItems = vm.selected;
            selectAll = vm.selectAll;
            for (var id in selectedItems) {
                if (selectedItems.hasOwnProperty(id)) {
                    selectedItems[id] = selectAll;
                }
            }
        };
        vm.toggleOne = function (selectedItems) {
            for (var id in selectedItems) {
                if (selectedItems.hasOwnProperty(id)) {
                    if(!selectedItems[id]) {
                        vm.selectAll = false;
                        return;
                    }
                }
            }
            vm.selectAll = true;
        };
        function _fnTransportSeach(sSource, aoData, fnCallback, oSettings) {
            vm.selectAll = false;
            vm.selected = {};
            var data = {};
            for(var i=0; aoData && i<aoData.length; ++i){
                var oData = aoData[i];
                data[oData.name] = oData.value;
            }
            var jqDt = this;
            TransportRecordService.getJqDataTableValues(data, oSettings).then(function (res){
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
        }
        var t;
        vm.dtOptions = BioBankDataTable.buildDTOption("NORMALLY", null, 10)
            .withOption('order', [[1, 'desc' ]])
            .withOption('serverSide',true)
            .withFnServerData(function ( sSource, aoData, fnCallback, oSettings ) {
                //各列搜索
                var aoPreSearchCols = oSettings.aoPreSearchCols;
                var len = _.filter(aoPreSearchCols,{sSearch:""}).length;
                // 搜索框为空的时候不用timer
                if(!oSettings.oPreviousSearch.sSearch && len == aoPreSearchCols.length){
                    _fnTransportSeach(sSource, aoData, fnCallback, oSettings);
                }else{
                    if(t){
                        clearTimeout(t);
                    }
                    t=setTimeout(function () {
                        _fnTransportSeach(sSource, aoData, fnCallback, oSettings);
                    },1000);
                }
            })
            .withOption('createdRow', createdRow)
            .withOption('headerCallback', function(header) {
                $compile(angular.element(header).contents())($scope);
            })
            .withColumnFilter({
                aoColumns: [
                    null,
                    {
                        type: 'text',
                        width:50
                    },{
                        type: 'text',
                        width:50
                    },{
                        type: 'text',
                        width:50
                    }, {
                        type: 'text',
                        bRegex: true,
                        bSmart: true
                    }, {
                        type: 'text',
                        bRegex: true,
                        bSmart: true
                    }, {
                        type: 'text',
                        bRegex: true,
                        bSmart: true
                    }, {
                        type: 'text',
                        bRegex: true,
                        bSmart: true
                    }, {
                        type: 'select',
                        bRegex: false,
                        width:50,
                        values: [
                            {value:"10",label:"10"},
                            {value:"9",label:"9"},
                            {value:"8",label:"8"},
                            {value:"7",label:"7"},
                            {value:"6",label:"6"},
                            {value:"5",label:"5"},
                            {value:"4",label:"4"},
                            {value:"3",label:"3"},
                            {value:"2",label:"2"},
                            {value:"1",label:"1"}
                            ]
                    }, {
                        type: 'select',
                        bRegex: true,
                        width:50,
                        values: [
                            {value:'1001',label:"进行中"},
                            {value:"1002",label:"待入库"},
                            {value:"1003",label:"已入库"},
                            {value:"1090",label:"已作废"},
                            {value:"1005",label:"转运完成"}
                            ]
                    }
                ]
            });
        var titleHtml = '<input type="checkbox" ng-model="vm.selectAll" ng-click="vm.toggleAll()">';
        vm.dtColumns = [
            DTColumnBuilder.newColumn("").withOption("width", "30").withTitle(titleHtml).withOption('searchable',false).notSortable().renderWith(_fnRowSelectorRender),
            DTColumnBuilder.newColumn('transhipCode').withTitle('转运编码'),
            DTColumnBuilder.newColumn('trackNumber').withTitle('运单号'),
            DTColumnBuilder.newColumn('projectSiteCode').withTitle('项目点'),
            DTColumnBuilder.newColumn('projectCode').withTitle('项目编号'),
            DTColumnBuilder.newColumn('transhipDate').withTitle('转运日期'),
            DTColumnBuilder.newColumn('receiver').withTitle('接收人'),
            DTColumnBuilder.newColumn('receiveDate').withTitle('接收日期'),
            DTColumnBuilder.newColumn('sampleSatisfaction').withTitle('满意度'),
            DTColumnBuilder.newColumn('transhipState').withTitle('状态'),
            DTColumnBuilder.newColumn("").withTitle('操作').withOption('searchable',false).notSortable().renderWith(actionsHtml)
        ];
        function createdRow(row, data, dataIndex) {
            var transhipState = '';
            switch (data.transhipState){
                case '1001': transhipState = '进行中';break;
                case '1002': transhipState = '待入库';break;
                case '1003': transhipState = '已入库';break;
                case '1090': transhipState = '已作废';break;
                case '1005': transhipState = '转运完成';break;
            }
            $('td:eq(9)', row).html(transhipState);
            $compile(angular.element(row).contents())($scope);
        }
        function _fnRowSelectorRender(data, type, full, meta) {

            var html = '';
            if(full.transhipState == '1005'){
                vm.selected[full.transhipCode] = false;
                html = '<input type="checkbox" ng-model="vm.selected[\'' + full.transhipCode + '\']" ng-click="vm.toggleOne(vm.selected)">';
            }
            return html;
        }
        function edit(person) {
            vm.message = 'You are trying to edit the row: ' + JSON.stringify(person);
            vm.dtInstance.reloadData();
        }
        function actionsHtml(data, type, full, meta) {
            var html = '';
            if(full.transhipState != '1001'){
                html = '<button type="button" class="btn btn-xs" ui-sref="transport-record-view({transhipId:'+ full.id +'})">' +
                    '   <i class="fa fa-eye"></i>' +
                    '</button>&nbsp;';
            }else{
                html = '<button type="button" class="btn btn-xs" ui-sref="transport-record-edit({transhipId:'+ full.id +'})">' +
                    '   <i class="fa fa-edit"></i>' +
                    '</button>&nbsp;';
            }
            return html;
        }
        function openCalendar (date) {
            vm.datePickerOpenStatus[date] = true;
        }
        function onError(error) {
            toastr.error(error.data.message);
        }
    }
    function StartStockInModalController($uibModalInstance) {
        var vm = this;
        vm.ok = function () {
            $uibModalInstance.close();
        };
        vm.cancel = function () {
            $uibModalInstance.dismiss('cancel');
        };
    }
})();
