/**
 * Created by gaokangkang on 2017/6/19.
 */
(function() {
    'use strict';

    angular
        .module('bioBankApp')
        .controller('StockInNewController', StockInNewController);
        // .controller('RescindPutAwayModalController', RescindPutAwayModalController);

    StockInNewController.$inject = ['$timeout','BioBankBlockUi','$state','$stateParams', '$scope','$compile','toastr','hotRegisterer','DTOptionsBuilder','DTColumnBuilder','$uibModal','BioBankDataTable',
        'entity','StockInService','StockInBoxService','StockInBoxByCodeService','StockInInputService','ProjectService','ProjectSitesByProjectIdService',
        'SampleTypeService','SampleService','FrozenBoxTypesService','RescindPutAwayService','MasterData'];
    // RescindPutAwayModalController.$inject = ['$uibModalInstance'];
    function StockInNewController($timeout,BioBankBlockUi,$state,$stateParams,$scope,$compile,toastr,hotRegisterer,DTOptionsBuilder,DTColumnBuilder,$uibModal,BioBankDataTable,
                                   entity,StockInService,StockInBoxService,StockInBoxByCodeService,StockInInputService,ProjectService,ProjectSitesByProjectIdService,
                                   SampleTypeService,SampleService,FrozenBoxTypesService,RescindPutAwayService,MasterData) {
        var vm = this;
        vm.datePickerOpenStatus = {};
        vm.openCalendar = openCalendar; //时间
        vm.entity = entity;
        vm.stockInCode = vm.entity.stockInCode;
        vm.entityBoxes = {};
        vm.splittingBox = null;
        vm.splittedBoxes = {};
        vm.dtInstance = {};
        var modalInstance;
        //保存入库记录
        vm.stockInSave = _fnStockInSave;
        function _fnStockInSave() {
            if(vm.entity.id){
                StockInInputService.saveEditStockIn(vm.entity).success(function (data) {
                    vm.entity = data;
                    if(!vm.saveStockInFlag){
                        toastr.success("保存入库信息成功!");
                    }
                });
            }else{
                StockInInputService.saveStockIn(vm.entity).success(function (data) {
                    vm.entity = data;
                    if(!vm.splittingBox){
                        toastr.success("保存入库信息成功!");
                    }
                });
            }

        }

        //入库完成
        vm.stockInFinish = function () {
            modalInstance = $uibModal.open({
                animation: true,
                templateUrl: 'app/bizs/stock-in/stock-in-info-modal.html',
                controller: 'StockInInfoModalController',
                controllerAs:'vm',
                resolve: {
                    items: function () {
                        return {
                            id: vm.entity.id,
                            stockInCode: vm.entity.stockInCode
                        };
                    }
                }
            });
            modalInstance.result.then(function (data) {
                $state.go('stock-in');
            });
        };

        _initStockInInfo();
        function _initStockInInfo() {
            //项目
            ProjectService.query({},onProjectSuccess, onError);
            function onProjectSuccess(data) {
                vm.projectOptions = data;
                if(!vm.entity.projectId){
                    vm.entity.projectId = data[0].id;
                }
                vm.entity.projectCode = _.find(vm.projectOptions,{id:vm.entity.projectId}).projectCode;
                ProjectSitesByProjectIdService.query({id:vm.entity.projectId},onProjectSitesSuccess,onError);
            }
            //项目
            vm.projectConfig = {
                valueField:'id',
                labelField:'projectName',
                maxItems: 1,
                onChange:function(value){
                    vm.entity.projectSiteId = "";
                    ProjectSitesByProjectIdService.query({id:value},onProjectSitesSuccess,onError);
                }
            };
            //项目点
            vm.projectSitesConfig = {
                valueField:'id',
                labelField:'projectSiteName',
                maxItems: 1,
                onChange:function (value) {
                }
            };

            function onProjectSitesSuccess(data) {
                vm.projectSitesOptions = data;
                if(!vm.entity.projectSiteId){
                    if(data.length){
                        vm.entity.projectSiteId = data[0].id;
                    }
                }
            }
        }
        _initStockInBoxesTable();

        function _initStockInBoxesTable(){
            vm.selectedStockInBoxes = {};
            vm.selected = {};
            vm.selectAll = false;

            // 处理盒子选中状态
            vm.toggleAll = function (selectAll, selectedItems) {
                selectedItems = vm.selected;
                selectAll = vm.selectAll;
                for (var id in selectedItems) {
                    if (selectedItems.hasOwnProperty(id)) {
                        selectedItems[id] = selectAll;
                    }
                }
            };
            vm.toggleOne = function (selectedItems) {
                for (var id in selectedItems) {
                    if (selectedItems.hasOwnProperty(id)) {
                        if(!selectedItems[id]) {
                            vm.selectAll = false;
                            return;
                        }
                    }
                }
                vm.selectAll = true;
            };

            vm.dtInstance = {};

            vm.dtOptions = BioBankDataTable.buildDTOption("NORMALLY", null, 6, "<'row'<'col-xs-6' TB> <'col-xs-6' f> r> t <'row'<'col-xs-6'i> <'col-xs-6'p>>", $scope)
            // 设置Tool button
                .withButtons([
                    {
                        text: '<i class="fa fa-sign-in"></i> 批量上架',
                        className: 'btn btn-default mr-5',
                        key: '1',
                        action: _fnActionPutInShelfButton
                    },
                    {
                        text: '<i class="fa fa-plus"></i> 添加冻存盒',
                        className: 'btn btn-default',
                        key: '1',
                        action: _fnActionAddBoxButton
                    }
                ])
                // 数据从服务器加载
                .withOption('serverSide',true)
                // 设置默认排序
                .withOption('order', [[7, 'asc' ], [1, 'asc' ]])
                // 指定数据加载方法
                .withFnServerData(_fnServerData)
                // 每行的渲染
                .withOption('createdRow', _fnCreatedRow)
                // 定义每个列过滤选项
                .withColumnFilter(_createColumnFilters());

            // 表格中每个列的定义
            vm.dtColumns = _createColumns();

            // 分装按钮
            // vm.splitIt = _splitABox;
            // 上架按钮
            vm.putInShelf = _putInShelf;
            // 上架按钮
            vm.editBox = _editBox;
            //撤销上架
            vm.rescindInShelf = _rescindInShelf;
        }

        function _fnServerData( sSource, aoData, fnCallback, oSettings ) {
            var data = {};
            for(var i=0; aoData && i<aoData.length; ++i){
                var oData = aoData[i];
                data[oData.name] = oData.value;
            }
            data["stockInCode"] = vm.entity.stockInCode;
            var jqDt = this;
            StockInBoxService.getJqDataTableValues(data, oSettings).then(function (res){
                vm.selectAll = false;
                vm.selected = {};

                var json = res.data;
                var error = json.error || json.sError;
                if ( error ) {
                    jqDt._fnLog( oSettings, 0, error );
                }
                oSettings.json = json;
                for(var i = 0; json.data && i < json.data.length; ++i){
                    vm.entityBoxes[json.data[i].frozenBoxCode] = json.data[i];
                }
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
        }
        function _fnCreatedRow(row, data, dataIndex) {
            var status = '';
            var isSplit = data.isSplit || 0;
            // var sampleType = data.sampleType && data.sampleType.sampleTypeName || '';
            // 冻存盒状态：2001：新建，2002：待入库，2003：已分装，2004：已入库，2005：已作废，2006：已上架，2008：待出库，2009：已出库
            status = MasterData.getFrozenBoxStatus(data.status);
            if(data.status == '2002'){
                if(isSplit){
                    status = "待分装";
                }else{
                    status = "待入库";
                }
            }
            // switch (data.status){
            //     case '2001': status = '新建'; break;
            //     case '2002': isSplit ? status = '待分装' : status = '待入库'; break;
            //     case '2003': status = '已分装'; break;
            //     case '2004': status = '已入库'; break;
            //     case '2005': status = '已作废'; break;
            //     case '2006': status = '已上架'; break;
            //     case '2008': status = '待出库'; break;
            //     case '2009': status = '已出库'; break;
            // }
            // $('td:eq(2)', row).html(sampleType);
            // $('td:eq(6)', row).html(isSplit ? '需要分装' : '');
            $('td:eq(6)', row).html(status);
            $compile(angular.element(row).contents())($scope);
        }
        function _fnActionButtonsRender(data, type, full, meta) {
            // console.log(vm.splitIt, vm.putInShelf);
            var buttonHtml = "";
            if (full.status == "2002"){
                // if (full.isSplit){
                //     buttonHtml += '<button type="button" class="btn btn-xs btn-warning" ng-click="vm.splitIt(\''+ full.frozenBoxCode +'\')">' +
                //         '   <i class="fa fa-sitemap"></i> 分装' +
                //         '</button>';
                // } else {
                    buttonHtml += '<button type="button" class="btn btn-xs btn-error" ng-click="vm.editBox(\''+ full.frozenBoxCode +'\')">' +
                        '   <i class="fa fa-sign-in"></i> 编辑 ' +
                        '</button>&nbsp;'+
                        '<button type="button" class="btn btn-xs btn-error" ng-click="vm.putInShelf(\''+ full.frozenBoxCode +'\')">' +
                        '   <i class="fa fa-sign-in"></i> 上架' +
                        '</button>';
                // }
            }
            if(full.status == "2006"){
                buttonHtml += '<button type="button" class="btn btn-xs btn-error" ng-click="vm.rescindInShelf(\''+ full.frozenBoxCode +'\')">' +
                    '   <i class="fa fa-sitemap"></i> 撤销上架' +
                    '</button>';
            }

            return buttonHtml;
            // return '<button type="button" class="btn btn-xs btn-warning" ng-click="vm.splitIt(\''+ full.frozenBoxCode +'\')">' +
            //     '   <i class="fa fa-sitemap"></i> 分装' +
            //     '</button>&nbsp;' +
            //     '<button type="button" class="btn btn-xs btn-error" ng-click="vm.putInShelf(\''+ full.frozenBoxCode +'\')">' +
            //     '   <i class="fa fa-sign-in"></i> 上架' +
            //     '</button>';
        }
        function _fnRowSelectorRender(data, type, full, meta) {
            // todo::已上架状态的盒子不应该再被选中
            vm.selected[full.frozenBoxCode] = false;
            var html = '';
            if (full.status == "2002" && !full.isSplit){
                html = '<input type="checkbox" ng-model="vm.selected[\'' + full.frozenBoxCode + '\']" ng-click="vm.toggleOne(vm.selected)">';
            }
            return html;
        }
        function _fnActionPutInShelfButton(e, dt, node, config){
            _putInShelf();
        }
        function _createColumnFilters(){
            var filters = {
                aoColumns: [
                    null,
                    {type: 'text',bRegex: true,bSmart: true,iFilterLength:3},
                    {type: 'text',bRegex: true,bSmart: true,iFilterLength:3},
                    {type: 'text',bRegex: true,bSmart: true,iFilterLength:3},
                    {type: 'text',bRegex: true,bSmart: true,iFilterLength:3},
                    {type: 'text',bRegex: true,bSmart: true,iFilterLength:3},
                    {
                        type: 'select',
                        // bRegex: true,
                        bSmart: true,
                        values: MasterData.frozenBoxStatus

                    },
                    null
                ]
            };

            return filters;
        }
        function _createColumns(){
            var titleHtml = '<input type="checkbox" ng-model="vm.selectAll" ng-click="vm.toggleAll()">';

            var columns = [
                // DTColumnBuilder.newColumn('id').withTitle('id').notVisible(),
                DTColumnBuilder.newColumn("").withOption("width", "30").withTitle(titleHtml).withOption('searchable',false).notSortable().renderWith(_fnRowSelectorRender),
                DTColumnBuilder.newColumn('frozenBoxCode').withTitle('冻存盒号'),
                DTColumnBuilder.newColumn('sampleTypeName').withOption("width", "80").withTitle('样本类型'),
                DTColumnBuilder.newColumn('sampleClassificationName').withOption("width", "120").withTitle('样本分类'),
                DTColumnBuilder.newColumn('position').withOption("width", "auto").withTitle('冻存位置'),
                DTColumnBuilder.newColumn('countOfSample').withOption("width", "90").withTitle('样本量'),
                DTColumnBuilder.newColumn('status').withOption("width", "80").withTitle('状态'),
                DTColumnBuilder.newColumn("").withOption("width", "120").withTitle('操作').withOption('searchable',false).notSortable().renderWith(_fnActionButtonsRender),
                // DTColumnBuilder.newColumn('sampleType').notVisible(),
                // DTColumnBuilder.newColumn('frozenBoxRows').notVisible(),
                // DTColumnBuilder.newColumn('frozenBoxColumns').notVisible()
            ];

            return columns;
        }

        // function _splitABox(code){
        //     _fnQuerySampleType();
        //     _fnTubeByBoxCode(code);
        //
        // }
        function _putInShelf(boxIds){
            var boxes = [];
            var table = vm.dtInstance.DataTable;
            if (boxIds && typeof boxIds !== "object"){
                boxIds = [boxIds];
                table.data().each( function (d) {
                    if (boxIds == d.frozenBoxCode){
                        boxes.push(d);
                    }
                });
            } else {
                boxIds = [];
                for(var id in vm.selected){
                    if(!vm.selected[id]) {
                        continue;
                    }
                    boxIds.push(id);
                    table.data().each( function (d) {
                        if (id == d.frozenBoxCode){
                            boxes.push(d);
                        }
                    });
                }

            }

            if (typeof boxIds === "undefined" || !boxIds.length){
                return;
            }

            modalInstance = $uibModal.open({
                animation: true,
                templateUrl: 'app/bizs/stock-in/box-putaway-modal.html',
                controller: 'BoxPutAwayModalController',
                controllerAs:'vm',
                // size:'lg',
                size:'90',
                resolve: {
                    items: function () {
                        return {
                            stockInCode: vm.entity.stockInCode,
                            boxIds: boxIds,
                            boxes: boxes
                        };
                    }
                }
            });
            modalInstance.result.then(function (data) {
                vm.dtOptions.isHeaderCompiled = false;
                vm.dtInstance.rerender();
            });
        }
        function _rescindInShelf(boxCode) {
            modalInstance = $uibModal.open({
                animation: true,
                templateUrl: 'app/bizs/stock-in/rescind-putaway-modal.html',
                controller: 'RescindPutAwayModalController',
                controllerAs:'vm'
            });
            modalInstance.result.then(function (data) {
                RescindPutAwayService.rescindPutAway(vm.entity.stockInCode,boxCode).then(function (data) {
                    vm.dtOptions.isHeaderCompiled = false;
                    vm.dtInstance.rerender();
                });
            });

        }
        vm.box = {};
        vm.editFlag = false;
        function _editBox(frozenBoxCode) {
            vm.editFlag = true;
            vm.splittingBox = true;
            _initBoxInfo();
            StockInInputService.queryEditStockInBox(frozenBoxCode).success(function (data) {
                if(data.frozenBoxCode){
                    vm.box = data;
                    _initSampleType();
                }

            });
        }



        vm.saveStockInFlag = false;
        //添加冻存盒
        function _fnActionAddBoxButton() {
            if(vm.entity.status == '7002'){
                return;
            }
            vm.box = {
                frozenTubeDTOS:[]
            };
            vm.saveStockInFlag = true;
            vm.stockInSave();
            _initBoxInfo();
            _initSampleType();
            vm.splittingBox = true;
            vm.editFlag = false;

        }
        //冻存盒搜索
        vm.frozenBoxForStockIn =_fnFrozenBoxForStockIn;
        function _fnFrozenBoxForStockIn() {
            if(vm.box.frozenBoxCode){
                StockInInputService.queryStockInBox(vm.box.frozenBoxCode).success(function (data) {
                    if(data.frozenBoxCode){
                        vm.box = data;
                        if(vm.box.id){
                            vm.editFlag = true;
                        }else{
                            vm.editFlag = false;
                        }
                        _initSampleType();
                        vm.reloadTubesForTable(vm.box);
                    }
                }).error(function (res) {
                    toastr.error(res.message);
                    vm.box.frozenBoxCode = "";
                });
            }

        }
        function _initBoxInfo() {
            //盒子类型 17:10*10 18:8*8
            FrozenBoxTypesService.query({},onFrozenBoxTypeSuccess, onError);
            function onFrozenBoxTypeSuccess(data) {
                vm.frozenBoxTypeOptions = _.orderBy(data, ['id'], ['asc']);
                if(vm.frozenBoxTypeOptions.length){
                    vm.box.frozenBoxTypeId = vm.frozenBoxTypeOptions[0].id;
                }
                // vm.box.frozenBoxType = vm.frozenBoxTypeOptions[0];
                vm.box.frozenBoxTypeRows = vm.frozenBoxTypeOptions[0].frozenBoxTypeRows;
                vm.box.frozenBoxTypeColumns = vm.frozenBoxTypeOptions[0].frozenBoxTypeColumns;

            }
            vm.boxTypeConfig = {
                valueField:'id',
                labelField:'frozenBoxTypeName',
                maxItems: 1,
                onChange:function(value){
                    var boxType = _.filter(vm.frozenBoxTypeOptions, {id:+value})[0];
                    if (!boxType) {
                        return;
                    }
                    vm.box.frozenBoxTypeId = value;
                    // vm.box.frozenBoxType = boxType;
                    vm.box.frozenBoxTypeRows = boxType.frozenBoxTypeRows;
                    vm.box.frozenBoxTypeColumns = boxType.frozenBoxTypeColumns;
                    var box = vm.box;
                    _fnQueryProjectSampleClass(vm.entity.projectId,vm.box.sampleTypeId,vm.isMixed);
                    vm.reloadTubesForTable(box);
                    hotRegisterer.getInstance('my-handsontable').render();
                }
            };
            _initFrozenBoxPanel();
            setTimeout(function () {
                vm.reloadTubesForTable(vm.box)
            },500);

        }
        function _initSampleType() {
            //样本类型
            SampleTypeService.querySampleType().success(function (data) {
                vm.sampleTypeOptions = _.orderBy(data,['sampleTypeName','asc']);
                _.remove(vm.sampleTypeOptions,{sampleTypeName:"99"});
                if(!vm.box.sampleTypeId){
                    vm.box.sampleTypeId = vm.sampleTypeOptions[0].id;
                    vm.box.sampleTypeName = vm.sampleTypeOptions[0].sampleTypeName;
                    vm.box.sampleTypeCode = vm.sampleTypeOptions[0].sampleTypeCode;
                }

                // vm.box.sampleType = vm.sampleTypeOptions[0];
                vm.isMixed = _.find(vm.sampleTypeOptions,{'id':+vm.box.sampleTypeId}).isMixed;
                setTimeout(function () {
                    _fnQueryProjectSampleClass(vm.entity.projectId,vm.box.sampleTypeId,vm.isMixed);
                },500);
            });
            vm.sampleTypeConfig = {
                valueField:'id',
                labelField:'sampleTypeName',
                maxItems: 1,
                onChange:function (value) {
                    vm.isMixed = _.find(vm.sampleTypeOptions,{'id':+value}).isMixed;
                    vm.box.sampleTypeName = _.find(vm.sampleTypeOptions,{'id':+value}).sampleTypeName;
                    vm.box.sampleTypeCode = _.find(vm.sampleTypeOptions,{'id':+value}).sampleTypeCode;
                    _fnQueryProjectSampleClass(vm.entity.projectId,value,vm.isMixed);
                }
            };
            //样本分类
            function _fnQueryProjectSampleClass(projectId,sampleTypeId,isMixed) {
                SampleTypeService.queryProjectSampleClasses(projectId,sampleTypeId).success(function (data) {
                    vm.projectSampleTypeOptions = data;
                    //是否混合类型 1：是混合类型
                    if(isMixed == 1){
                        // if(data.length){
                        //     //类型下有分类
                        //     for(var k = 0; k < data.length; k++){
                        //         for (var i = 0; i < vm.frozenTubeArray.length; i++) {
                        //             for (var j = 0; j < vm.frozenTubeArray[i].length; j++) {
                        //                 if(data[k].columnsNumber == j+1){
                        //                     vm.frozenTubeArray[i][j].sampleClassificationId = data[k].sampleClassificationId;
                        //                     vm.frozenTubeArray[i][j].sampleTypeId = sampleTypeId;
                        //                     vm.frozenTubeArray[i][j].sampleTypeName = _.find(vm.sampleTypeOptions,{'id':+sampleTypeId}).sampleTypeName;
                        //
                        //                 }
                        //             }
                        //         }
                        //     }
                        //
                        // }else{
                        //     //混合型无分类
                        for (var i = 0; i < vm.frozenTubeArray.length; i++) {
                            for (var j = 0; j < vm.frozenTubeArray[i].length; j++) {
                                if(!vm.frozenTubeArray[i][j].sampleCode){
                                    vm.frozenTubeArray[i][j].sampleTypeId = sampleTypeId;
                                    vm.frozenTubeArray[i][j].sampleTypeName = _.find(vm.sampleTypeOptions,{'id':+sampleTypeId}).sampleTypeName;
                                    vm.frozenTubeArray[i][j].sampleTypeCode = _.find(vm.sampleTypeOptions,{'id':+sampleTypeId}).sampleTypeCode;
                                    vm.frozenTubeArray[i][j].backColor = _.find(vm.sampleTypeOptions,{'id':+sampleTypeId}).backColor;
                                }
                            }

                        }
                        // }

                    }else{
                        if(!vm.box.sampleClassificationId){
                            if(vm.projectSampleTypeOptions.length){
                                vm.box.sampleClassificationId = vm.projectSampleTypeOptions[0].sampleClassificationId;
                                vm.box.sampleClassificationCode = vm.projectSampleTypeOptions[0].sampleClassificationCode;
                            }
                        }

                        for (var i = 0; i < vm.frozenTubeArray.length; i++) {
                            for (var j = 0; j < vm.frozenTubeArray[i].length; j++) {
                                if(vm.box.sampleClassificationId){
                                    if(!vm.frozenTubeArray[i][j].sampleCode){
                                        vm.frozenTubeArray[i][j].sampleClassificationId = vm.box.sampleClassificationId;
                                        vm.frozenTubeArray[i][j].sampleClassificationCode = vm.box.sampleClassificationCode;

                                    }
                                }
                                vm.frozenTubeArray[i][j].sampleTypeId = sampleTypeId;
                                vm.frozenTubeArray[i][j].sampleTypeName = _.find(vm.sampleTypeOptions,{'id':+sampleTypeId}).sampleTypeName;
                                vm.frozenTubeArray[i][j].sampleTypeCode = _.find(vm.sampleTypeOptions,{'id':+sampleTypeId}).sampleTypeCode;
                                vm.frozenTubeArray[i][j].backColor = _.find(vm.sampleTypeOptions,{'id':+sampleTypeId}).backColor;
                            }
                        }
                        // vm.box.sampleType = _.find(vm.sampleTypeOptions,{id:+sampleTypeId});
                        // for(var m = 0; m < vm.box.frozenTubeDTOS.length; m++){
                        //     if(vm.box.sampleClassification){
                        //         vm.box.frozenTubeDTOS[m].sampleClassification.id = vm.box.sampleClassificationId;
                        //     }
                        //     vm.box.frozenTubeDTOS[m].sampleType.id = vm.box.sampleTypeId;
                        // }
                    }

                    hotRegisterer.getInstance('my-handsontable').render();
                });
            }
            vm.projectSampleTypeConfig = {
                valueField:'sampleClassificationId',
                labelField:'sampleClassificationName',
                maxItems: 1,
                onChange:function (value) {
                    vm.box.sampleClassificationId = value;
                    vm.box.sampleClassificationName = _.find(vm.projectSampleTypeOptions,{sampleClassificationId:+value}).sampleClassificationName;
                    vm.box.sampleClassificationCode = _.find(vm.projectSampleTypeOptions,{sampleClassificationId:+value}).sampleClassificationCode;
                    for (var i = 0; i < vm.frozenTubeArray.length; i++) {
                        for (var j = 0; j < vm.frozenTubeArray[i].length; j++) {
                            vm.frozenTubeArray[i][j].sampleClassificationId = vm.box.sampleClassificationId;
                            vm.frozenTubeArray[i][j].sampleClassificationCode = vm.box.sampleClassificationCode;
                        }
                    }
                    hotRegisterer.getInstance('my-handsontable').render();
                }
            };
        }
        // _initSampleType();
        //初始化冻存管
        function _initFrozenBoxPanel(){
            vm.frozenTubeArray = [];//初始管子数据二位数组
            var remarkArray;//批注
            var domArray = [];//单元格操作的数据
            var operateColor;//单元格颜色

            initFrozenTube(10,10);

            vm.settings = {
                colHeaders : ['1','2','3','4','5','6','7','8','9','10'],
                rowHeaders : ['A','B','C','D','E','F','G','H','J','K'],
                data:vm.frozenTubeArray,
                renderer: myCustomRenderer,
                minRows: 10,
                minCols: 10,
                fillHandle:false,
                stretchH: 'all',
                autoWrapCol:true,
                wordWrap:true,
                colWidths: 80,
                rowHeaderWidth: 30,
                editor: 'tubeInput',
                multiSelect: true,
                comments: true,
                onAfterSelectionEnd:function (row, col, row2, col2) {
                    vm.nextFlag = true;
                    vm.remarkFlag = true;
                    var td = this;
                    remarkArray = this.getData(row,col,row2,col2);
                    var selectTubeArrayIndex = this.getSelected();
                    if(window.event && window.event.ctrlKey){
                        //换位
                        vm.exchangeFlag = true;
                        domArray.push(vm.frozenTubeArray[row][col]);

                        //备注
                        _fnRemarkSelectData(td,remarkArray,selectTubeArrayIndex);
                    }else{
                        domArray = [];
                        domArray.push(vm.frozenTubeArray[row][col]);
                        //备注
                        $(".temp").remove();
                        aRemarkArray = [];
                        _fnRemarkSelectData(td,remarkArray,selectTubeArrayIndex);


                    }
                    //管子
                    if(vm.flagStatus){
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
                },
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

                },
                afterChange:function (change,source) {
                    if(source == 'edit'){
                        for (var i=0; i<change.length; ++i){
                            var item = change[i];
                            var row = item[0];
                            var col = item[1];
                            var oldTube = item[2];
                            var newTube = item[3];
                            if (oldTube.id
                                || (oldTube.sampleCode && oldTube.sampleCode.length > 1)
                                || (oldTube.sampleTempCode && oldTube.sampleTempCode.length > 1)){

                                // When delete a tube.
                                if (newTube === ""){
                                    newTube = angular.copy(oldTube);
                                    newTube.id = null;
                                    newTube.sampleCode = "";
                                    newTube.sampleTempCode = "";
                                    hotRegisterer.getInstance('my-handsontable').setDataAtCell(row, col, newTube);
                                }else{
                                    // console.log(JSON.stringify(oldTube));
                                    if(!vm.oldTube){
                                        vm.oldTube = oldTube;
                                    }

                                    StockInInputService.queryTube(oldTube.sampleCode,vm.entity.projectCode,oldTube.sampleTypeId).success(function (data) {
                                        var stockInTubes;
                                        if(vm.box.sampleTypeName != "98"){
                                            stockInTubes = _.filter(data,{sampleTypeId:vm.box.sampleTypeId});
                                        }else{
                                            stockInTubes = data;
                                        }

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
                                                        status :1,
                                                        tubes:stockInTubes,
                                                        sampleCode:oldTube.sampleCode,
                                                        projectSiteId:vm.entity.projectSiteId,
                                                        projectId:vm.entity.projectId,
                                                        projectCode:vm.entity.projectCode,
                                                        sampleTypeId:vm.box.sampleTypeId,
                                                        sampleTypeName:vm.box.sampleTypeName,
                                                        sampleTypeCode:vm.box.sampleTypeCode,
                                                        sampleClassificationId:vm.box.sampleClassificationId,
                                                        sampleClassificationName:vm.box.sampleClassificationName,
                                                        sampleClassificationCode:vm.box.sampleClassificationCode
                                                    };
                                                }
                                            }

                                        });
                                        modalInstance.result.then(function (tube) {
                                            var tableCtrl = _getTableCtrl();
                                            for(var i = 0; i < vm.frozenTubeArray.length; i++){
                                                for(var j = 0; j < vm.frozenTubeArray[i].length; j++){
                                                    if(vm.frozenTubeArray[i][j].sampleCode == tube.sampleCode){
                                                        // vm.frozenTubeArray[i][j] = tube;
                                                        vm.frozenTubeArray[i][j].status = tube.status;
                                                        vm.frozenTubeArray[i][j].projectSiteId = tube.projectSiteId;
                                                        vm.frozenTubeArray[i][j].memo = tube.memo;
                                                        vm.frozenTubeArray[i][j].sampleTypeId = tube.sampleTypeId;
                                                        vm.frozenTubeArray[i][j].sampleTypeName = tube.sampleTypeName;
                                                        vm.frozenTubeArray[i][j].backColor = tube.backColor;
                                                        vm.frozenTubeArray[i][j].sampleVolumns = tube.sampleVolumns;
                                                        if(tube.sampleClassificationId){
                                                            vm.frozenTubeArray[i][j].sampleClassificationId = tube.sampleClassificationId;
                                                            vm.frozenTubeArray[i][j].sampleClassificationName = tube.sampleClassificationName;
                                                            vm.frozenTubeArray[i][j].sampleClassitionCode = tube.sampleClassitionCode;
                                                            vm.frozenTubeArray[i][j].backColorForClass = tube.backColorForClass;
                                                        }
                                                    }
                                                }
                                            }
                                            hotRegisterer.getInstance('my-handsontable').render();
                                        },function (tube) {
                                            var tableCtrl = _getTableCtrl();
                                            for(var i = 0; i < vm.frozenTubeArray.length; i++){
                                                for(var j = 0; j < vm.frozenTubeArray[i].length; j++){
                                                    if(vm.frozenTubeArray[i][j].sampleCode == oldTube.sampleCode){
                                                        vm.frozenTubeArray[row][col].sampleCode = "";
                                                    }
                                                }
                                            }
                                            tableCtrl.loadData(vm.frozenTubeArray);
                                        });

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
                                }
                            }
                        }

                        return;
                    }
                    if(source == 'loadData'){

                    }
                },
                beforeKeyDown:function (event) {
                    if(vm.flagStatus){
                        if(event.keyCode == 9 || event.keyCode == 37 || event.keyCode == 38 || event.keyCode == 39 || event.keyCode == 40){
                            Handsontable.Dom.stopImmediatePropagation(event);
                        }
                    }

                },
                beforeChange: function(changes, source) {
                    for (var k=0; k<changes.length; ++k){
                        var item = changes[k];
                        var row = item[0];
                        var col = item[1];
                        var oldTube = item[2];
                        var newTube = item[3];
                        if(oldTube.sampleCode){
                            for(var i = 0; i < vm.frozenTubeArray.length; i++){
                                for(var j = 0; j < vm.frozenTubeArray[i].length; j++){
                                    if(i == row  && j == col){
                                        continue;
                                    }
                                    if(vm.frozenTubeArray[i][j].sampleCode == oldTube.sampleCode){
                                        vm.frozenTubeArray[row][col].sampleCode = "";
                                        hotRegisterer.getInstance('my-handsontable').render();
                                        toastr.error("冻存管编码不能重复!");
                                        vm.nextFlag = false;
                                        return false;
                                    }

                                }
                            }
                        }else{
                            var tableCtrl = _getTableCtrl();
                            vm.oldTube.sampleCode = "";
                            vm.oldTube.tubeRows = getTubeRows(+row)+"";
                            vm.oldTube.tubeColumns = getTubeColumns(+col);
                            // vm.frozenTubeArray[row][col].backColor = vm.oldTube.backColor;
                            vm.frozenTubeArray[row][col] = vm.oldTube;
                            // hotRegisterer.getInstance('my-handsontable').setDataAtCell(row, col, "");
                            hotRegisterer.getInstance('my-handsontable').render();
                            // tableCtrl.loadData(vm.frozenTubeArray);
                            return false;
                        }

                    }


                },
                afterBeginEditing:function (row,col) {
                    // console.log(row)
                    // console.log(col)
                }

            };
            // 获取控制实体
            function _getTableCtrl(){
                vm.TableCtrl = hotRegisterer.getInstance('my-handsontable');
                return vm.TableCtrl;
            }
            //渲染管子表格
            function myCustomRenderer(hotInstance, td, row, col, prop, value, cellProperties) {
                var tube= value||{};
                td.style.position = 'relative';

                if(tube.memo && tube.memo != " "){
                    cellProperties.comment = tube.memo;
                }
                if(vm.box.sampleTypeName == '98'){
                    if(tube.backColorForClass){
                        td.style.backgroundColor = tube.backColorForClass;
                    }else{
                        td.style.backgroundColor = tube.backColor;
                    }

                }else{
                    //样本类型
                    if(tube.sampleClassificationId){
                        SampleService.changeSampleType(tube.sampleClassificationId,td,vm.projectSampleTypeOptions,1);
                    }else{
                        if(vm.sampleTypeOptions){
                            SampleService.changeSampleType(tube.sampleTypeId,td,vm.sampleTypeOptions,2);
                        }
                    }
                }


                //样本状态 status3001：正常，3002：空管，3003：空孔；3004：异常
                if(tube.status){
                    changeSampleStatus(tube.status,row,col,td,cellProperties);
                }

                var code = tube.sampleCode && tube.sampleCode != " " ? tube.sampleCode : tube.sampleTempCode;
                $(td).html("");
                var $div = $("<div/>").html(code).css({
                    'line-height': '20px',
                    'word-wrap': 'break-word'
                }).appendTo(td);
                $div = $("<div id='microtubesStatus'/>").html(tube.status).hide().appendTo(td);

            }

            //修改样本状态正常、空管、空孔、异常
            function changeSampleStatus(sampleStatus,row,col,td,cellProperties) {

                operateColor = td.style.backgroundColor;
                //正常
                if(sampleStatus == 3001){
                }
                //空管
                if(sampleStatus == 3002){
                    td.style.background = 'linear-gradient(to right,'+operateColor+',50%,black';
                }
                //空孔
                if(sampleStatus == 3003){
                    td.style.background = '';
                    td.style.backgroundColor = '#ffffff';
                    td.style.color = '#ffffff';
                }
                //异常
                if(sampleStatus == 3004){
                    td.style.backgroundColor = 'red';
                    td.style.border = '3px solid red;margin:-3px';
                }
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

            // 创建一个对象用于管子Table的控件
            function _createTubeForTableCell(tubeInBox, box, rowNO, colNO, pos){
                var tube = {
                    id: null,
                    sampleCode: "",
                    sampleTempCode: "",
                    sampleTypeId: "",
                    sampleTypeName: "",
                    sampleClassificationId:"",
                    frozenBoxId: box.frozenBoxTypeId,
                    frozenBoxCode: box.frozenBoxCode,
                    status: "3001",
                    memo: "",
                    tubeRows: pos.tubeRows,
                    tubeColumns: pos.tubeColumns,
                    rowNO: rowNO,
                    colNO: colNO,
                    projectId:vm.entity.projectId,
                    projectSiteId:vm.entity.projectSiteId
                };

                if (tubeInBox){
                    tube.id = tubeInBox.id;
                    tube.sampleCode = tubeInBox.sampleCode;
                    tube.sampleTempCode = tubeInBox.sampleTempCode;
                    tube.status = tubeInBox.status;
                    tube.memo = tubeInBox.memo;
                    if(tubeInBox.sampleClassificationId){
                        tube.sampleClassificationId = tubeInBox.sampleClassificationId;
                        tube.backColorForClass = tubeInBox.backColorForClass;
                    }
                    tube.sampleTypeId = tubeInBox.sampleTypeId;
                    tube.sampleTypeName = tubeInBox.sampleTypeName;
                    tube.backColor = tubeInBox.backColor;
                }else{
                    tube.sampleTypeId = box.sampleTypeId;
                    tube.sampleTypeName = box.sampleTypeName;
                    tube.sampleClassificationId = box.sampleClassificationId;

                }

                return tube;
            }
            // 重新加载管子表控件
            vm.reloadTubesForTable = _reloadTubesForTable;
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

            }
            vm.reloadTubesForTable = _reloadTubesForTable;



            var aRemarkArray = [];
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
                // toastr.success("两个空冻存盒不能被交换!");

                if(vm.exchangeFlag && domArray.length == 2){
                    if((!domArray[0].sampleCode && !domArray[1].sampleCode)){
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
                        templateUrl: 'app/bizs/transport-record/microtubes-remark-modal.html',
                        controller: 'microtubesRemarkModalController',
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
                        for(var i = 0; i < aRemarkArray.length; i++){

                            if(aRemarkArray[i].sampleCode){
                                aRemarkArray[i].memo = memo;
                            }
                        }

                        aRemarkArray = [];
                        hotRegisterer.getInstance('my-handsontable').render();
                    });
                }
            };
            //删除盒子
            vm.delBox = _fnDelBox;
            //删除盒子
            function _fnDelBox() {
                modalInstance = $uibModal.open({
                    animation: true,
                    templateUrl: 'app/bizs/transport-record/frozen-box-delete-modal.html',
                    controller: 'FrozenBoxDeleteController',
                    backdrop:'static',
                    controllerAs: 'vm'

                });
                modalInstance.result.then(function (flage) {
                    if (flage){
                        FrozenBoxDelService.delete({code:vm.box.frozenBoxCode},onDelBoxSuccess,onError);
                    }
                    function onDelBoxSuccess() {
                        toastr.success("删除成功!");
                        vm.loadBox();
                        vm.box = null;
                        vm.boxStr = null;
                        initFrozenTube(10,10);
                        hotRegisterer.getInstance('my-handsontable').render();
                    }
                });
            }
        }

        function onError(error) {
            toastr.error(error.data.message);
        }
        //保存冻存盒
        function _fnSaveBox() {
            vm.box.frozenTubeDTOS = [];
            var tubeList = _.flatten(vm.frozenTubeArray);
            for(var i = 0; i< tubeList.length; i++){
                if(tubeList[i].sampleCode){
                    vm.box.frozenTubeDTOS.push(tubeList[i]);
                }
            }
            StockInInputService.saveStockInBox(vm.entity.stockInCode,vm.box).success(function (data) {
                toastr.success("保存冻存盒成功！");
                _initStockInBoxesTable();
                vm.splittingBox = false;
            }).error(function (data) {
                // console.log(data);
                toastr.error(data.message)
            })
        }
        vm.saveBox = _fnSaveBox;
        //关闭
        vm.closeBox = function () {

            modalInstance = $uibModal.open({
                animation: true,
                templateUrl: 'app/bizs/stock-in/stock-in-close-splittingBox-modal.html',
                controller: 'CloseSplittingBoxController',
                controllerAs:'vm',
                size:'sm',
                resolve: {
                    items: function () {
                        return {
                            status :1
                        };
                    }
                }
            });
            modalInstance.result.then(function (flag) {
                if(flag){
                    _fnSaveBox();
                }
                vm.splittingBox = false;
                vm.saveStockInFlag = false;
            });
        };

        function openCalendar (date) {
            vm.datePickerOpenStatus[date] = true;
        }
    }
    // function RescindPutAwayModalController($uibModalInstance) {
    //     var vm = this;
    //     vm.ok = function () {
    //         $uibModalInstance.close(true);
    //     };
    //     vm.cancel = function () {
    //         $uibModalInstance.dismiss('cancel');
    //     };
    // }
})();
