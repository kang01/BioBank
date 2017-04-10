/**
 * Created by gaoyankang on 2017/4/9.
 */
(function() {
    'use strict';

    angular
        .module('bioBankApp')
        .controller('AddBoxModalController', AddBoxModalController);

    AddBoxModalController.$inject = ['$uibModalInstance','$uibModal','items','AlertService'];

    function AddBoxModalController($uibModalInstance,$uibModal,items,AlertService) {
        var vm = this;
        vm.box = {
            frozenBoxRows:10,
            frozenBoxColumns:10,
            frozenBoxCode:'',
            sampleType:{},
            stockInFrozenTubeList:[]
        };
        // var tubeList = [];
        // for(var j = 0; j < 10;j++){
        //     tubeList[j] = [];
        //     for(var k = 0; k < 10; k++){
        //         tubeList[j][k] = {
        //             frozenBoxCode: vm.box.frozenBoxCode,
        //             tubeColumns: k+1,
        //             tubeRows: String.fromCharCode(j+65)
        //         };
        //         vm.box.stockInFrozenTubeList.push(tubeList[j][k])
        //     }
        // }
        vm.sampleTypesOptions = items.sampleTypes;
        vm.sampleTypesConfig = {
            valueField:'sampleTypeCode',
            labelField:'sampleTypeName',
            maxItems: 1,
            onChange:function (value) {
                for(var i =0; i < vm.sampleTypesOptions.length; i++){
                    if(value == vm.sampleTypesOptions[i].sampleTypeCode){
                        vm.box.sampleType.sampleTypeName = vm.sampleTypesOptions[i].sampleTypeName;
                        vm.box.sampleType.backColor = vm.sampleTypesOptions[i].backColor
                    }
                }
            }
        };
        vm.cancel = function () {
            $uibModalInstance.dismiss('cancel');
        };
        vm.ok = function () {
            $uibModalInstance.close(vm.box);
        };
    }
})();
