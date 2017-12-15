/**
 * Created by gaokangkang on 2017/12/5.
 */
(function() {
    'use strict';

    angular
        .module('bioBankApp')
        .controller('GiveBackDetailController', GiveBackDetailController);

    GiveBackDetailController.$inject = ['$scope', '$compile', 'BioBankDataTable','$uibModal', 'toastr', '$stateParams','Principal',
        'GiveBackService','StockInInputService','EquipmentAllService','SampleUserService','AreasByEquipmentIdService','SupportacksByAreaIdService','RequirementService'];
    function GiveBackDetailController($scope, $compile, BioBankDataTable, $uibModal, toastr, $stateParams,Principal,
                                      GiveBackService,StockInInputService,EquipmentAllService,SampleUserService,AreasByEquipmentIdService,SupportacksByAreaIdService,RequirementService) {
        var vm = this;
        //归还记录对象
        vm.giveBackRecord = {};
        //冻存盒
        vm.box = {};
        //设备
        vm.equipmentOptions = [];
        //日期控件
        vm.datePickerOpenStatus = {};
        //管子实例
        vm.htInstance = {};
        //冻存管状态编辑的开关
        vm.flagStatus = false;
        //模态框
        var _modalInstance;

        //归还控件信息（项目编码、委托方、接收人、临时位置）
        _initRecordInfoControl();
        //委托方
        _fuQueryDelegates();
        //接收人
        _fnQueryReceiver();
        //获取项目编码
        _queryApplyProject();
        //冻存盒列表
        _initBoxDataTable();



        vm.saveBox = _saveBox;
        vm.saveGiveBackRecord = _saveGiveBackRecord;
        vm.completeGiveBack = _completeGiveBack;

        //日期选择
        vm.openCalendar = function (date) {
            vm.datePickerOpenStatus[date] = true;
        };
        //删除盒子
        vm.delBox = function () {

        };
        //重新导入
        vm.reloadBoxData = function () {
            _queryBoxDetail(53783);
        };
        //导入冻存盒
        vm.importBox = function () {
            _modalInstance = $uibModal.open({
                animation: true,
                templateUrl: 'app/bizs/give-back/modal/import-box-modal.html',
                controller: 'ImportBoxModalController',
                controllerAs:'vm',
                size:'lg w-1200',
                backdrop:'static',
                resolve: {
                    items: function () {
                        return {
                            equipmentOptions:vm.equipmentOptions,
                            applyCode:vm.giveBackRecord.applyCode
                        };
                    }
                }

            });
            _modalInstance.result.then(function (data) {
                vm.boxOptions.withOption("data",data);
            },function () {

            })
        };
        //换位
        vm.exchange = function(){
            vm.htInstance.api.exchangeSelectedTubePosition();
        };
        //添加备注
        vm.tubeRemark = function () {
            var selectedData = vm.htInstance.api.getSelectedData();
            if(selectedData.length){
                _modalInstance = $uibModal.open({
                    animation: true,
                    templateUrl: 'app/bizs/transport-record/modal/tubes-remark-modal.html',
                    controller: 'TubesRemarkModalController',
                    backdrop:'static',
                    controllerAs: 'vm',
                    resolve: {
                        items: function () {
                            return {};
                        }
                    }

                });
                _modalInstance.result.then(function (memo) {
                    vm.htInstance.api.setMemoOfSelectedTubes(memo);
                });
            }else{
                toastr.error("请选择要加备注的冻存管!");
            }

        };
        //更改冻存管状态
        vm.changeStatus = function () {
            if(vm.flagStatus){
                vm.htInstance.api.updateSettings({
                    isCellValueEditable: false,
                    isCellStatusEditable: true
                });
            }else{
                vm.htInstance.api.updateSettings({
                    isCellValueEditable: true,
                    isCellStatusEditable: false
                });
            }

        };
        //盒子位置
        vm.splitPlace = function () {
            if(vm.boxRowCol){
                vm.box.columnsInShelf = vm.boxRowCol.charAt(0);
                vm.box.rowsInShelf = vm.boxRowCol.substring(1);
            }else{
                vm.box.columnsInShelf = "";
                vm.box.rowsInShelf = "";
            }
        };

        //保存归还记录基本信息
        function _saveGiveBackRecord(callback) {
            _saveBox(function () {
                GiveBackService.saveGiveBackRecord(vm.giveBackRecord).success(function (data) {
                    if (typeof callback === "function"){
                        callback();
                    }else{
                        toastr.success("归还记录保存成功!");
                    }
                });
            });

        }
        //接收完成
        function _completeGiveBack() {
            _saveGiveBackRecord(function () {
                toastr.success("接收完成!");
            });

        }
        //保存冻存盒
        function _saveBox(callback) {
            var tubes = vm.htInstance.api.getTubesData();
            if(tubes.length){
                vm.sampleCount = vm.htInstance.api.sampleCount();
                vm.flagStatus = false;
                vm.changeStatus();
            }

            if (typeof callback === "function"){
                callback();
            }else{
                toastr.success("冻存盒保存成功!");
            }

        }
        //归还控件信息（项目编码、委托方、接收人、临时位置）
        function _initRecordInfoControl() {
            //归还单id
            var giveBackId = $stateParams.giveBackId;
            if(giveBackId){
                GiveBackService.queryGiveBackInfo(giveBackId).success(function (data) {
                    vm.giveBackRecord = data;
                    //当前用户
                    _fnQueryUser();
                    //格式归还日期及获取区域
                    _initRecordAreaAndDate();
                    //根据归还记录单号查询冻存盒
                    _fnQueryBoxByGiveBackId();

                });
            }

            //项目编码
            vm.projectConfig = {
                valueField:'id',
                labelField:'projectName',
                maxItems: 1,
                searchField:'projectName',
                onChange:function(value){}
            };
            vm.recordEquipmentConfig = {
                valueField:'id',
                labelField:'equipmentCode',
                maxItems: 1,
                onChange:function (value) {
                    vm.giveBackRecord.tempAreaId = "";
                    if(value){
                        _fnQueryArea(value);
                    }else{
                        vm.recordAreaOptions = [];
                        vm.recordAreaOptions.push({id:"",areaCode:""});
                    }
                }
            };
            //区域
            vm.recordAreaConfig = {
                valueField:'id',
                labelField:'areaCode',
                maxItems: 1,
                onChange:function (value) {}
            };
            //委托方
            vm.delegatesConfig = {
                valueField:'id',
                labelField:'delegateName',
                maxItems: 1
            };
            //接收人
            vm.receiverConfig = {
                valueField:'id',
                labelField:'userName',
                maxItems: 1

            };

        }
        //初始化归还记录数据（归还日期、根据设备取区域）
        function _initRecordAreaAndDate() {
            if(vm.giveBackRecord.receiveDate){
                vm.giveBackRecord.receiveDate = new Date(vm.giveBackRecord.receiveDate);
            }else{
                vm.giveBackRecord.receiveDate = new Date();
            }
            if(vm.giveBackRecord.tempEquipmentId){
                _fnQueryArea(vm.giveBackRecord.tempEquipmentId);
            }
        }
        //根据归还记录单号查询冻存盒
        function _fnQueryBoxByGiveBackId() {
            GiveBackService.queryGiveBackBox(vm.giveBackRecord.transhipCode).success(function (data) {
                vm.boxOptions.withOption("data",data);
            });
        }

        //归还盒子信息
        function _initBoxDataTable() {
            //盒列
            var columns = [
                {
                    name:"frozenBoxCode",
                    title:"#",
                    width:"50px",
                    notSortable:true
                },
                {
                    name:"frozenBoxCode",
                    title:"冻存盒号",
                    renderWith:_fnRowBoxCodeRender


                }

            ];
            //初始化盒子上暂存位置
            _initBoxTempPos();

            var dom = "<'row mt-10'<'col-xs-8 text-left pl-25' f> <'col-xs-4 text-right mb-5' > r> t <'row mt-0'<'col-xs-6'i> <'col-xs-6'p>>";
            vm.boxColumns = BioBankDataTable.buildDTColumn(columns);
            vm.boxOptions = BioBankDataTable.buildDTOption("SORTING,SEARCHING","410",null,dom,null,1)
                .withOption('rowCallback', rowCallback);

            function _fnRowBoxCodeRender(data, type, full, meta) {
                var frozenBoxCode = '';
                if(full.frozenBoxCode1D){
                    frozenBoxCode = "1D:"+full.frozenBoxCode1D;
                }else{
                    frozenBoxCode = "2D:"+full.frozenBoxCode;
                }
                return frozenBoxCode;
            }

            function rowCallback(nRow, oData, iDisplayIndex, iDisplayIndexFull) {
                $('td:first', nRow).html(iDisplayIndex+1);
                $('td', nRow).unbind('click');
                $(nRow).bind('click', function() {
                    var tr = this;
                    $(tr).closest('table').find('.rowLight').removeClass("rowLight");
                    $(tr).addClass('rowLight');
                    _queryBoxDetail(53783);
                });
            }

        }
        //初始化盒子上暂存位置
        function _initBoxTempPos() {
            vm.equipmentConfig = {
                valueField:'id',
                labelField:'equipmentCode',
                maxItems: 1,
                onChange:function (value) {
                    vm.box.areaId = "";
                    vm.box.supportRackId = "";
                    _clearPos();
                    if(value){
                        _queryArea(value);
                    }else{
                        vm.frozenBoxAreaOptions = [];
                        vm.frozenBoxAreaOptions.push({id:"",areaCode:""});

                        vm.frozenBoxShelfOptions = [];
                        vm.frozenBoxShelfOptions.push({id:"",supportRackCode:""});


                        $scope.$apply();
                    }
                }
            };
            vm.frozenBoxAreaConfig = {
                valueField:'id',
                labelField:'areaCode',
                maxItems: 1,
                onChange:function (value) {
                    vm.box.supportRackId = "";
                    _clearPos();
                    if(value){
                        // for(var i = 0; i < vm.frozenBoxAreaOptions.length; i++){
                        //     if(value == vm.frozenBoxAreaOptions[i].id){
                        //         vm.box.areaCode = vm.frozenBoxAreaOptions[i].areaCode;
                        //     }
                        // }
                        _queryShelf(value);
                    }else{
                        vm.frozenBoxShelfOptions = [];
                        vm.frozenBoxShelfOptions.push({id:"",supportRackCode:""});
                        $scope.$apply();
                    }


                }
            };
            vm.frozenBoxShelfConfig = {
                valueField:'id',
                labelField:'supportRackCode',
                maxItems: 1,
                onChange:function (value) {
                    _clearPos();
                    // if(value){
                        // for(var i = 0; i < vm.frozenBoxShelfOptions.length; i++){
                        //     if(value == vm.frozenBoxShelfOptions[i].id){
                        //         vm.box.supportRackCode = vm.frozenBoxShelfOptions[i].areaCode;
                        //     }
                        // }
                    // }
                    $scope.$apply();
                }
            };

            //设备
            _queryEquipment();


        }
        //获取冻存盒信息详情（盒子信息和管子信息）
        function _queryBoxDetail(boxId) {
            StockInInputService.queryEditStockInBox(boxId).success(function (data) {
                vm.box = data;
                vm.htInstance.api.loadData(data, data.frozenTubeDTOS);
                vm.sampleCount = vm.htInstance.api.sampleCount();
            });
        }
        //获取申请单中的项目编码
        function _queryApplyProject() {
            var applyCode = $stateParams.applyCode;
            GiveBackService.queryApplyInfo(applyCode).success(function (data) {
                vm.projectOptions = data.projectDTOS;
            }).error(function (data) {
                toastr.error(data.message);
            });
        }
        //委托方查询
        function _fuQueryDelegates() {
            RequirementService.queryDelegates().success(function (data) {
                vm.delegatesOptions = data;
                if(!vm.giveBackRecord.delegateId){
                    vm.giveBackRecord.delegateId = vm.delegatesOptions[0].id;
                }
            });
        }
        //当前用户
        function _fnQueryUser() {
            Principal.identity().then(function(account) {
                vm.account = account;
                if(vm.account.login != "admin" && vm.account.login != "user"){
                    if(!vm.giveBackRecord.receiverId){
                        vm.giveBackRecord.receiverId = vm.account.id;
                    }

                }
            });
        }
        //接收人
        function _fnQueryReceiver() {
            SampleUserService.query({},onReceiverSuccess, onError);
            function onReceiverSuccess(data) {
                vm.receiverOptions = data;
            }
        }
        //设备查询
        function _queryEquipment() {
            EquipmentAllService.query({},onEquipmentSuccess, onError);
        }
        //冻存盒区域查询
        function _queryArea(equipmentId) {
            AreasByEquipmentIdService.query({id:equipmentId},onAreaSuccess, onError);
        }
        //归还记录单中的区域查询
        function _fnQueryArea(tempEquipmentId) {
            AreasByEquipmentIdService.query({id:tempEquipmentId},onRecordAreaSuccess, onError);
            function onRecordAreaSuccess(data) {
                vm.recordAreaOptions = data;
                vm.recordAreaOptions.push({id:"",areaCode:""});
            }
        }
        //架子查询
        function _queryShelf(areaId) {
            SupportacksByAreaIdService.query({id:areaId},onShelfSuccess, onError);
        }
        //清除位置
        function _clearPos() {
            vm.boxRowCol = "";
            vm.box.columnsInShelf = "";
            vm.box.rowsInShelf = "";
        }
        //区域
        function onAreaSuccess(data) {
            vm.frozenBoxAreaOptions = data;
            vm.frozenBoxAreaOptions.push({id:"",areaCode:""});
        }
        //架子
        function onShelfSuccess(data) {
            vm.frozenBoxShelfOptions = data;
            vm.frozenBoxShelfOptions.push({id:"",supportRackCode:""});
        }
        //设备
        function onEquipmentSuccess(data) {
            vm.equipmentOptions = _.orderBy(data,['equipmentCode'],['asc']);
            // vm.recordPlaceOptions = _.orderBy(data,['equipmentCode'],['asc']);

        }

        function onError(error) {
            // toastr.error(error.data.message);
            // BioBankBlockUi.blockUiStop();
            // vm.repeatSampleArray = JSON.parse(error.data.params[0]);
            // hotRegisterer.getInstance('my-handsontable').render();
        }
    }
})();
