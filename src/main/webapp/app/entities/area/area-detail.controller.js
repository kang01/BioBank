(function() {
    'use strict';

    angular
        .module('bioBankApp')
        .controller('AreaDetailController', AreaDetailController);

    AreaDetailController.$inject = ['$scope', '$rootScope', '$stateParams', 'previousState', 'entity', 'Area', 'Equipment'];

    function AreaDetailController($scope, $rootScope, $stateParams, previousState, entity, Area, Equipment) {
        var vm = this;

        vm.area = entity;
        vm.previousState = previousState.name;

        var unsubscribe = $rootScope.$on('bioBankApp:areaUpdate', function(event, result) {
            vm.area = result;
        });
        $scope.$on('$destroy', unsubscribe);
    }
})();
