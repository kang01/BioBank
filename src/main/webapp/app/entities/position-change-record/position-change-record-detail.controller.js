(function() {
    'use strict';

    angular
        .module('bioBankApp')
        .controller('PositionChangeRecordDetailController', PositionChangeRecordDetailController);

    PositionChangeRecordDetailController.$inject = ['$scope', '$rootScope', '$stateParams', 'previousState', 'entity', 'PositionChangeRecord', 'PositionChange'];

    function PositionChangeRecordDetailController($scope, $rootScope, $stateParams, previousState, entity, PositionChangeRecord, PositionChange) {
        var vm = this;

        vm.positionChangeRecord = entity;
        vm.previousState = previousState.name;

        var unsubscribe = $rootScope.$on('bioBankApp:positionChangeRecordUpdate', function(event, result) {
            vm.positionChangeRecord = result;
        });
        $scope.$on('$destroy', unsubscribe);
    }
})();
