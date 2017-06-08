(function() {
    'use strict';

    angular
        .module('bioBankApp')
        .controller('TranshipTubeDetailController', TranshipTubeDetailController);

    TranshipTubeDetailController.$inject = ['$scope', '$rootScope', '$stateParams', 'previousState', 'entity', 'TranshipTube', 'TranshipBox', 'FrozenTube'];

    function TranshipTubeDetailController($scope, $rootScope, $stateParams, previousState, entity, TranshipTube, TranshipBox, FrozenTube) {
        var vm = this;

        vm.transhipTube = entity;
        vm.previousState = previousState.name;

        var unsubscribe = $rootScope.$on('bioBankApp:transhipTubeUpdate', function(event, result) {
            vm.transhipTube = result;
        });
        $scope.$on('$destroy', unsubscribe);
    }
})();
