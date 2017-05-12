/**
 * Created by gaokangkang on 2017/5/12.
 * 出库申请详情页面
 */
(function() {
    'use strict';

    angular
        .module('bioBankApp')
        .controller('PlanDetailController', PlanDetailController);

    PlanDetailController.$inject = ['$scope','$uibModal'];

    function PlanDetailController($scope,$uibModal) {
        var vm = this;
        var modalInstance;
        //查看出库任务列表详情
        vm.taskDescModal = _fnTaskDescModal;

        function _fnTaskDescModal() {
            modalInstance = $uibModal.open({
                animation: true,
                templateUrl: 'app/bizs/stock-out/plan/modal/plan-task-desc-modal.html',
                controller: 'PlanTaskDescModalController',
                controllerAs:'vm',
                size:'lg',
                resolve: {
                    items: function () {
                        return {
                        }
                    }
                }
            });

            modalInstance.result.then(function (data) {

            });
        }
        function _fnSampleDescModal() {
            modalInstance = $uibModal.open({
                animation: true,
                templateUrl: 'app/bizs/stock-out/requirement/modal/requirement-sample-desc-modal.html',
                controller: 'RequirementSampleDescModalController',
                controllerAs:'vm',
                size:'lg',
                resolve: {
                    items: function () {
                        return {
                        }
                    }
                }
            });

            modalInstance.result.then(function (data) {

            });
        }
    }
})();
