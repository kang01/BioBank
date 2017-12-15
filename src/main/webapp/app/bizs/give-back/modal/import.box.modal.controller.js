/**
 * Created by gaokangkang on 2017/12/5.
 */
(function() {
    'use strict';

    angular
        .module('bioBankApp')
        .controller('ImportBoxModalController', ImportBoxModalController);
    ImportBoxModalController.$inject = ['$scope','$q','toastr','$timeout','BioBankDataTable','BioBankSelectize','$uibModalInstance','items','blockUI','blockUIConfig','AreasByEquipmentIdService','GiveBackService'];

    function ImportBoxModalController($scope,$q,toastr,$timeout,BioBankDataTable,BioBankSelectize,$uibModalInstance,items,blockUI,blockUIConfig,AreasByEquipmentIdService,GiveBackService) {

        var vm = this;
        //设备
        vm.equipmentOptions = items.equipmentOptions;
        //归还申请单号
        var _applyCode = items.applyCode;
        //导入数据遮罩层
        vm.progressFlag = false;
        //导入冻存盒数据
        vm.importBoxData = _importBoxData;
        var _deferred = null;

        _initTempPos();

        _inputBoxCode();

        _initBoxTable();

        //暂存位置
        function _initTempPos(){
            //设备
            vm.equipmentConfig = {
                valueField:'id',
                labelField:'equipmentCode',
                maxItems: 1,
                onChange:function (value) {
                    if(value){
                        _queryAreaByEquipmentId(value)
                    }else{
                        vm.frozenBoxHoldAreaOptions = [{id:"",areaCode:""}];
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
        }
        //盒子编码录入
        function _inputBoxCode() {
            var changeBoxTableTimer = null;

            var selectizeObj = {
                create : true,
                persist:false,
                clearMaxItemFlag : true
            };

            vm.boxCodeConfig = BioBankSelectize.buildSettings(selectizeObj);

            vm.boxCodeConfig.onChange = function (value) {
                // vm.boxCodeSelectize = vm.boxCodeConfig.selectizeInstance;

                clearTimeout(changeBoxTableTimer);

                changeBoxTableTimer = setTimeout(function () {

                    _changeBoxTable();

                    if ($scope.$digest()){
                        $scope.$apply();
                    }
                },300);
            };
            vm.boxCodeConfig.onItemRemove = function (value) {
                _.remove(vm.boxList,{frozenBoxCode:value});
            };
        }
        //盒子table详情
        function _initBoxTable() {
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
        //导入冻存盒数据
        function _importBoxData() {
            vm.progressFlag = true;
            var arrayPromise = [];
            var boxCodeArray = [];
            _deferred = $q.defer();
            boxCodeArray = _.chunk(vm.arrayBoxCode, 10);




            _.forEach(boxCodeArray,function (code,i) {
                var codeStr = _.join(code, ',');
                console.log(codeStr);
                var importData = GiveBackService.queryBatchGiveBackBox(_applyCode,codeStr,_deferred);
                arrayPromise.push(
                    importData.success(function (res) {
                        vm.boxList = _updateBoxData(res);
                    }).error(function (res) {

                    }).finally(function (res) {
                        console.log(res + "finally");
                        vm.progressFlag = false;
                    }).$promise
                );
            });
            $q.all(arrayPromise).then(function(datas){

            });

            console.log(JSON.stringify(vm.boxList));

        }
        //根据设备Id查询区域
        function _queryAreaByEquipmentId(equipmentId) {
            AreasByEquipmentIdService.query({id:equipmentId},onAreaTempSuccess, onError);
        }
        //更新冻存盒列表
        function _changeBoxTable() {
            var boxList = [];
            var arrayBoxCode = _.reverse(vm.arrayBoxCode);

            _.forEach(arrayBoxCode,function (code) {
                var len = _.filter(boxList,{frozenBoxCode:code}).length;
                if(!len){
                    var box = _createTempBox(code);

                    boxList.push(box);
                }
            });

            vm.boxList = boxList;
        }
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
                isRealData:-1,
                frozenTubeDTOS:[],
                createTime:new Date().getTime()
            };
            box.frozenBoxCode = code;

            return box;
        }
        //更新冻存盒数据
        function _updateBoxData(boxDatas) {
            for(var i = 0,len = boxDatas.length; i < len; i++){
                for(var j = 0,len1 = vm.boxList.length; j < len1; j++){
                    if(boxDatas[i].frozenBoxCode == vm.boxList[j].frozenBoxCode){
                        vm.boxList[j].isRealData = boxDatas[i].isRealData;
                    }
                }
            }
            return vm.boxList;
        }

        function onAreaTempSuccess(data) {
            vm.frozenBoxHoldAreaOptions = data;
            vm.frozenBox.areaId = vm.frozenBoxHoldAreaOptions[0].id;
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
