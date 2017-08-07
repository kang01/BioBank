/**
 * Created by gaokangkang on 2017/8/4.
 * 清单交换操作
 */
(function() {
    'use strict';

    angular
        .module('bioBankApp')
        .controller('ListExchangeDestroyModalController', ListExchangeDestroyModalController);

    ListExchangeDestroyModalController.$inject = ['$uibModalInstance','$uibModal','items','SampleUserService','toastr','Principal'];

    function ListExchangeDestroyModalController($uibModalInstance,$uibModal,items,SampleUserService,toastr,Principal) {

        var vm = this;
        //1.交换 2.销毁
        vm.operateStatus = items.operateStatus;
        vm.entity = {};
        //获取当前用户
        function _fnQueryCurrentUser() {
            Principal.identity().then(function(account) {
                vm.account = account;
                vm.entity.userId = vm.account.id;
            });
        }
        function _loadUser() {
            vm.userConfig = {
                valueField: 'id',
                labelField: 'userName',
                maxItems: 1,
                onChange:function (value) {
                }
            };
            //出库负责人
            SampleUserService.query({}, onUserSuccess, onError);
            function onUserSuccess(data) {
                vm.userOptions = data;
            }
        }
        _fnQueryCurrentUser();
        _loadUser();




        function onError(error) {
            toastr.error(error.data.message);
        }

        vm.ok = function () {
            $uibModalInstance.close(vm.entity);
        };
        vm.cancel = function () {
            $uibModalInstance.dismiss('cancel');
        };


    }
})();
