/**
 * Created by gaokangkang on 2017/12/5.
 */
(function() {
    'use strict';

    angular
        .module('bioBankApp')
        .controller('ImportBoxModalController', ImportBoxModalController);
    ImportBoxModalController.$inject = ['$scope','$q','$compile','toastr','$timeout','BioBankDataTable','BioBankSelectize','$uibModalInstance','items','blockUI','blockUIConfig','AreasByEquipmentIdService','GiveBackService','RequirementService'];

    function ImportBoxModalController($scope,$q,$compile,toastr,$timeout,BioBankDataTable,BioBankSelectize,$uibModalInstance,items,blockUI,blockUIConfig,AreasByEquipmentIdService,GiveBackService,RequirementService) {

        var vm = this;
        //设备
        vm.equipmentOptions = items.equipmentOptions;
        var _projectCode = items.projectCode;
        //归还记录单的ID
        var _giveBackId = items.giveBackId;
        //导入数据遮罩层
        vm.progressFlag = false;
        vm.tempPos = {
            equipmentId:null,
            areaId:null
        };
        //进度条的宽度
        vm.progressBar = {
            width: 0
        };
        //请求次数
        vm.count = 0;
        var _deferred = null;
        var _boxList = [];


        //删除冻存盒
        vm.delBox = _fnDelBox;
        //导入冻存盒数据
        vm.importBoxData = _importBoxData;
        //单个重新导入
        vm.importSingleBoxData = _importSingleBoxData;
        //中止正在导入的数据
        vm.stopImportBoxData = _stopImportBoxData;

        _initBoxTable();

        _initTempPos();

        _inputBoxCode();



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
                        vm.tempPos.areaId = "";
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
                    vm.tempPos.areaId  = value;
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
                _.remove(_boxList,{frozenBoxCode:value});
            };
        }
        //盒子table详情
        function _initBoxTable() {
            vm.dtInstance = {};
            var columns = [
                {name:"frozenBoxCode",title:"冻存盒号",width:"auto",notSortable:true},
                {name:"frozenBoxCode1D",title:"一维编码",width:"auto",notSortable:true},
                {name:"isRealData",title:"状态",width:"80",notSortable:true,renderWith:_statusHtml},
                {name:"sampleTypeName",title:"样本类型",width:"80",notSortable:true},
                {name:"countOfSample",title:"样本数",width:"80",notSortable:true},
                {name:"isSplit",title:"是否分装",width:"80",notSortable:true,renderWith:_isSplitHtml},
                {name:"",title:"操作",notSortable:true,width:"80",renderWith:_actionsHtml}
            ];
            vm.dtOptions = BioBankDataTable.buildDTOption("BASIC,SEARCHING","240",null,null,$scope)
                .withOption('createdRow', function (row, data, dataIndex) {
                    $compile(angular.element(row).contents())($scope);
                });
            vm.dtColumns = BioBankDataTable.buildDTColumn(columns);

            $timeout(function () {
                vm.dtInstance.rerender();
            },200)
        }
        //操作
        function _actionsHtml(data, type, full, meta) {
            var html = '';
            html = '<button type="button" class="btn btn-xs" ng-click="vm.delBox(\''+full.frozenBoxCode+'\',\''+full.frozenBoxCode1D+'\')">' +
                '   <i class="fa fa-times"></i>' +
                '</button>&nbsp;';
            if(full.isRealData != 1){
                html += '<button type="button" class="btn btn-xs" ng-click="vm.importSingleBoxData(\''+full.frozenBoxCode+'\')">' +
                    '   <i class="fa fa-rotate-right"></i>' +
                    '</button>&nbsp;';
            }

            return html;
        }
        //导入状态
        function _statusHtml(data, type, full, meta) {
            var html = "";
            switch (full.isRealData){
                case -1:
                    html = "";
                    break;
                case 0:
                    html = "<span class='red-color'>导入失败</span>";
                    break;
                case 1:
                    html = "<span>导入成功</span>";
                    break;
            }

            return html;
        }
        //是否分装
        function _isSplitHtml(data, type, full, meta) {
            var html = "";
            switch (full.isSplit){
                case 0:
                    html = "<span >否</span>";
                    break;
                case 1:
                    html = "<span>是</span>";
                    break;
            }

            return html;
        }
        //更新datatable数据
        function _updateTableDataOption(data) {
            vm.dtOptions.withOption("data",data)
        }
        //删除盒子
        function _fnDelBox(frozenBoxCode,frozenBoxCode1D) {
            _.remove(_boxList,{frozenBoxCode:frozenBoxCode});
            _.pull(vm.arrayBoxCode,frozenBoxCode);
            _.pull(vm.arrayBoxCode,frozenBoxCode1D);
            vm.boxCodeSelectize.removeOption(frozenBoxCode);
            vm.boxCodeSelectize.removeOption(frozenBoxCode1D);
            _updateTableDataOption(_boxList);
        }
        //导入冻存盒数据
        function _importBoxData(callback) {
            //请求次数
            vm.count = 0;
            vm.progressFlag = true;
            var arrayPromise = [];
            var boxCodeArray = [];
            _deferred = $q.defer();


            var arrayBoxCode = angular.copy(vm.arrayBoxCode);
            _.forEach(_boxList,function (box) {
                if(box.isRealData == 1){
                    arrayBoxCode = _.pull(arrayBoxCode, box.frozenBoxCode);
                }
            });
            boxCodeArray = _.chunk(arrayBoxCode, 10);


            blockUIConfig.autoBlock = false;
            vm.boxLen = arrayBoxCode.length;
            _.forEach(boxCodeArray,function (code,i) {
                var codeStr = _.join(code, ',');
                var importData = GiveBackService.queryBatchGiveBackBox(_projectCode,codeStr,_deferred);
                arrayPromise.push(
                    importData.success(function (res) {
                        _updateBoxData(res);
                    }).error(function (res) {
                        for(var i = 0; i < code.length; i++){
                            for(var j = 0; j < _boxList.length; j++){
                                if(code[i] == _boxList[j].frozenBoxCode){
                                    _boxList[j].isRealData = 0;
                                }
                            }
                        }
                        vm.errorLen = _.filter(_boxList,{isRealData:0}).length;
                        _updateTableDataOption(_boxList);
                        setTimeout(function () {
                            vm.progressFlag = false;
                            // blockUIConfig.autoBlock = true;
                            $scope.$apply();
                        },500)
                    }).finally(function (res) {
                        vm.count++;
                        var percentage = parseInt((vm.count/boxCodeArray.length)*100);
                        vm.progressBar.width = percentage+'%';
                        if(vm.progressBar.width == '100%'){
                            setTimeout(function () {
                                vm.progressFlag = false;
                                blockUIConfig.autoBlock = true;
                            },500)
                        }
                        vm.errorLen = _.filter(_boxList,{isRealData:0}).length;
                    })
                );

            });
            $q.all(arrayPromise).then(function(res){
                if(vm.progressBar.width == '100%'){

                    setTimeout(function () {
                        vm.progressFlag = false;
                        // blockUIConfig.autoBlock = true;
                        $scope.$apply();
                    },500)
                }
                if (typeof callback === "function"){
                    callback();
                }
            });
        }
        //保存冻存盒
        function _saveBox(callback) {
            _importBoxData(function(){
                blockUI.start("正在保存冻存盒中……");
                _.forEach(_boxList,function (box) {
                    if(vm.tempPos.equipmentId){
                        box.equipmentId = vm.tempPos.equipmentId;
                    }
                    if(vm.tempPos.areaId){
                        box.areaId = vm.tempPos.areaId;
                    }
                    box.supportRackId = "";
                    box.columnsInShelf = "";
                    box.rowsInShelf = "";
                });
                GiveBackService.saveBox(_giveBackId,_boxList).success(function (data) {
                    toastr.success("保存成功!");
                    blockUI.stop();
                    if (typeof callback === "function"){
                        callback();
                    }
                }).error(function (data) {
                    toastr.error(data.message);
                    blockUI.stop();
                });
            });
        }
        //导入单个冻存盒数据
        function _importSingleBoxData(frozenBoxCode) {
            GiveBackService.queryBatchGiveBackBox(_projectCode,frozenBoxCode).success(function (data) {
                for(var i = 0,len = _boxList.length; i < len; i++){
                    if(_boxList[i].frozenBoxCode == frozenBoxCode){
                        _boxList[i] = data[0];
                    }
                }
                _updateTableDataOption(_boxList);
            }).error(function (data) {
                for(var i = 0,len = _boxList.length; i < len; i++){
                    if(_boxList[i].frozenBoxCode == frozenBoxCode){
                        _boxList[i].isRealData = 0;
                    }
                }
                toastr.error(data.message);
            });
        }
        //中止正在导入的数据
        function _stopImportBoxData() {
            if (_deferred){
                _deferred.resolve();
            }
            vm.progressFlag = false;
        }
        //根据设备Id查询区域
        function _queryAreaByEquipmentId(equipmentId) {
            AreasByEquipmentIdService.query({id:equipmentId},onAreaTempSuccess, onError);
        }
        //扫码时，更新冻存盒列表
        function _changeBoxTable() {
            // _boxList = [];


            _.forEach(vm.arrayBoxCode,function (code) {
                var len = _.filter(_boxList,{frozenBoxCode:code}).length;
                if(!len){
                    var box = _createTempBox(code);

                    _boxList.unshift(box);
                }
            });
            // var arrayBox = _.reverse(_boxList);
            _updateTableDataOption(_boxList);
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
        //导入数据时，更新冻存盒数据
        function _updateBoxData(boxDatas) {
            for(var i = 0,len = boxDatas.length; i < len; i++){
                for(var j = 0,len1 = _boxList.length; j < len1; j++){
                    if(boxDatas[i].frozenBoxCode == _boxList[j].frozenBoxCode){
                        _boxList[j] = boxDatas[i];
                    }
                }
            }
            _updateTableDataOption(_boxList);
        }


        function onAreaTempSuccess(data) {
            vm.frozenBoxHoldAreaOptions = data;
            vm.tempPos.areaId = vm.frozenBoxHoldAreaOptions[0].id;
        }
        function onError(data) {}

        vm.cancel = function () {
            $uibModalInstance.dismiss('cancel');
        };
        vm.ok = function () {
            _saveBox(function () {
                $uibModalInstance.close();
            });
        };

    }

})();
