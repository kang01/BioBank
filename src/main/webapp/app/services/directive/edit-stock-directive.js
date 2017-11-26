(function()  {
    'use  strict';

    angular
        .module('bioBankApp')
        .directive('editStockDirective',  editStockDirective)
        .controller('EditStockController',EditStockController);

    editStockDirective.$inject  =  [];
    EditStockController.$inject  =  ['$scope','hotRegisterer','$uibModal','FrozenBoxTypesService','toastr','SampleTypeService','StockInInputService','SampleService'];
    // =： 通过属性进行双向数据绑定，内外变化会保持一致；
    // @： 通过属性值进行绑定，但是单向的，即外界控制器可以把值传进来供内部使用，但内部对这个属性值进行修改时不会影响到外部的；
    // &： 调用父级作用域中的方法（function）；
    function  editStockDirective()  {
        var  directive  =  {
            restrict: 'EAC',
            replace:true,
            scope  :  {
                stockInInfo  :  '=stockInInfo',
                stockInBox  :  '=stockInBox',
                reloadData: "&",
                editToSpiltTube: "&",
                editFlag: "=",
                showFlag: "="
            },
            templateUrl: 'app/bizs/stock-in/template/edit-stockInBox-template.html',
            link: function(scope, elem, attrs){

                // elem.html($compile(scope.compile)(scope))
            },
            controller: EditStockController,
            controllerAs: 'vm'
        };
        return  directive;
    }
    function EditStockController($scope,hotRegisterer,$uibModal,FrozenBoxTypesService,toastr,SampleTypeService,StockInInputService,SampleService) {
        var vm = this;
        var modalInstance;
        vm.obox = $scope.stockInBox;
        // var strBox = JSON.stringify(vm.obox);
        vm.entity = $scope.stockInInfo;
        var stockInBox1 = angular.copy($scope.stockInBox);
        var stockInBox2 = angular.copy($scope.stockInBox);
        $scope.$watch('stockInBox',function () {
            vm.obox = $scope.stockInBox;
            vm.editFlag = Boolean($scope.editFlag);
            // _initFrozenBoxPanel();
            _initBoxInfo();

        });
        $scope.$watch('stockInInfo',function () {
            vm.entity = $scope.stockInInfo;
        });
        //重复的样本编码
        vm.repeatSampleArray = [];
        _initFrozenBoxPanel();
        // _initBoxInfo();
        vm.saveStockInFlag = false;
        //单次录入 批量录入
        vm.singleMultipleFlag = "multiple";
        //批量编辑录入样本
        vm.editEntering = _fnEditEntering;
        //冻存盒搜索
        vm.frozenBoxForStockIn =_fnFrozenBoxForStockIn;
        //保存冻存盒
        vm.saveBox = _fnSaveBox;
        //生成新的冻存盒号
        vm.makeNewBoxCode = _fnMakeNewBoxCode;

        function _fnMakeNewBoxCode() {
            StockInInputService.makeNewBoxCode(vm.entity.projectId,vm.obox.sampleTypeId,vm.obox.sampleClassificationId).success(function (data) {
                vm.obox.frozenBoxCode = data.code;
            }).error(function (data) {
                toastr.error(data.message);
            })
        }
        //初始化冻存管
        var aRemarkArray = [];
        var domArray = [];//单元格操作的数据
        function _initFrozenBoxPanel() {
            var remarkArray;//批注
            vm.frozenTubeArray = [];//初始管子数据二位数组

            var operateColor;//单元格颜色

            initFrozenTube(10,10);
            var delFlag = false;

            vm.settings = {
                colHeaders : ['1','2','3','4','5','6','7','8','9','10'],
                rowHeaders : ['A','B','C','D','E','F','G','H','J','K'],
                data:vm.frozenTubeArray,
                minRows: 10,
                minCols: 10,
                fillHandle:false,
                stretchH: 'all',
                autoWrapCol:true,
                wordWrap:true,
                colWidths: 80,
                rowHeaderWidth: 30,
                editor: 'tube',
                multiSelect: true,
                comments: true,
                renderer: myCustomRenderer,
                onAfterSelectionEnd:function (row, col, row2, col2) {
                    //是否下一个
                    vm.nextFlag = true;
                    vm.remarkFlag = true;
                    var td = this;
                    remarkArray = this.getData(row,col,row2,col2);
                    var selectTubeArrayIndex = this.getSelected();
                    var array = _.flatten(remarkArray);
                    vm.selectedPos = this.getSelected();
                    vm.selectedTubesForDel = _.flatten(remarkArray);

                    if(window.event && window.event.ctrlKey){
                        //换位
                        vm.exchangeFlag = true;
                        domArray.push(vm.frozenTubeArray[row][col]);

                        //备注
                        _fnRemarkSelectData(td,remarkArray,selectTubeArrayIndex);
                    }else{
                        if(array.length == 2){
                            vm.exchangeFlag = true;
                            domArray = [];
                            for(var i = 0; i < array.length; i++){
                                domArray.push(array[i])
                            }
                        }
                        if(array.length == 1){
                            domArray = [];
                            domArray.push(array[0])
                        }
                        //备注
                        $(".temp").remove();
                        aRemarkArray = [];
                        _fnRemarkSelectData(td,remarkArray,selectTubeArrayIndex);
                    }
                    //管子
                    if(vm.flagStatus){
                        if(vm.frozenTubeArray[row][col].sampleCode || vm.frozenTubeArray[row][col].sampleTempCode){
                            var tubeStatus = vm.frozenTubeArray[row][col].status;
                            if(tubeStatus == 3001){
                                vm.frozenTubeArray[row][col].status = 3002;
                            }
                            if(tubeStatus == 3002){
                                vm.frozenTubeArray[row][col].status = 3003;
                            }
                            if(tubeStatus == 3003){
                                vm.frozenTubeArray[row][col].status = 3004;
                            }
                            if(tubeStatus == 3004){
                                vm.frozenTubeArray[row][col].status = 3001;
                            }
                            hotRegisterer.getInstance('my-handsontable').render();
                        }

                    }
                },
                enterMoves:function () {
                    var hotMoves = hotRegisterer.getInstance('my-handsontable');
                    var selectedRow = hotMoves.getSelected()[0];
                    var selectedCol = hotMoves.getSelected()[1];
                    var tubes = _.flatten(vm.frozenTubeArray);
                    var tube = _.find(tubes,{sampleCode:""});
                    if(tube){
                        var rowIndex = tube.rowNO - selectedRow;
                        var colIndex = tube.colNO - selectedCol - 1;
                        return{row:rowIndex,col:colIndex};
                    }else{
                        // return{row:0,col:0};

                        // added by zhuyu for 扫码不近格
                        if (vm.singleMultipleFlag == "single"){
                            return{row:0,col:0};
                        } else if(selectedCol + 1 < hotMoves.countCols()){
                            return{row:0,col:1};
                        } else{
                            return{row:1,col:-selectedCol};
                        }
                    }

                    // if(vm.nextFlag){
                    //     var hotMoves = hotRegisterer.getInstance('my-handsontable');
                    //     var selectedCol = hotMoves.getSelected()[1];
                    //     if(selectedCol + 1 < hotMoves.countCols()){
                    //         return{row:0,col:1};
                    //     } else{
                    //         return{row:1,col:-selectedCol};
                    //     }
                    // }else{
                    //     return{row:0,col:0};
                    // }

                },
                afterChange:function (change,source) {
                    if(vm.singleMultipleFlag != "single"){
                        return;
                    }
                    var tableCtrl = _getTableCtrl();
                    if(source == 'edit'){
                        for (var i=0; i<change.length; ++i){
                            var item = change[i];
                            var row = item[0];
                            var col = item[1];
                            var oldTube = item[2];
                            var newTube = item[3];
                            if (newTube.id
                                || (newTube.sampleCode && newTube.sampleCode.length > 1)
                                || (newTube.sampleTempCode && newTube.sampleTempCode.length > 1)){
                                StockInInputService.queryTube(oldTube.sampleCode,vm.entity.projectCode,vm.obox.frozenBoxId,oldTube.sampleTypeId,oldTube.sampleClassificationId).success(function (data) {
                                    var stockInTubes;
                                    if(vm.obox.sampleTypeName != "98" || vm.obox.sampleTypeName != "97"){
                                        stockInTubes = _.filter(data,{sampleTypeId:vm.obox.sampleTypeId});
                                    }else{
                                        stockInTubes = data;
                                    }
                                    if(vm.singleMultipleFlag == "single"){
                                        modalInstance = $uibModal.open({
                                            animation: true,
                                            templateUrl: 'app/bizs/stock-in/modal/stock-in-add-sample-modal.html',
                                            controller: 'StockInAddSampleModal',
                                            backdrop:'static',
                                            controllerAs: 'vm',
                                            size:'lg',
                                            resolve: {
                                                items: function () {
                                                    return {
                                                        tubes:stockInTubes,
                                                        sampleCode:oldTube.sampleCode,
                                                        frozenBoxId:vm.obox.frozenBoxId,
                                                        projectSiteId:vm.entity.projectSiteId,
                                                        projectId:vm.entity.projectId,
                                                        projectCode:vm.entity.projectCode,
                                                        sampleTypeId:vm.obox.sampleTypeId,
                                                        sampleTypeName:vm.obox.sampleTypeName,
                                                        sampleTypeCode:vm.obox.sampleTypeCode,
                                                        sampleClassificationId:vm.obox.sampleClassificationId,
                                                        sampleClassificationName:vm.obox.sampleClassificationName,
                                                        sampleClassificationCode:vm.obox.sampleClassificationCode,
                                                        singleMultipleFlag:vm.singleMultipleFlag
                                                    };
                                                }
                                            }

                                        });
                                        modalInstance.result.then(function (tubes) {
                                            for(var i = 0; i < vm.frozenTubeArray.length; i++){
                                                for(var j = 0; j < vm.frozenTubeArray[i].length; j++){
                                                    for(var k = 0; k < tubes.length; k++ ){
                                                        if(vm.frozenTubeArray[i][j].sampleCode == tubes[k].sampleCode){
                                                            vm.frozenTubeArray[i][j].status = tubes[k].status;
                                                            vm.frozenTubeArray[i][j].frozenTubeId = tubes[k].frozenTubeId;
                                                            vm.frozenTubeArray[i][j].projectSiteId = tubes[k].projectSiteId;
                                                            vm.frozenTubeArray[i][j].memo = tubes[k].memo;
                                                            vm.frozenTubeArray[i][j].sampleTypeId = tubes[k].sampleTypeId;
                                                            vm.frozenTubeArray[i][j].sampleTypeCode = tubes[k].sampleTypeCode;
                                                            vm.frozenTubeArray[i][j].sampleTypeName = tubes[k].sampleTypeName;
                                                            vm.frozenTubeArray[i][j].backColor = tubes[k].backColor;
                                                            vm.frozenTubeArray[i][j].sampleVolumns = tubes[k].sampleVolumns;
                                                            if(tubes[k].sampleClassificationId){
                                                                vm.frozenTubeArray[i][j].sampleClassificationId = tubes[k].sampleClassificationId;
                                                                vm.frozenTubeArray[i][j].sampleClassificationName = tubes[k].sampleClassificationName;
                                                                vm.frozenTubeArray[i][j].sampleClassitionCode = tubes[k].sampleClassitionCode;
                                                                vm.frozenTubeArray[i][j].backColorForClass = tubes[k].backColorForClass;
                                                            }
                                                        }
                                                    }

                                                }
                                            }
                                            tableCtrl.render();
                                        },function (tube) {
                                            // var tableCtrl = _getTableCtrl();
                                            // for(var i = 0; i < vm.frozenTubeArray.length; i++){
                                            //     for(var j = 0; j < vm.frozenTubeArray[i].length; j++){
                                            //         if(vm.frozenTubeArray[i][j].sampleCode == oldTube.sampleCode){
                                            //             vm.frozenTubeArray[row][col].sampleCode = "";
                                            //         }
                                            //     }
                                            // }
                                            // tableCtrl.loadData(vm.frozenTubeArray);
                                        });
                                    }
                                }).error(function (data) {
                                    vm.nextFlag = false;
                                    var tableCtrl = _getTableCtrl();
                                    toastr.error(data.message);
                                    for(var i = 0; i < vm.frozenTubeArray.length; i++){
                                        for(var j = 0; j < vm.frozenTubeArray[i].length; j++){
                                            if(vm.frozenTubeArray[i][j].sampleCode == oldTube.sampleCode){
                                                vm.frozenTubeArray[row][col].sampleCode = "";
                                            }
                                        }
                                    }
                                    tableCtrl.loadData(vm.frozenTubeArray);
                                });

                                // When delete a tube.
                                // if (newTube === ""){
                                //     newTube = angular.copy(oldTube);
                                //     newTube.id = null;
                                //     newTube.sampleCode = "";
                                //     newTube.sampleTempCode = "";
                                //     tableCtrl.setDataAtCell(row, col, newTube);
                                //     tableCtrl.render();
                                // }
                            }
                        }

                        return;
                    }
                },
                beforeKeyDown:function (event,change) {
                    if(vm.flagStatus){
                        //9Tab键 left Arrow37  up Arrow38 right Arrow39 Dw Arrow40
                        if(event.keyCode == 9 || event.keyCode == 37 || event.keyCode == 38 || event.keyCode == 39 || event.keyCode == 40){
                            event.stopImmediatePropagation();
                        }
                    }
                    //后退键
                    if(event.keyCode == 8){
                        event.stopImmediatePropagation();
                    }
                    //删除键
                    if(event.keyCode == 46){
                        var len = _.filter(vm.selectedTubesForDel,{flag:"2"}).length;
                        if(!len){
                            var tableCtrl = _getTableCtrl();
                            var tubeObject = {
                                id : "",
                                sampleCode : "",
                                sampleTempCode : "",
                                status : "3001",
                                memo : ""
                            };
                            if(vm.obox.sampleTypeId){
                                tubeObject.sampleTypeId = vm.obox.sampleTypeId;
                                tubeObject.sampleTypeCode = vm.obox.sampleTypeCode;
                                tubeObject.sampleTypeName = vm.obox.sampleTypeName;
                                tubeObject.backColor = vm.obox.backColor;
                            }
                            if(vm.obox.sampleClassificationId){
                                tubeObject.sampleClassificationId = vm.obox.sampleClassificationId;
                                tubeObject.sampleClassificationCode = vm.obox.sampleClassificationCode;
                                tubeObject.sampleClassificationName = vm.obox.sampleClassificationName;
                                tubeObject.backColorForClass = vm.obox.backColorForClass;
                            }
                            if(vm.obox.sampleTypeCode == '97' || vm.obox.sampleTypeCode == '98'){
                                tubeObject.sampleClassificationId = "";
                                tubeObject.sampleClassificationCode = "";
                                tubeObject.sampleClassificationName = "";
                                tubeObject.backColorForClass = "";
                            }
                            delFlag = true;
                            var start1,end1,start2,end2;
                            if(vm.selectedPos[0] > vm.selectedPos[2]){
                                start1 = vm.selectedPos[2];
                                end1 = vm.selectedPos[0];
                            }else{
                                start1 = vm.selectedPos[0];
                                end1 = vm.selectedPos[2];
                            }
                            if(vm.selectedPos[1] > vm.selectedPos[3]){
                                start2 = vm.selectedPos[3];
                                end2 = vm.selectedPos[1];
                            }else{
                                start2 = vm.selectedPos[1];
                                end2 = vm.selectedPos[3];
                            }
                            for(var i = start1;i <= end1; i++){
                                for(var j = start2;  j <= end2;j++){
                                    tableCtrl.setDataAtCell(i, j, tubeObject);
                                }
                            }
                        }


                        event.stopImmediatePropagation();
                    }else{
                        delFlag = false;
                    }


                },
                beforeChange: function(changes, source) {
                    vm.selectedPos = changes;
                    var tableCtrl = _getTableCtrl();
                    for (var i = 0; i < changes.length; ++i){
                        var item = changes[i];
                        var row = item[0];
                        var col = item[1];
                        var oldTube = item[2] || {};
                        var newTube = item[3];

                        if(!delFlag){
                            if(oldTube.sampleCode){
                                for(var i = 0; i < vm.frozenTubeArray.length; i++){
                                    for(var j = 0; j < vm.frozenTubeArray[i].length; j++){
                                        if(i == row  && j == col){
                                            continue;
                                        }
                                        if(vm.frozenTubeArray[i][j].sampleCode == oldTube.sampleCode){
                                            vm.frozenTubeArray[row][col].sampleCode = "";
                                            tableCtrl.render();
                                            toastr.error("冻存管编码不能重复!");
                                            vm.nextFlag = false;
                                            return false;
                                        }

                                    }
                                }
                            }else{
                                // var tableCtrl = _getTableCtrl();
                                // vm.oldTube.sampleCode = "";
                                // vm.oldTube.tubeRows = getTubeRows(+row)+"";
                                // vm.oldTube.tubeColumns = getTubeColumns(+col);
                                // vm.frozenTubeArray[row][col] = vm.oldTube;
                                // tableCtrl.render();
                                return false;
                            }
                        }




                    }

                },
                cells: _changeCellProperties,
                beforeOnCellMouseDown: function (event, coords, element) {
                    var self = this;
                    if(coords.row == "-1" && coords.col == "-1" && $(element).is("th")){
                        var row2 = this.countRows()-1;
                        var col2 = this.countCols()-1;
                        setTimeout(function(){
                            self.selectCell(0,0,row2,col2,true,true);
                        },200);
                    }
                }
            };

            //渲染管子表格
            function myCustomRenderer(hotInstance, td, row, col, prop, value, cellProperties) {
                td.style.position = 'relative';
                var tube= value||{};
                if (cellProperties.readOnly){
                    $(td).addClass('htDimmed');
                    $(td).addClass('htReadOnly');

                } else {
                    $(td).removeClass('htDimmed');
                    $(td).removeClass('htReadOnly');
                }
                if(tube.memo && tube.memo != " "){
                    cellProperties.comment = {value:tube.memo};
                }
                //样本类型
                if(tube.backColorForClass || tube.sampleClassificationId){
                    td.style.backgroundColor = tube.backColorForClass;
                }else{
                    td.style.backgroundColor = tube.backColor;
                }
                //样本状态 status3001：正常，3002：空管，3003：空孔；3004：异常
                if(tube.status){
                    changeSampleStatus(tube.status,td);
                }

                var code = tube.sampleCode && tube.sampleCode != " " ? tube.sampleCode : tube.sampleTempCode;
                $(td).html("");
                var $div = $("<div/>").html(code).css({
                    'line-height': '20px',
                    'word-wrap': 'break-word'
                }).appendTo(td);
                $div = $("<div  class='tube-status'/>").html(tube.status).appendTo(td);
                if(vm.repeatSampleArray.length){
                    var len = _.filter(vm.repeatSampleArray,{sampleCode:tube.sampleCode}).length;
                    if(len){
                        $div = $("<div class='repeat-sample-class stockInAnimation'/>").appendTo(td);
                    }
                }



            }
            //修改样本状态正常、空管、空孔、异常
            function changeSampleStatus(sampleStatus,td) {
                //正常
                if(sampleStatus == 3001){
                    $(td).removeClass("error-tube-color");
                }
                //空管
                if(sampleStatus == 3002){
                    $(td).addClass("empty-tube-color");
                }
                //空孔
                if(sampleStatus == 3003){
                    $(td).removeClass("empty-tube-color");
                    $(td).addClass("empty-hole-color");
                }
                //异常
                if(sampleStatus == 3004){
                    $(td).removeClass("empty-hole-color");
                    $(td).addClass("error-tube-color");
                }

                // operateColor = td.style.backgroundColor;
                // //正常
                // if(sampleStatus == 3001){
                // }
                // //空管
                // if(sampleStatus == 3002){
                //     td.style.background = 'linear-gradient(to right,'+operateColor+',50%,black';
                // }
                // //空孔
                // if(sampleStatus == 3003){
                //     td.style.background = '';
                //     td.style.backgroundColor = '#ffffff';
                //     td.style.color = '#ffffff';
                // }
                // //异常
                // if(sampleStatus == 3004){
                //     td.style.backgroundColor = 'red';
                //     td.style.border = '3px solid red;margin:-3px';
                // }
            }
            // 修改单元格属性
            function _changeCellProperties(row, col, prop) {
                var tableCtrl = _getTableCtrl();
                if (!tableCtrl){
                    return;
                }
                var cellProperties = {};
                var cellData = tableCtrl.getDataAtCell(row, col);
                if (cellData && cellData.flag == 2) {
                    // 单元格有数据，并且有冻存盒ID，表示该单元格在库里有位置
                    // 该单元格不能编辑
                    cellProperties.editor = false;
                    // 该单元格只读
                    cellProperties.readOnly = true;
                    // 该单元格的样式
                    cellProperties.className = 'a00';
                    // cellProperties.readOnlyCellClassName = 'htDimmed';
                }

                return cellProperties;
            }

            function initFrozenTube(row,col) {
                for(var i = 0; i < row; i++){
                    vm.frozenTubeArray[i] = [];
                    for(var j = 0;j < col; j++){
                        vm.frozenTubeArray[i][j] = "";
                    }
                }
            }
            function getTubeRows(row) {
                return String.fromCharCode(row + 65);
            }
            function getTubeColumns(col) {
                return col + 1;
            }
            function getTubeRowIndex(row) {
                return row.charCodeAt(0) -65;
            }
            function getTubeColumnIndex(col) {
                return +col -1;
            }

            //备注 选择单元格数据
            function _fnRemarkSelectData(td,remarkArray,selectTubeArrayIndex) {
                var txt = '<div class="temp" style="position:absolute;top:0;bottom:0;left:0;right:0;border:1px dashed #5292F7;background-color: rgba(82,146,247,0.2)"></div>';
                for(var m = 0; m < remarkArray.length; m++){
                    for (var n = 0; n < remarkArray[m].length; n++){
                        if ((remarkArray[m][n].sampleCode && remarkArray[m][n].sampleCode.length > 1)
                            || (remarkArray[m][n].sampleTempCode && remarkArray[m][n].sampleTempCode.length > 1)){
                            aRemarkArray.push(remarkArray[m][n]);
                        }
                    }
                }
                var start1,end1,start2,end2;
                if(selectTubeArrayIndex[0] > selectTubeArrayIndex[2]){
                    start1 = selectTubeArrayIndex[2];
                    end1 = selectTubeArrayIndex[0];
                }else{
                    start1 = selectTubeArrayIndex[0];
                    end1 = selectTubeArrayIndex[2];
                }
                if(selectTubeArrayIndex[1] > selectTubeArrayIndex[3]){
                    start2 = selectTubeArrayIndex[3];
                    end2 = selectTubeArrayIndex[1];
                }else{
                    start2 = selectTubeArrayIndex[1];
                    end2 = selectTubeArrayIndex[3];
                }
                for(var i = start1;i <= end1; i++){
                    for(var j = start2;  j <= end2;j++)
                        if($(td.getCell(i,j))[0].childElementCount !=3){
                            $(td.getCell(i,j)).append(txt);
                        }

                }
            }
            //修改样本状态
            vm.flagStatus = false;
            vm.editStatus = function () {
                var settings = {
                    editor: vm.flagStatus ? false : 'tubeInput'
                };
                hotRegisterer.getInstance('my-handsontable').updateSettings(settings);
            };
            //换位
            vm.exchangeFlag = false;
            vm.exchange = function () {
                if(vm.exchangeFlag && domArray.length == 2){
                    var v1 = (!domArray[0].sampleCode && !domArray[1].sampleCode);
                    var v2 = (!domArray[0].sampleTempCode && !domArray[1].sampleTempCode);
                    if((!domArray[0].sampleCode && !domArray[1].sampleCode)
                        && (!domArray[0].sampleTempCode && !domArray[1].sampleTempCode)){
                        toastr.error("两个空冻存盒不能被交换!");
                        return;
                    }
                    var row = getTubeRowIndex(domArray[0].tubeRows);
                    var col = getTubeColumnIndex(domArray[0].tubeColumns);
                    var row1 = getTubeRowIndex(domArray[1].tubeRows);
                    var col1 = getTubeColumnIndex(domArray[1].tubeColumns);
                    if(row > 8 ){
                        vm.frozenTubeArray[row-1][col] = domArray[1];
                        vm.frozenTubeArray[row-1][col].tubeRows = getTubeRows(row);
                        vm.frozenTubeArray[row-1][col].tubeColumns = getTubeColumns(col);

                    }else{
                        vm.frozenTubeArray[row][col] = domArray[1];
                        vm.frozenTubeArray[row][col].tubeRows = getTubeRows(row);
                        vm.frozenTubeArray[row][col].tubeColumns = getTubeColumns(col);
                    }
                    if(row1 > 8){
                        vm.frozenTubeArray[row1-1][col1] = domArray[0];
                        vm.frozenTubeArray[row1-1][col1].tubeRows = getTubeRows(row1);
                        vm.frozenTubeArray[row1-1][col1].tubeColumns = getTubeColumns(col1);
                    }else{
                        vm.frozenTubeArray[row1][col1] = domArray[0];
                        vm.frozenTubeArray[row1][col1].tubeRows = getTubeRows(row1);
                        vm.frozenTubeArray[row1][col1].tubeColumns = getTubeColumns(col1);
                    }


                    domArray = [];
                    vm.exchangeFlag = false;

                }else{
                    toastr.error("只能选择两个进行交换！",{},'center');
                    domArray = [];
                }
                hotRegisterer.getInstance('my-handsontable').render();
            };
            //批注
            vm.remarkFlag = false;
            vm.tubeRemark = function () {
                if(aRemarkArray.length > 0){
                    modalInstance = $uibModal.open({
                        animation: true,
                        templateUrl: 'app/bizs/transport-record/modal/tubes-remark-modal.html',
                        controller: 'TubesRemarkModalController',
                        backdrop:'static',
                        controllerAs: 'vm',
                        resolve: {
                            items: function () {
                                return {
                                    remarkArray :aRemarkArray
                                };
                            }
                        }

                    });
                    modalInstance.result.then(function (memo) {
                        var tableCtrl = _getTableCtrl();
                        for(var i = 0; i < aRemarkArray.length; i++){
                            if(aRemarkArray[i].sampleCode || aRemarkArray[i].sampleTempCode){
                                aRemarkArray[i].memo = memo;
                            }
                        }
                        aRemarkArray = [];
                        tableCtrl.render();
                    });
                }else{
                    toastr.error("请选择样本!");
                }
            };
        }
        // 创建一个对象用于管子Table的控件
        function _createTubeForTableCell(tubeInBox, box, rowNO, colNO, pos){
            var tube = {
                id: null,
                tubeRows: pos.tubeRows,
                tubeColumns: pos.tubeColumns,
                status: "3001",
                memo: null,
                // frozenBoxId: box.frozenBoxTypeId,
                frozenBoxCode: box.frozenBoxCode,
                sampleTempCode: null,
                sampleCode: null,
                sampleTypeCode: null,
                sampleTypeName: null,
                sampleVolumns :null, //计量
                sampleTypeId: null,
                sampleClassificationId:null,
                sampleClassificationCode:null,
                sampleClassificationName:null,
                projectId:vm.entity.projectId,
                projectSiteId:vm.entity.projectSiteId,
                frozenTubeId:vm.obox.frozenTubeId,
                backColorForClass:null,
                backColor:null,
                flag:"",
                rowNO: rowNO,
                colNO: colNO
            };

            if (tubeInBox){
                tube.id = tubeInBox.id;
                tube.frozenTubeId = tubeInBox.frozenTubeId;
                tube.sampleCode = tubeInBox.sampleCode;
                tube.sampleTempCode = tubeInBox.sampleTempCode;
                tube.status = tubeInBox.status;
                tube.memo = tubeInBox.memo;
                tube.sampleVolumns = tubeInBox.sampleVolumns;
                tube.flag = tubeInBox.flag;
                if(tubeInBox.sampleClassificationId){
                    tube.sampleClassificationId = tubeInBox.sampleClassificationId;
                    tube.sampleClassificationCode = tubeInBox.sampleClassificationCode;
                    tube.sampleClassificationName = tubeInBox.sampleClassificationName;
                    tube.backColorForClass = tubeInBox.backColorForClass;
                }
                tube.sampleTypeId = tubeInBox.sampleTypeId;
                tube.sampleTypeCode = tubeInBox.sampleTypeCode;
                tube.sampleTypeName = tubeInBox.sampleTypeName;
                tube.backColor = tubeInBox.backColor;
            }else{
                tube.sampleTypeId = box.sampleTypeId;
                tube.sampleTypeCode = box.sampleTypeCode;
                tube.sampleTypeName = box.sampleTypeName;
                tube.backColor = box.backColor;
                tube.sampleClassificationId = box.sampleClassificationId;
                tube.sampleClassificationCode = box.sampleClassificationCode;
                tube.sampleClassificationName = box.sampleClassificationName;
                tube.backColorForClass = box.backColorForClass;

            }

            return tube;
        }
        // 重新加载管子表控件
        vm.reloadTubesForTable = _reloadTubesForTable;
        var strBox;
        function _reloadTubesForTable(box){

            var tableCtrl = _getTableCtrl();
            var tableWidth = $(tableCtrl.container).width();
            var settings = {
                minCols: +box.frozenBoxTypeColumns,
                minRows: +box.frozenBoxTypeRows
            };

            var rowHeaderWidth = 30;
            // 架子定位列表每列的宽度
            var colWidth = (tableWidth - rowHeaderWidth) / settings.minCols;

            // colHeaders : ['1','2','3','4','5','6','7','8','9','10'],
            // rowHeaders : ['A','B','C','D','E','F','G','H','I','J'],

            var tubesInTable = [];
            var colHeaders = [];
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
                    if (colHeaders.length < settings.minCols){
                        colHeaders.push(pos.tubeColumns);
                    }
                    var tubeInBox = _.filter(box.frozenTubeDTOS, pos)[0];
                    var tube = _createTubeForTableCell(tubeInBox, box, i, j + 1, pos);
                    //混合类型
                    if(box.isMixed == "1"){
                        for (var l = 0; l < vm.projectSampleTypeOptions.length; l++) {
                            if (vm.projectSampleTypeOptions[l].columnsNumber == pos.tubeColumns) {
                                if(!tube.sampleClassificationId){
                                    tube.sampleClassificationId = vm.projectSampleTypeOptions[l].sampleClassificationId;
                                    tube.sampleClassificationName = vm.projectSampleTypeOptions[l].sampleClassificationName;
                                    tube.sampleClassificationCode = vm.projectSampleTypeOptions[l].sampleClassificationCode;
                                    tube.backColorForClass = vm.projectSampleTypeOptions[l].backColorForClass;
                                }

                            }
                        }
                    }
                    tubes.push(tube);
                }
                tubesInTable.push(tubes);
            }

            vm.frozenTubeArray = tubesInTable;

            settings.rowHeaders = rowHeaders;
            settings.colHeaders = colHeaders;
            settings.colWidths = colWidth;
            settings.manualColumnResize = colWidth;
            tableCtrl.updateSettings(settings);
            tableCtrl.loadData(tubesInTable);
            _initSampleType();
            vm.obox.frozenTubeDTOS =  _.flattenDeep(angular.copy(vm.frozenTubeArray));
            strBox = JSON.stringify(vm.obox);
        }

        //批量编辑录入样本
        function _fnEditEntering() {
            if(!aRemarkArray.length){
                toastr.error("请选择样本进行修改!");
                return;
            }
            //flag:2 原盒
            var len =  _.filter(aRemarkArray,{flag:"2"}).length;
            if(len){
                toastr.error("选择的样本中不能包含原盒内样本!");
                return;
            }
            var singleMultipleFlag = "multiple";
            var sampleCode;
            if(aRemarkArray.length == 1){
                singleMultipleFlag = "single";
                sampleCode = aRemarkArray[0].sampleCode;
            }
            var selectedData = _.uniq(aRemarkArray);
            modalInstance = $uibModal.open({
                animation: true,
                templateUrl: 'app/bizs/stock-in/modal/stock-in-add-sample-modal.html',
                controller: 'StockInAddSampleModal',
                backdrop:'static',
                controllerAs: 'vm',
                size:'lg',
                resolve: {
                    items: function () {
                        return {
                            singleMultipleFlag:singleMultipleFlag,
                            sampleSelectedArray:selectedData,
                            sampleCode:sampleCode,
                            frozenBoxId:vm.obox.frozenBoxId,
                            projectSiteId:vm.entity.projectSiteId,
                            projectId:vm.entity.projectId,
                            projectCode:vm.entity.projectCode,
                            sampleTypeId:vm.obox.sampleTypeId,
                            sampleTypeName:vm.obox.sampleTypeName,
                            sampleTypeCode:vm.obox.sampleTypeCode,
                            sampleClassificationId:vm.obox.sampleClassificationId,
                            sampleClassificationName:vm.obox.sampleClassificationName,
                            sampleClassificationCode:vm.obox.sampleClassificationCode
                        };
                    }
                }

            });
            modalInstance.result.then(function (tubes) {
                var tableCtrl = _getTableCtrl();
                for(var i = 0; i < vm.frozenTubeArray.length; i++){
                    for(var j = 0; j < vm.frozenTubeArray[i].length; j++){
                        if(singleMultipleFlag == "single"){
                            if(vm.frozenTubeArray[i][j].sampleCode == tubes[0].oldSampleCode){
                                vm.frozenTubeArray[i][j].sampleCode = tubes[0].sampleCode;
                                vm.frozenTubeArray[i][j].frozenTubeId = tubes[0].frozenTubeId;
                                vm.frozenTubeArray[i][j].memo = tubes[0].memo;
                                vm.frozenTubeArray[i][j].sampleTypeId = tubes[0].sampleTypeId;
                                vm.frozenTubeArray[i][j].sampleTypeName = tubes[0].sampleTypeName;
                                vm.frozenTubeArray[i][j].backColor = tubes[0].backColor;
                                vm.frozenTubeArray[i][j].sampleVolumns = tubes[0].sampleVolumns;
                                if(tubes[0].sampleClassificationId){
                                    vm.frozenTubeArray[i][j].sampleClassificationId = tubes[0].sampleClassificationId;
                                    vm.frozenTubeArray[i][j].sampleClassificationName = tubes[0].sampleClassificationName;
                                    vm.frozenTubeArray[i][j].sampleClassitionCode = tubes[0].sampleClassitionCode;
                                    vm.frozenTubeArray[i][j].backColorForClass = tubes[0].backColorForClass;
                                }
                            }
                        }else{
                            for(var k = 0; k < tubes.length; k++){
                                if(vm.frozenTubeArray[i][j].sampleCode == tubes[k].sampleCode){
                                    vm.frozenTubeArray[i][j].frozenTubeId = tubes[k].frozenTubeId;
                                    vm.frozenTubeArray[i][j].memo = tubes[k].memo;
                                    vm.frozenTubeArray[i][j].sampleTypeId = tubes[k].sampleTypeId;
                                    vm.frozenTubeArray[i][j].sampleTypeName = tubes[k].sampleTypeName;
                                    vm.frozenTubeArray[i][j].backColor = tubes[k].backColor;
                                    vm.frozenTubeArray[i][j].sampleVolumns = tubes[k].sampleVolumns;
                                    if(tubes[k].sampleClassificationId){
                                        vm.frozenTubeArray[i][j].sampleClassificationId = tubes[k].sampleClassificationId;
                                        vm.frozenTubeArray[i][j].sampleClassificationName = tubes[k].sampleClassificationName;
                                        vm.frozenTubeArray[i][j].sampleClassitionCode = tubes[k].sampleClassitionCode;
                                        vm.frozenTubeArray[i][j].backColorForClass = tubes[k].backColorForClass;
                                    }
                                }
                            }

                        }
                    }
                }
                tableCtrl.render();
                aRemarkArray = [];
                $(".temp").remove();
            },function (tube) {
                aRemarkArray = [];
                $(".temp").remove();
            });
        }

        //冻存盒搜索
        function _fnFrozenBoxForStockIn() {
            if(vm.obox.frozenBoxCode){
                StockInInputService.queryStockInBox(vm.obox.frozenBoxCode).success(function (data) {
                    if(data.frozenBoxCode){
                        vm.obox = data;
                        if(vm.obox.id){
                            vm.editFlag = true;
                        }else{
                            vm.editFlag = false;
                        }
                        vm.reloadTubesForTable(vm.obox);
                        // _initSampleType();

                    }
                }).error(function (res) {
                    toastr.error(res.message);
                    vm.obox.frozenBoxCode = "";
                });
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
                var boxType = _.filter(vm.frozenBoxTypeOptions, {id:+value})[0];
                if (!boxType) {
                    return;
                }
                vm.obox.frozenBoxTypeId = value;
                vm.obox.frozenBoxTypeRows = boxType.frozenBoxTypeRows;
                vm.obox.frozenBoxTypeColumns = boxType.frozenBoxTypeColumns;
                var box = vm.obox;
                // _fnQueryProjectSampleClass(vm.entity.projectId,vm.box.sampleTypeId,vm.isMixed);
                vm.reloadTubesForTable(box);
                // hotRegisterer.getInstance('my-handsontable').render();
            }
        };
        vm.sampleTypeConfig = {
            valueField:'id',
            labelField:'sampleTypeName',
            maxItems: 1,
            onChange:function (value) {
                vm.obox.sampleClassificationId = "";
                if(value){
                    vm.obox.sampleTypeId = value;
                    vm.obox.sampleTypeName = _.find(vm.sampleTypeOptions,{'id':+value}).sampleTypeName;
                    vm.obox.sampleTypeCode = _.find(vm.sampleTypeOptions,{'id':+value}).sampleTypeCode;
                    vm.isMixed = _.find(vm.sampleTypeOptions,{'id':+value}).isMixed;
                    //是否混合类型 1：是混合类型 不能添加99类型的的盒子只能添加97类型98类型
                    if(vm.isMixed == 1){
                        vm.projectSampleTypeOptions = [];
                        vm.projectSampleTypeOptions.push({sampleClassificationId:"",sampleClassificationName:""});
                        vm.obox.sampleClassificationId = "";
                        vm.obox.sampleClassificationName = "";
                        vm.obox.sampleClassificationCode = "";
                        vm.obox.backColorForClass = "";
                        $scope.$apply();
                        //混合型无分类
                        for (var i = 0; i < vm.frozenTubeArray.length; i++) {
                            for (var j = 0; j < vm.frozenTubeArray[i].length; j++) {
                                if(!vm.frozenTubeArray[i][j].sampleCode){
                                    vm.frozenTubeArray[i][j].sampleClassificationId = "";
                                    vm.frozenTubeArray[i][j].sampleClassificationName = "";
                                    vm.frozenTubeArray[i][j].sampleClassificationCode = "";
                                    vm.frozenTubeArray[i][j].backColorForClass = "";
                                    vm.frozenTubeArray[i][j].sampleTypeId = vm.obox.sampleTypeId;
                                    vm.frozenTubeArray[i][j].sampleTypeName = _.find(vm.sampleTypeOptions,{'id':+vm.obox.sampleTypeId}).sampleTypeName;
                                    vm.frozenTubeArray[i][j].sampleTypeCode = _.find(vm.sampleTypeOptions,{'id':+vm.obox.sampleTypeId}).sampleTypeCode;
                                    vm.frozenTubeArray[i][j].backColor = _.find(vm.sampleTypeOptions,{'id':+vm.obox.sampleTypeId}).backColor;
                                }
                            }
                        }
                        hotRegisterer.getInstance('my-handsontable').render();
                    }else{
                        _fnQueryProjectSampleClass(vm.entity.projectId,vm.obox.sampleTypeId);
                    }
                }

                // Added by Zhuyu 2017/10/09 For: 选中RNA时自动切换冻存盒为大橘盒，选中99时切换为10x10
                var sampleTypeCode = _.find(vm.sampleTypeOptions,{'id':+value}).sampleTypeCode;
                var boxType = _.filter(vm.frozenBoxTypeOptions, {frozenBoxTypeCode: SampleTypeService.getBoxTypeCode(sampleTypeCode)})[0];
                if (boxType) {
                    setTimeout(function(){
                        vm.boxTypeInstance.setValue(boxType.id);
                    }, 100);
                }
                // end added
            }
        };
        vm.projectSampleTypeConfig = {
            valueField:'sampleClassificationId',
            labelField:'sampleClassificationName',
            maxItems: 1,
            onChange:function (value) {
                vm.obox.sampleClassificationId = value;
                vm.obox.sampleClassificationName = _.find(vm.projectSampleTypeOptions,{sampleClassificationId:+value}).sampleClassificationName;
                vm.obox.sampleClassificationCode = _.find(vm.projectSampleTypeOptions,{sampleClassificationId:+value}).sampleClassificationCode;
                vm.obox.backColorForClass = _.find(vm.projectSampleTypeOptions,{sampleClassificationId:+value}).backColor;
                for (var i = 0; i < vm.frozenTubeArray.length; i++) {
                    for (var j = 0; j < vm.frozenTubeArray[i].length; j++) {
                        vm.frozenTubeArray[i][j].sampleClassificationId = vm.obox.sampleClassificationId;
                        vm.frozenTubeArray[i][j].sampleClassificationCode = vm.obox.sampleClassificationCode;
                        vm.frozenTubeArray[i][j].backColorForClass = vm.obox.backColorForClass;
                    }
                }
                hotRegisterer.getInstance('my-handsontable').render();
            }
        };
        function _initBoxInfo() {
            //盒子类型
            FrozenBoxTypesService.query({},onFrozenBoxTypeSuccess, onError);
            function onFrozenBoxTypeSuccess(data) {
                vm.frozenBoxTypeOptions = _.orderBy(data, ['id'], ['asc']);
                if(vm.frozenBoxTypeOptions.length){
                    if(!vm.obox.frozenBoxTypeId){
                        vm.obox.frozenBoxTypeId = vm.frozenBoxTypeOptions[0].id;
                        vm.obox.frozenBoxTypeName = vm.frozenBoxTypeOptions[0].frozenBoxTypeName;
                        vm.obox.frozenBoxTypeRows = vm.frozenBoxTypeOptions[0].frozenBoxTypeRows;
                        vm.obox.frozenBoxTypeColumns = vm.frozenBoxTypeOptions[0].frozenBoxTypeColumns;
                    }

                }
                setTimeout(function () {
                    vm.reloadTubesForTable(vm.obox);
                },500);
            }
        }

        function _initSampleType() {

            //样本类型
            SampleTypeService.querySampleType().success(function (data) {
                vm.sampleTypeOptions = _.orderBy(data,['sampleTypeCode','asc']);
                if(!vm.editFlag){
                    _.remove(vm.sampleTypeOptions,{sampleTypeCode:"99"});
                }
                // _.remove(vm.sampleTypeOptions,{sampleTypeCode:"99"});
                if(!vm.obox.sampleTypeId){
                    vm.obox.sampleTypeId = vm.sampleTypeOptions[0].id;
                    vm.obox.sampleTypeName = vm.sampleTypeOptions[0].sampleTypeName;
                    vm.obox.sampleTypeCode = vm.sampleTypeOptions[0].sampleTypeCode;
                    vm.obox.backColor = vm.sampleTypeOptions[0].backColor;
                }
                vm.isMixed = _.find(vm.sampleTypeOptions,{'sampleTypeCode':vm.obox.sampleTypeCode}).isMixed;

                //是否混合类型 1：是混合类型 不能添加99类型的的盒子只能添加97类型98类型 2：非混合类型
                if(vm.isMixed == 1){
                    vm.projectSampleTypeOptions = [];
                    vm.projectSampleTypeOptions.push({sampleClassificationId:"",sampleClassificationName:""});
                    vm.obox.sampleClassificationId = "";
                    vm.obox.sampleClassificationName = "";
                    vm.obox.sampleClassificationCode = "";
                    vm.obox.backColorForClass = "";
                    // $scope.$apply();
                    //混合型无分类
                    if(vm.obox.sampleTypeCode != '99'){
                        for (var i = 0; i < vm.frozenTubeArray.length; i++) {
                            for (var j = 0; j < vm.frozenTubeArray[i].length; j++) {
                                if(!vm.frozenTubeArray[i][j].sampleCode){
                                    vm.frozenTubeArray[i][j].sampleClassificationId = "";
                                    vm.frozenTubeArray[i][j].sampleClassificationName = "";
                                    vm.frozenTubeArray[i][j].sampleClassificationCode = "";
                                    vm.frozenTubeArray[i][j].backColorForClass = "";
                                    if(!vm.frozenTubeArray[i][j].sampleTypeId){
                                        vm.frozenTubeArray[i][j].sampleTypeId = vm.obox.sampleTypeId;
                                        vm.frozenTubeArray[i][j].sampleTypeName = _.find(vm.sampleTypeOptions,{'id':+vm.obox.sampleTypeId}).sampleTypeName;
                                        vm.frozenTubeArray[i][j].sampleTypeCode = _.find(vm.sampleTypeOptions,{'id':+vm.obox.sampleTypeId}).sampleTypeCode;
                                        vm.frozenTubeArray[i][j].backColor = _.find(vm.sampleTypeOptions,{'id':+vm.obox.sampleTypeId}).backColor;
                                    }

                                }
                            }
                        }
                    }else{
                        _fnQueryProjectSampleClass(vm.entity.projectId,vm.obox.sampleTypeId);
                    }

                    hotRegisterer.getInstance('my-handsontable').render();
                }else{
                    _fnQueryProjectSampleClass(vm.entity.projectId,vm.obox.sampleTypeId);
                }
            });

            function _loadTube() {
                //是否混合类型 1：是混合类型 不能添加99类型的的盒子只能添加97类型98类型
                if(vm.isMixed == 1){
                    vm.projectSampleTypeOptions = [];
                    vm.projectSampleTypeOptions.push({sampleClassificationId:"",sampleClassificationName:""});
                    vm.box.sampleClassificationId = "";
                    vm.box.sampleClassificationName = "";
                    vm.box.sampleClassificationCode = "";
                    vm.box.backColorForClass = "";
                    $scope.$apply();
                    //混合型无分类
                    for (var i = 0; i < vm.frozenTubeArray.length; i++) {
                        for (var j = 0; j < vm.frozenTubeArray[i].length; j++) {
                            if(!vm.frozenTubeArray[i][j].sampleCode){
                                vm.frozenTubeArray[i][j].sampleTypeId = vm.box.sampleTypeId;
                                vm.frozenTubeArray[i][j].sampleTypeName = _.find(vm.sampleTypeOptions,{'id':+vm.box.sampleTypeId}).sampleTypeName;
                                vm.frozenTubeArray[i][j].sampleTypeCode = _.find(vm.sampleTypeOptions,{'id':+vm.box.sampleTypeId}).sampleTypeCode;
                                vm.frozenTubeArray[i][j].backColor = _.find(vm.sampleTypeOptions,{'id':+vm.box.sampleTypeId}).backColor;
                            }
                        }
                    }
                    hotRegisterer.getInstance('my-handsontable').render();
                }else{
                    _fnQueryProjectSampleClass(vm.entity.projectId,vm.box.sampleTypeId);
                }
            }


        }
        //样本分类
        function _fnQueryProjectSampleClass(projectId,sampleTypeId) {
            SampleTypeService.queryProjectSampleClasses(projectId,sampleTypeId).success(function (data) {
                vm.projectSampleTypeOptions = data;
                if(!vm.projectSampleTypeOptions.length || vm.obox.sampleTypeCode == '99'){
                    vm.projectSampleTypeOptions.push({sampleClassificationId:"",sampleClassificationName:""});
                    vm.obox.sampleClassificationId = "";
                    vm.obox.sampleClassificationName = "";
                    vm.obox.sampleClassificationCode = "";
                    vm.obox.backColorForClass = "";
                }else{
                    if(!vm.obox.sampleClassificationId){
                        vm.obox.sampleClassificationId = vm.projectSampleTypeOptions[0].sampleClassificationId;
                        vm.obox.sampleClassificationCode = vm.projectSampleTypeOptions[0].sampleClassificationCode;
                        vm.obox.sampleClassificationName = vm.projectSampleTypeOptions[0].sampleClassificationName;
                        vm.obox.backColorForClass = vm.projectSampleTypeOptions[0].backColor;
                    }

                }

                for (var i = 0; i < vm.frozenTubeArray.length; i++) {
                    for (var j = 0; j < vm.frozenTubeArray[i].length; j++) {
                        if(vm.obox.sampleTypeCode != '99'){
                            if(vm.obox.sampleClassificationId){
                                if(!vm.frozenTubeArray[i][j].sampleCode){
                                    vm.frozenTubeArray[i][j].sampleClassificationId = "";
                                    vm.frozenTubeArray[i][j].sampleClassificationName = "";
                                    vm.frozenTubeArray[i][j].sampleClassificationCode = "";
                                    vm.frozenTubeArray[i][j].backColorForClass = "";
                                    if(!vm.frozenTubeArray[i][j].sampleClassificationId){
                                        vm.frozenTubeArray[i][j].sampleClassificationId = vm.obox.sampleClassificationId;
                                        vm.frozenTubeArray[i][j].sampleClassificationName = vm.obox.sampleClassificationName;
                                        vm.frozenTubeArray[i][j].sampleClassificationCode = vm.obox.sampleClassificationCode;
                                        vm.frozenTubeArray[i][j].backColorForClass = vm.obox.backColorForClass;
                                    }

                                }
                            }else{
                                vm.frozenTubeArray[i][j].sampleClassificationId = "";
                                vm.frozenTubeArray[i][j].sampleClassificationName = "";
                                vm.frozenTubeArray[i][j].sampleClassificationCode = "";
                                vm.frozenTubeArray[i][j].backColorForClass = "";
                            }
                        }
                        vm.frozenTubeArray[i][j].sampleTypeId = "";
                        vm.frozenTubeArray[i][j].sampleTypeName = "";
                        vm.frozenTubeArray[i][j].sampleTypeCode = "";
                        vm.frozenTubeArray[i][j].backColor = "";
                        if(!vm.frozenTubeArray[i][j].sampleTypeId){
                            vm.frozenTubeArray[i][j].sampleTypeId = sampleTypeId;
                            vm.frozenTubeArray[i][j].sampleTypeName = _.find(vm.sampleTypeOptions,{'id':+sampleTypeId}).sampleTypeName;
                            vm.frozenTubeArray[i][j].sampleTypeCode = _.find(vm.sampleTypeOptions,{'id':+sampleTypeId}).sampleTypeCode;
                            vm.frozenTubeArray[i][j].backColor = _.find(vm.sampleTypeOptions,{'id':+sampleTypeId}).backColor;
                        }

                    }
                }

                for(var k = 0; k < vm.projectSampleTypeOptions.length; k++){
                    for (var m = 0; m < vm.frozenTubeArray.length; m++) {
                        for (var n = 0; n < vm.frozenTubeArray[m].length; n++) {
                            if(vm.projectSampleTypeOptions[k].columnsNumber == n+1){
                                vm.frozenTubeArray[m][n].sampleClassificationId = vm.projectSampleTypeOptions[k].sampleClassificationId;
                                vm.frozenTubeArray[m][n].backColorForClass = vm.projectSampleTypeOptions[k].backColor;
                                vm.frozenTubeArray[m][n].sampleTypeId = sampleTypeId;
                            }
                        }
                    }
                }

                hotRegisterer.getInstance('my-handsontable').render();
            });
        }
        //格式化数据
        function _fnChangeData() {
            if(!vm.obox.sampleClassificationCode){
                vm.obox.sampleClassificationCode = null;
            }
            if(!vm.obox.sampleClassificationId){
                vm.obox.sampleClassificationId = null;
            }
            if(!vm.obox.sampleClassificationName){
                vm.obox.sampleClassificationName = null;
            }
            if(!vm.obox.backColorForClass){
                vm.obox.backColorForClass = null;
            }
            vm.obox.frozenTubeDTOS = [];
            var tubeList = _.flatten(vm.frozenTubeArray);
            for(var i = 0; i< tubeList.length; i++){
                delete tubeList[i].rowNO;
                delete tubeList[i].colNO;
                if(tubeList[i].sampleCode|| tubeList[i].sampleTempCode){
                    if(!tubeList[i].sampleClassificationCode){
                        tubeList[i].sampleClassificationCode = null;
                    }
                    if(!tubeList[i].sampleClassificationId){
                        tubeList[i].sampleClassificationId = null;
                    }
                    if(!tubeList[i].sampleClassificationName){
                        tubeList[i].sampleClassificationName = null;
                    }
                    if(!tubeList[i].backColorForClass){
                        tubeList[i].backColorForClass = null;
                    }
                    if(!tubeList[i].projectSiteId){
                        tubeList[i].projectSiteId = null;
                    }
                    vm.obox.frozenTubeDTOS.push(tubeList[i]);
                }
            }

        }
        //保存冻存盒
        function _fnSaveBox() {
            _fnChangeData();
            if(vm.obox.sampleTypeCode == '97' ){
                var len = _.filter(vm.obox.frozenTubeDTOS,{"sampleTypeCode":"97"}).length;
                if(len){
                    toastr.error("冻存盒中样本类型不能为97类型，请修改为其他样本类型！");
                    return
                }
            }
            if(vm.obox.sampleTypeCode == '98' ){
                var len = _.filter(vm.obox.frozenTubeDTOS,{"sampleTypeCode":"98"}).length;
                if(len){
                    toastr.error("冻存盒中样本类型不能为98类型，请修改为其他样本类型！");
                    return
                }
            }
            StockInInputService.saveStockInBox(vm.entity.stockInCode,vm.obox).success(function (data) {
                toastr.success("保存冻存盒成功！");
                $scope.reloadData();
                // $scope.showFlag = false;
                vm.editFlag = true;
                // strBox = JSON.stringify(vm.obox);
            }).error(function (data) {
                toastr.error(data.message);
                $scope.reloadData();
                vm.editFlag = false;
                vm.repeatSampleArray = JSON.parse(data.params[0]);
                var tableCtrl = _getTableCtrl();
                tableCtrl.render();

            })

        }
        //关闭
        vm.closeBox = function (status) {
            // if(stockInBox1.frozenTubeDTOS){
            //     for(var i = 0; i < stockInBox1.frozenTubeDTOS.length;i++){
            //         delete stockInBox1.frozenTubeDTOS[i].createdBy;
            //         delete stockInBox1.frozenTubeDTOS[i].createdDate;
            //         delete stockInBox1.frozenTubeDTOS[i].lastModifiedBy;
            //         delete stockInBox1.frozenTubeDTOS[i].lastModifiedDate;
            //         delete stockInBox1.frozenTubeDTOS[i].frozenTubeTypeCode;
            //         delete stockInBox1.frozenTubeDTOS[i].frozenTubeTypeName;
            //         delete stockInBox1.frozenTubeDTOS[i].sampleUsedTimesMost;
            //         delete stockInBox1.frozenTubeDTOS[i].sampleUsedTimes;
            //         delete stockInBox1.frozenTubeDTOS[i].frozenTubeVolumns;
            //         delete stockInBox1.frozenTubeDTOS[i].frozenTubeVolumnsUnit;
            //         delete stockInBox1.frozenTubeDTOS[i].errorType;
            //         delete stockInBox1.frozenTubeDTOS[i].frozenTubeState;
            //         delete stockInBox1.frozenTubeDTOS[i].frozenTubeTypeId;
            //         delete stockInBox1.frozenTubeDTOS[i].frozenTubeType;
            //         delete stockInBox1.frozenTubeDTOS[i].sampleType;
            //         delete stockInBox1.frozenTubeDTOS[i].projectCode;
            //         delete stockInBox1.frozenTubeDTOS[i].frozenTubeCode;
            //         delete stockInBox1.frozenTubeDTOS[i].projectSiteCode;
            //         delete stockInBox1.frozenTubeDTOS[i].stockInBoxId;
            //         delete stockInBox1.frozenTubeDTOS[i].isMixed;
            //         delete stockInBox1.frozenTubeDTOS[i].frontColorForClass;
            //         delete stockInBox1.frozenTubeDTOS[i].frontColor;
            //         delete stockInBox1.frozenTubeDTOS[i].sampleClassification;
            //     }
            // }
            //
            // var strBox = JSON.stringify(stockInBox1);
            // _fnChangeData();
            // angular.extend(stockInBox2,vm.obox);
            vm.obox.frozenTubeDTOS =  _.flattenDeep(angular.copy(vm.frozenTubeArray));
            var boxStr = JSON.stringify(vm.obox);
            if(strBox == boxStr){
                $scope.showFlag = false;
                vm.saveStockInFlag = false;
                $scope.reloadData();
            }else{
                if(vm.obox.frozenBoxCode && vm.obox.sampleTypeId && vm.obox.frozenBoxTypeId){
                    modalInstance = $uibModal.open({
                        animation: true,
                        templateUrl: 'app/bizs/stock-in/modal/stock-in-close-splittingBox-modal.html',
                        controller: 'CloseSplittingBoxController',
                        controllerAs:'vm',
                        size:'sm',
                        resolve: {
                            items: function () {
                                return {
                                    status :status
                                };
                            }
                        }
                    });
                    modalInstance.result.then(function (flag) {
                        if(flag){
                            _fnSaveBox();
                        }
                        $scope.showFlag = false;
                        vm.saveStockInFlag = false;
                        $scope.reloadData();
                    });
                }else{
                    $scope.showFlag = false;
                    vm.saveStockInFlag = false;
                    $scope.reloadData();
                }

            }

        };
        //分装操作
        vm.subPackage = function () {
            $scope.editFlag = false;
            $scope.showFlag = false;
            vm.closeBox(4);
            $scope.editToSpiltTube();
        };

        function onError(error) {
            toastr.error(error.data.message);
        }
        // 获取控制实体
        function _getTableCtrl(){
            vm.TableCtrl = hotRegisterer.getInstance('my-handsontable');
            return vm.TableCtrl;
        }
    }
})();
