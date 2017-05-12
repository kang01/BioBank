/**
 * Created by gaokangkang on 2017/5/12.
 * 出库申请详情页面
 */
(function() {
    'use strict';

    angular
        .module('bioBankApp')
        .controller('TaskDetailController', TaskDetailController);

    TaskDetailController.$inject = ['$scope','$uibModal'];

    function TaskDetailController($scope,$uibModal) {
        var vm = this;
        var modalInstance;
        //异常、撤销
        vm.abnormalModal = _fnAbnormalModal;
        //未出库样本、已出库样本批注
        vm.commentModal = _fnCommentModal;
        //装盒
        vm.boxInModal = _fnBoxInModal;
        //出库
        vm.taskStockOutModal = _fnTaskStockOutModal;

        function _fnAbnormalModal() {
            modalInstance = $uibModal.open({
                animation: true,
                templateUrl: 'app/bizs/stock-out/task/modal/abnormal-recall-modal.html',
                controller: 'AbnormalRecallModalController',
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
        function _fnCommentModal() {
            modalInstance = $uibModal.open({
                animation: true,
                templateUrl: 'app/bizs/stock-out/task/modal/comment-modal.html',
                controller: 'TaskCommentModalController',
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
        function _fnBoxInModal() {
            modalInstance = $uibModal.open({
                animation: true,
                templateUrl: 'app/bizs/stock-out/task/modal/box-in-modal.html',
                controller: 'TaskBoxInModalController',
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
        function _fnTaskStockOutModal() {
            modalInstance = $uibModal.open({
                animation: true,
                templateUrl: 'app/bizs/stock-out/task/modal/box-in-modal.html',
                controller: 'TaskBoxInModalController',
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
