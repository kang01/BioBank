(function() {
    'use strict';

    angular
        .module('bioBankApp')
        .controller('DelegateDetailController', DelegateDetailController);

    DelegateDetailController.$inject = ['$scope', '$rootScope', '$stateParams', 'previousState', 'entity', 'Delegate'];

    function DelegateDetailController($scope, $rootScope, $stateParams, previousState, entity, Delegate) {
        var vm = this;

        vm.delegate = entity;
        vm.previousState = previousState.name;

        var unsubscribe = $rootScope.$on('bioBankApp:delegateUpdate', function(event, result) {
            vm.delegate = result;
        });
        $scope.$on('$destroy', unsubscribe);
    }
})();
