(function() {
    'use strict';

    angular
        .module('bioBankApp')
        .controller('HomeController', HomeController);

    HomeController.$inject = ['$scope', 'Principal', 'LoginService', '$state','TranshipNewEmptyService','toastr','RequirementService'];

    function HomeController ($scope, Principal, LoginService, $state,TranshipNewEmptyService,toastr,RequirementService) {
        var vm = this;

        vm.account = null;
        vm.isAuthenticated = null;
        vm.login = LoginService.open;
        vm.register = register;
        //创建转运
        vm.addTransport = _fnAddTransport;
        vm.addRequirement = _fnAddRequirement;
        $scope.$on('authenticationSuccess', function() {
            getAccount();
        });

        getAccount();

        function getAccount() {
            Principal.identity().then(function(account) {
                vm.account = account;
                vm.isAuthenticated = Principal.isAuthenticated;
            });
        }
        function register () {
            $state.go('register');
        }
        function _fnAddRequirement() {
            RequirementService.saveRequirementEmpty().success(function (data) {
                $state.go('requirement-edit', {applyId: data.id, applyCode: data.applyCode});
            });

        }
        function _fnAddTransport() {
            TranshipNewEmptyService.save({},onTranshipNewEmptyService,onError);
        }

        function onTranshipNewEmptyService(data) {
            $state.go('transport-record-edit',{transhipId : data.id,transhipCode : data.transhipCode});
        }
        function onError(error) {
            toastr.error(error.message);
        }
        vm.create = function () {
            vm.isActive = true;
        };
        vm.close = function () {
            vm.isActive = false;
        }
    }
})();
