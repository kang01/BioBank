/**
 * Created by gaokangkang on 2017/6/29.
 */
(function() {
    'use strict';

    angular
        .module('bioBankApp')
        .controller('SampleInventoryDescController', SampleInventoryDescController);

    SampleInventoryDescController.$inject = ['$scope','$compile','$stateParams','DTColumnBuilder','BioBankDataTable','MasterData','SampleInventoryService'];

    function SampleInventoryDescController($scope,$compile,$stateParams,DTColumnBuilder,BioBankDataTable,MasterData,SampleInventoryService) {
        var vm = this;
        var sampleId = $stateParams.id;
        SampleInventoryService.querySampleDesList(sampleId).success(function (data) {
            vm.dtOptions.withOption('data', data);
        });
        SampleInventoryService.queryTubeDes(sampleId).success(function (data) {
           vm.entity = data;
           vm.status = MasterData.getStatus(vm.entity.status);
           vm.sex = MasterData.getSex(vm.entity.gender);
        });


        vm.dtOptions = BioBankDataTable.buildDTOption("BASIC", null, 10)
            .withOption('createdRow', createdRow);


        vm.dtColumns = [
            DTColumnBuilder.newColumn('operateTime').withTitle('日期').withOption("width", "80").notSortable(),
            DTColumnBuilder.newColumn('type').withTitle('变更').withOption("width", "50").notSortable(),
            DTColumnBuilder.newColumn('position').withTitle('冻存位置').withOption("width", "140").notSortable(),
            DTColumnBuilder.newColumn('frozenBoxCode').withTitle('冻存盒编码').withOption("width", "100").notSortable(),
            DTColumnBuilder.newColumn('positionInBox').withTitle('盒内位置').withOption("width", "80").notSortable(),
            DTColumnBuilder.newColumn('operator').withTitle('操作员').withOption("width", "80").notSortable(),
            DTColumnBuilder.newColumn('memo').withTitle('批注').withOption("width", "auto").notSortable(),
            DTColumnBuilder.newColumn('status').withTitle('状态').withOption("width", "80").notSortable()
        ];
        function createdRow(row, data, dataIndex) {
            var type = "";
            var status = "";
            var operateTime = moment(data.operateTime).format("YYYY-MM-DD");
            switch(data.type){
                case "101" :
                    type = "转运" , status = "待入库"; break;
                case "102" :
                    type = "入库", status = "已入库";break;
                case "103" :
                    type = "出库", status = "已出库";break;
                case "104" :
                    type = "交接", status = "已交接";break;
                case "105" :
                    type = "移位", status = "已入库";break;
                case "106" :
                    type = "换位", status = "已换位";break;
                case "107" :
                    type = "销毁", status = "已销毁";break;
            }
            $('td:eq(0)', row).html(operateTime);
            $('td:eq(1)', row).html(type);
            $('td:eq(7)', row).html(status);
            $compile(angular.element(row).contents())($scope);
        }
    }
})();
