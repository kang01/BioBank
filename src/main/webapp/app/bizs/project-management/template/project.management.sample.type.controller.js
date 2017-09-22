/**
 * Created by gaokangkang on 2017/9/6.
 */
(function() {
    'use strict';

    angular
        .module('bioBankApp')
        .controller('ProjectManagementSampleTypeController', ProjectManagementSampleTypeController);

    ProjectManagementSampleTypeController.$inject = ['$scope','$state','$stateParams','hotRegisterer','SampleTypeService','FrozenBoxTypesService'];

    function ProjectManagementSampleTypeController($scope,$state,$stateParams,hotRegisterer,SampleTypeService,FrozenBoxTypesService) {
        console.log($stateParams);
        var vm = this;
        vm.sampleTypeItem = [
            {id:1,sampleTypeCode:"",num:"",sampleClassificationId:"",color:""}
        ];
        vm.box = {};
        //
        vm.createSampleType = _fnCreateSampleType;
        //
        vm.removeSampleType = _fnRemoveSampleType;
        //是否启用
        vm.isEnabled = _fnIsEnabled;


        function _fnCreateSampleType() {
            // console.log(JSON.stringify(vm.sampleTypeItem));
            // _.forEach(vm.sampleTypeItem,function (item) {
            //     _.remove(vm.sampleTypeOptions,{sampleTypeCode:item.sampleTypeCode});
            // });

            if(vm.sampleTypeItem.length < 11){
                vm.sampleTypeItem.push({id:vm.sampleTypeItem.length,sampleTypeCode:"",sampleClassificationId:"",color:""});
            }

        }
        function _fnRemoveSampleType(index) {
            vm.sampleTypeItem.splice(index,1);
        }
        function _fnQuerySampleType() {
            SampleTypeService.querySampleType().success(function (data) {
                vm.sampleTypeOptions = _.orderBy(data, ['sampleTypeId'], ['asc']);
                _.remove(vm.sampleTypeOptions,{sampleTypeCode:"99"});
                _.remove(vm.sampleTypeOptions,{sampleTypeCode:"98"});
                _.remove(vm.sampleTypeOptions,{sampleTypeCode:"97"});
            });
        }
        _fnQuerySampleType();
        vm.sampleTypeConfig = {
            valueField:'sampleTypeCode',
            labelField:'sampleTypeName',
            maxItems: 1,
            onChange:function (value) {
                // var len = _.filter(vm.sampleTypeItem,{sampleTypeCode:value}).length;
                // console.log(len);
            }
        };

        //99样本
        function _initFrozenTube() {

            vm.settings = {
                rowHeaders : ['A','B','C','D','E','F','G','H','J','K'],
                minRows: 10,
                minCols: 10,
                fillHandle:false,
                stretchH: 'all',
                colWidths: 80,
                rowHeaderWidth: 30,
                columnHeaderHeight:30,
                editor: false,
                data:vm.tubeList,
                colHeaders : function() {
                    return  "<input type='text' class='column_name_edit form-control' style='width: 78%;margin-top: 3px;padding: 0 12px;' disabled>";
                },
                renderer:function (hotInstance, td, row, col, prop, value, cellProperties) {
                    return "";
                },
                afterGetColHeader:function (col, TH) {
                    if(vm.startUseFlag){
                        $(TH).find(".column_name_edit").attr({"disabled":false});
                    }

                    $(TH).find(".column_name_edit").blur(function () {
                        var hot = _getTableCtrl();
                        var countRows = hot.countRows();
                        for(var i = 0; i < countRows;i++){
                            if($(this)[0].value){
                                hot.getCell(i,col).style.backgroundColor = "#eee";
                            }else{
                                hot.getCell(i,col).style.backgroundColor = "#fff";
                            }
                        }
                    })
                }



            };
        }
        _initFrozenTube();
        _fnQueryBoxType();
        function _fnQueryBoxType() {
           //盒子类型
           FrozenBoxTypesService.query({},function (data) {
               vm.frozenBoxTypeOptions = _.orderBy(data, ['id'], ['asc']);
               vm.box.frozenBoxTypeId = vm.frozenBoxTypeOptions[0].id;
               vm.box.frozenBoxTypeColumns = vm.frozenBoxTypeOptions[0].frozenBoxTypeColumns;
           }, function (data) {

           });
       }

        // 创建一个对象用于管子Table的控件
        function _createTubeForTableCell(tubeInBox, box, rowNO, colNO, pos){
            var tube = {
                tubeRows: pos.tubeRows,
                tubeColumns: pos.tubeColumns,
                rowNO: rowNO,
                colNO: colNO
            };
            return tube;
        }
        function _reloadTubesForTable(box){
            var tableCtrl = hotRegisterer.getInstance('my-handsontable');
            var tableWidth = $(tableCtrl.container).width();
            var settings = {
                minCols: +box.frozenBoxTypeColumns,
                minRows: +box.frozenBoxTypeRows
            };

            var rowHeaderWidth = 30;
            // 架子定位列表每列的宽度
            var colWidth = (tableWidth - rowHeaderWidth) / settings.minCols;

            var tubesInTable = [];
            // var colHeaders = [];
            var rowHeaders = [];
            for (var i=0; i<settings.minRows; ++i){
                var pos = {tubeRows: String.fromCharCode('A'.charCodeAt(0) + i), tubeColumns: 1 + ""};
                if(i > 7){
                    pos.tubeRows = String.fromCharCode('A'.charCodeAt(0) + i+1);
                }
                var tubes = [];
                rowHeaders.push(pos.tubeRows);
                for (var j = 0; j < settings.minCols; ++j){
                    pos.tubeColumns = j + 1 + "";
                    // if (colHeaders.length < settings.minCols){
                    //     colHeaders.push(pos.tubeColumns);
                    // }
                    var tubeInBox = _.filter(box.frozenTubeDTOS, pos)[0];
                    var tube = _createTubeForTableCell(tubeInBox, box, i, j + 1, pos);
                    tubes.push(tube);
                }
                tubesInTable.push(tubes);
            }

            vm.frozenTubeArray = tubesInTable;

            settings.rowHeaders = rowHeaders;
            // settings.colHeaders = colHeaders;
            settings.colWidths = colWidth;
            settings.manualColumnResize = colWidth;
            tableCtrl.updateSettings(settings);
            tableCtrl.loadData(tubesInTable);
        }
        //盒子类型
        vm.boxTypeConfig = {
            valueField:'id',
            labelField:'frozenBoxTypeName',
            maxItems: 1,
            onChange:function(value) {
                var boxType = _.find(vm.frozenBoxTypeOptions, {id: +value});
                if (!boxType) {
                    return;
                }
                vm.box.frozenBoxTypeId = value;
                vm.box.frozenBoxTypeRows = boxType.frozenBoxTypeRows;
                vm.box.frozenBoxTypeColumns = boxType.frozenBoxTypeColumns;
                vm.box.frozenTubeDTOS = [];
                var tubeList = [];
                for (var i = 0; i < vm.box.frozenBoxTypeRows; i++) {
                    tubeList[i] = [];
                    var rowNO = i > 7 ? i + 1 : i;
                    rowNO = String.fromCharCode(rowNO + 65);
                    for (var j = 0; j < vm.box.frozenBoxTypeColumns; j++) {
                        tubeList[i][j] = {
                            sampleTypeId: "",
                            sampleClassificationId: "",
                            tubeRows: rowNO,
                            tubeColumns: j + 1+""
                        };
                        vm.box.frozenTubeDTOS.push(tubeList[i][j]);
                    }
                }
                _reloadTubesForTable(vm.box);
                hotRegisterer.getInstance('my-handsontable').render();
            }
        };

        function _fnIsEnabled() {
           var hot =  _getTableCtrl();
           hot.render();
        }

        // 获取上架位置列表的控制实体
        function _getTableCtrl() {
            vm.tableCtrl = hotRegisterer.getInstance('my-handsontable');
            return vm.tableCtrl;
        }



    }
})();
