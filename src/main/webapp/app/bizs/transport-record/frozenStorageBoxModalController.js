/**
 * Created by gaokangkang on 2017/3/14.
 */
(function() {
    'use strict';

    angular
        .module('bioBankApp')
        .controller('FrozenStorageBoxModalController', FrozenStorageBoxModalController)
        .controller('ModalInstanceCtrl', ModalInstanceCtrl);

    FrozenStorageBoxModalController.$inject = ['$scope','AlertService','$timeout','DTOptionsBuilder','DTColumnBuilder','$uibModalInstance','$uibModal','items','TranshipBoxService','blockUI'];
    ModalInstanceCtrl.$inject = ['$uibModalInstance','$uibModal'];

    function FrozenStorageBoxModalController($scope,AlertService,$timeout,DTOptionsBuilder,DTColumnBuilder,$uibModalInstance,$uibModal,items,TranshipBoxService,blockUI) {

        var vm = this;
        vm.items = items;
        vm.importSample = importSample;//导入样本数据
        var codeList = [];//扫码录入的盒号
        var sampleTypeOptions = _.orderBy(items.sampleTypeOptions, ['sampleTypeCode'], ['esc']);
        var frozenBoxTypeOptions = _.orderBy(items.frozenBoxTypeOptions, ['id'], ['esc']);
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
        vm.myConfig = {
            create: true,
            persist:false,
            onChange: function(value){
                vm.obox.frozenBoxDTOList = [];
                if(value.length){
                    codeList = value.reverse();
                    var tubeList=[];
                    for(var i = 0; i < codeList.length; i++){
                        vm.obox.frozenBoxDTOList[i] = {
                            frozenBoxCode:codeList[i],
                            frozenBoxTypeId:frozenBoxTypeOptions[0].id,//冻存盒类型ID 17：10*10 18：8*8
                            isSplit: 0,//是否分装:'否:0003 是：0002',
                            status: "2001",//状态
                            sampleTypeId: sampleTypeOptions[0].id,//样本类型ID
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
                                    sampleTypeId: sampleTypeOptions[0].id,
                                    sampleTypeCode: sampleTypeOptions[0].sampleTypeCode,
                                    status: "3001",
                                    memo:"",
                                    tubeColumns: k+1,
                                    tubeRows: String.fromCharCode(j+65)
                                };
                                vm.obox.frozenBoxDTOList[i].frozenTubeDTOS.push(tubeList[j][k])
                            }
                        }

                    }
                }
                $scope.$apply();
            }
        };



        // function addData(event) {
        //     if(window.event.keyCode == 13){
        //
        //     }
        // }
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
            blockUI.start("正在保存冻存盒中……");
            TranshipBoxService.save(vm.obox,onSaveBoxSuccess,onError);

        };
        function onSaveBoxSuccess(data) {
            blockUI.stop();
            $uibModalInstance.close();

        }
        function onError(data) {
            console.log(data);
            AlertService.error(data.data.message);
            $timeout(function () {
                blockUI.stop();
            },1000)

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


