/**
 * Created by gaokangkang on 2017/4/11.
 */
(function() {
    'use strict';

    angular
        .module('bioBankApp')
        .controller('StockInNewModalController', StockInNewModalController);

    StockInNewModalController.$inject = ['$uibModalInstance','$uibModal','toastr','StockInInputService','ProjectService','items'];

    function StockInNewModalController($uibModalInstance,$uibModal,toastr,StockInInputService,ProjectService,items) {
        var vm = this;

        vm.projectId = null;
        vm.stockInId = null;

        vm.cancel = function () {
            $uibModalInstance.dismiss('cancel');
        };
        vm.ok = function () {
            var entity = {projectId: vm.projectId};
            StockInInputService.saveStockIn(entity).success(function (data) {
                vm.stockInId = data.id;
                if(!vm.saveStockInFlag){
                    toastr.success("保存入库信息成功!");
                }
                $uibModalInstance.close(vm.stockInId);
            });
        };

        //项目
        ProjectService.query({},onProjectSuccess, onError);
        function onProjectSuccess(data) {
            vm.projectOptions = data;
            // if(!vm.projectId){
            //     vm.projectId = data[0].id;
            // }
        }
        function onError(error) {
            toastr.error(error.message);
        }
        //项目
        vm.projectConfig = {
            valueField:'id',
            labelField:'projectName',
            searchField:'projectName',
            maxItems: 1,
            onChange:function(value){}
        };
    }
})();
