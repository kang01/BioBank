/**
 * Created by gaokangkang on 2017/12/5.
 * 归还列表
 */
(function() {
    'use strict';

    angular
        .module('bioBankApp')
        .controller('GiveBackTableController', GiveBackTableController);

    GiveBackTableController.$inject = ['$scope', '$compile', 'BioBankDataTable','$uibModal', 'toastr', '$state','MasterData','GiveBackService'];
    function GiveBackTableController($scope, $compile, BioBankDataTable, $uibModal, toastr, $state,MasterData,GiveBackService) {
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
                name:"applyCode",
                title:"申请单号"
            },
            {
                name:"returnBackPeople",
                title:"归还人"
            },
            {
                name:"receiver",
                title:"接收人"
            },
            {
                name:"receiveDate",
                title:"接收日期"
            },
            {
                name:"transhipState",
                title:"状态"
            },{
                name:"",
                title:"操作",
                width:"30",
                notSortable:true,
                renderWith:actionsHtml
            }
        ];
        vm.dtInstance = {};
        var t;
        vm.dtOptions = BioBankDataTable.buildDTOption("NORMALLY",null,10,null,$scope)
            .withOption('order', [[1, 'desc' ]])
            .withOption('serverSide',true)
            .withFnServerData(function ( sSource, aoData, fnCallback, oSettings ) {
                if(!oSettings.oPreviousSearch.sSearch){
                    _fnTransportSearch(sSource, aoData, fnCallback, oSettings);
                }else{
                    if(t){
                        clearTimeout(t);
                    }
                    t=setTimeout(function () {
                        _fnTransportSearch(sSource, aoData, fnCallback, oSettings);
                    },1000);
                }
            })
            .withOption('createdRow', function (row, data, dataIndex) {
                var giveBackStatus = MasterData.getStatus(data.transhipState);
                $('td:eq(5)', row).html(giveBackStatus);

                $compile(angular.element(row).contents())($scope);
            });
        vm.dtColumns = BioBankDataTable.buildDTColumn(columns);
        vm.toggleAll = function () {
        };
        function _fnRowRender(data, type, full, meta) {
            var html = "<div class='text-ellipsis' style='width: 100px' title='"+full.transhipCode+"'>"+ full.transhipCode  +"</div>";
            return html;
        }
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
            // if(full.transhipState == '1005'){
                vm.selected[full.transhipCode] = false;
                html = '<input type="checkbox" ng-model="vm.selected[\'' + full.transhipCode + '\']" ng-click="vm.toggleOne(vm.selected)">';
            // }
            return html;
        }
        function actionsHtml(data, type, full, meta) {
            var html = '';
            if(full.transhipState != '1001'){
                html = '<button type="button" class="btn btn-xs" ui-sref="transport-record-view({transhipId:'+ full.id +'})">' +
                    '   <i class="fa fa-eye"></i>' +
                    '</button>&nbsp;';
            }else{
                html = '<button type="button" class="btn btn-xs" ui-sref="give-back-detail({giveBackId:'+ full.id +'})">' +
                    '   <i class="fa fa-edit"></i>' +
                    '</button>&nbsp;';
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
            modalInstance.result.then(function (applyId) {
                GiveBackService.saveGiveBackEmpty(applyId).success(function (data) {
                    $state.go("give-back-detail",{giveBackId:data.id});
                });

            });
        }
    }
})();
