/**
 * Created by gaokangkang on 2017/8/3.
 * 指定样本详情
 */
(function() {
    'use strict';

    angular
        .module('bioBankApp')
        .controller('RequirementAppointSampleDescModalController', RequirementAppointSampleDescModalController);

    RequirementAppointSampleDescModalController.$inject = ['$scope','$uibModalInstance','toastr','items','PaginationUtil','paginationConstants','DTColumnBuilder','RequirementAppointSampleDescService','BioBankDataTable'];

    function RequirementAppointSampleDescModalController($scope,$uibModalInstance,toastr,items,PaginationUtil,paginationConstants,DTColumnBuilder,RequirementAppointSampleDescService,BioBankDataTable) {
        var vm = this;
        vm.transition = transition;
        //样本需求ID
        var sampleRequirementId = items.sampleRequirementId;
        //一页30条数据
        vm.itemsPerPage = 30;
        //第几页
        vm.page = 1;

        //加载指定样本详情
        function loadSample() {
            RequirementAppointSampleDescService.query({
                requirementId:sampleRequirementId,
                page:vm.page-1,
                size: vm.itemsPerPage
            }, onSuccess, onError);
            function onSuccess(data,headers) {
                vm.totalItems = headers('X-Total-Count');
                vm.queryCount = vm.totalItems;
                vm.samples = _.chunk(data,3);
            }
        }
        loadSample();
        //每次分页都需要请求数据
        function transition() {
            loadSample();
        }

        function onError(data) {
            toastr.error(data.message)
        }

        // vm.ok = function () {
        //     $uibModalInstance.close();
        // };
        vm.cancel = function () {
            $uibModalInstance.dismiss('cancel');
        };
    }
})();
