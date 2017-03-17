(function() {
    'use strict';

    angular
        .module('bioBankApp')
        .controller('SampleTypeDetailController', SampleTypeDetailController);

    SampleTypeDetailController.$inject = ['$scope', '$rootScope', '$stateParams', 'previousState', 'entity', 'SampleType'];

    function SampleTypeDetailController($scope, $rootScope, $stateParams, previousState, entity, SampleType) {
        var vm = this;

        vm.sampleType = entity;
        vm.previousState = previousState.name;

        var unsubscribe = $rootScope.$on('bioBankApp:sampleTypeUpdate', function(event, result) {
            vm.sampleType = result;
        });
        $scope.$on('$destroy', unsubscribe);
    }
})();
