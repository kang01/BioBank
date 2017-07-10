(function() {
    'use strict';

    angular
        .module('bioBankApp')
        .controller('SerialNoDetailController', SerialNoDetailController);

    SerialNoDetailController.$inject = ['$scope', '$rootScope', '$stateParams', 'previousState', 'entity', 'SerialNo'];

    function SerialNoDetailController($scope, $rootScope, $stateParams, previousState, entity, SerialNo) {
        var vm = this;

        vm.serialNo = entity;
        vm.previousState = previousState.name;

        var unsubscribe = $rootScope.$on('bioBankApp:serialNoUpdate', function(event, result) {
            vm.serialNo = result;
        });
        $scope.$on('$destroy', unsubscribe);
    }
})();
