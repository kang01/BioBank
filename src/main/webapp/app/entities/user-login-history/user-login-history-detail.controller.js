(function() {
    'use strict';

    angular
        .module('bioBankApp')
        .controller('UserLoginHistoryDetailController', UserLoginHistoryDetailController);

    UserLoginHistoryDetailController.$inject = ['$scope', '$rootScope', '$stateParams', 'previousState', 'entity', 'UserLoginHistory'];

    function UserLoginHistoryDetailController($scope, $rootScope, $stateParams, previousState, entity, UserLoginHistory) {
        var vm = this;

        vm.userLoginHistory = entity;
        vm.previousState = previousState.name;

        var unsubscribe = $rootScope.$on('bioBankApp:userLoginHistoryUpdate', function(event, result) {
            vm.userLoginHistory = result;
        });
        $scope.$on('$destroy', unsubscribe);
    }
})();
