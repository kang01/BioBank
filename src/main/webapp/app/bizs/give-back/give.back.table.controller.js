/**
 * Created by gaokangkang on 2017/12/5.
 * 归还列表
 */
(function() {
    'use strict';

    angular
        .module('bioBankApp')
        .controller('GiveBackTableController', GiveBackTableController)
        .controller('StartStockInModalController', StartStockInModalController);

    GiveBackTableController.$inject = ['$scope', '$compile', 'BioBankDataTable','$uibModal', 'toastr', '$state','MasterData','GiveBackService','TranshipStockInService'];
    StartStockInModalController.$inject = ['$uibModalInstance'];
    function GiveBackTableController($scope, $compile, BioBankDataTable, $uibModal, toastr, $state,MasterData,GiveBackService,TranshipStockInService) {
        var vm = this;

        vm.add = _add;
        vm.startStockIn = _startStockIn;
        var titleHtml = '<input type="checkbox" ng-model="vm.selectAll" ng-click="vm.toggleAll()">';
        var columns =  [
            {
                name:"",
                title:titleHtml,
                width:"30",
                notSortable:true,
                renderWith:_fnRowSelectorRender
            },
            {
                name:"transhipCode",
                title:"归还记录单"
            },
            {
                name:"applyCode",
                title:"出库申请单"
            },
            {
                name:"projectCode",
                title:"项目编码",
                width:"80"
            },
            {
                name:"delegateName",
                title:"归还单位",
                width:"240"
            },
            {
                name:"applyPersonName",
                title:"归还人"
            },
            {
                name:"receiveDate",
                title:"归还日期"
            },
            {
                name:"receiver",
                title:"接收人"
            },
            {
                name:"transhipState",
                title:"状态"
            },{
                name:"",
                title:"操作",
                width:"30",
                notSortable:true,
                renderWith:_actionsHtml
            }
        ];
        vm.dtInstance = {};
        var _timer;
        vm.dtOptions = BioBankDataTable.buildDTOption("NORMALLY",null,10,null,$scope)
            .withOption('order', [[1, 'desc' ]])
            .withOption('serverSide',true)
            .withFnServerData(function ( sSource, aoData, fnCallback, oSettings ) {
                //各列搜索
                var aoPreSearchCols = oSettings.aoPreSearchCols;
                var len = _.filter(aoPreSearchCols,{sSearch:""}).length;
                // 搜索框为空的时候不用timer
                if(!oSettings.oPreviousSearch.sSearch && len == aoPreSearchCols.length){
                    _fnTransportSearch(sSource, aoData, fnCallback, oSettings);
                }else{
                    if(_timer){
                        clearTimeout(_timer);
                    }
                    _timer = setTimeout(function () {
                        _fnTransportSearch(sSource, aoData, fnCallback, oSettings);
                    },1000);
                }
            })
            .withOption('createdRow', function (row, data, dataIndex) {
                var giveBackStatus = MasterData.getStatus(data.transhipState);
                $('td:eq(8)', row).html(giveBackStatus);
                $compile(angular.element(row).contents())($scope);
            })
            .withColumnFilter(_createColumnFilters());
        vm.dtColumns = BioBankDataTable.buildDTColumn(columns);
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
        function _fnTransportSearch(sSource, aoData, fnCallback, oSettings) {
            vm.selectAll = false;
            vm.selected = {};
            var data = {};
            for(var i=0; aoData && i<aoData.length; ++i){
                var oData = aoData[i];
                data[oData.name] = oData.value;
            }
            var jqDt = this;
            GiveBackService.queryGiveBackTable(data, oSettings).then(function (res){
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
        function _fnRowSelectorRender(data, type, full, meta) {

            var html = '';
            if(full.transhipState == '1005'){
                vm.selected[full.transhipCode] = false;
                html = '<input type="checkbox" ng-model="vm.selected[\'' + full.transhipCode + '\']" ng-click="vm.toggleOne(vm.selected)">';
            }
            return html;
        }
        function _actionsHtml(data, type, full, meta) {
            var html = '';
            if(full.transhipState != '1001'){
                html = '<button type="button" class="btn btn-default btn-xs" ui-sref="give-back-view({giveBackId:'+ full.id +'})">' +
                    '   <i class="fa fa-eye"></i>' +
                    '</button>&nbsp;';
            }else{
                html = '<button type="button" class="btn btn-default btn-xs" ui-sref="give-back-detail({giveBackId:'+ full.id +',applyCode:\''+full.applyCode+'\'})">' +
                    '   <i class="fa fa-edit"></i>' +
                    '</button>&nbsp;';
            }
            return html;
        }
        function _createColumnFilters(){
            var filters = {
                aoColumns: [
                    null,
                    {type: 'text',bRegex: true,bSmart: true},
                    {type: 'text',bRegex: true,bSmart: true},
                    {type: 'text',bRegex: true,bSmart: true},
                    {type: 'text',bRegex: true,bSmart: true},
                    {type: 'text',bRegex: true,bSmart: true},
                    {type: 'text',bRegex: true,bSmart: true},
                    {type: 'text',bRegex: true,bSmart: true},
                    {
                        type: 'select',
                        // bRegex: true,
                        bSmart: true,
                        values: MasterData.transportStatus

                    },
                    null
                ]
            };

            return filters;
        }
        //添加申请单号的归还详情
        function _add() {
            var modalInstance = $uibModal.open({
                animation: true,
                templateUrl: 'app/bizs/give-back/modal/give-back-application-number-modal.html',
                controller: 'ApplicationNumberModalController',
                controllerAs:'vm',
                backdrop:'static',
                resolve: {
                    items:{}
                }
            });
            modalInstance.result.then(function (giveBackInfo) {
                GiveBackService.saveGiveBackEmpty(giveBackInfo).success(function (data) {
                    $state.go("give-back-detail",{giveBackId:data.id,applyCode:giveBackInfo.applyCode});
                });

            });
        }
        //开始入库
        function _startStockIn() {
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
                toastr.error("请选择接收完成的记录!");
                vm.selectAll = false;
            }
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
