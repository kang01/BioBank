/**
 * Created by gaokangkang on 2017/3/21.
 */
(function() {
    'use strict';

    angular
        .module('bioBankApp')
        .controller('TubesRemarkModalController', TubesRemarkModalController);

    TubesRemarkModalController.$inject = ['$scope','$uibModalInstance','$uibModal','items'];

    function TubesRemarkModalController($scope,$uibModalInstance,$uibModal,items) {
        var vm = this;
        vm.items = items;
        $("#tubeContent").focus();
        // $scope.$apply();
        this.cancel = function () {
            $uibModalInstance.dismiss('cancel');
        };
        this.ok = function () {
            $uibModalInstance.close(vm.memo);
        };
    }
})();
