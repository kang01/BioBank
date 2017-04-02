/**
 * Created by gaokangkang on 2017/3/14.
 */
(function() {
    'use strict';

    angular
        .module('bioBankApp')
        .controller('FrozenStorageBoxModalController', FrozenStorageBoxModalController)
        .controller('ModalInstanceCtrl', ModalInstanceCtrl);

    FrozenStorageBoxModalController.$inject = ['DTOptionsBuilder','DTColumnBuilder','$uibModalInstance','$uibModal','items','TranshipBoxService'];
    ModalInstanceCtrl.$inject = ['$uibModalInstance','$uibModal'];

    function FrozenStorageBoxModalController(DTOptionsBuilder,DTColumnBuilder,$uibModalInstance,$uibModal,items,TranshipBoxService) {

        var vm = this;
        vm.items = items;
        vm.importSample = importSample;//导入样本数据
        vm.addData = addData; //按键事件
        var codeList = [];//扫码录入的盒号
        vm.obox = {
            transhipId:vm.items.transhipId,
            frozenBoxDTOList:[]
        };

        vm.dtOptions = DTOptionsBuilder.newOptions()
            .withOption('searching', false)
            .withOption('paging', false)
            .withOption('sorting', false)
            .withScroller()
            .withOption('deferRender', true)
            .withOption('scrollY', 300);

        function addData(event) {
            if(window.event.keyCode == 13){
                if(vm.boxCode != ''){
                    codeList = _.uniq((vm.boxCode.split("\n")).reverse());
                    var tubeList=[];

                    for(var i = 0; i < codeList.length; i++){
                        vm.obox.frozenBoxDTOList[i] = {
                            frozenBoxCode:codeList[i],
                            frozenBoxTypeId:17,//冻存盒类型ID 17：10*10 18：8*8
                            isSplit: 0,//是否分装:'否:0003 是：0002',
                            status: "2001",//状态
                            sampleTypeId: 5,//样本类型ID
                            frozenTubeDTOS:[]
                        };
                        for(var j = 0; j < 10;j++){
                            tubeList[j] = [];
                            for(var k = 0; k < 10; k++){
                                tubeList[j][k] = {
                                    frozenBoxCode: codeList[i],
                                    frozenTubeCode:codeList[i]+j,
                                    sampleCode: "",
                                    sampleTempCode: codeList[i]+"-"+String.fromCharCode(j+65)+(k+1),
                                    sampleTypeId: 5,
                                    status: "3003",
                                    memo:"",
                                    tubeColumns: k+1,
                                    tubeRows: String.fromCharCode(j+65)
                                };
                                vm.obox.frozenBoxDTOList[i].frozenTubeDTOS.push(tubeList[j][k])
                            }
                        }
                    }
                }
            }
        }
        function importSample() {
            var modalInstance = $uibModal.open({
                animation: true,
                templateUrl: 'stackedModal.html',
                size: 'sm',
                controller: 'ModalInstanceCtrl',
                controllerAs: 'ctrl'
            });
            modalInstance.result.then(function (flag) {

            }, function () {
            });
        }
        this.cancel = function () {
            $uibModalInstance.dismiss('cancel');
        };
        this.ok = function () {
            console.log(JSON.stringify(vm.obox));
            TranshipBoxService.save(vm.obox,onSaveBoxSuccess,onError);

        };
        function onSaveBoxSuccess(data) {
            $uibModalInstance.close();
        }
        function onError() {

        }

    }
    function ModalInstanceCtrl($uibModalInstance,$uibModal) {
        var ctrl = this;
        ctrl.ok = function () {
            $uibModalInstance.close(true);
        };
        ctrl.cancel = function () {
            $uibModalInstance.dismiss('cancel');
        };
    }
})();


