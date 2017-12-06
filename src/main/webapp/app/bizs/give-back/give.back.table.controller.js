/**
 * Created by gaokangkang on 2017/12/5.
 * 归还列表
 */
(function() {
    'use strict';

    angular
        .module('bioBankApp')
        .controller('GiveBackTableController', GiveBackTableController);

    GiveBackTableController.$inject = ['$scope', '$compile', 'BioBankDataTable','$uibModal', 'toastr', '$state','DTColumnBuilder'];
    function GiveBackTableController($scope, $compile, BioBankDataTable, $uibModal, toastr, $state,DTColumnBuilder) {
        var vm = this;

        vm.add = _add;



        //添加申请单号的归还详情
        function _add() {
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
            modalInstance.result.then(function () {
                $state.go("give-back-detail");
            });
        }
    }
})();
