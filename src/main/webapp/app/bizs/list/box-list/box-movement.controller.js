/**
 * Created by gaokangkang on 2017/6/30.
 */
(function() {
    'use strict';

    angular
        .module('bioBankApp')
        .controller('BoxMovementController', BoxMovementController)
        .controller('ModalInstanceCtrl', ModalInstanceCtrl);

    BoxMovementController.$inject = ['$scope','$compile','$state','$stateParams','$uibModal','DTColumnBuilder','ProjectService','EquipmentService','AreasByEquipmentIdService','SupportacksByAreaIdService','EquipmentInventoryService','BioBankDataTable'];
    ModalInstanceCtrl.$inject = ['$uibModalInstance'];

    function BoxMovementController($scope,$compile,$state,$stateParams,$uibModal,DTColumnBuilder,ProjectService,EquipmentService,AreasByEquipmentIdService,SupportacksByAreaIdService,EquipmentInventoryService,BioBankDataTable) {
        var vm = this;
        vm.dtInstance = {};
        vm.dto = {};
        vm.authorities = ['ROLE_USER', 'ROLE_ADMIN'];
        var selectedBox = $stateParams.selectedBox || [];
        function _init() {}
        _init();

        vm.close = _fnClose;
        function _fnClose() {
            if(selectedBox.length){
                var modalInstance = $uibModal.open({
                    templateUrl: 'myModalContent.html',
                    controller: 'ModalInstanceCtrl',
                    controllerAs: 'vm'
                });
                modalInstance.result.then(function () {
                    $state.go("box-inventory");
                }, function () {
                });
            }else{
                $state.go("box-inventory");
            }

        }

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
    function ModalInstanceCtrl($uibModalInstance) {
        var vm = this;
        vm.ok = function () {
            $uibModalInstance.close();
        };
        vm.cancel = function () {
            $uibModalInstance.dismiss('cancel');
        };
    }
})();
