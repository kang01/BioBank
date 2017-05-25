/**
 * Created by gaokangkang on 2017/5/12.
 * 出库交接详情页面
 */
(function() {
    'use strict';

    angular
        .module('bioBankApp')
        .controller('TakeOverViewController', TakeOverViewController);

    TakeOverViewController.$inject = ['$scope','$uibModal'];

    function TakeOverViewController($scope,$uibModal) {
        var vm = this;
        var modalInstance;
        //样本交接
        vm.takeOverModal = _fnTakeOverModal;


        function _fnTakeOverModal(){
            modalInstance = $uibModal.open({
                animation: true,
                templateUrl: 'app/bizs/stock-out/take-over/modal/take-over-modal.html',
                controller: 'TakeOverModalController',
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
