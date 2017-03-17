(function() {
    'use strict';

    angular
        .module('bioBankApp')
        .controller('EquipmentGroupDetailController', EquipmentGroupDetailController);

    EquipmentGroupDetailController.$inject = ['$scope', '$rootScope', '$stateParams', 'previousState', 'entity', 'EquipmentGroup'];

    function EquipmentGroupDetailController($scope, $rootScope, $stateParams, previousState, entity, EquipmentGroup) {
        var vm = this;

        vm.equipmentGroup = entity;
        vm.previousState = previousState.name;

        var unsubscribe = $rootScope.$on('bioBankApp:equipmentGroupUpdate', function(event, result) {
            vm.equipmentGroup = result;
        });
        $scope.$on('$destroy', unsubscribe);
    }
})();
