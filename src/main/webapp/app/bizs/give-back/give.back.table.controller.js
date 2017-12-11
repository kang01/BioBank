/**
 * Created by gaokangkang on 2017/12/5.
 * 归还列表
 */
(function() {
    'use strict';

    angular
        .module('bioBankApp')
        .controller('GiveBackTableController', GiveBackTableController);

    GiveBackTableController.$inject = ['$scope', '$compile', 'BioBankDataTable','$uibModal', 'toastr', '$state','DTColumnBuilder','TransportRecordService'];
    function GiveBackTableController($scope, $compile, BioBankDataTable, $uibModal, toastr, $state,DTColumnBuilder,TransportRecordService) {
        var vm = this;

        vm.add = _add;
        var titleHtml = '<input type="checkbox" ng-model="vm.selectAll" ng-click="vm.toggleAll()">';
        var columns = [
            {
                name:"",
                title:titleHtml,
                width:"30",
                notSortable:true,
                renderWith:_fnRowSelectorRender
            },
            {
                name:"transhipCode",
                title:"转运编码",
                notSortable:true,
                renderWith:_fnRowRender
            }
        ];
        vm.dtInstance = {};
        var t;
        vm.dtOptions = BioBankDataTable.buildDTOption("NORMALLY",null,null,null,$scope)
            .withOption('serverSide',true)
            .withFnServerData(function ( sSource, aoData, fnCallback, oSettings ) {
                if(!oSettings.oPreviousSearch.sSearch){
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
            .withOption('createdRow', function (row, data, dataIndex) {
                $compile(angular.element(row).contents())($scope);
            });
        vm.dtColumns = BioBankDataTable.buildDTColumn(columns);
        vm.toggleAll = function () {
        };
        function _fnRowRender(data, type, full, meta) {
            var html = "<div class='text-ellipsis' style='width: 100px' title='"+full.transhipCode+"'>"+ full.transhipCode  +"</div>";
            return html;
        }
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
        function _fnRowSelectorRender(data, type, full, meta) {

            var html = '';
            if(full.transhipState == '1005'){
                vm.selected[full.transhipCode] = false;
                html = '<input type="checkbox" ng-model="vm.selected[\'' + full.transhipCode + '\']" ng-click="vm.toggleOne(vm.selected)">';
            }
            return html;
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
            modalInstance.result.then(function () {
                $state.go("give-back-detail");
            });
        }
    }
})();
