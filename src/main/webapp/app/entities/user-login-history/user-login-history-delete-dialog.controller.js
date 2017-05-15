(function() {
    'use strict';

    angular
        .module('bioBankApp')
        .controller('UserLoginHistoryDeleteController',UserLoginHistoryDeleteController);

    UserLoginHistoryDeleteController.$inject = ['$uibModalInstance', 'entity', 'UserLoginHistory'];

    function UserLoginHistoryDeleteController($uibModalInstance, entity, UserLoginHistory) {
        var vm = this;

        vm.userLoginHistory = entity;
        vm.clear = clear;
        vm.confirmDelete = confirmDelete;

        function clear () {
            $uibModalInstance.dismiss('cancel');
        }

        function confirmDelete (id) {
            UserLoginHistory.delete({id: id},
                function () {
                    $uibModalInstance.close(true);
                });
        }
    }
})();
