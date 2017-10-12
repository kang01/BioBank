/**
 * Created by gaokangkang on 2017/8/3.
 * 指定样本详情
 */
(function() {
    'use strict';

    angular
        .module('bioBankApp')
        .controller('RequirementAppointSampleDescModalController', RequirementAppointSampleDescModalController);

    RequirementAppointSampleDescModalController.$inject = ['$scope','$uibModalInstance','toastr','items','PaginationUtil','paginationConstants','DTColumnBuilder','RequirementService','BioBankDataTable'];

    function RequirementAppointSampleDescModalController($scope,$uibModalInstance,toastr,items,PaginationUtil,paginationConstants,DTColumnBuilder,RequirementService,BioBankDataTable) {
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
            RequirementService.assignSampleDes(sampleRequirementId).success(function (data) {
                for(var i =0; i < data.length; i++){
                    data[i].id = i+1;
                }
                vm.samples = _.chunk(data,2);

                for(var m = 0; m < vm.samples.length; m++){
                    var $tr = $("<tr>");

                    for(var n = 0; n < vm.samples[m].length; n++){
                        var $td = $("<td>");
                        var $id = $('<div class="col-md-2"></div>');
                        var $code = $('<div class="col-md-3 pl-0"></div>');
                        var $frozenBoxCode1D = $('<div class="col-md-3"></div>');
                        var $type = $('<div class="col-md-3"></div>');
                        $id.text(vm.samples[m][n].id);
                        $code.text(vm.samples[m][n].code);
                        $frozenBoxCode1D.text(vm.samples[m][n].frozenBoxCode1D);
                        $type.text(vm.samples[m][n].type);
                        $id.appendTo($td);
                        $code.appendTo($td);
                        $frozenBoxCode1D.appendTo($td);
                        $type.appendTo($td);
                        $td.appendTo($tr);
                    }

                    $(".sample-desc tbody").append($tr);
                }

            }).error(function (data) {

            });
            // RequirementAppointSampleDescService.query({
            //     requirementId:sampleRequirementId,
            //     page:vm.page-1,
            //     size: vm.itemsPerPage
            // }, onSuccess, onError);
            // function onSuccess(data,headers) {
            //     vm.totalItems = headers('X-Total-Count');
            //     vm.queryCount = vm.totalItems;
            //     vm.samples = _.chunk(data,3);
            // }
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
