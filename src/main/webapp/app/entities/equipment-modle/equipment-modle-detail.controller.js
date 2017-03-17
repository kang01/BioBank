(function() {
    'use strict';

    angular
        .module('bioBankApp')
        .controller('EquipmentModleDetailController', EquipmentModleDetailController);

    EquipmentModleDetailController.$inject = ['$scope', '$rootScope', '$stateParams', 'previousState', 'entity', 'EquipmentModle'];

    function EquipmentModleDetailController($scope, $rootScope, $stateParams, previousState, entity, EquipmentModle) {
        var vm = this;

        vm.equipmentModle = entity;
        vm.previousState = previousState.name;

        var unsubscribe = $rootScope.$on('bioBankApp:equipmentModleUpdate', function(event, result) {
            vm.equipmentModle = result;
        });
        $scope.$on('$destroy', unsubscribe);
    }
})();
