/**
 * Created by gaokangkang on 2017/6/30.
 */
(function() {
    'use strict';

    angular
        .module('bioBankApp')
        .controller('SampleMovementController', SampleMovementController);

    SampleMovementController.$inject = ['$scope','$compile','$state','$stateParams','$uibModal','DTColumnBuilder','ProjectService','EquipmentService','AreasByEquipmentIdService','SupportacksByAreaIdService','EquipmentInventoryService','BioBankDataTable'];

    function SampleMovementController($scope,$compile,$state,$stateParams,$uibModal,DTColumnBuilder,ProjectService,EquipmentService,AreasByEquipmentIdService,SupportacksByAreaIdService,EquipmentInventoryService,BioBankDataTable) {
        var vm = this;
        vm.dtInstance = {};
        vm.dto = {};
        vm.authorities = ['ROLE_USER', 'ROLE_ADMIN'];
        var selectedSample = $stateParams.selectedSample || [];
        function _init() {

        }
        _init();
        vm.close = _fnClose;
        function _fnClose() {
            if(selectedSample.length){
                var modalInstance = $uibModal.open({
                    templateUrl: 'myModalContent.html',
                    controller: 'ModalInstanceCtrl',
                    controllerAs: 'vm'
                });
                modalInstance.result.then(function () {
                    $state.go("sample-inventory");
                }, function () {
                });
            }else{
                $state.go("sample-inventory");
            }

        }
        vm.selectedOptions = BioBankDataTable.buildDTOption("BASIC", null, 10);
        vm.selectedColumns = [
            DTColumnBuilder.newColumn('sampleCode').withTitle('样本编码').withOption("width", "130"),
            DTColumnBuilder.newColumn('sampleType').withTitle('样本类型').withOption("width", "60"),
            DTColumnBuilder.newColumn('sampleClassification').withTitle('样本分类').withOption("width", "60"),

        ];
        vm.selectedOptions.withOption('data', selectedSample);


        function onError(error) {
            // BioBankBlockUi.blockUiStop();
            // toastr.error(error.data.message);
        }
    }
})();
