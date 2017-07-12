(function() {
    'use strict';

    angular
        .module('bioBankApp')
        .controller('PositionMoveRecordDetailController', PositionMoveRecordDetailController);

    PositionMoveRecordDetailController.$inject = ['$scope', '$rootScope', '$stateParams', 'previousState', 'entity', 'PositionMoveRecord', 'Equipment', 'Area', 'SupportRack', 'FrozenBox', 'FrozenTube', 'Project', 'ProjectSite', 'PositionMove'];

    function PositionMoveRecordDetailController($scope, $rootScope, $stateParams, previousState, entity, PositionMoveRecord, Equipment, Area, SupportRack, FrozenBox, FrozenTube, Project, ProjectSite, PositionMove) {
        var vm = this;

        vm.positionMoveRecord = entity;
        vm.previousState = previousState.name;

        var unsubscribe = $rootScope.$on('bioBankApp:positionMoveRecordUpdate', function(event, result) {
            vm.positionMoveRecord = result;
        });
        $scope.$on('$destroy', unsubscribe);
    }
})();
