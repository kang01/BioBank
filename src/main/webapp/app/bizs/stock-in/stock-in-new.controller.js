/**
 * Created by gaokangkang on 2017/6/19.
 */
(function() {
    'use strict';

    angular
        .module('bioBankApp')
        .controller('StockInNewController', StockInNewController)
        .controller('StockInNewModalController', StockInNewModalController)
        .controller('StockInCancellationModalController', StockInCancellationModalController);

    StockInNewController.$inject = ['$timeout','BioBankBlockUi','$state','$stateParams', '$scope','$compile','toastr','hotRegisterer','DTOptionsBuilder','DTColumnBuilder','$uibModal','BioBankDataTable',
        'entity','StockInService','StockInBoxService','StockInBoxByCodeService','StockInInputService','ProjectService','ProjectSitesByProjectIdService',
        'SampleTypeService','SampleService','FrozenBoxTypesService','RescindPutAwayService','MasterData'];
    StockInNewModalController.$inject = ['$uibModalInstance'];
    StockInCancellationModalController.$inject = ['$uibModalInstance'];
    function StockInNewController($timeout,BioBankBlockUi,$state,$stateParams,$scope,$compile,toastr,hotRegisterer,DTOptionsBuilder,DTColumnBuilder,$uibModal,BioBankDataTable,
                                   entity,StockInService,StockInBoxService,StockInBoxByCodeService,StockInInputService,ProjectService,ProjectSitesByProjectIdService,
                                   SampleTypeService,SampleService,FrozenBoxTypesService,RescindPutAwayService,MasterData) {
        var vm = this;
        vm.datePickerOpenStatus = {};
        vm.openCalendar = openCalendar; //时间
        vm.entity = entity;
        vm.stockInCode = vm.entity.stockInCode;
        vm.entityBoxes = {};
        vm.showFlag = null;
        vm.splittedBoxes = {};
        vm.dtInstance = {};
        vm.box = {
            frozenTubeDTOS:[]
        };
        var modalInstance;
        //保存入库记录
        vm.stockInSave = _fnStockInSave;
        vm.initStockInBoxesTable = _initStockInBoxesTable;
        //作废
        vm.cancellation = _fnCancellation;
        //入库完成
        vm.stockInFinish = function () {
            modalInstance = $uibModal.open({
                animation: true,
                templateUrl: 'app/bizs/stock-in/modal/stock-in-info-modal.html',
                controller: 'StockInInfoModalController',
                controllerAs:'vm',
                resolve: {
                    items: function () {
                        return {
                            id: vm.entity.id,
                            stockInCode: vm.entity.stockInCode,
                            stockInBox:vm.stockInBox
                        };
                    }
                }
            });
            modalInstance.result.then(function (data) {
                $state.go('stock-in');
            });
        };
        //保存入库记录
        function _fnStockInSave() {
            vm.saveStockInFlag = false;
            _saveStockIn();
        }
        function _saveStockIn() {
            StockInInputService.saveStockIn(vm.entity).success(function (data) {
                vm.entity = data;
                if(!vm.saveStockInFlag){
                    toastr.success("保存入库信息成功!");
                }
            });
        }
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
                searchField:'projectName',
                maxItems: 1,
                onChange:function(value){
                    if(value){
                        ProjectSitesByProjectIdService.query({id:value},onProjectSitesSuccess,onError);
                    }else{
                        vm.entity.projectSiteId = "";
                        vm.projectSitesOptions = [
                            {id:"",projectSiteName:""}
                        ]
                    }


                }
            };
            //项目点
            vm.projectSitesConfig = {
                valueField:'id',
                labelField:'projectSiteName',
                searchField:'projectSiteName',
                maxItems: 1,
                onChange:function (value) {
                }
            };

            function onProjectSitesSuccess(data) {
                vm.projectSitesOptions = data;
                vm.projectSitesOptions.push({id:"",projectSiteName:""});
                vm.entity.projectSiteId = "";
                // if(!vm.entity.projectSiteId){
                //     if(data.length){
                //         vm.entity.projectSiteId = data[0].id;
                //     }
                // }
            }
        }

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

            vm.dtOptions = BioBankDataTable.buildDTOption("NORMALLY", null, 6)
            // 设置Tool button
                .withButtons([
                    {
                        text: '<i class="fa fa-sign-in "></i> 批量上架',
                        className: 'btn btn-default mr-5 btn-primary',
                        key: '1',
                        action: _fnActionPutInShelfButton
                    },
                    {
                        text: '<i class="fa fa-plus"></i> 添加冻存盒',
                        className: 'btn btn-default btn-primary',
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
                .withOption('headerCallback', function(header) {
                    $compile(angular.element(header).contents())($scope);
                })
                // 定义每个列过滤选项
                .withColumnFilter(_createColumnFilters());

            // 表格中每个列的定义
            vm.dtColumns = _createColumns();

            // 分装按钮
            // vm.splitIt = _splitABox;
            // 上架按钮
            vm.putInShelf = _putInShelf;
            // 编辑
            vm.editBox = _editBox;
            //撤销上架
            vm.rescindInShelf = _rescindInShelf;
        }
        //作废
        function _fnCancellation() {
            modalInstance = $uibModal.open({
                animation: true,
                templateUrl: 'stockInCancellation.html',
                controller: 'StockInCancellationModalController',
                controllerAs:'vm'
            });
            modalInstance.result.then(function (data) {
                StockInInputService.stockInCancellation(vm.entity.stockInCode).success(function (data) {
                    toastr.success("作废成功!");
                    $state.go('stock-in');
                }).error(function (data) {
                    toastr.error(data.message);
                })
            },function (data) {

            });
        }

        _initStockInInfo();
        _initStockInBoxesTable();

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
                vm.stockInBox = res.data.data;
                _isStockInFinish();
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
            // 冻存盒状态：2001：新建，2002：待入库，2003：已分装，2004：已入库，2090：已作废，2006：已上架，2008：待出库，2009：已出库
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
            //     case '2090': status = '已作废'; break;
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
                buttonHtml += '<button type="button" class="btn btn-xs btn-error" ng-click="vm.editBox(\''+ full.id +'\')">' +
                    '   <i class="fa fa-edit"></i> 编辑 ' +
                    '</button>&nbsp;'+
                    '<button type="button" class="btn btn-xs btn-error" ng-click="vm.putInShelf(\''+ full.frozenBoxCode +'\')">' +
                    '   <i class="fa fa-sign-in"></i> 上架' +
                    '</button>';
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
        //上架
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
        //撤销上架
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
        //有上架的就可以入库完成
        function _isStockInFinish() {
            //已上架
            var putInLen = _.filter(vm.stockInBox,{"status":"2006"}).length;
            if(putInLen){
                vm.stockInflag = true;
            }else{
                vm.stockInflag = false;
            }
        }

        //添加冻存盒
        function _fnActionAddBoxButton() {
            //显示冻存盒信息
            vm.showFlag = true;
            // 冻存盒号是否可以编辑，编辑盒子时，无法编辑，新增盒子，可以编辑
            vm.editFlag = false;
            //已入库
            if(vm.entity.status == '7002'){
                return;
            }
            if(!vm.entity.id){
                var modalInstance = $uibModal.open({
                    templateUrl: 'stockInNew.html',
                    controller: 'StockInNewModalController',
                    controllerAs: 'vm'
                });
                modalInstance.result.then(function () {
                    vm.saveStockInFlag = true;
                    _saveStockIn();
                }, function () {
                });
            }else{
                vm.saveStockInFlag = true;
                _saveStockIn();
                vm.box = {};

            }
        }
        // 冻存盒号是否可以编辑，编辑盒子时，无法编辑，新增盒子，可以编辑
        vm.editFlag = false;
        function _editBox(stockInBoxId) {
            StockInInputService.queryEditStockInBox(stockInBoxId).success(function (data) {
                vm.box = data;
                vm.editFlag = true;
                vm.showFlag = true;
            }).error(function (data) {
                toastr.error(data.message)
            });
        }

        function openCalendar (date) {
            vm.datePickerOpenStatus[date] = true;
        }

        function onError(error) {
            toastr.error(error.data.message);
        }
    }
    function StockInNewModalController($uibModalInstance) {
        var vm = this;
        vm.ok = function () {
            $uibModalInstance.close();
        };
        vm.cancel = function () {
            $uibModalInstance.dismiss('cancel');
        };
    }
    function StockInCancellationModalController($uibModalInstance) {
        var vm = this;





        vm.ok = function () {
            $uibModalInstance.close();
        };
        vm.cancel = function () {
            $uibModalInstance.dismiss('cancel');
        };
    }
})();
