/**
 * Created by gaokangkang on 2017/12/5.
 */
(function() {
    'use strict';

    angular
        .module('bioBankApp')
        .controller('ImportBoxModalController', ImportBoxModalController);
    ImportBoxModalController.$inject = ['$scope','$q','toastr','$timeout','BioBankDataTable','BioBankSelectize','$uibModalInstance','$uibModal','items','TranshipBoxService','blockUI','blockUIConfig','AreasByEquipmentIdService','EquipmentAllService','SampleTypeService','TransportRecordService'];

    function ImportBoxModalController($scope,$q,toastr,$timeout,BioBankDataTable,BioBankSelectize,$uibModalInstance,$uibModal,items,TranshipBoxService,blockUI,blockUIConfig,AreasByEquipmentIdService,EquipmentAllService,SampleTypeService,TransportRecordService) {

        var vm = this;
        //设备
        vm.equipmentOptions = items.equipmentOptions;
        _fnBoxInfo();
        _fnBoxCodeInput();
        _fnBoxTable();


        //盒子信息
        function _fnBoxInfo() {
            _fnTempPos();
            //暂存位置
            function _fnTempPos(){
                //设备
                vm.equipmentConfig = {
                    valueField:'id',
                    labelField:'equipmentCode',
                    maxItems: 1,
                    onChange:function (value) {
                        if(value){
                            AreasByEquipmentIdService.query({id:value},onAreaTempSuccess, onError);
                        }else{
                            vm.frozenBoxHoldAreaOptions = [
                                {id:"",areaCode:""}
                            ];
                            vm.frozenBox.areaId = "";
                            $scope.$apply();
                        }
                    }
                };
                //区域
                vm.frozenBoxAreaConfigTemp = {
                    valueField:'id',
                    labelField:'areaCode',
                    maxItems: 1,
                    onChange:function (value) {
                        vm.frozenBox.areaId  = value;
                    }
                };

                function onAreaTempSuccess(data) {
                    vm.frozenBoxHoldAreaOptions = data;
                    vm.frozenBox.areaId = vm.frozenBoxHoldAreaOptions[0].id;
                }
            }
        }
        //盒子编码录入
        function _fnBoxCodeInput() {
            var changeBoxTableTimer = null;
            var selectizeObj = {
                create : true,
                persist:false,
                clearMaxItemFlag : true
            };
            vm.boxCodeConfig = BioBankSelectize.buildSettings(selectizeObj);

            vm.boxCodeConfig.onChange = function (value) {
                vm.boxCodeSelectize = vm.boxCodeConfig.selectizeInstance;
                clearTimeout(changeBoxTableTimer);
                changeBoxTableTimer = setTimeout(function () {
                    _changeBoxTable();
                    if ($scope.$digest()){
                        $scope.$apply();
                    }
                },300);
            };
            vm.boxCodeConfig.onItemRemove = function (value) {
                _.remove(boxList,{frozenBoxCode:value});
            };

            //更行盒子列表的数据
            var boxList = [];
            vm.boxList = [];
            function _changeBoxTable() {
                _.forEach(vm.arrayBoxCode,function (code) {
                    var len = _.filter(boxList,{frozenBoxCode:code}).length;
                    if(!len){
                        var box = _createTempBox(code);
                        boxList.push(box);
                    }
                });
                vm.boxList = _.orderBy(boxList, ['createTime'], ['desc']);
                //创建临时盒子
                function _createTempBox(code) {
                    var box = {
                        frozenBoxCode:null,
                        frozenBoxCode1D:null,
                        status:null,
                        sampleTypeName:null,
                        sampleTypeCode:null,
                        sampleTypeId:null,
                        isSplit:null,
                        countOfSample:null,
                        frozenTubeDTOS:[],
                        createTime:new Date().getTime()
                    };
                    box.frozenBoxCode = code;

                    return box;
                }

            }
        }
        //盒子详情
        function _fnBoxTable() {
            var columns = [
                {name:"0",width:"auto"},
                {name:"1",width:"auto"},
                {name:"2",width:"80"},
                {name:"3",width:"80"},
                {name:"4",width:"80"},
                {name:"5",width:"80"},
                {name:"6",width:"80"}
            ];
            vm.dtOptions = BioBankDataTable.buildDTOption("BASIC,SEARCHING","260");
            vm.dtColumns = BioBankDataTable.buildDTColumn(columns);
        }

        function onError(data) {}

        vm.cancel = function () {
            $uibModalInstance.dismiss('cancel');
        };
        vm.ok = function () {
            $uibModalInstance.close(angular.copy(vm.boxList));
        };


    }

})();
