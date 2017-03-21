(function() {
    'use strict';

    angular
        .module('bioBankApp')
        .controller('TranshipBoxDetailController', TranshipBoxDetailController);

    TranshipBoxDetailController.$inject = ['$scope', '$rootScope', '$stateParams', 'previousState', 'entity', 'TranshipBox', 'Tranship'];

    function TranshipBoxDetailController($scope, $rootScope, $stateParams, previousState, entity, TranshipBox, Tranship) {
        var vm = this;

        vm.transhipBox = entity;
        vm.previousState = previousState.name;

        var unsubscribe = $rootScope.$on('bioBankApp:transhipBoxUpdate', function(event, result) {
            vm.transhipBox = result;
        });
        $scope.$on('$destroy', unsubscribe);
    }
})();
