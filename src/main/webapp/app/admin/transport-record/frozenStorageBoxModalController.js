/**
 * Created by gaokangkang on 2017/3/14.
 */
(function() {
    'use strict';

    angular
        .module('bioBankApp')
        .controller('FrozenStorageBoxModalController', FrozenStorageBoxModalController)
        .controller('ModalInstanceCtrl', ModalInstanceCtrl);

    FrozenStorageBoxModalController.$inject = ['DTOptionsBuilder','DTColumnBuilder','$uibModalInstance','$uibModal'];
    ModalInstanceCtrl.$inject = ['$uibModalInstance','$uibModal'];

    function FrozenStorageBoxModalController(DTOptionsBuilder,DTColumnBuilder,$uibModalInstance,$uibModal) {

        var vm = this;
        vm.importSample = importSample;//导入样本数据
        vm.addData = addData; //按键事件
        vm.boxCodeList = [];//冻存盒信息
        var codeList = [];//扫码录入的盒号
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
                    for(var i = 0; i < codeList.length; i++){
                        vm.boxCodeList[i] = {
                            frozenBoxCode:codeList[i],
                            frozenBoxTypeId:17,//冻存盒类型ID 17：10*10 18：8*8
                            frozenBoxTypeCode:'BOX_TYPE_0002',
                            isSplit: "0003",//是否分装:'否:0003 是：0002',
                            memo:'',
                            status: "3003",//状态
                            dislocationNumber:0,
                            emptyHoleNumber:0,
                            emptyTubeNumber:0,
                            frozenBoxColumns:10,
                            frozenBoxRows:10,
                            equipmentId:'',
                            equipmentCode:'',
                            areaId:'',
                            areaCode:'',
                            supportRackId:'',
                            supportRackCode:'',
                            columnsInShelf:"",//所在架子行数
                            rowsInShelf:"",//所在架子列数,
                            isRealData:'4002',
                            sampleNumber: 100,//样本数量
                            sampleTypeId: '',//样本类型ID
                            sampleTypeCode:'',//样本类型
                            frozenTubeDTOS:"",//管子
                            sampleTypeName:'',
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
            $uibModalInstance.close(vm.boxCodeList);
        };
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


