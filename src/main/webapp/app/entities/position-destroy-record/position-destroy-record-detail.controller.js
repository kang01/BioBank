(function() {
    'use strict';

    angular
        .module('bioBankApp')
        .controller('PositionDestroyRecordDetailController', PositionDestroyRecordDetailController);

    PositionDestroyRecordDetailController.$inject = ['$scope', '$rootScope', '$stateParams', 'previousState', 'entity', 'PositionDestroyRecord'];

    function PositionDestroyRecordDetailController($scope, $rootScope, $stateParams, previousState, entity, PositionDestroyRecord) {
        var vm = this;

        vm.positionDestroyRecord = entity;
        vm.previousState = previousState.name;

        var unsubscribe = $rootScope.$on('bioBankApp:positionDestroyRecordUpdate', function(event, result) {
            vm.positionDestroyRecord = result;
        });
        $scope.$on('$destroy', unsubscribe);
    }
})();
