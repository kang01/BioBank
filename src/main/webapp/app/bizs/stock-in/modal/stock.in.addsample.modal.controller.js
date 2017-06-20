/**
 * Created by gaokangkang on 2017/6/20.
 */
(function() {
    'use strict';

    angular
        .module('bioBankApp')
        .controller('StockInAddSampleModal', StockInAddSampleModal);

    StockInAddSampleModal.$inject = ['$uibModalInstance','items'];

    function StockInAddSampleModal($uibModalInstance,items) {
        var vm = this;
        vm.status = items.status;
        vm.tubes = items.tubes;
        vm.sampleBoxSelect = _fnSampleBoxSelect;
        var tube;
        function _fnSampleBoxSelect(item,$event) {
            tube = item;
            console.log(JSON.stringify(item));
            $($event.target).closest('ul').find('.box-selected').removeClass("box-selected");
            $($event.target).addClass("box-selected");
        }
        vm.cancel = function () {
            $uibModalInstance.close(false);
        };
        vm.ok = function () {
            $uibModalInstance.close(tube);
        };
    }
})();
