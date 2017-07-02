/**
 * Created by gaokangkang on 2017/6/30.
 */
(function() {
    'use strict';

    angular
        .module('bioBankApp')
        .controller('BoxMovementController', BoxMovementController);

    BoxMovementController.$inject = ['$scope','$compile','$state','$stateParams','DTColumnBuilder','ProjectService','EquipmentService','AreasByEquipmentIdService','SupportacksByAreaIdService','EquipmentInventoryService','BioBankDataTable'];

    function BoxMovementController($scope,$compile,$state,$stateParams,DTColumnBuilder,ProjectService,EquipmentService,AreasByEquipmentIdService,SupportacksByAreaIdService,EquipmentInventoryService,BioBankDataTable) {
        var vm = this;
        vm.dtInstance = {};
        vm.dto = {};
        vm.authorities = ['ROLE_USER', 'ROLE_ADMIN'];
        var selectedBox = $stateParams.selectedBox;
        function _init() {

        }
        _init();





        vm.selectedOptions = BioBankDataTable.buildDTOption("NORMALLY", null, 10)
            .withOption('searching', false);


        vm.selectedColumns = [
            DTColumnBuilder.newColumn('frozenBoxCode').withTitle('冻存盒编码').withOption("width", "110"),
            DTColumnBuilder.newColumn('sampleType').withTitle('样本类型').withOption("width", "60"),
            DTColumnBuilder.newColumn('sampleClassification').withTitle('样本分类').withOption("width", "60"),

        ];
        vm.selectedOptions.withOption('data', selectedBox);


        function onError(error) {
            // BioBankBlockUi.blockUiStop();
            // toastr.error(error.data.message);
        }
    }
})();
