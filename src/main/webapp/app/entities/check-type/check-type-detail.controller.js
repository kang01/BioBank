(function() {
    'use strict';

    angular
        .module('bioBankApp')
        .controller('CheckTypeDetailController', CheckTypeDetailController);

    CheckTypeDetailController.$inject = ['$scope', '$rootScope', '$stateParams', 'previousState', 'entity', 'CheckType'];

    function CheckTypeDetailController($scope, $rootScope, $stateParams, previousState, entity, CheckType) {
        var vm = this;

        vm.checkType = entity;
        vm.previousState = previousState.name;

        var unsubscribe = $rootScope.$on('bioBankApp:checkTypeUpdate', function(event, result) {
            vm.checkType = result;
        });
        $scope.$on('$destroy', unsubscribe);
    }
})();
