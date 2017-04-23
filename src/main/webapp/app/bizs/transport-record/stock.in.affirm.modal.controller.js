/**
 * Created by gaoyankang on 2017/4/13.
 */
(function() {
    'use strict';

    angular
        .module('bioBankApp')
        .controller('StockInAffirmModalController', StockInAffirmModalController)

    StockInAffirmModalController.$inject = ['$uibModalInstance','$uibModal','items'];

    function StockInAffirmModalController($uibModalInstance,$uibModal,items) {

        var vm = this;
        vm.box = items.box;
        vm.boxRowCol = items.boxRowCol;
        if(!vm.box.equipmentId || !vm.box.areaId || !vm.box.supportRackId || !vm.boxRowCol){
            vm.placeFlag = true
        }else{
            vm.placeFlag = false;
        }
        vm.cancel = function () {
            $uibModalInstance.dismiss('cancel');
        };
        vm.ok = function () {
            $uibModalInstance.close(true);
        };
    }
})();
