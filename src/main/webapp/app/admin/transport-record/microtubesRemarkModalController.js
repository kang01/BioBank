/**
 * Created by gaokangkang on 2017/3/21.
 */
(function() {
    'use strict';

    angular
        .module('bioBankApp')
        .controller('microtubesRemarkModalController', microtubesRemarkModalController);

    microtubesRemarkModalController.$inject = ['$uibModalInstance','$uibModal','items'];

    function microtubesRemarkModalController($uibModalInstance,$uibModal,items) {
        var vm = this;
        vm.items = items;
        // console.log(JSON.stringify(vm.items));

        this.cancel = function () {
            $uibModalInstance.dismiss('cancel');
        };
        this.ok = function () {
            for(var i = 0; i < vm.items.domArray.length; i++){
                if(vm.items.domArray[i].value){
                    vm.items.domArray[i].value.remark = vm.remark;
                }
            }
            $uibModalInstance.close(vm.items);
        };
    }
})();
