/**
 * Created by gaokangkang on 2017/12/5.
 */
(function() {
    'use strict';

    angular
        .module('bioBankApp')
        .controller('GiveBackDetailController', GiveBackDetailController)
        .controller('WarningModalCtrl', WarningModalCtrl);

    GiveBackDetailController.$inject = ['$scope', '$compile', '$state','BioBankDataTable','$uibModal', 'toastr', '$stateParams','Principal',
        'GiveBackService','StockInInputService','EquipmentAllService','SampleUserService','AreasByEquipmentIdService','SupportacksByAreaIdService','RequirementService'];
    WarningModalCtrl.$inject = ['$uibModalInstance','items'];
    function GiveBackDetailController($scope, $compile, $state,BioBankDataTable, $uibModal, toastr, $stateParams,Principal,
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
        vm.boxInstance = {};
        //模态框
        var _modalInstance;
        //最开始的盒子对象字符串
        var _startBoxStr;
        var _startTubes = [];
        //切换冻存盒时，上一次的盒子编码
        var _rowBoxCode;

        //归还单信息数据
        _queryGiveBackInfo();
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
        //初始化盒子上暂存位置
        _initBoxTempPos();
        //获取检测类型
        _fnQueryCheckType();



        vm.saveBox = _onSaveBoxHandler;
        vm.saveGiveBackRecord = _saveGiveBackRecord;
        vm.completeGiveBack = _completeGiveBack;
        vm.invalid = _onInvalidHandler;
        vm.uploadFile = _uploadFile;
        vm.delUploadFile = _delUploadFile;

        //日期选择
        vm.openCalendar = function (date) {
            vm.datePickerOpenStatus[date] = true;
        };
        //删除盒子
        vm.delBox = function () {
            _modalInstance = $uibModal.open({
                animation: true,
                templateUrl: 'warningModal.html',
                size: 'sm',
                controller: 'WarningModalCtrl',
                controllerAs: 'vm',
                backdrop:'static',
                resolve: {
                    items: function () {
                        return {
                            status:'3'
                        };
                    }
                }
            });
            _modalInstance.result.then(function () {
                GiveBackService.delBox(vm.box.id).success(function (data) {
                    toastr.success("删除成功!");
                    _fnQueryBoxByGiveBackId();
                    _queryGiveBackInfo();
                    vm.box = {};
                    vm.htInstance.api.clearData();
                    _startBoxStr = JSON.stringify(vm.box);
                });
            }, function () {
            });

        };
        //重新导入
        vm.reloadBoxData = function () {
            _modalInstance = $uibModal.open({
                animation: true,
                templateUrl: 'warningModal.html',
                size: 'sm',
                controller: 'WarningModalCtrl',
                controllerAs: 'vm',
                resolve: {
                    items: function () {
                        return {
                           status:'1'
                        };
                    }
                }
            });
            _modalInstance.result.then(function () {
                vm.flagStatus = false;
                vm.changeStatus();
                _queryBoxDetail(vm.box.id);
            }, function () {
            });

        };
        //撤销
        vm.repealTubes = function () {
            var selectedData = vm.htInstance.api.getSelectedData();
            if(selectedData.length){
                _modalInstance = $uibModal.open({
                    animation: true,
                    templateUrl: 'app/bizs/common/prompt-modal.html',
                    size: 'sm',
                    controller: 'PromptModalController',
                    controllerAs: 'vm',
                    resolve: {
                        items: function () {
                            return {
                                status:'8'
                            };
                        }
                    }
                });
                _modalInstance.result.then(function () {
                    vm.flagStatus = false;
                    vm.changeStatus();
                    _queryBoxDetail(vm.box.id);
                }, function () {
                });
            }else{
                toastr.error("请选择要撤销的冻存管!");
            }

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
                            applyCode:vm.giveBackRecord.applyCode,
                            giveBackId:$stateParams.giveBackId
                        };
                    }
                }

            });
            _modalInstance.result.then(function (data) {
                _fnQueryBoxByGiveBackId();
                _queryGiveBackInfo();
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
            var memo;
            if(selectedData.length == 1){
                memo = selectedData[0].data.memo;
            }else{
                memo = ""
            }
            if(selectedData.length){
                setTimeout(function(){
                    _modalInstance = $uibModal.open({
                        animation: true,
                        templateUrl: 'app/bizs/transport-record/modal/tubes-remark-modal.html',
                        controller: 'TubesRemarkModalController',
                        backdrop:'static',
                        controllerAs: 'vm',
                        resolve: {
                            items: function () {
                                return {
                                    memo:memo
                                };
                            }
                        }

                    });
                    _modalInstance.result.then(function (memo) {
                        vm.htInstance.api.setMemoOfSelectedTubes(memo);
                    },function () {
                    });
                }, 200);
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
            _onSaveBoxHandler(function () {
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

                _modalInstance = $uibModal.open({
                    animation: true,
                    templateUrl: 'app/bizs/give-back/modal/complete-give-back-modal.html',
                    controller: 'CompleteGiveBackModalController',
                    backdrop:'static',
                    controllerAs: 'vm',
                    resolve:{
                        items:function () {
                            return{
                                receiverId:vm.giveBackRecord.receiverId,
                                receiveDate: vm.giveBackRecord.receiveDate,
                                receiverOptions:vm.receiverOptions
                            }
                        }
                    }
                });
                _modalInstance.result.then(function (completeInfo) {
                    _saveGiveBackRecord(function () {
                        GiveBackService.completeGiveBack(vm.giveBackRecord.transhipCode,completeInfo).success(function (data) {
                            toastr.success("接收完成!");
                            $state.go('give-back-table');
                        }).error(function (data) {
                            toastr.error(data.message);
                        });



                    });
                });





        }
        //保存冻存盒
        function _onSaveBoxHandler(callback,tr,oData) {
            if(vm.box.frozenBoxCode){
                var tubes = vm.htInstance.api.getTubesData();
                var boxList = [];
                if(tubes.length){
                    //统计样本
                    vm.sampleCount = vm.htInstance.api.sampleCount();
                    vm.flagStatus = false;
                    vm.changeStatus();
                }
                vm.box.transhipTubeDTOS = tubes;
                boxList.push(vm.box);
                var isValidTubeDataFlag = vm.htInstance.api.validTubeDataSampleCountOrSampleCode(_startTubes);
                if(!isValidTubeDataFlag){
                    _modalInstance = $uibModal.open({
                        animation: true,
                        templateUrl: 'app/bizs/common/prompt-modal.html',
                        size: 'sm',
                        controller: 'PromptModalController',
                        controllerAs: 'vm',
                        backdrop:'static',
                        resolve: {
                            items: function () {
                                return {
                                    status:'6'
                                };
                            }
                        }
                    });
                    _modalInstance.result.then(function () {
                        _editSaveBox(callback,boxList,tr,oData);
                    }, function () {

                    });
                }else{
                    _editSaveBox(callback,boxList);
                }
            }else{
                callback();
            }
        }
        function _editSaveBox(callback,boxList,tr,oData) {
            return GiveBackService.editSaveBox($stateParams.giveBackId,boxList).success(function (data) {
                if (typeof callback === "function"){
                    callback();
                    if(tr){
                        _changeRowStyle(tr,oData);
                        toastr.success("冻存盒保存成功!");
                    }
                }else{
                    _startBoxStr = JSON.stringify(vm.box);
                    _startTubes = vm.box.transhipTubeDTOS;
                    _queryGiveBackInfo();
                    toastr.success("冻存盒保存成功!");

                }
                return data;
            }).error(function (data) {
                toastr.error(data.message);
                var errorSampleArray = JSON.parse(data.params[0]);
                vm.htInstance.api.errorData(errorSampleArray);
                return data;
            });
        }
        //作废
        function _onInvalidHandler() {
            _modalInstance = $uibModal.open({
                animation: true,
                templateUrl: 'app/bizs/common/prompt-content-modal.html',
                size: 'md',
                controller: 'PromptContentModalController',
                controllerAs: 'vm',
                backdrop:'static',
                resolve: {
                    items: function () {
                        return {
                            status:'2'
                        };
                    }
                }
            });
            _modalInstance.result.then(function (invalidReason) {
                _invalidGiveBack(invalidReason);
            }, function () {
            });
        }
        //获取归还信息
        function _queryGiveBackInfo() {
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
                    //获取附件
                    _queryAttachmentFile();

                });
            }
        }
        //归还控件信息（项目编码、委托方、接收人、临时位置）
        function _initRecordInfoControl() {
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
                vm.boxList  = data;
                vm.boxOptions.withOption("data",data);
            });
        }
        //作废
        function _invalidGiveBack(invalidReason) {
            var obj = {
                invalidReason:invalidReason
            };
            GiveBackService.invalidGiveBack(vm.giveBackRecord.transhipCode,obj).success(function(data){
                $state.go('give-back-table');
                toastr.success("作废成功!");
            }).error(function (data) {
                toastr.error(data.message);
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
            var dom = "<'row mt-10'<'col-xs-12 text-left pl-25' f> <'col-xs-1 text-right mb-5' > r> t <'row mt-0'<'col-xs-6'i> <'col-xs-6'p>>";
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
                    if(_rowBoxCode && _rowBoxCode != oData.frozenBoxCode){
                        _switchRowClick(tr,oData);
                    }else{
                        _changeRowStyle(tr,oData);
                        _queryBoxDetail(oData.id);
                    }

                });
            }
            //切换盒子
            function _switchRowClick(tr,oData) {
                if(vm.box.frozenBoxCode){
                    var tubes = vm.htInstance.api.getTubesData();
                    vm.box.transhipTubeDTOS = tubes;
                }
                var _endBoxStr = JSON.stringify(vm.box);

                if(_startBoxStr == _endBoxStr){
                    _queryBoxDetail(oData.id);
                    _changeRowStyle(tr,oData);
                }else{
                    _modalInstance = $uibModal.open({
                        animation: true,
                        templateUrl: 'app/bizs/common/prompt-modal.html',
                        size: 'sm',
                        controller: 'PromptModalController',
                        controllerAs: 'vm',
                        backdrop:'static',
                        resolve:{
                            items:function () {
                                return{
                                    status: '4'
                                };
                            }
                        }
                    });
                    _modalInstance.result.then(function (flag) {
                        if(flag){
                            //保存
                            _onSaveBoxHandler(function () {
                                _queryBoxDetail(oData.id);
                            },tr,oData);
                        }else{
                            //不保存
                            _queryBoxDetail(oData.id);
                            vm.flagStatus = false;
                            vm.changeStatus();
                            _changeRowStyle(tr,oData);
                        }

                    }, function () {
                    // 取消
                    });
                }
            }

        }
        //改变行的选中样式
        function _changeRowStyle(tr,oData) {
            $(tr).closest('table').find('.rowLight').removeClass("rowLight");
            $(tr).addClass('rowLight');
            _rowBoxCode = oData.frozenBoxCode;
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
                    $scope.$apply();
                }
            };

            //设备
            _queryEquipment();


        }
        //获取检测类型
        function _fnQueryCheckType() {
            vm.checkTypeConfig = {
                valueField:'id',
                labelField:'checkTypeName',
                maxItems: 1,
                onChange:function (value) {
                }
            };
            RequirementService.queryCheckTypes().success(function (data) {
                vm.checkTypeOptions = data;
            });
        }
        //获取冻存盒信息详情（盒子信息和管子信息）
        function _queryBoxDetail(boxId) {
            GiveBackService.queryBoxDesc(boxId).success(function (data) {
                vm.box = data;
                if(vm.box.equipmentId){
                    _queryArea(vm.box.equipmentId);
                }
                if(vm.box.areaId){
                    _queryShelf(vm.box.areaId);
                }
                if(vm.box.columnsInShelf && vm.box.rowsInShelf){
                    vm.boxRowCol =  vm.box.columnsInShelf + vm.box.rowsInShelf;
                }
                vm.htInstance.api.loadData(data, data.transhipTubeDTOS);
                vm.sampleCount = vm.htInstance.api.sampleCount();

                vm.box.transhipTubeDTOS =  _.sortBy(vm.box.transhipTubeDTOS, ["tubeRows", function(o){return +o.tubeColumns}]);
                _startTubes = vm.box.transhipTubeDTOS;
                _startBoxStr = JSON.stringify(vm.box);
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
        //附件
        function _queryAttachmentFile() {
            GiveBackService.queryAttachment(vm.giveBackRecord.transhipCode).success(function (data) {
                vm.transportRecordUploadInfo = data;
            }).error(function (data) {
                toastr.error(data.message);
            })
        }
        //上传
        function _uploadFile(status,imgData) {
            _modalInstance = $uibModal.open({
                animation: true,
                templateUrl: 'app/bizs/transport-record/modal/transport-record-upload-image-modal.html',
                controller: 'transportUploadImageModalCtrl',
                backdrop:'static',
                controllerAs: 'vm',
                resolve:{
                    items:function () {
                        return{
                            transportId:vm.giveBackRecord.id,
                            status:status,
                            imgData:imgData
                        };
                    }
                }
            });
            _modalInstance.result.then(function () {
                _queryAttachmentFile();
            },function (data) {
                _queryAttachmentFile();
            });
        }
        //删除附件
        function _delUploadFile(imgId) {
            _modalInstance = $uibModal.open({
                animation: true,
                templateUrl: 'app/bizs/common/prompt-modal.html',
                controller: 'PromptModalController',
                backdrop:'static',
                controllerAs: 'vm',
                size: 'sm',
                resolve:{
                    items:function () {
                        return{
                            status: '5'
                        };
                    }
                }
            });
            _modalInstance.result.then(function () {
                //删除
                GiveBackService.deleteAttachment(imgId).success(function (data) {
                    toastr.success("删除成功!");
                    _queryAttachmentFile();
                }).error(function (data) {
                    toastr.error(data.message);
                });

            },function (data) {
                _queryAttachmentFile();
            });
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

    function WarningModalCtrl($uibModalInstance,items) {
        var vm = this;
        vm.status = items.status;
        vm.ok = function () {
            $uibModalInstance.close(true);
        };
        vm.unSave = function () {
            $uibModalInstance.close(false);
        };
        vm.cancel = function () {
            $uibModalInstance.dismiss('cancel');
        };
    }
})();
