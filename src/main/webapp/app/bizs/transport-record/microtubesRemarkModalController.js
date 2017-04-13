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

        this.cancel = function () {
            $uibModalInstance.dismiss('cancel');
        };
        this.ok = function () {
            for(var i = 0; i < vm.items.remarkArray.length; i++){
                for(var j =0; j < vm.items.remarkArray[i].length;j++){
                    if(vm.items.remarkArray[i][j].sampleCode){
                        vm.items.remarkArray[i][j].memo = vm.memo;
                    }
                }

            }
            $uibModalInstance.close(vm.items);
        };
    }
})();
