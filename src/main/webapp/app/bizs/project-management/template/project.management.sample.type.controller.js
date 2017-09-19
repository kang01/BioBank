/**
 * Created by gaokangkang on 2017/9/6.
 */
(function() {
    'use strict';

    angular
        .module('bioBankApp')
        .controller('ProjectManagementSampleTypeController', ProjectManagementSampleTypeController);

    ProjectManagementSampleTypeController.$inject = ['$scope','$state','hotRegisterer','SampleTypeService','FrozenBoxTypesService'];

    function ProjectManagementSampleTypeController($scope,$state,hotRegisterer,SampleTypeService,FrozenBoxTypesService) {
        var vm = this;
        vm.sampleTypeItem = [
            {id:1,sampleTypeId:"",num:"",sampleClassificationId:"",color:""}
        ];
        vm.box = {};
        vm.nums = [];
        vm.createSampleType = _fnCreateSampleType;
        vm.changeColor = _fnChangeColor;
        vm.changeSampleTypeColor = _fnChangeSampleTypeColor;
        vm.removeSampleType = _fnRemoveSampleType;

        function _fnChangeColor(color) {
            vm.obj = {
                "background-color" : color
            };
        }
        function _fnCreateSampleType() {
            if(vm.sampleTypeItem.length < 11){
                vm.sampleTypeItem.push({id:vm.sampleTypeItem.length,sampleTypeId:"",sampleClassificationId:"",color:""});
            }

        }
        var colIndexs = [];
        function _fnChangeSampleTypeColor(colIndex,item) {
            console.log($("#tableHeader"));
            // var tags = $(".column_name_edit");
            // console.log($("#tableHeader").getAttribute("index"));
            // for(var i in tags){
            //     console.log(tags[i].getAttribute("index"));
                // if(tags[i].nodeType==1){
                //     if(tags[i].getAttribute("index") == classnames){ //如果某元素的class值为所需要
                //         objArray[index]=tags[i];
                //         index++;
                //     }
                // }
            // }



            // var tableCtrl = _getTableCtrl();
            // var countCols = tableCtrl.countCols();
            // var index = _.lastIndexOf(colIndexs, colIndex);
            //
            // if(!item){
            //     if(index != -1){
            //         colIndexs.splice(index,1);
            //     }
            // }else{
            //     if(index == -1){
            //         colIndexs.push(colIndex);
            //     }
            // }
            //
            // console.log(colIndex);
            // console.log(item);
            // tableCtrl.render();
        }
        function _fnRemoveSampleType(index) {
            vm.sampleTypeItem.splice(index,1);
        }
        function _initFrozenTube() {

            vm.settings = {
                rowHeaders : ['A','B','C','D','E','F','G','H','J','K'],
                minRows: 10,
                minCols: 10,
                fillHandle:false,
                stretchH: 'all',
                colWidths: 80,
                rowHeaderWidth: 30,
                rowHeaderHeight: 20,
                data:vm.tubeList,
                colHeaders : function(index) {
                    // console.log(index);
                    // document.getElementById("tableHeader").setAttribute("index",index);
                    var html = "<input type='text' class='column_name_edit form-control' style='width: 78%' onblur='document.getElementById(\"tableHeader\").setAttribute(\"index\","+index+");document.getElementById(\"tableHeader\").click()'>";
                    return html;
                },
                renderer: function(hotInstance, td, row, col, prop, value, cellProperties) {
                    for(var i = 0; i < colIndexs.length;i++){
                        if(col == colIndexs[i]){
                            td.style.backgroundColor = 'blue';
                        }
                    }
                    var countCols = hotInstance.countCols();
                }



            };
        }
        _initFrozenTube();
        _fnQuerySampleType();
        _fnQueryBoxType();
       function _fnQueryBoxType() {
           //盒子类型
           FrozenBoxTypesService.query({},function (data) {
               vm.frozenBoxTypeOptions = _.orderBy(data, ['id'], ['asc']);
               vm.box.frozenBoxTypeId = vm.frozenBoxTypeOptions[0].id;
               vm.box.frozenBoxTypeColumns = vm.frozenBoxTypeOptions[0].frozenBoxTypeColumns;
               for(var i = 0; i < vm.box.frozenBoxTypeColumns;i++){
                   vm.nums.push("");
               }
           }, function (data) {

           });
       }
        function _fnQuerySampleType() {
            SampleTypeService.querySampleType().success(function (data) {
                vm.sampleTypeOptions = _.orderBy(data, ['sampleTypeId'], ['asc']);
                _.remove(vm.sampleTypeOptions,{sampleTypeCode:"99"});
                _.remove(vm.sampleTypeOptions,{sampleTypeCode:"98"});
                _.remove(vm.sampleTypeOptions,{sampleTypeCode:"97"});
            });
        }
        function _fnQueryProjectSampleClass(projectId,sampleTypeId) {
            SampleTypeService.queryProjectSampleClasses(projectId,sampleTypeId).success(function (data) {
                vm.projectSampleTypeOptions = data;
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
        vm.sampleTypeConfig = {
            valueField:'id',
            labelField:'sampleTypeName',
            maxItems: 1,
            onChange:function (value) {
                _fnQueryProjectSampleClass('31',value)
            }
        };
        vm.projectSampleTypeConfig = {
            valueField:'sampleClassificationId',
            labelField:'sampleClassificationName',
            maxItems: 1,
            onChange:function (value) {
            }
        };
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
                vm.nums = [];
                for(var i = 0; i < vm.box.frozenBoxTypeColumns;i++){

                    vm.nums.push("");
                }
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

        // 获取上架位置列表的控制实体
        function _getTableCtrl() {
            vm.tableCtrl = hotRegisterer.getInstance('my-handsontable');
            return vm.tableCtrl;
        }



    }
})();
