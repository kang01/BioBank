(function() {
    'use strict';

    angular
        .module('bioBankApp')
        .controller('HomeController', HomeController);

    HomeController.$inject = ['$scope', 'Principal', 'LoginService', '$state','$uibModal','TranshipNewEmptyService','toastr','RequirementService','GiveBackService','$interval'];

    function HomeController ($scope, Principal, LoginService, $state,$uibModal,TranshipNewEmptyService,toastr,RequirementService,GiveBackService, $interval) {
        var vm = this;

        vm.account = null;
        vm.isAuthenticated = null;
        vm.login = LoginService.open;
        vm.register = register;
        //创建转运
        vm.addTransport = _fnAddTransport;
        //创建入库单
        vm.addStockIn = _fnAddStockIn;
        //创建申请
        vm.addRequirement = _fnAddRequirement;
        //创建归还盒
        vm.addGiveBack = _addGiveBack;
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
            var modalInstance = $uibModal.open({
                animation: true,
                templateUrl: 'app/bizs/stock-out/requirement/modal/requirement-add-modal.html',
                controller: 'RequirementAddModalController',
                controllerAs:'vm',
                backdrop:'static',
                resolve: {
                    items:{}
                }
            });
            modalInstance.result.then(function (requirementInfo) {
                RequirementService.saveRequirementEmpty(requirementInfo).success(function (data) {
                    $state.go('requirement-edit', {applyId: data.id, applyCode: data.applyCode});
                }).error(function (data) {
                    toastr.error(data.message);
                });
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
        function _addGiveBack() {
            var modalInstance = $uibModal.open({
                animation: true,
                templateUrl: 'app/bizs/give-back/modal/give-back-application-number-modal.html',
                controller: 'ApplicationNumberModalController',
                controllerAs:'vm',
                backdrop:'static',
                resolve: {
                    items:{}
                }
            });
            modalInstance.result.then(function (giveBackInfo) {
                GiveBackService.saveGiveBackEmpty(giveBackInfo).success(function (data) {
                    $state.go("give-back-detail",{giveBackId:data.id});
                }).error(function (data) {
                    toastr.error(data.message);
                });
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

        var inputValue = "";
        var clearTimer = null;
        $scope.$on("bbis:global:keyup", function(handler, $event){
            if (clearTimer){
                $interval.cancel(clearTimer);
                clearTimer = null;
            }
            if ($event.keyCode != 13){
                inputValue += String.fromCharCode($event.keyCode);
                clearTimer = $interval(function(){
                    inputValue = "";
                }, 50);
                return;
            }
            toastr.error(inputValue);
            inputValue = "";
        });
    }
})();
