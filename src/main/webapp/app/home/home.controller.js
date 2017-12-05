(function() {
    'use strict';

    angular
        .module('bioBankApp')
        .controller('HomeController', HomeController);

    HomeController.$inject = ['$scope', 'Principal', 'LoginService', '$state','$uibModal','TranshipNewEmptyService','toastr','RequirementService'];

    function HomeController ($scope, Principal, LoginService, $state,$uibModal,TranshipNewEmptyService,toastr,RequirementService) {
        var vm = this;

        vm.account = null;
        vm.isAuthenticated = null;
        vm.login = LoginService.open;
        vm.register = register;
        //创建转运
        vm.addTransport = _fnAddTransport;
        //创建入库单
        vm.addStockIn = _fnAddStockIn;
        vm.addRequirement = _fnAddRequirement;
        var modalInstance;

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
            modalInstance = $uibModal.open({
                animation: true,
                templateUrl: 'app/bizs/transport-record/modal/transport-record-select-project-modal.html',
                controller: 'SelectProjectModalController',
                backdrop:'static',
                controllerAs: 'vm'
            });
            modalInstance.result.then(function (project) {
                TranshipNewEmptyService.saveTransportEmpty(project.projectId,project.projectSiteId).success(function (data) {
                    $state.go('transport-record-edit',{
                        transhipId : data.id,
                        transhipCode : data.transhipCode
                    });
                });
            });
        }
        function _fnAddStockIn() {
            var modalInstance = $uibModal.open({
                animation: true,
                templateUrl: 'app/bizs/stock-in/modal/stock-in-new-modal.html',
                controller: 'StockInNewModalController',
                controllerAs:'vm',
                backdrop:'static',
                resolve: {
                    items:{}
                }
            });
            modalInstance.result.then(function (data) {
                $state.go("stock-in-add-box-edit", {id: data});
            });
        }

        // function onTranshipNewEmptyService(data) {
        //     $state.go('transport-record-edit',{transhipId : data.id,transhipCode : data.transhipCode});
        // }
        function onError(error) {
            toastr.error(error.data.message);
        }
        vm.create = function () {
            vm.isActive = true;
        };
        vm.close = function () {
            vm.isActive = false;
        }
    }
})();
