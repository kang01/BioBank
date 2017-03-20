(function() {
    'use strict';

    angular
        .module('bioBankApp')
        .controller('FrozenTubeRecordDetailController', FrozenTubeRecordDetailController);

    FrozenTubeRecordDetailController.$inject = ['$scope', '$rootScope', '$stateParams', 'previousState', 'entity', 'FrozenTubeRecord', 'SampleType', 'FrozenTubeType'];

    function FrozenTubeRecordDetailController($scope, $rootScope, $stateParams, previousState, entity, FrozenTubeRecord, SampleType, FrozenTubeType) {
        var vm = this;

        vm.frozenTubeRecord = entity;
        vm.previousState = previousState.name;

        var unsubscribe = $rootScope.$on('bioBankApp:frozenTubeRecordUpdate', function(event, result) {
            vm.frozenTubeRecord = result;
        });
        $scope.$on('$destroy', unsubscribe);
    }
})();
