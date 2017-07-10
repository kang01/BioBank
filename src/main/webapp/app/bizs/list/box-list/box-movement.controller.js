/**
 * Created by gaokangkang on 2017/6/30.
 */
(function() {
    'use strict';

    angular
        .module('bioBankApp')
        .controller('BoxMovementController', BoxMovementController)
        .controller('ModalInstanceCtrl', ModalInstanceCtrl);

    BoxMovementController.$inject = ['$scope','hotRegisterer','$compile','$state','$stateParams','$uibModal','DTColumnBuilder','ProjectService','EquipmentService','AreasByEquipmentIdService','SupportacksByAreaIdService','EquipmentInventoryService','BioBankDataTable'];
    ModalInstanceCtrl.$inject = ['$uibModalInstance'];

    function BoxMovementController($scope,hotRegisterer,$compile,$state,$stateParams,$uibModal,DTColumnBuilder,ProjectService,EquipmentService,AreasByEquipmentIdService,SupportacksByAreaIdService,EquipmentInventoryService,BioBankDataTable) {
        var vm = this;
        vm.shelfInstance = {};
        vm.dto = {};
        vm.authorities = ['ROLE_USER', 'ROLE_ADMIN'];
        var selectedBox = $stateParams.selectedBox || [];
        vm.movementFlag = false;


        function _init() {
            _initHandsonTablePanel();
        }
        _init();
        vm.searchShelf = _fnSearchShelf;
        //关闭移位
        vm.close = _fnClose;
        function _fnClose() {
            if(selectedBox.length){
                var modalInstance = $uibModal.open({
                    templateUrl: 'myModalContent.html',
                    controller: 'ModalInstanceCtrl',
                    controllerAs: 'vm'
                });
                modalInstance.result.then(function () {
                    $state.go("box-inventory");
                }, function () {
                });
            }else{
                $state.go("box-inventory");
            }

        }
        //移动冻存盒
        vm.selectedOptions = BioBankDataTable.buildDTOption("NORMALLY", null, 10)
            .withOption('searching', false);
        vm.selectedColumns = [
            DTColumnBuilder.newColumn('frozenBoxCode').withTitle('冻存盒编码').withOption("width", "110"),
            DTColumnBuilder.newColumn('sampleType').withTitle('样本类型').withOption("width", "60"),
            DTColumnBuilder.newColumn('sampleClassification').withTitle('样本分类').withOption("width", "60"),

        ];
        vm.selectedOptions.withOption('data', selectedBox);

        //目标冻存架
        var shelfArray = [
        ];
        vm.shelfOptions = BioBankDataTable.buildDTOption("NORMALLY", null, 10)
            .withOption('searching', false)
            .withOption('serverSide',true)
            .withFnServerData(function ( sSource, aoData, fnCallback, oSettings ) {
                var data = {};
                for(var i=0; aoData && i<aoData.length; ++i){
                    var oData = aoData[i];
                    data[oData.name] = oData.value;
                }
                var jqDt = this;
                vm.dto.spaceType= "1";
                vm.dto.compareType= "1";
                vm.dto.number= 0;
                var searchForm = angular.toJson(vm.dto);
                EquipmentInventoryService.queryEquipmentList(data,searchForm).then(function (res){
                    var json = res.data;
                    vm.equipmentData = res.data.data;
                    var error = json.error || json.sError;
                    if ( error ) {
                        jqDt._fnLog( oSettings, 0, error );
                    }
                    oSettings.json = json;
                    fnCallback( json );
                }).catch(function(res){
                    console.log(res);
                    var ret = jqDt._fnCallbackFire( oSettings, null, 'xhr', [oSettings, null, oSettings.jqXHR] );

                    if ( $.inArray( true, ret ) === -1 ) {
                        if ( error == "parsererror" ) {
                            jqDt._fnLog( oSettings, 0, 'Invalid JSON response', 1 );
                        }
                        else if ( res.readyState === 4 ) {
                            jqDt._fnLog( oSettings, 0, 'Ajax error', 7 );
                        }
                    }

                    jqDt._fnProcessingDisplay( oSettings, false );
                });
            })
            .withOption('createdRow', createdRow);
        vm.shelfColumns = [
            DTColumnBuilder.newColumn('equipmentType').withTitle('设备类型').withOption("width", "80"),
            DTColumnBuilder.newColumn('position').withTitle('冻存架位置').withOption("width", "120").renderWith(_fnRowPositionRender),
            DTColumnBuilder.newColumn('countOfUsed').withTitle('已用').withOption("width", "50"),
            DTColumnBuilder.newColumn('countOfRest').withTitle('剩余').withOption("width", "50"),
            DTColumnBuilder.newColumn('projectName').withTitle('项目名称').withOption("width", "auto")

        ];
        function createdRow(row, data, dataIndex) {
            $compile(angular.element(row).contents())($scope);
        }
        function _fnRowPositionRender(data, type, full, meta) {
            var html = '';
            html = '<a ng-click="vm.searchShelf('+full.equipmentType+')">'+full.position+'</a>';
            return html;
        }
        function _fnSearchShelf() {
            vm.movementFlag = true;
        }

        function _initHandsonTablePanel(){
            vm.handsonTableArray = [];//初始管子数据二位数组

            initHandsonTable(4,4);

            vm.settings = {
                colHeaders : ['1','2','3','4','5','6','7','8','9','10'],
                rowHeaders : ['A','B','C','D','E','F','G','H','J','K'],
                outsideClickDeselectsCache: false,
                outsideClickDeselects: false,
                data:vm.handsonTableArray,
                minRows: 4,
                minCols: 4,
                fillHandle:false,
                stretchH: 'all',
                autoWrapCol:true,
                wordWrap:true,
                colWidths: 80,
                rowHeaderWidth: 30,
                multiSelect: false,
                comments: true,
                editor: false,
                renderer: myCustomRenderer,
                cells: _changeCellProperties,
                enterMoves:function () {
                    if(vm.nextFlag){
                        var hotMoves = hotRegisterer.getInstance('my-handsontable');
                        var selectedCol = hotMoves.getSelected()[1];
                        if(selectedCol + 1 < hotMoves.countCols()){
                            return{row:0,col:1};
                        } else{
                            return{row:1,col:-selectedCol};
                        }
                    }else{
                        return{row:0,col:0};
                    }

                }

            };
            //渲染管子表格
            function myCustomRenderer(hotInstance, td, row, col, prop, value, cellProperties) {
                var hot = _getTableCtrl();
                var cellData = value;
                if (hot){
                    cellData = hot.getDataAtCell(row, col);
                }

                if (cellProperties.readOnly){
                    $(td).addClass('htDimmed');
                    $(td).addClass('htReadOnly');
                } else {
                    $(td).removeClass('htDimmed');
                    $(td).removeClass('htReadOnly');
                }

                var $div = $("<div/>").addClass(cellProperties && cellProperties.className ? cellProperties.className : "");
                if (value){
                    $div.html(value.frozenBoxCode)
                        .data("frozenBoxId", value.frozenBoxId)
                        .data("frozenBoxCode", value.frozenBoxCode)
                        .data("columnsInShelf", value.columnsInShelf)
                        .data("rowsInShelf", value.rowsInShelf)
                        .data("countOfSample", value.countOfSample);
                }

                $(td).html("");
                $(td).append($div);

            }
            // 修改单元格属性
            function _changeCellProperties(row, col, prop) {
                var hot = _getTableCtrl();
                if (!hot){
                    return;
                }
                var cellProperties = {};
                var cellData = hot.getDataAtCell(row, col);
                cellProperties.isEmpty = true;
                if (cellData && cellData.frozenBoxId) {
                    // 单元格有数据，并且有冻存盒ID，表示该单元格在库里有位置
                    // 该单元格不能被使用
                    cellProperties.isEmpty = false;
                    // 该单元格不能编辑
                    cellProperties.editor = false;
                    // 该单元格只读
                    cellProperties.readOnly = true;
                    // 该单元格的样式
                    cellProperties.className = 'a00';
                    // cellProperties.readOnlyCellClassName = 'htDimmed';
                } else {
                    cellProperties.isEmpty = cellData && cellData.isEmpty;
                }

                return cellProperties;
            }


            function initHandsonTable(row,col) {
                for(var i = 0; i < row; i++){
                    vm.handsonTableArray[i] = [];
                    for(var j = 0;j < col; j++){
                        vm.handsonTableArray[i][j] = "";
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

        }



        // 获取上架位置列表的控制实体
        function _getTableCtrl(){
            vm.shelfDetailsTableCtrl = hotRegisterer.getInstance('shelfDetailsTable');
            return vm.shelfDetailsTableCtrl;
        }
        function onError(error) {
            // BioBankBlockUi.blockUiStop();
            // toastr.error(error.data.message);
        }
    }
    function ModalInstanceCtrl($uibModalInstance) {
        var vm = this;
        vm.ok = function () {
            $uibModalInstance.close();
        };
        vm.cancel = function () {
            $uibModalInstance.dismiss('cancel');
        };
    }
})();
