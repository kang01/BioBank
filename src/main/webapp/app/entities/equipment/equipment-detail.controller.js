(function() {
    'use strict';

    angular
        .module('bioBankApp')
        .controller('EquipmentDetailController', EquipmentDetailController);

    EquipmentDetailController.$inject = ['$scope', '$rootScope', '$stateParams', 'previousState', 'entity', 'Equipment', 'EquipmentGroup', 'EquipmentModle'];

    function EquipmentDetailController($scope, $rootScope, $stateParams, previousState, entity, Equipment, EquipmentGroup, EquipmentModle) {
        var vm = this;

        vm.equipment = entity;
        vm.previousState = previousState.name;

        var unsubscribe = $rootScope.$on('bioBankApp:equipmentUpdate', function(event, result) {
            vm.equipment = result;
        });
        $scope.$on('$destroy', unsubscribe);
    }
})();
