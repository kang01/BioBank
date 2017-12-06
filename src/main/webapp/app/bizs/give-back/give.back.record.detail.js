/**
 * Created by gaokangkang on 2017/12/5.
 */
(function() {
    'use strict';

    angular
        .module('bioBankApp')
        .controller('GiveBackDetailController', GiveBackDetailController);

    GiveBackDetailController.$inject = ['$scope', '$compile', 'BioBankDataTable','$uibModal', 'toastr', '$state',
        'FrozenBoxTypesService','StockInInputService','EquipmentAllService','SampleTypeService','AreasByEquipmentIdService','SupportacksByAreaIdService'];
    function GiveBackDetailController($scope, $compile, BioBankDataTable, $uibModal, toastr, $state,
                                      FrozenBoxTypesService,StockInInputService,EquipmentAllService,SampleTypeService,AreasByEquipmentIdService,SupportacksByAreaIdService) {
        var vm = this;
        var modalInstance;

        _fnRecordInfo();
        _fnRecordBox();

        //归还信息
        function _fnRecordInfo() {
            //归还记录对象
            vm.giveBackRecord = {
                sampleSatisfaction:8
            };

            //日期控件
            vm.datePickerOpenStatus = {};
            vm.openCalendar = openCalendar;
            function openCalendar (date) {
                vm.datePickerOpenStatus[date] = true;
            }
            //滿意程度
            vm.rating = 0;
            vm.ratings = [{
                current: vm.giveBackRecord.sampleSatisfaction,
                max: 10
            }];
            vm.getSelectedRating = function (rating) {
                vm.giveBackRecord.sampleSatisfaction = rating;
            };
        }
        //归还盒子信息
        function _fnRecordBox() {
            vm.box = {};
            vm.tubes = [];
            //设备
            vm.frozenBoxPlaceOptions = [];
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
                    notSortable:true,
                    renderWith:_fnRowRender


                }

            ];

            _fnBoxInfoInit();

            vm.boxColumns = BioBankDataTable.buildDTColumn(columns);
            vm.boxOptions = BioBankDataTable.buildDTOption("SORTING,SEARCHING","400")
                .withOption('order', [[1, 'asc' ]])
                .withOption('rowCallback', rowCallback);

            function _fnRowRender(data, type, full, meta) {
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
                $(nRow).bind('click', function() {
                    var tr = this;
                    $(tr).closest('table').find('.rowLight').removeClass("rowLight");
                    $(tr).addClass('rowLight');
                    _queryBox(53783);
                });
            }

            //初始化信息（样本类型、样本分类、盒类型、暂存位置）
            function _fnBoxInfoInit() {
                //盒子类型 17:10*10 18:8*8
                vm.boxTypeInstance = {};
                vm.boxTypeConfig = {
                    valueField:'id',
                    labelField:'frozenBoxTypeName',
                    maxItems: 1,
                    onInitialize: function(selectize){
                        vm.boxTypeInstance = selectize;
                    },
                    onChange:function(value) {
                    }
                };
                vm.sampleTypeConfig = {
                    valueField:'id',
                    labelField:'sampleTypeName',
                    maxItems: 1,
                    onChange:function (value) {}
                };
                vm.frozenBoxPlaceConfig = {
                    valueField:'id',
                    labelField:'equipmentCode',
                    maxItems: 1,
                    onChange:function (value) {
                        vm.box.areaId = "";
                        vm.box.supportRackId = "";
                        vm.boxRowCol = "";
                        vm.box.columnsInShelf = "";
                        vm.box.rowsInShelf = "";
                        if(value){
                            AreasByEquipmentIdService.query({id:value},onAreaSuccess, onError);
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
                        vm.boxRowCol = "";
                        vm.box.columnsInShelf = "";
                        vm.box.rowsInShelf = "";
                        if(value){
                            for(var i = 0; i < vm.frozenBoxAreaOptions.length; i++){
                                if(value == vm.frozenBoxAreaOptions[i].id){
                                    vm.box.areaCode = vm.frozenBoxAreaOptions[i].areaCode;
                                }
                            }
                            SupportacksByAreaIdService.query({id:value},onShelfSuccess, onError);
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
                        vm.boxRowCol = "";
                        vm.box.columnsInShelf = "";
                        vm.box.rowsInShelf = "";
                        if(value){
                            for(var i = 0; i < vm.frozenBoxShelfOptions.length; i++){
                                if(value == vm.frozenBoxShelfOptions[i].id){
                                    vm.box.supportRackCode = vm.frozenBoxShelfOptions[i].areaCode;
                                }
                            }
                        }
                        $scope.$apply();
                    }
                };

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
                //盒子类型
                FrozenBoxTypesService.query({},onFrozenBoxTypeSuccess, onError);
                //设备
                EquipmentAllService.query({},onEquipmentSuccess, onError);

                _querySampleType();
                function onEquipmentSuccess(data) {
                    vm.frozenBoxPlaceOptions = _.orderBy(data,['equipmentCode'],['asc']);
                }
                function onFrozenBoxTypeSuccess(data) {
                    vm.frozenBoxTypeOptions = _.orderBy(data, ['id'], ['esc']);
                }
                //获取样本分类
                function _querySampleType() {
                    SampleTypeService.querySampleType().success(function (data) {
                        vm.sampleTypeOptions = data;
                        _.remove(vm.sampleTypeOptions,{sampleTypeName:"98"});
                        _.remove(vm.sampleTypeOptions,{sampleTypeName:"97"});
                    });
                }


            }

            //获取冻存盒信息（盒子信息和管子信息）
            function _queryBox(boxId) {
                StockInInputService.queryEditStockInBox(boxId).success(function (data) {
                    vm.box = data;
                    vm.tubes = data.frozenTubeDTOS;
                    vm.htInstance.api.loadData(data, data.frozenTubeDTOS);
                });
            }
            function onError(error) {
                // toastr.error(error.data.message);
                // BioBankBlockUi.blockUiStop();
                // vm.repeatSampleArray = JSON.parse(error.data.params[0]);
                // hotRegisterer.getInstance('my-handsontable').render();
            }



            //导入冻存盒
            vm.importBox = function () {
                modalInstance = $uibModal.open({
                    animation: true,
                    templateUrl: 'app/bizs/give-back/modal/import-box-modal.html',
                    controller: 'ImportBoxModalController',
                    controllerAs:'vm',
                    size:'lg w-1200',
                    backdrop:'static',
                    resolve: {
                        items: function () {
                            return {
                                frozenBoxPlaceOptions:vm.frozenBoxPlaceOptions
                            };
                        }
                    }

                });
                modalInstance.result.then(function (data) {
                    console.log(JSON.stringify(data));
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
                    modalInstance = $uibModal.open({
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
                    modalInstance.result.then(function (memo) {
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

            }
        }
    }
})();
