/**
 * Created by gaoyankang on 2017/4/9.
 */
(function() {
    'use strict';

    angular
        .module('bioBankApp')
        .controller('AddBoxModalController', AddBoxModalController);

    AddBoxModalController.$inject = ['$scope','$uibModalInstance','toastr','StockInInputService','items','AlertService','FrozenBoxTypesService','EquipmentAllService','AreasByEquipmentIdService','SupportacksByAreaIdService','BoxCodeIsRepeatService','SampleTypeService'];

    function AddBoxModalController($scope,$uibModalInstance,toastr,StockInInputService,items,AlertService,FrozenBoxTypesService,EquipmentAllService,AreasByEquipmentIdService,SupportacksByAreaIdService,BoxCodeIsRepeatService,SampleTypeService) {
        var vm = this;
        vm.createBoxflag = false;
        var boxes = items.incompleteBoxes;
        var stockInFrozenTubeList = angular.copy(items.box.stockInFrozenTubeList);
        vm.box = {
            frozenBoxCode1D:null
        };
        var projectId = items.projectId;
        var stockInCode = items.stockInCode;
        //是否混合类型 1：是
        vm.isMixed = items.isMixed;
        //样本类型
        var sampleTypeId = items.sampleTypeId;
        var sampleTypeCode = items.sampleTypeCode;
        vm.box.sampleTypeId = items.sampleTypeId;
        vm.box.sampleTypeCode = items.sampleTypeCode;
        //样本分类
        var sampleTypeClassId = items.sampleTypeClassId;
        var sampleTypeClassCode = items.sampleTypeClassCode;
        //盒类型Id
        var frozenBoxTypeId = items.frozenBoxTypeId;
        vm.box.frozenBoxType = items.frozenBoxType;
        var status = items.status;


        var countFlag = true;
        var initData = function () {
            //盒子类型
            FrozenBoxTypesService.query({},onFrozenBoxTypeSuccess, onError);
            //样本分类
            _fnQuerySampleType();
        };
        initData();
        //获取样本类型
        function _fnQuerySampleType() {
            SampleTypeService.querySampleType().success(function (data) {
                vm.sampleTypeOptions = _.orderBy(data, ['id'], ['asc']);
                //去除混合类型
                _.remove(vm.sampleTypeOptions,{sampleTypeName:"98"});
                _.remove(vm.sampleTypeOptions,{sampleTypeName:"99"});
                //1:新加第二个盒子 2：新加第一个盒子
                if(status == "1"){
                    vm.problemOptions = _.remove(vm.sampleTypeOptions,{id:sampleTypeId});
                    vm.box.sampleTypeId = sampleTypeId;
                    vm.box.sampleTypeCode = sampleTypeCode;
                    vm.box.sampleType = _.find(vm.problemOptions,{'id': + vm.box.sampleTypeId});
                    vm.fullBoxFlag = true;
                }else{
                    if(vm.isMixed == "1"){
                        //99类型下有分类时，选择完了分类的变化
                        vm.problemOptions = angular.copy(vm.sampleTypeOptions);
                        if(boxes.length){
                            // var problemSampleTypeId = _.find(vm.problemOptions,{sampleTypeCode:"97"}).id;
                            for (var i = 0; i < boxes.length; i++) {
                                if(boxes[i].sampleTypeCode == "97"){
                                    _.remove(vm.problemOptions,{sampleTypeCode:"97"});
                                }
                            }
                            vm.noSampleClassFlag = false;
                            if(!sampleTypeClassId){
                                if(boxes.length){
                                    vm.sampleFlag = false;
                                    vm.createBoxflag = true;
                                    vm.fullBoxFlag = true;
                                    return;
                                }
                                //是混合类型，并且无分类，为问题样本
                                vm.problemOptions = _.remove(vm.problemOptions,function (o) {
                                    if(o.sampleTypeCode == "97"){
                                        return o
                                    }
                                });
                            }
                        }else{
                            vm.createBoxflag = true;
                            if(!sampleTypeClassId){
                                //是混合类型，并且无分类，为问题样本
                                vm.problemOptions = _.remove(vm.problemOptions,function (o) {
                                    if(o.sampleTypeCode == "97"){
                                        return o
                                    }
                                });
                            }
                        }
                        if(vm.problemOptions.length){
                            vm.box.sampleType = vm.problemOptions[0];
                            vm.box.sampleTypeId = vm.problemOptions[0].id;
                            vm.box.sampleTypeCode = vm.problemOptions[0].sampleTypeCode;
                        }
                    }else{
                        //不是混合类型,只用取97类型跟盒子类型的样本类型
                        vm.noSampleClassFlag = true;
                        vm.problemOptions = _.remove(vm.sampleTypeOptions,function (o) {
                            if(o.sampleTypeCode == "97" || o.sampleTypeCode == vm.box.sampleTypeCode){
                                return o
                            }
                        });
                        vm.problemOptions = _.remove(vm.problemOptions,function (o) {
                            if(boxes.length){
                                for(var i = 0; i < boxes.length; i++){
                                    if(boxes[i].sampleTypeCode != o.sampleTypeCode){
                                        return o;
                                    }
                                }
                            }else{
                                return o;
                            }

                        });
                        vm.box.sampleTypeId = vm.problemOptions[0].id;
                        vm.box.sampleTypeCode = vm.problemOptions[0].sampleTypeCode;
                        vm.box.sampleType = vm.problemOptions[0];

                    }
                }
                // var problemSampleTypeCode = vm.box.sampleTypeCode;
                if(vm.box.sampleTypeCode != "97"){
                    _fnQueryProjectSampleClasses(projectId,vm.box.sampleTypeId);
                }else{
                    vm.createBoxflag = true;
                    vm.noSampleClassFlag = true;
                    _createBox();
                }

            });
        }
        vm.fullBoxFlag = false;
        function _fnQueryProjectSampleClasses(projectId,sampleTypeId) {

            SampleTypeService.queryProjectSampleClasses(projectId,sampleTypeId).success(function (data) {
                vm.sampleTypeClassOptions = _.orderBy(data, ['sampleClassificationId'], ['asc']);
                vm.noSampleClassFlag = true;
                //1:新加第二个盒子 2：新加第一个盒子
                if(status == "2"){
                    if(boxes.length){
                        if(vm.isMixed == "1" && sampleTypeClassId) {
                            for (var i = 0; i < boxes.length; i++) {
                                for (var j = 0; j < vm.sampleTypeClassOptions.length; j++) {
                                    if (boxes[i].sampleTypeCode == vm.sampleTypeClassOptions[j].sampleClassificationCode) {
                                        _.pullAt(vm.sampleTypeClassOptions, j);
                                    }
                                }
                            }

                            if(vm.sampleTypeClassOptions.length){
                                vm.noSampleClassFlag = true;
                                vm.box.sampleClassificationId = vm.sampleTypeClassOptions[0].sampleClassificationId;
                                vm.box.sampleClassificationCode = vm.sampleTypeClassOptions[0].sampleClassificationCode;
                                vm.box.sampleClassification = vm.sampleTypeClassOptions[0];
                            }else{
                                vm.box.sampleClassificationId = "";
                                vm.box.sampleClassificationCode = "";
                                vm.box.sampleClassification = "";
                                vm.sampleTypeClassOptions = [];
                                if(vm.box.sampleType.sampleTypeCode == "97"){
                                    vm.noSampleClassFlag = true;
                                }else{
                                    vm.noSampleClassFlag = false;
                                }


                            }
                        }
                    }else{
                        vm.noSampleClassFlag = true;
                        if(vm.sampleTypeClassOptions.length){
                            if(!vm.box.sampleClassificationCode){
                                vm.box.sampleClassificationId = vm.sampleTypeClassOptions[0].sampleClassificationId;
                                vm.box.sampleClassificationCode = vm.sampleTypeClassOptions[0].sampleClassificationCode;
                                vm.box.sampleClassification = vm.sampleTypeClassOptions[0];
                            }

                        }
                    }
                }
                if(countFlag){
                    //创建第一个新盒子，空管子
                    if(!stockInFrozenTubeList.length){
                        vm.createBoxflag = true;
                        _createBox();
                    }else{
                        vm.fullBoxFlag = true;
                    }
                }

            });
        }

        function onFrozenBoxTypeSuccess(data) {
            vm.frozenBoxTypeOptions = data;
            if(!items.box.frozenBoxTypeId){
                vm.box.frozenBoxTypeId = vm.frozenBoxTypeOptions[0].id;
                vm.box.frozenBoxType = vm.frozenBoxTypeOptions[0];
            }
        }
        function onError(error) {
            AlertService.error(error.data.message);
        }
        vm.sampleFlag = true;
        //是否手动创建盒子
        function _createBox() {
            if(vm.createBoxflag){
                // vm.box.frozenBoxCode="";
                // vm.box.memo = "";
                // vm.box.stockInFrozenTubeList = [];
                if(stockInFrozenTubeList.length){
                    var rows = +items.box.sampleType.frozenBoxTypeRows;
                    var cols = +items.box.sampleType.frozenBoxTypeColumns;
                    var m = 0,n = 0;
                    for(var i = 0; i < stockInFrozenTubeList.length; i++){
                        if(i >= rows){
                            m++;n = 0;
                            stockInFrozenTubeList[i].tubeRows = String.fromCharCode(m+65);
                            stockInFrozenTubeList[i].tubeColumns = n + 1;

                        }else{
                            n++;
                            stockInFrozenTubeList[i].tubeRows = String.fromCharCode(m + 65);
                            stockInFrozenTubeList[i].tubeColumns = n;
                        }

                    }
                    vm.box.stockInFrozenTubeList = stockInFrozenTubeList;
                }
                //盒子类型
                if(frozenBoxTypeId){
                    vm.box.frozenBoxTypeId = frozenBoxTypeId;
                    vm.box.frozenBoxType = _.filter(vm.frozenBoxTypeOptions,{'id':+vm.box.frozenBoxTypeId})[0];
                }



                //不是混合类型、有分类
                if(vm.isMixed != "1" && sampleTypeClassId){
                    if(!vm.fullBoxFlag){
                        for(var i = 0; i < boxes.length; i++){
                            for(var j = 0; j < vm.sampleTypeClassOptions.length; j++){
                                if(boxes[i].sampleTypeCode == vm.sampleTypeClassOptions[j].sampleClassificationCode){
                                    _.pullAt(vm.sampleTypeClassOptions,j);
                                }
                            }
                        }
                        // var problemSampleTypeId = _.find(vm.problemOptions,{sampleTypeCode:"97"}).id;
                        for (var i = 0; i < boxes.length; i++) {
                            if(boxes[i].sampleTypeCode == "97"){
                                _.remove(vm.problemOptions,{sampleTypeCode:"97"});
                            }
                        }
                        vm.box.sampleTypeId = sampleTypeId;
                        vm.box.sampleTypeCode = sampleTypeCode;
                        vm.box.sampleType = _.filter(vm.problemOptions,{'id':+vm.box.sampleTypeId})[0];

                        if(vm.sampleTypeClassOptions.length){
                            vm.box.sampleClassificationId = vm.sampleTypeClassOptions[0].sampleClassificationId;
                            vm.box.sampleClassificationCode = vm.sampleTypeClassOptions[0].sampleClassificationCode;
                            vm.box.sampleClassification = vm.sampleTypeClassOptions[0];
                            sampleTypeClassId = vm.box.sampleClassificationId;
                            sampleTypeClassCode = vm.box.sampleClassificationCode;
                        }else{
                            _.remove(vm.problemOptions,{"id":sampleTypeId});

                            if(vm.problemOptions.length){
                                // var problemSampleTypeId = _.find(vm.problemOptions,{sampleTypeCode:"97"}).id;
                                for (var i = 0; i < boxes.length; i++) {
                                    if(boxes[i].sampleTypeCode == "97"){
                                        _.remove(vm.problemOptions,{sampleTypeCode:"97"});
                                    }
                                }
                                vm.problemOptions = _.remove(vm.problemOptions,function (o) {
                                    if(o.sampleTypeCode == "97"){
                                        return o
                                    }
                                });

                                vm.sampleFlag = true;
                                vm.box.sampleTypeId = vm.problemOptions[0].id;
                                vm.box.sampleTypeCode = vm.problemOptions[0].sampleTypeCode;
                                vm.box.sampleType = vm.problemOptions[0];
                            }else{
                                vm.sampleFlag = false;
                            }
                        }
                    }else{

                        if(vm.sampleTypeClassOptions.length){
                            vm.box.sampleClassificationId = _.find(vm.sampleTypeClassOptions,{sampleClassificationId:sampleTypeClassId}).sampleClassificationId;
                            vm.box.sampleClassificationCode = _.find(vm.sampleTypeClassOptions,{sampleClassificationId:sampleTypeClassId}).sampleClassificationCode;
                            vm.box.sampleClassification = _.find(vm.sampleTypeClassOptions,{sampleClassificationId:sampleTypeClassId});
                        }
                    }
                }
                //是混合、无分类
                if(vm.isMixed == "1" && !sampleTypeClassId){
                    if(!vm.fullBoxFlag){
                        // for(var i = 0; i < boxes.length; i++){
                        //     for(var j = 0; j < vm.sampleTypeOptions.length; j++){
                        //         if(boxes[i].sampleTypeId == vm.sampleTypeOptions[j].id){
                        //             _.pullAt(vm.sampleTypeOptions,j);
                        //         }
                        //     }
                        // }

                        // if(vm.problemOptions.length){
                        //     vm.box.sampleTypeId = vm.problemOptions[0].id;
                        //     vm.box.sampleType = _.filter(vm.problemOptions,{'id':+vm.box.sampleTypeId})[0];
                        // }else{
                        //     vm.sampleFlag = false;
                        // }
                    }else{
                        if(vm.sampleTypeOptions.length){
                            vm.box.sampleTypeId = vm.problemOptions[0].id;
                            vm.box.sampleTypeCode = vm.problemOptions[0].sampleTypeCode;
                            vm.box.sampleType = vm.problemOptions[0];
                        }
                    }


                }

                if(!vm.fullBoxFlag){
                    // 是混合、有分类
                    if(vm.isMixed == "1" && sampleTypeClassId){
                        if(boxes.length){
                            for(var i = 0; i < boxes.length; i++){
                                for(var j = 0; j < vm.sampleTypeClassOptions.length; j++){
                                    if(boxes[i].sampleTypeCode == vm.sampleTypeClassOptions[j].sampleClassificationCode){
                                        _.pullAt(vm.sampleTypeClassOptions,j);
                                    }
                                }
                            }
                        }
                        countFlag = true;
                        //样本类型下的样本分类为空时，样本类型也应该不存在
                        if(!vm.sampleTypeClassOptions.length){
                            // var sampleTypeCode = _.find(vm.problemOptions,{id:vm.box.sampleTypeId}).sampleTypeCode;
                            if(vm.box.sampleTypeCode != "97"){
                                for(var k = 0; k < vm.problemOptions.length; k++){
                                    if(vm.box.sampleTypeCode == vm.problemOptions[k].sampleTypeCode){
                                        _.pullAt(vm.problemOptions,k);
                                    }
                                }
                            }


                            if(vm.problemOptions.length){
                                vm.box.sampleTypeId = vm.problemOptions[0].id;
                                vm.box.sampleTypeCode = vm.problemOptions[0].sampleTypeCode;
                                vm.box.sampleType = vm.problemOptions[0];
                                if(vm.box.sampleTypeCode != "97"){
                                    _fnQueryProjectSampleClasses(projectId,vm.box.sampleTypeId);
                                }else{
                                    vm.noSampleClassFlag = true;
                                }
                            }else{
                                vm.sampleFlag = false;
                            }
                        }else{
                            vm.box.sampleTypeId = vm.problemOptions[0].id;
                            vm.box.sampleTypeCode = vm.problemOptions[0].sampleTypeCode;
                            vm.box.sampleType = _.filter(vm.problemOptions,{'id':+vm.box.sampleTypeId})[0];
                            vm.box.sampleClassificationId = vm.sampleTypeClassOptions[0].sampleClassificationId;
                            vm.box.sampleClassificationCode = vm.sampleTypeClassOptions[0].sampleClassificationCode;
                            vm.box.sampleClassification = vm.sampleTypeClassOptions[0];
                            sampleTypeClassId = vm.box.sampleClassificationId;
                            sampleTypeClassCode = vm.box.sampleClassificationCode;

                        }
                    }
                    //不是混合、无样本分类
                    if(vm.isMixed != "1" && !sampleTypeClassId){
                        if(boxes.length == 2){
                            vm.sampleFlag = false;
                        }
                    }

                }else{
                    if(vm.sampleTypeClassOptions.length){
                        vm.box.sampleClassificationId = _.find(vm.sampleTypeClassOptions,{sampleClassificationId:sampleTypeClassId}).sampleClassificationId;
                        vm.box.sampleClassificationCode = _.find(vm.sampleTypeClassOptions,{sampleClassificationId:sampleTypeClassId}).sampleClassificationCode;
                        vm.box.sampleClassification = _.find(vm.sampleTypeClassOptions,{sampleClassificationId:sampleTypeClassId});
                    }


                }




                // //样本类型为混合型时，同时有样本分类
                // if(vm.isMixed == "1"){
                //     vm.box.sampleTypeId = vm.sampleTypeOptions[0].id;
                //     vm.box.sampleType = _.filter(vm.sampleTypeOptions,{'id':+vm.box.sampleTypeId})[0];
                // }else{
                //     vm.box.sampleTypeId = sampleTypeId;
                //     vm.box.sampleType = _.filter(vm.sampleTypeOptions,{'id':+vm.box.sampleTypeId})[0];
                // }
                // //有无样本分类
                // if(sampleTypeClassId){
                //     vm.box.sampleClassificationId = sampleTypeClassId;
                //     vm.box.sampleClassification = _.filter(vm.sampleTypeClassOptions,{'sampleClassificationId':+vm.box.sampleClassificationId})[0];
                // }else{
                //     vm.box.sampleClassificationId = "";
                //     vm.box.sampleClassification = "";
                // }




            }
        }
        vm.boxTypeInstance = {};
        vm.boxTypeConfig = {
            valueField:'id',
            labelField:'frozenBoxTypeName',
            maxItems: 1,
            onInitialize: function(selectize){
                vm.boxTypeInstance = selectize;
            },
            onChange:function(value){
                vm.box.frozenBoxType = _.find(vm.frozenBoxTypeOptions,{'id':+value});
                vm.box.frozenBoxTypeId = value;
            }
        };
        vm.sampleTypeConfig = {
            valueField:'id',
            labelField:'sampleTypeName',
            maxItems: 1,
            onChange:function (value) {
                if(value){
                    vm.box.sampleType = _.find(vm.problemOptions,{'id':+value});
                    vm.box.sampleTypeCode = _.find(vm.problemOptions,{'id':+value}).sampleTypeCode;
                    vm.box.sampleTypeId = value;
                    countFlag = false;
                    // var problemSampleTypeCode = _.find(vm.problemOptions,{id:+value}).sampleTypeCode;
                    if(vm.box.sampleTypeCode != "97"){
                        _fnQueryProjectSampleClasses(projectId,value);
                    }else{
                        vm.box.sampleClassificationId = "";
                        vm.box.sampleClassificationCode = "";
                        vm.box.sampleClassification = "";
                        vm.sampleTypeClassOptions = [];
                        vm.noSampleClassFlag = true;
                        $scope.$apply();
                    }
                }else{
                    vm.noSampleClassFlag = false;
                }
                _changeBoxType(value);



            }
        };

        function _changeBoxType(value) {
            var sampleTypeCode;
            if(vm.isMixed == "1"){
                // Added by Zhuyu 2017/10/09 For: 选中RNA时自动切换冻存盒为大橘盒，选中97时切换为10x10
                sampleTypeCode = _.find(vm.sampleTypeOptions,{'id':+value}).sampleTypeCode;
            }else{
                sampleTypeCode = _.find(vm.problemOptions,{'id':+value}).sampleTypeCode;
            }

            var boxType = _.filter(vm.frozenBoxTypeOptions, {frozenBoxTypeCode: SampleTypeService.getBoxTypeCode(sampleTypeCode)})[0];
            if (boxType) {
                setTimeout(function(){
                    vm.boxTypeInstance.setValue(boxType.id);
                }, 100);
            }
            // end added
        }
        //样本分类
        vm.sampleTypeClassConfig = {
            valueField:'sampleClassificationId',
            labelField:'sampleClassificationName',
            maxItems: 1,
            onChange:function (value) {
                vm.box.sampleClassification = _.find(vm.sampleTypeClassOptions,{'sampleClassificationId':+value});
                vm.box.sampleClassificationCode = _.find(vm.sampleTypeClassOptions,{'sampleClassificationId':+value}).sampleClassificationCode;
                vm.box.sampleClassificationId = value;
            }
        };

        vm.isBoxCodeRepeat = function () {
            vm.isRepeat = false;
        };
        //搜索库存内的冻存盒
        vm.queryFrozenBox = function () {
            if(vm.box.frozenBoxCode){
                StockInInputService.queryUnfullStockInBox(vm.box.frozenBoxCode,projectId,stockInCode).success(function (data) {
                    if(data.frozenBoxCode){
                        vm.box = data;
                        vm.isMixed = vm.box.isMixed;
                        sampleTypeId = vm.box.sampleTypeId;
                        sampleTypeClassId = vm.box.sampleTypeClassId;
                        stockInFrozenTubeList = angular.copy(vm.box.stockInFrozenTubeList);
                        _fnQueryProjectSampleClasses(projectId,vm.box.sampleTypeId);
                        _changeBoxType(vm.box.sampleTypeId);
                    }
                }).error(function (res) {
                    toastr.error(res.message);
                    // vm.box.frozenBoxCode = "";
                });
            }

        };

        vm.cancel = function () {
            $uibModalInstance.dismiss('cancel');
        };
        vm.ok = function () {
            // vm.isRepeat = false;
            // BoxCodeIsRepeatService.getByCode(vm.box.frozenBoxCode).then(function (data) {
            //     vm.isRepeat = data;
            //     if (vm.isRepeat){
            //         return;
            //     }
            //     data = _.filter(boxes, function(b){
            //         var box = _.filter(b.boxList, {frozenBoxCode: vm.box.frozenBoxCode});
            //         return box && box.length;
            //     });
            //     vm.isRepeat = data && data.length;
            //     if (vm.isRepeat){
            //         return;
            //     }
            //
            //     if(vm.boxRowCol){
            //         vm.box.columnsInShelf = vm.boxRowCol.charAt(0);
            //         vm.box.rowsInShelf = vm.boxRowCol.charAt(vm.boxRowCol.length - 1);
            //     }
            //     vm.box.countOfSample = vm.box.stockInFrozenTubeList.length;
            //     $uibModalInstance.close(vm.box);
            // });


            if(vm.boxRowCol){
                vm.box.columnsInShelf = vm.boxRowCol.charAt(0);
                vm.box.rowsInShelf = vm.boxRowCol.charAt(vm.boxRowCol.length - 1);
            }
            if(!vm.box.stockInFrozenTubeList){
                vm.box.stockInFrozenTubeList = [];
            }
            vm.box.countOfSample = vm.box.stockInFrozenTubeList.length;
            $uibModalInstance.close(vm.box);

        };

        vm.yes = function () {
            vm.createBoxflag = true;
            _createBox();
        };
        vm.no = function () {
            $uibModalInstance.dismiss('cancel');
        };

        //生成新的冻存盒号
        vm.makeNewBoxCode = _fnMakeNewBoxCode;

        function _fnMakeNewBoxCode() {
            StockInInputService.makeNewBoxCode(projectId,vm.box.sampleTypeId,vm.box.sampleClassificationId).success(function (data) {
                vm.box.frozenBoxCode = data.code;
            }).error(function (data) {
                toastr.error(data.message);
                vm.box.frozenBoxCode = "";
            })
        }

    }
})();
