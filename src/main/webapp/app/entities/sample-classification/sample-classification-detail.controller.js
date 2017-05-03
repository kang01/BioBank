(function() {
    'use strict';

    angular
        .module('bioBankApp')
        .controller('SampleClassificationDetailController', SampleClassificationDetailController);

    SampleClassificationDetailController.$inject = ['$scope', '$rootScope', '$stateParams', 'previousState', 'entity', 'SampleClassification'];

    function SampleClassificationDetailController($scope, $rootScope, $stateParams, previousState, entity, SampleClassification) {
        var vm = this;

        vm.sampleClassification = entity;
        vm.previousState = previousState.name;

        var unsubscribe = $rootScope.$on('bioBankApp:sampleClassificationUpdate', function(event, result) {
            vm.sampleClassification = result;
        });
        $scope.$on('$destroy', unsubscribe);
    }
})();
