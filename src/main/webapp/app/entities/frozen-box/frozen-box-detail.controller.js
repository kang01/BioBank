(function() {
    'use strict';

    angular
        .module('bioBankApp')
        .controller('FrozenBoxDetailController', FrozenBoxDetailController);

    FrozenBoxDetailController.$inject = ['$scope', '$rootScope', '$stateParams', 'previousState', 'entity', 'FrozenBox', 'FrozenBoxType', 'SampleType'];

    function FrozenBoxDetailController($scope, $rootScope, $stateParams, previousState, entity, FrozenBox, FrozenBoxType, SampleType) {
        var vm = this;

        vm.frozenBox = entity;
        vm.previousState = previousState.name;

        var unsubscribe = $rootScope.$on('bioBankApp:frozenBoxUpdate', function(event, result) {
            vm.frozenBox = result;
        });
        $scope.$on('$destroy', unsubscribe);
    }
})();
