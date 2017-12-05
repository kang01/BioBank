/**
 * Created by gaokangkang on 2017/12/5.
 */
(function() {
    'use strict';

    angular
        .module('bioBankApp')
        .controller('GiveBackDetailController', GiveBackDetailController);

    GiveBackDetailController.$inject = ['$scope', '$compile', 'BioBankDataTable','$uibModal', 'toastr', '$state','DTColumnBuilder','StockInInputService','EquipmentAllService'];
    function GiveBackDetailController($scope, $compile, BioBankDataTable, $uibModal, toastr, $state,DTColumnBuilder,StockInInputService,EquipmentAllService) {
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
                    name:"frozenBoxId",
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
            _queryBox();


            vm.boxColumns = BioBankDataTable.buildDTColumn(columns);
            vm.boxOptions = BioBankDataTable.buildDTOption("SORTING,SEARCHING","400")
                .withOption('order', [[1, 'asc' ]]);

            function _fnRowRender(data, type, full, meta) {
                var frozenBoxCode = '';
                if(full.frozenBoxCode1D){
                    frozenBoxCode = "1D:"+full.frozenBoxCode1D;
                }else{
                    frozenBoxCode = "2D:"+full.frozenBoxCode;
                }
                return frozenBoxCode;
            }
            //初始化信息（样本类型、样本分类、盒类型、暂存位置）
            function _fnBoxInfoInit() {
                EquipmentAllService.query({},onEquipmentSuccess, onError);
                function onEquipmentSuccess(data) {
                    vm.frozenBoxPlaceOptions = _.orderBy(data,['equipmentCode'],['asc']);
                }
            }
            //获取冻存盒信息（盒子信息和管子信息）
            function _queryBox() {
                StockInInputService.queryEditStockInBox(53783).success(function (data) {
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
