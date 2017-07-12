(function() {
    'use strict';

    angular
        .module('bioBankApp')
        .controller('PositionMoveRecordDialogController', PositionMoveRecordDialogController);

    PositionMoveRecordDialogController.$inject = ['$timeout', '$scope', '$stateParams', '$uibModalInstance', 'entity', 'PositionMoveRecord', 'Equipment', 'Area', 'SupportRack', 'FrozenBox', 'FrozenTube', 'Project', 'ProjectSite', 'PositionMove'];

    function PositionMoveRecordDialogController ($timeout, $scope, $stateParams, $uibModalInstance, entity, PositionMoveRecord, Equipment, Area, SupportRack, FrozenBox, FrozenTube, Project, ProjectSite, PositionMove) {
        var vm = this;

        vm.positionMoveRecord = entity;
        vm.clear = clear;
        vm.save = save;
        vm.equipment = Equipment.query();
        vm.areas = Area.query();
        vm.supportracks = SupportRack.query();
        vm.frozenboxes = FrozenBox.query();
        vm.frozentubes = FrozenTube.query();
        vm.projects = Project.query();
        vm.projectsites = ProjectSite.query();
        vm.positionmoves = PositionMove.query();

        $timeout(function (){
            angular.element('.form-group:eq(1)>input').focus();
        });

        function clear () {
            $uibModalInstance.dismiss('cancel');
        }

        function save () {
            vm.isSaving = true;
            if (vm.positionMoveRecord.id !== null) {
                PositionMoveRecord.update(vm.positionMoveRecord, onSaveSuccess, onSaveError);
            } else {
                PositionMoveRecord.save(vm.positionMoveRecord, onSaveSuccess, onSaveError);
            }
        }

        function onSaveSuccess (result) {
            $scope.$emit('bioBankApp:positionMoveRecordUpdate', result);
            $uibModalInstance.close(result);
            vm.isSaving = false;
        }

        function onSaveError () {
            vm.isSaving = false;
        }


    }
})();
