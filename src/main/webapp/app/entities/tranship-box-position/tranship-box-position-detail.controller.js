(function() {
    'use strict';

    angular
        .module('bioBankApp')
        .controller('TranshipBoxPositionDetailController', TranshipBoxPositionDetailController);

    TranshipBoxPositionDetailController.$inject = ['$scope', '$rootScope', '$stateParams', 'previousState', 'entity', 'TranshipBoxPosition', 'Equipment', 'Area', 'SupportRack', 'TranshipBox'];

    function TranshipBoxPositionDetailController($scope, $rootScope, $stateParams, previousState, entity, TranshipBoxPosition, Equipment, Area, SupportRack, TranshipBox) {
        var vm = this;

        vm.transhipBoxPosition = entity;
        vm.previousState = previousState.name;

        var unsubscribe = $rootScope.$on('bioBankApp:transhipBoxPositionUpdate', function(event, result) {
            vm.transhipBoxPosition = result;
        });
        $scope.$on('$destroy', unsubscribe);
    }
})();
