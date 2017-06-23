/**
 * Created by gaokangkang on 2017/6/20.
 */
(function() {
    'use strict';

    angular
        .module('bioBankApp')
        .controller('StockInAddSampleModal', StockInAddSampleModal);

    StockInAddSampleModal.$inject = ['$uibModalInstance','items','toastr'];

    function StockInAddSampleModal($uibModalInstance,items,toastr) {
        var vm = this;
        vm.status = items.status;
        vm.tubes = items.tubes;
        var oldTube = items.oldTube;
        vm.sampleBoxSelect = _fnSampleBoxSelect;
        var tube;
        function _fnSampleBoxSelect(item,$event) {
            tube = item;
            console.log(JSON.stringify(item));
            $($event.target).closest('table').find('.rowLight').removeClass("rowLight");
            $($event.target).closest('tr').addClass("rowLight");
        }
        vm.cancel = function () {
            $uibModalInstance.dismiss('cancel');
        };
        vm.ok = function () {
            if(oldTube.sampleTypeName != "98"){
                if(oldTube.sampleTypeId != tube.sampleTypeId){
                    toastr.error("不同样本类型不能被选择！");
                    return;
                }else{
                    if(oldTube.sampleClassificationId){
                        if(oldTube.sampleClassificationId != tube.sampleClassificationId){
                            toastr.error("不同样本分类不能被选择！");
                            return;
                        }
                    }

                }

            }
            $uibModalInstance.close(tube);
        };
    }
})();
