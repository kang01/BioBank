(function() {
    'use strict';

    angular
        .module('bioBankApp')
        .controller('ProvinceDetailController', ProvinceDetailController);

    ProvinceDetailController.$inject = ['$scope', '$rootScope', '$stateParams', 'previousState', 'entity', 'Province'];

    function ProvinceDetailController($scope, $rootScope, $stateParams, previousState, entity, Province) {
        var vm = this;

        vm.province = entity;
        vm.previousState = previousState.name;

        var unsubscribe = $rootScope.$on('bioBankApp:provinceUpdate', function(event, result) {
            vm.province = result;
        });
        $scope.$on('$destroy', unsubscribe);
    }
})();
