(function() {
    'use strict';

    angular
        .module('bioBankApp')
        .controller('RelationsDetailController', RelationsDetailController);

    RelationsDetailController.$inject = ['$scope', '$rootScope', '$stateParams', 'previousState', 'entity', 'Relations', 'FrozenBoxType', 'FrozenTubeType', 'SampleType'];

    function RelationsDetailController($scope, $rootScope, $stateParams, previousState, entity, Relations, FrozenBoxType, FrozenTubeType, SampleType) {
        var vm = this;

        vm.relations = entity;
        vm.previousState = previousState.name;

        var unsubscribe = $rootScope.$on('bioBankApp:relationsUpdate', function(event, result) {
            vm.relations = result;
        });
        $scope.$on('$destroy', unsubscribe);
    }
})();
