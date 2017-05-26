/**
 * Created by gaokangkang on 2017/5/12.
 * 任务中装盒操作
 */
(function() {
    'use strict';

    angular
        .module('bioBankApp')
        .controller('TaskBoxInModalController', TaskBoxInModalController);

    TaskBoxInModalController.$inject = ['$scope','$compile','$uibModalInstance','$uibModal','items','DTOptionsBuilder','DTColumnBuilder','PlanService'];

    function TaskBoxInModalController($scope,$compile,$uibModalInstance,$uibModal,items,DTOptionsBuilder,DTColumnBuilder,PlanService) {
        var vm = this;

        vm.tempBoxInstance = {};

        vm.tempBoxOptions = DTOptionsBuilder.newOptions()
            // .withOption('processing',true)
            // .withOption('serverSide',true)
            // .withFnServerData(function ( sSource, aoData, fnCallback, oSettings ) {
            //     var data = {};
            //     for(var i=0; aoData && i<aoData.length; ++i){
            //         var oData = aoData[i];
            //         data[oData.name] = oData.value;
            //     }
            //     var jqDt = this;
            //     if(vm.sampleIds){
            //         PlanService.queryPlanBoxes(vm.sampleIds,data).then(function (res){
            //             vm.selectAll = false;
            //             vm.selected = {};
            //             var json = res.data;
            //             var error = json.error || json.sError;
            //             if ( error ) {
            //                 jqDt._fnLog( oSettings, 0, error );
            //             }
            //             oSettings.json = json;
            //             fnCallback( json );
            //         }).catch(function(res){
            //             // console.log(res);
            //             //
            //             var ret = jqDt._fnCallbackFire( oSettings, null, 'xhr', [oSettings, null, oSettings.jqXHR] );
            //
            //             if ( $.inArray( true, ret ) === -1 ) {
            //                 if ( error == "parsererror" ) {
            //                     jqDt._fnLog( oSettings, 0, 'Invalid JSON response', 1 );
            //                 }
            //                 else if ( res.readyState === 4 ) {
            //                     jqDt._fnLog( oSettings, 0, 'Ajax error', 7 );
            //                 }
            //             }
            //             jqDt._fnProcessingDisplay( oSettings, false );
            //         });
            //     }else{
            //         var array = {
            //             draw : 1,
            //             recordsTotal : 100,
            //             recordsFiltered : 10,
            //             data: [ ],
            //             error : ""
            //         }
            //         fnCallback( array );
            //
            //     }
            //
            // })
            .withPaginationType('full_numbers')
            // .withOption('createdRow', function(row, data, dataIndex) {
            //     $compile(angular.element(row).contents())($scope);
            // })
            // .withOption('headerCallback', function(header) {
            //     $compile(angular.element(header).contents())($scope);
            // });

        // var titleHtml = '<input type="checkbox" ng-model="vm.selectAll" ng-click="vm.toggleAll()">';

        vm.tempBoxColumns = [
            // DTColumnBuilder.newColumn("").withOption("width", "30").withTitle(titleHtml).notSortable().renderWith(_fnRowSelectorRender),
            DTColumnBuilder.newColumn('frozenBoxCode').withTitle('临时盒编码'),
            DTColumnBuilder.newColumn('sampleTypeName').withTitle('盒内样本数'),
            DTColumnBuilder.newColumn('id').notVisible()
        ];


        function _fnRowSelectorRender(data, type, full, meta) {
            vm.selected[full.id] = false;
            var html = '';
            html = '<input type="checkbox" ng-model="vm.selected[' + full.id + ']" ng-click="vm.toggleOne(vm.selected)">';
            return html;
        }


        vm.ok = function () {
            $uibModalInstance.close();
        };
        vm.cancel = function () {
            $uibModalInstance.dismiss('cancel');
        };


    }
})();
