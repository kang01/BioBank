/**
 * Created by gaokangkang on 2017/12/25.
 */
(function() {
    'use strict';

    angular
        .module('bioBankApp')
        .controller('SplitBoxDescModalController', SplitBoxDescModalController);

    SplitBoxDescModalController.$inject = ['$scope','$uibModalInstance'];

    function SplitBoxDescModalController($scope,$uibModalInstance) {
        var vm = this;



        vm.ok = function () {
            $uibModalInstance.close();
        };

        vm.cancel = function () {
            $uibModalInstance.dismiss('cancel');
        };
    }
})();
