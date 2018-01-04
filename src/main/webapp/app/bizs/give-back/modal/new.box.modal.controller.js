/**
 * Created by gaokangkang on 2017/12/27.
 */
(function() {
    'use strict';

    angular
        .module('bioBankApp')
        .controller('NewBoxModalController', NewBoxModalController);
    NewBoxModalController.$inject = ['$scope','$uibModalInstance','$q','toastr','items','MasterMethod','GiveBackService','SupportRackType','FrozenBoxTypesService','SampleTypeService','StockInInputService','AreasByEquipmentIdService','SupportacksByAreaIdService'];

    function NewBoxModalController($scope,$uibModalInstance,$q,toastr,items,MasterMethod,GiveBackService,SupportRackType,FrozenBoxTypesService,SampleTypeService,StockInInputService,AreasByEquipmentIdService,SupportacksByAreaIdService) {

        var vm = this;
        vm.box = {};
        vm.equipmentOptions = items.equipmentOptions;
        var _projectId = items.projectId;
        var _giveBackId = items.giveBackId;
        //生成二维编码码
        vm.btnSettings={
            icon:"fa-plus-circle",
            makeNewBoxCode:_makeNewBoxCode
        };
        //检测冻存盒编码是否重复
        vm.isRepeatCode = _isRepeatCode;

        _loadInitializeDataFromServer();
        // 加载页面上的初始化数据
        function _loadInitializeDataFromServer(){
            // 获取所有冻存盒类型
            var promiseForBoxType =  FrozenBoxTypesService.query({},function (data) {
                vm.boxTypeOptions = _.orderBy(data, ['id'], ['esc']);
                vm.box.frozenBoxTypeId = vm.boxTypeOptions[0].id;
            }, onError).$promise;
            // 获取所有样本类型
            var promiseForSampleType =  SampleTypeService.querySampleType().success(function (data) {
                vm.sampleTypeOptions = _.orderBy(data, ['sampleTypeCode'], ['desc']);
                _.remove(vm.sampleTypeOptions,{sampleTypeCode:"99"});
                //默认白细胞
                vm.box.sampleTypeId = _.find(vm.sampleTypeOptions,{"sampleTypeCode":"W"}).id;
                _querySampleClass(vm.box.sampleTypeId)
            });
            $q.all([promiseForBoxType,promiseForSampleType]).then(function(data){

            });
            //盒类型
            vm.boxTypeConfig = {
                valueField:'id',
                labelField:'frozenBoxTypeName',
                maxItems: 1,
                onInitialize: function(selectize){
                    vm.boxTypeInstance = selectize;
                },
                onChange:function(value) {}
            };
            //样本类型
            vm.sampleTypeConfig = {
                valueField:'id',
                labelField:'sampleTypeName',
                maxItems: 1,
                onChange:function (value) {
                    vm.sampleClassSelectize.clearOptions();
                    vm.box.sampleClassificationId = null;
                    if(value){
                        var sampleTypeCode = _.find(vm.sampleTypeOptions,{'id':+value}).sampleTypeCode;
                        //获取样本分类
                        _querySampleClass(value);
                        //RNA：大橘盒 DNA：96孔板
                        _changeBoxType(sampleTypeCode,vm.boxTypeOptions,vm.boxTypeInstance);

                    }
                }
            };
            //样本分类
            vm.sampleClassConfig = {
                valueField:'sampleClassificationId',
                labelField:'sampleClassificationName',
                maxItems: 1,
                onInitialize:function (initialize) {
                    vm.sampleClassSelectize = initialize;
                },
                onChange:function (value) {
                }
            };
            //设备位置
            vm.equipmentConfig = {
                valueField:'id',
                labelField:'equipmentCode',
                maxItems: 1,
                onChange:function (value) {
                    vm.areaSelectize.clearOptions();
                    vm.box.areaId = null;
                    _queryArea(value)
                }
            };
            //区域位置
            vm.areaConfig = {
                valueField:'id',
                labelField:'areaCode',
                maxItems: 1,
                onInitialize:function (initialize) {
                    vm.areaSelectize = initialize;
                },
                onChange:function (value) {
                    vm.shelfSelectize.clearOptions();
                    vm.box.supportRackId = null;
                    _queryShelf(value);
                }
            };
            //架子位置
            vm.shelfConfig = {
                valueField:'id',
                labelField:'supportRackCode',
                maxItems: 1,
                onInitialize:function (initialize) {
                    vm.shelfSelectize = initialize;
                },
                onChange:function (value) {
                    vm.boxRowCol = null;
                    vm.box.columnsInShelf = null;
                    vm.box.rowsInShelf = null;
                    $scope.$apply();
                }
            };
        }
        //生成二维编码码
        function _makeNewBoxCode() {
            if(!vm.box.sampleTypeId || !vm.box.sampleClassificationId){
                toastr.error("请选择样本类型或样本分类!");
                return;
            }
            StockInInputService.makeNewBoxCode(_projectId,vm.box.sampleTypeId,vm.box.sampleClassificationId).success(function (data) {
                vm.box.frozenBoxCode = data.code;
            }).error(function (data) {
                toastr.error(data.message);
            })
        }
        //获取样本分类
        function _querySampleClass(sampleTypeId) {
            if(!sampleTypeId || !_projectId){
                return
            }
            SampleTypeService.queryProjectSampleClasses(_projectId,sampleTypeId).success(function (data) {
                vm.sampleClassOptions = data;
                vm.sampleClassOptions.push({sampleClassificationId:null,sampleClassificationName:null});
                vm.box.sampleClassificationId = vm.sampleClassOptions[0].sampleClassificationId;

            })
        }
        //获取冻存区域
        function _queryArea(equipmentId) {
            if(!equipmentId){
                return
            }
            AreasByEquipmentIdService.query({id:equipmentId},function (data) {
                vm.areaOptions = data;
                vm.areaOptions.push({id:null,areaCode:null});
            }, onError);
        }
        //获取架子
        function _queryShelf(areaId) {
            if(!areaId){
                return
            }
            SupportacksByAreaIdService.query({id:areaId},function (data) {
                vm.shelfOptions = data;
            }, onError);
        }
        //改变盒类型 RNA：大橘盒 DNA：96孔板
        function _changeBoxType(sampleTypeCode,boxTypeOptions,boxTypeInstance) {
            var boxType = MasterMethod.changeBoxType(boxTypeOptions,sampleTypeCode);
            if (boxType) {
                boxTypeInstance.setValue(boxType.id);
            }
        }
        //位置的行号列号
        vm.splitPlace = function () {
            if(vm.boxRowCol){
                vm.box.columnsInShelf = vm.boxRowCol.charAt(0);
                vm.box.rowsInShelf = vm.boxRowCol.substring(1);
            }else{
                vm.box.columnsInShelf = null;
                vm.box.rowsInShelf = null;
            }
        };
        //检测冻存盒编码是否重复
        function _isRepeatCode() {
            if(vm.box.frozenBoxCode){
                GiveBackService.queryRepeatBoxCode(vm.box.frozenBoxCode).success(function (data) {

                }).error(function (data) {
                    toastr.error(data.message);
                    vm.box.frozenBoxCode = "";
                })
            }

        }

        function onError(error) {
            toastr.error(error.data.message);
        }

        vm.ok = function () {
            var boxList = [];
            boxList.push(vm.box);
            GiveBackService.saveNewBox(_giveBackId,boxList).success(function (data) {
                toastr.success("保存成功！");
                $uibModalInstance.close();
            }).error(function (data) {
                toastr.error(data.message);
            });
        };
        vm.cancel = function () {
            $uibModalInstance.dismiss('cancel');
        };


    }

})();
