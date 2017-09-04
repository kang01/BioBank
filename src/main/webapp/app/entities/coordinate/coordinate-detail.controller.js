(function() {
    'use strict';

    angular
        .module('bioBankApp')
        .controller('CoordinateDetailController', CoordinateDetailController);

    CoordinateDetailController.$inject = ['$scope', '$rootScope', '$stateParams', 'previousState', 'entity', 'Coordinate'];

    function CoordinateDetailController($scope, $rootScope, $stateParams, previousState, entity, Coordinate) {
        var vm = this;

        vm.coordinate = entity;
        vm.previousState = previousState.name;

        var unsubscribe = $rootScope.$on('bioBankApp:coordinateUpdate', function(event, result) {
            vm.coordinate = result;
        });
        $scope.$on('$destroy', unsubscribe);
    }
})();
