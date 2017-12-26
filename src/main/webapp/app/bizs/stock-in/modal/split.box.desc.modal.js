/**
 * Created by gaokangkang on 2017/12/25.
 */
(function() {
    'use strict';

    angular
        .module('bioBankApp')
        .controller('SplitBoxDescModalController', SplitBoxDescModalController);

    SplitBoxDescModalController.$inject = ['$scope','$uibModalInstance','items','StockInInputService'];

    function SplitBoxDescModalController($scope,$uibModalInstance,items,StockInInputService) {
        var vm = this;
        vm.box = angular.copy(items.box);
        vm.tubes = vm.box.stockInFrozenTubeList;
        vm.htInstance = {};

        setTimeout(function () {
            vm.htInstance.api.loadData(vm.box, vm.tubes);
            vm.htInstance.api.updateSettings({
                isCellValueEditable: false,
                multiSelect:false
            });
        },500);

        vm.ok = function () {
            $uibModalInstance.close();
        };

        vm.cancel = function () {
            $uibModalInstance.dismiss('cancel');
        };
    }
})();
