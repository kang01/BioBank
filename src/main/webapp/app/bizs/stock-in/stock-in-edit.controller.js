/**
 * Created by zhuyu on 2017/3/14.
 */
(function() {
    'use strict';

    angular
        .module('bioBankApp')
        .controller('StockInEditController', StockInEditController)
        .controller('RescindPutAwayModalController', RescindPutAwayModalController);

    StockInEditController.$inject = ['$scope','blockUIConfig','EquipmentService','BioBankBlockUi','$state','SupportacksByAreaIdService', '$compile','toastr','hotRegisterer','DTOptionsBuilder','DTColumnBuilder','$uibModal','BioBankDataTable',
        'entity','AreasByEquipmentIdService','StockInBoxService','StockInBoxByCodeService','SplitedBoxService','ProjectSitesByProjectIdService',
        'SampleTypeService','SampleService','IncompleteBoxService','RescindPutAwayService','MasterData','ProjectService','SampleUserService','Principal','StockInInputService'];
    RescindPutAwayModalController.$inject = ['$uibModalInstance'];
    function StockInEditController($scope,blockUIConfig,EquipmentService,BioBankBlockUi,$state,SupportacksByAreaIdService,$compile,toastr,hotRegisterer,DTOptionsBuilder,DTColumnBuilder,$uibModal,BioBankDataTable,
                                  entity,AreasByEquipmentIdService,StockInBoxService,StockInBoxByCodeService,SplitedBoxService,ProjectSitesByProjectIdService,
                                  SampleTypeService,SampleService,IncompleteBoxService,RescindPutAwayService,MasterData,ProjectService,SampleUserService,Principal,StockInInputService) {
        var vm = this;
        vm.datePickerOpenStatus = {};
        vm.openCalendar = openCalendar; //时间
        vm.entity = entity;
        vm.stockInCode = vm.entity.stockInCode;
        vm.entityBoxes = {};
        vm.splittingBox = null;
        vm.splittedBoxes = {};
        vm.dtInstance = {};
        vm.dto = {};

        var modalInstance;
        vm.checked = false;
        vm.selectedDataFlag = false;
        vm.search = _fnSearch;
        vm.empty = _fnEmpty;
        vm.initStockInBoxesTable = _initStockInBoxesTable;
        vm.tableRender = function () {
          vm.dtInstance.rerender();
        };

        if(vm.entity.receiveDate){
            vm.entity.receiveDate = new Date(vm.entity.receiveDate);
        }else{
            vm.entity.receiveDate = new Date();
        }
        if(!vm.entity.receiveName){
            _fnQueryUser();
        }
        function _initStockInInfo() {
            _statusInit();
            _sampleTypeInit();
            _positionInit();
            _fnProjectInit();
            _fnUserInit();
        }
        _initStockInInfo();
        function _statusInit() {
            vm.statusConfig = {
                valueField:'value',
                labelField:'label',
                maxItems: 1,
                onChange:function(value){
                    if(!value){
                        delete vm.dto.status
                    }
                }
            };
            vm.statusOptions = MasterData.frozenBoxStatus;
            vm.statusOptions.push({value:"20022",label:"待分装"})
        }
        function _sampleTypeInit() {
            vm.noSampleClassFlag = false;
            //样本类型
            vm.sampleTypeConfig = {
                valueField:'id',
                labelField:'sampleTypeName',
                maxItems: 1,
                onChange:function (value) {
                    if(value){
                        vm.dto.sampleTypeCode = _.find(vm.sampleTypeOptions,{id:+value}).sampleTypeCode;
                        vm.dto.sampleTypeName = _.find(vm.sampleTypeOptions,{id:+value}).sampleTypeName;
                        if(vm.dto.sampleTypeCode == "99"){
                            vm.noSampleClassFlag = false;
                            vm.sampleClassOptions = [];
                            vm.dto.sampleClassificationId = "";
                            vm.dto.sampleClassificationName = "";
                            vm.dto.sampleClassificationCode = "";
                            $scope.$apply();
                        }else{
                            _fnQuerySampleClass(vm.entity.projectId,value);
                        }
                    }else{
                        vm.noSampleClassFlag = false;
                        vm.sampleClassOptions = [];
                        vm.dto.sampleTypeCode = "";
                        vm.dto.sampleTypeName = "";
                        vm.dto.sampleClassificationId = "";
                        vm.dto.sampleClassificationName = "";
                        vm.dto.sampleClassificationCode = "";
                        $scope.$apply();
                    }

                }
            };
            vm.sampleTypeOptions = [];
            SampleTypeService.querySampleType().success(function (data) {
                vm.sampleTypeOptions = _.orderBy(data,['sampleTypeName','asc']);
                _.remove(vm.sampleTypeOptions,{sampleTypeCode:"98"})
            });
            vm.sampleClassConfig = {
                valueField:'sampleClassificationId',
                labelField:'sampleClassificationName',
                maxItems: 1,
                onChange:function (value) {
                    if(value){
                        vm.dto.sampleClassificationName = _.find(vm.sampleClassOptions,{sampleClassificationId:+value}).sampleClassificationName;
                        vm.dto.sampleClassificationCode = _.find(vm.sampleClassOptions,{sampleClassificationId:+value}).sampleClassificationCode;
                    }else{
                        vm.dto.sampleClassificationId = "";
                        vm.dto.sampleClassificationName = "";
                        vm.dto.sampleClassificationCode = "";
                    }


                }
            };
            //样本分类
            function _fnQuerySampleClass(projectId,sampleTypeId) {
                SampleTypeService.queryProjectSampleClasses(projectId,sampleTypeId).success(function (data) {
                    vm.sampleClassOptions = data;
                    if(vm.sampleClassOptions.length){
                        vm.noSampleClassFlag = true;
                        vm.dto.sampleClassificationId = vm.sampleClassOptions[0].sampleClassificationId;
                        vm.dto.sampleClassificationName = vm.sampleClassOptions[0].sampleClassificationName;
                        vm.dto.sampleClassificationCode = vm.sampleClassOptions[0].sampleClassificationCode;
                    }else{
                        vm.noSampleClassFlag = false
                    }
                });
            }

        }
        function _positionInit() {
            var equipmentCode;
            var areaCode;
            var supportRackCode;
            //设备
            EquipmentService.query({},onEquipmentSuccess, onError);
            function onEquipmentSuccess(data) {
                vm.frozenBoxPlaceOptions = data;
            }
            //盒子位置
            vm.frozenBoxPlaceConfig = {
                valueField:'id',
                labelField:'equipmentCode',
                maxItems: 1,
                onChange:function (value) {
                    if(value){
                        vm.noClassFlag = false;
                        vm.noshelvesFlag = false;
                        equipmentCode = _.find(vm.frozenBoxPlaceOptions,{id:+value}).equipmentCode;
                        AreasByEquipmentIdService.query({id:value},onAreaSuccess, onError);
                    }else{
                        vm.noClassFlag = true;
                        vm.noshelvesFlag = true;
                        vm.frozenBoxAreaOptions = [];
                        vm.dto.areaId = "";
                        vm.frozenBoxShelfOptions = [];
                        vm.dto.shelvesId = "";
                        supportRackCode = "";
                        areaCode = "";
                        vm.dto.position = "";
                        $scope.$apply();
                    }

                }
            };
            function onAreaSuccess(data) {
                vm.frozenBoxAreaOptions = data;
                if(vm.frozenBoxAreaOptions.length){
                    vm.dto.areaId = vm.frozenBoxAreaOptions[0].id;
                    areaCode = vm.frozenBoxAreaOptions[0].areaCode;
                    SupportacksByAreaIdService.query({id:vm.dto.areaId},onShelfSuccess, onError);
                }
            }
            vm.frozenBoxAreaConfig = {
                valueField:'id',
                labelField:'areaCode',
                maxItems: 1,
                onChange:function (value) {
                    if(value){
                        vm.noshelvesFlag = false;
                        areaCode = _.find(vm.frozenBoxAreaOptions,{id:+value}).areaCode;
                        SupportacksByAreaIdService.query({id:value},onShelfSuccess, onError);
                    }else{
                        vm.noshelvesFlag = true;
                        vm.frozenBoxShelfOptions = [];
                        vm.dto.shelvesId = "";
                        supportRackCode = "";
                        areaCode = "";
                        vm.dto.position = equipmentCode;
                        $scope.$apply();
                    }

                }
            };
            //架子
            function onShelfSuccess(data) {
                vm.frozenBoxShelfOptions = data;
                vm.dto.shelvesId = vm.frozenBoxShelfOptions[0].id;
                supportRackCode = vm.frozenBoxShelfOptions[0].supportRackCode;
                vm.dto.position = equipmentCode + "." + areaCode+"." + supportRackCode
            }
            vm.frozenBoxShelfConfig = {
                valueField:'id',
                labelField:'supportRackCode',
                maxItems: 1,
                onChange:function (value) {
                    if(value){
                        supportRackCode = _.find(vm.frozenBoxShelfOptions,{id:+value}).supportRackCode;
                        vm.dto.position = equipmentCode + "." + areaCode+"." + supportRackCode
                    }else{
                        supportRackCode = "";
                        vm.dto.position = equipmentCode + "." + areaCode
                    }
                }
            };
        }
        function _fnProjectInit() {
            //项目
            ProjectService.query({},onProjectSuccess, onError);
            function onProjectSuccess(data) {
                vm.projectOptions = data;
                if(!vm.entity.projectId){
                    vm.entity.projectId = data[0].id;
                }
                vm.entity.projectCode = _.find(vm.projectOptions,{id:vm.entity.projectId}).projectCode;
                // ProjectSitesByProjectIdService.query({id:vm.entity.projectId},onProjectSitesSuccess,onError);
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
            // vm.projectSitesConfig = {
            //     valueField:'id',
            //     labelField:'projectSiteName',
            //     searchField:'projectSiteName',
            //     maxItems: 1,
            //     onChange:function (value) {
            //     }
            // };

            // function onProjectSitesSuccess(data) {
            //     vm.projectSitesOptions = data;
            //     vm.projectSitesOptions.push({id:"",projectSiteName:""});
            //     vm.entity.projectSiteId = "";
            // }
        }
        function _fnUserInit() {
            //接收人
            vm.receiverConfig = {
                valueField:'login',
                labelField:'userName',
                maxItems: 1

            };
            SampleUserService.query({},onReceiverSuccess, onError);
            function onReceiverSuccess(data) {
                vm.receiverOptions = data;
            }
        }
        function _fnQueryUser() {
            Principal.identity().then(function(account) {
                vm.account = account;
                vm.entity.receiveName = vm.account.login;
            });
        }

        var selected = {};
        function _fnSearch() {

            var searchObj = {};
            _.forEach(vm.dto, function(value, key) {
               if(vm.dto[key] != ""){
                   searchObj[key] = vm.dto[key];
               }
            });
            delete searchObj.position;
            delete searchObj.frozenBoxCode;
            delete searchObj.sampleTypeId;
            delete searchObj.sampleTypeName;
            delete searchObj.sampleClassificationId;
            delete searchObj.sampleClassificationName;
            delete searchObj.equipmentId;
            delete searchObj.areaId;
            delete searchObj.shelvesId;

            var stockInBoxList = angular.copy(vm.stockInBox);
            var stockInBoxArray;
            var dataArray = [];
            var boxArray = [];
            //待入库无带分装
            if(searchObj.status == '2002'){
                searchObj.isSplit = 0;
            }
            //待分装
            if(searchObj.status == '20022'){
                searchObj.isSplit = 1;
                searchObj.status = '2002';
            }
            stockInBoxArray = _.filter(stockInBoxList, searchObj);
            dataArray = _.filter(stockInBoxList, searchObj);
            //模糊搜索冻存盒编码
            if(vm.dto.frozenBoxCode){
                dataArray = [];
                for(var i = 0 ; i <  stockInBoxArray.length; i++){
                    if(stockInBoxArray[i].frozenBoxCode.indexOf(vm.dto.frozenBoxCode) >= 0){
                        dataArray.push(stockInBoxArray[i]);
                        boxArray.push(stockInBoxArray[i]);
                    }
                }
            }
            //模糊搜索位置
            if(vm.dto.position){
                if(vm.dto.frozenBoxCode){
                    dataArray = [];
                    for(var i = 0 ; i <  boxArray.length; i++){
                        if(boxArray[i].position.indexOf(vm.dto.position) >= 0){
                            dataArray.push(boxArray[i]);
                        }
                    }
                }else{
                    dataArray = [];
                    for(var i = 0 ; i <  stockInBoxArray.length; i++){
                        if(stockInBoxArray[i].position.indexOf(vm.dto.position) >= 0){
                            dataArray.push(stockInBoxArray[i]);
                        }
                    }
                }

            }

            dataArray = _.uniq(dataArray);

            vm.dtOptions.withOption("data",dataArray).withOption('serverSide',false);
            vm.checked = false;
            vm.selectAll = false;
            selected = {};
            _.forEach(dataArray,function (full) {
                selected[full.frozenBoxCode] = false;
            });
            vm.selectedDataFlag = true;
            // vm.stockInBox = dataArray;
            _isStockInFinish();
        }
        function _fnEmpty() {
            vm.dto = {};
            vm.dto.sampleTypeName = "";
            vm.dto.sampleTypeCode = "";
            vm.dto.sampleTypeClassName = "";
            vm.dto.sampleTypeClassCode = "";
            _initStockInBoxesTable();
        }

        _initStockInBoxesTable();

        function _initStockInBoxesTable(){
            vm.selectedStockInBoxes = {};
            vm.selected = {};
            vm.selectAll = false;

            // 处理盒子选中状态
            vm.toggleAll = function (selectAll, selectedItems) {
                if(vm.selectedDataFlag){
                    vm.selected = selected;
                }

                selectedItems = vm.selected;
                selectAll = vm.selectAll;
                for (var id in selectedItems) {
                    var status = _.find(vm.stockInBox,{frozenBoxCode:id}).status;
                    var isSplit = _.find(vm.stockInBox,{frozenBoxCode:id}).isSplit;
                   if(status == "2002"  && isSplit){
                       continue;
                   }
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



            vm.dtOptions = BioBankDataTable.buildDTOption("BASIC", 320, 10)
                // 设置Tool button
                .withButtons([
                    {
                        text: '<i class="fa fa-sign-in"></i> 批量上架',
                        className: 'btn btn-default btn-primary',
                        key: '1',
                        action: _fnActionPutInShelfButton
                    },
                    {
                        text: '<i class="fa fa-search"></i> 高级搜索',
                        className: 'btn btn-default btn-primary ml-5',
                        key: '1',
                        action: _fnActionSearchButton
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
                });


            // 表格中每个列的定义
            vm.dtColumns = _createColumns();

            // 分装按钮
            vm.splitIt = _splitABox;
            // 编辑
            vm.editBox = _editBox;
            // 上架按钮
            vm.putInShelf = _putInShelf;
            //撤销上架
            vm.rescindInShelf = _rescindInShelf;
        }

        function _fnServerData( sSource, aoData, fnCallback, oSettings ) {
            var data = {};
            for(var i=0; aoData && i<aoData.length; ++i){
                var oData = aoData[i];
                data[oData.name] = oData.value;
            }
            data["stockInCode"] = vm.stockInCode;
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
            $('td:eq(6)', row).html(isSplit ? '需要分装' : '');
            $('td:eq(7)', row).html(status);
            $compile(angular.element(row).contents())($scope);
        }
        function _fnActionButtonsRender(data, type, full, meta) {
            // console.log(vm.splitIt, vm.putInShelf);
            var buttonHtml = "";
            if (full.status == "2002"){
                if(full.sampleTypeCode != '99'){
                    buttonHtml +='<button type="button" class="btn btn-xs btn-error" ng-click="vm.editBox(\''+ full.id +'\')">' +
                        '   <i class="fa fa-edit"></i> 编辑 ' +
                        '</button>&nbsp;';
                }

                if (full.isSplit){
                    buttonHtml += '<button type="button" class="btn btn-xs btn-warning" ng-click="vm.splitIt(\''+ full.id +'\')">' +
                       '   <i class="fa fa-sitemap"></i> 分装' +
                        '</button>';
                } else {
                    buttonHtml += '<button type="button" class="btn btn-xs btn-error" ng-click="vm.putInShelf(\''+ full.frozenBoxCode +'\')">' +
                        '   <i class="fa fa-sign-in"></i> 上架' +
                        '</button>';
                }
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

        //高级搜索
        function _fnActionSearchButton() {
            vm.checked = true;
            $scope.$apply();
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
                        values: [
                            {value:0,label:"否"},
                            {value:1,label:"是"}
                        ]
                    },
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
                DTColumnBuilder.newColumn('isSplit').withOption("width", "100").withTitle('是否分装'),
                DTColumnBuilder.newColumn('status').withOption("width", "80").withTitle('状态'),
                DTColumnBuilder.newColumn("").withOption("width", "120").withTitle('操作').withOption('searchable',false).notSortable().renderWith(_fnActionButtonsRender),
                // DTColumnBuilder.newColumn('id').notVisible()
                // DTColumnBuilder.newColumn('sampleType').notVisible(),
                // DTColumnBuilder.newColumn('frozenBoxRows').notVisible(),
                // DTColumnBuilder.newColumn('frozenBoxColumns').notVisible()
            ];

            return columns;
        }

        // 冻存盒号是否可以编辑，编辑盒子时，无法编辑，新增盒子，可以编辑
        vm.editFlag = false;
        //编辑
        function _editBox(stockInBoxId) {
            vm.box = {};
            vm.splittingBox = false;
            StockInInputService.queryEditStockInBox(stockInBoxId).success(function (data) {
                vm.box = data;
                vm.editFlag = true;
                vm.showFlag = true;
                vm.dtInstance.rerender();
                setTimeout(function () {
                    document.body.scrollTop = document.body.scrollHeight  - window.innerHeight - 30;
                },300);
            }).error(function (data) {
                toastr.error(data.message);
            });
        }
        //分装
        function _splitABox(stockInBoxId){
            _fnQuerySampleType();
            _fnTubeByBoxCode(stockInBoxId);
            //重置
            //编辑时，盒子信息的可以编辑
            vm.editFlag = false;
            //关闭编辑的区域
            vm.showFlag = false;
            //选中的管子数量清零
            // vm.selectedSampleLen = 0;
            //选中的冻存盒清除
            vm.frozenBoxCode = "";

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
                backdrop:'static',
                // size:'lg',
                size:'90',
                resolve: {
                    items: function () {
                        return {
                            stockInCode: vm.stockInCode,
                            boxIds: boxIds,
                            boxes: boxes
                        };
                    }
                }
            });
            modalInstance.result.then(function (data) {
                _fnEmpty();
                vm.dtOptions.isHeaderCompiled = false;
                _fnSearch();
                vm.showFlag = false;
                vm.splittingBox = false;
                vm.dtInstance.rerender();
            });
        }
        //撤销上架
        function _rescindInShelf(boxCode) {
            modalInstance = $uibModal.open({
                animation: true,
                templateUrl: 'app/bizs/stock-in/rescind-putaway-modal.html',
                controller: 'RescindPutAwayModalController',
                controllerAs:'vm',
                backdrop:'static'
            });
            modalInstance.result.then(function (data) {
                RescindPutAwayService.rescindPutAway(vm.entity.stockInCode,boxCode).then(function (data) {
                    vm.dtOptions.isHeaderCompiled = false;
                    vm.dtInstance.rerender();
                });

            });

        }
        //全部上架可以入库完成
        function _isStockInFinish() {
            //已上架
            var putInLen = _.filter(vm.stockInBox,{"status":"2006"}).length;
            if(putInLen){
                vm.stockInFlag = true;
            }else{
                vm.stockInFlag = false;
            }
        }


        //入库完成
        vm.saveStockIn = function () {
            modalInstance = $uibModal.open({
                animation: true,
                templateUrl: 'app/bizs/stock-in/modal/stock-in-info-modal.html',
                controller: 'StockInInfoModalController',
                controllerAs:'vm',
                backdrop:'static',
                resolve: {
                    items: function () {
                        return {
                            id: vm.entity.id,
                            stockInCode: vm.entity.stockInCode,
                            stockInDate: vm.entity.stockInDate,
                            storeKeeper1: vm.entity.storeKeeper1,
                            storeKeeper2: vm.entity.storeKeeper2,
                            stockInBox:vm.stockInBox
                    };
                    }
                }
            });
            modalInstance.result.then(function (data) {
                $state.go('stock-in');
            });
        };
        vm.isShowSplittingPanel = function(){
            return vm.splittingBox && true;
        };


        vm.frozenTubeArray = [];//初始管子的单元格
        vm.incompleteBoxesList = []; //分装后的样本类型盒子，未装满样本的盒子
        var tempTubeArray = [];//选中未满样本盒子的临时数据，需要操作管子
        var selectList = [];//选择单元格的管子数据
        var size = 10;
        var htm;
        //根据盒子编码取管子
        function _fnTubeByBoxCode(stockInBoxId) {
            blockUIConfig.autoBlock = false;
            StockInInputService.queryEditStockInBox(stockInBoxId).success(function (data) {
                blockUIConfig.autoBlock = true;
                vm.tableRender();
                vm.box =  data;
                if(!vm.box.frozenTubeDTOS.length){
                    vm.splittingBox = false;
                }else{
                    vm.splittingBox = true;
                }

                //获取样本分类
                _fnQueryProjectSampleClass(vm.entity.projectId,vm.box.sampleType.id,vm.box.sampleType.isMixed);
                var settings = {
                    minCols: +vm.box.frozenBoxType.frozenBoxTypeColumns,
                    minRows: +vm.box.frozenBoxType.frozenBoxTypeRows
                };
                var tubesInTable = [];
                var colHeaders = [];
                var rowHeaders = [];
                for(var i = 0; i < settings.minRows; i++){
                    var pos = {tubeRows: String.fromCharCode('A'.charCodeAt(0) + i), tubeColumns: 1 + ""};
                    if(i > 7){
                        pos.tubeRows = String.fromCharCode('A'.charCodeAt(0) + i+1);
                    }
                    rowHeaders.push(pos.tubeRows);
                    var tubes = [];
                    for(var j = 0; j < settings.minCols;j++){
                        pos.tubeColumns = j + 1 + "";
                        if (colHeaders.length < settings.minCols){
                            colHeaders.push(pos.tubeColumns);
                        }
                        var tubeInBox = _.filter(vm.box.frozenTubeDTOS, pos)[0];
                        var tube = _createTubeForTableCell(tubeInBox, vm.box, pos);
                        tubes.push(tube);
                    }
                    tubesInTable.push(tubes);
                }
                vm.frozenTubeArray = tubesInTable;
                settings.rowHeaders = rowHeaders;
                settings.colHeaders = colHeaders;
                setTimeout(function () {
                    hotRegisterer.getInstance('my-handsontable').updateSettings(settings);
                    hotRegisterer.getInstance('my-handsontable').loadData(tubesInTable);
                    hotRegisterer.getInstance('my-handsontable').render();
                },500);
                //取未满盒子
                _fnIncompleteBox();
            });
            // function onFrozenSuccess(data) {
            //     vm.box =  data;
            //     if(!vm.box.frozenTubeDTOS.length){
            //         vm.splittingBox = false;
            //     }else{
            //         vm.splittingBox = true;
            //     }
            //
            //     //获取样本分类
            //     _fnQueryProjectSampleClass(vm.entity.projectId,vm.box.sampleType.id,vm.box.sampleType.isMixed);
            //     var settings = {
            //         minCols: +vm.box.frozenBoxType.frozenBoxTypeColumns,
            //         minRows: +vm.box.frozenBoxType.frozenBoxTypeRows
            //     };
            //     var tubesInTable = [];
            //     var colHeaders = [];
            //     var rowHeaders = [];
            //     for(var i = 0; i < settings.minRows; i++){
            //         var pos = {tubeRows: String.fromCharCode('A'.charCodeAt(0) + i), tubeColumns: 1 + ""};
            //         if(i > 7){
            //             pos.tubeRows = String.fromCharCode('A'.charCodeAt(0) + i+1);
            //         }
            //         rowHeaders.push(pos.tubeRows);
            //         var tubes = [];
            //         for(var j = 0; j < settings.minCols;j++){
            //             pos.tubeColumns = j + 1 + "";
            //             if (colHeaders.length < settings.minCols){
            //                 colHeaders.push(pos.tubeColumns);
            //             }
            //             var tubeInBox = _.filter(vm.box.frozenTubeDTOS, pos)[0];
            //             var tube = _createTubeForTableCell(tubeInBox, vm.box, pos);
            //             tubes.push(tube);
            //         }
            //         tubesInTable.push(tubes);
            //     }
            //     vm.frozenTubeArray = tubesInTable;
            //     settings.rowHeaders = rowHeaders;
            //     settings.colHeaders = colHeaders;
            //     setTimeout(function () {
            //         hotRegisterer.getInstance('my-handsontable').updateSettings(settings);
            //         hotRegisterer.getInstance('my-handsontable').loadData(tubesInTable);
            //         hotRegisterer.getInstance('my-handsontable').render();
            //     },500);
            //     //取未满盒子
            //     _fnIncompleteBox();
            // }

        }
        var customRenderer = function (hotInstance, td, row, col, prop, value, cellProperties) {
            if(value){
                if(value.memo && value.memo != " "){
                    cellProperties.comment = value.memo;
                }
                if(value.sampleCode == null){
                    value.sampleCode = "";
                }
                //样本类型
                if(value.sampleClassificationId){
                    SampleService.changeSampleType(value.sampleClassificationId,td,vm.projectSampleTypeOptions,1);
                }else{
                    if(vm.sampleTypeOptions){
                        SampleService.changeSampleType(value.sampleTypeId,td,vm.sampleTypeOptions,2);
                    }
                }
                //样本状态 status3001：正常，3002：空管，3003：空孔；3004：异常
                if(value.status){
                    changeSampleStatus(value.status,row,col,td,cellProperties);
                }
                // htm = "<div ng-if='value.sampleCode' style='line-height: 20px'>"+value.sampleCode+"</div>"+
                //     "<div ng-if='value.sampleTmpCode && !value.sampleCode' style='line-height: 20px'>"+value.sampleTempCode+"</div>";
                var code = value.sampleCode && value.sampleCode != " " ? value.sampleCode : value.sampleTempCode;
                $(td).html("");
                var $div = $("<div/>").html(code).css({
                    'line-height': '20px',
                    'word-wrap': 'break-word'
                }).appendTo(td);
                // $div = $("<div id='microtubesStatus'/>").html(value.status).hide().appendTo(td);
                if(value.selectedAll){
                    if(value.sampleCode || value.sampleTempCode) {
                        selectList.push(value);
                    }
                    $('<div class="temp selected-sample-color"/>').appendTo(td);
                }
            }else {
                $(td).html("");
            }
            td.style.position = 'relative';


            // td.innerHTML = htm;
        };
        var operateColor;
        var selectedTubesArray = [];
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
        vm.settings ={
            colHeaders : ['1','2','3','4','5','6','7','8','9','10'],
            rowHeaders : ['A','B','C','D','E','F','G','H','J','K'],
            minRows: 10,
            minCols: 10,
            renderer:customRenderer,
            fillHandle:false,
            stretchH: 'all',
            wordWrap:true,
            colWidths: 90,
            columnHeaderHeight:35,
            editor: false,
            outsideClickDeselects:true,
            multiSelect: true,
            comments: true,
            onAfterSelectionEnd:function (row, col, row2, col2) {
                //去除全选
                for(var i = 0; i < vm.frozenTubeArray.length; i++){
                    for(var j = 0; j < vm.frozenTubeArray[i].length;j++){
                        vm.frozenTubeArray[i][j].selectedAll = false;
                    }
                }
                hotRegisterer.getInstance('my-handsontable').render();
                var cell = this;
                // vm.selectCell = $(this.getData(row,col,row2,col2));
                selectedTubesArray = this.getData(row,col,row2,col2);
                var selectTubeArrayIndex = this.getSelected();
                if(window.event && window.event.ctrlKey){
                    _fnSelectTubesData(cell,selectedTubesArray,selectTubeArrayIndex);
                }else{
                    //备注
                    $(".temp").remove();
                    selectList = [];
                    _fnSelectTubesData(cell,selectedTubesArray,selectTubeArrayIndex);
                }

            },
            enterMoves:function () {
                var hotMoves = hotRegisterer.getInstance('my-handsontable');
                var selectedCol = hotMoves.getSelected()[1];
                if(selectedCol + 1 < hotMoves.countCols()){
                    return{row:0,col:1};
                } else{
                    return{row:1,col:-selectedCol};
                }
            },
            cells: function (row, col, prop) {
                var cellProperties = {};
            }
        };
        //选择单元格数据
        function _fnSelectTubesData(td,selectedTubesArray,selectTubeArrayIndex) {
            var txt = '<div class="temp selected-sample-color"></div>';
            for(var m = 0; m < selectedTubesArray.length; m++){
                for (var n = 0; n < selectedTubesArray[m].length; n++){
                    if(selectedTubesArray[m][n].sampleCode || selectedTubesArray[m][n].sampleTempCode) {
                        selectList.push(selectedTubesArray[m][n]);
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
                for(var j = start2;  j <= end2;j++) {
                    if($(td.getCell(i,j))[0].childElementCount !=3){
                        $(td.getCell(i,j)).append(txt);
                    }
                }
            }

        }
        // 创建一个对象用于管子Table的控件
        function _createTubeForTableCell(tubeInBox, box, pos){
            var tube = {
                id: null,
                sampleCode: "",
                sampleTempCode: "",
                sampleTypeId: box.sampleType.id,
                sampleTypeCode: box.sampleType.sampleTypeCode,
                frozenBoxId: box.id,
                frozenBoxCode: box.frozenBoxCode,
                status: "",
                memo: "",
                tubeRows: pos.tubeRows,
                tubeColumns: pos.tubeColumns
            };
            if(box.sampleClassification){
                tube.sampleClassificationId = box.sampleClassification.id;
                tube.sampleClassificationCode = box.sampleClassification.sampleClassificationCode;
            }
            if (tubeInBox){
                tube.id = tubeInBox.id;
                tube.sampleCode = tubeInBox.sampleCode;
                tube.sampleTempCode = tubeInBox.sampleTempCode;
                tube.sampleTypeId = tubeInBox.sampleType.id;
                tube.status = tubeInBox.status;
                tube.memo = tubeInBox.memo;
                if(tubeInBox.sampleClassification){
                    tube.sampleClassificationId = tubeInBox.sampleClassification.id;
                    tube.sampleClassificationCode = tubeInBox.sampleClassification.sampleClassificationCode;
                }
            }

            return tube;
        }
        //获取样本类型
        function _fnQuerySampleType() {
            SampleTypeService.querySampleType().success(function (data) {
                vm.sampleTypeOptions = _.orderBy(data, ['sampleTypeId'], ['esc']);
            });
        }
        //不同项目下的样本分类
        function _fnQueryProjectSampleClass(projectId,sampleTypeId,isMixed) {
            SampleTypeService.queryProjectSampleClasses(projectId,sampleTypeId).success(function (data) {
                vm.projectSampleTypeOptions = data;
                if(isMixed == 1){
                    for(var k = 0; k < data.length; k++){
                        for (var i = 0; i < vm.frozenTubeArray.length; i++) {
                            for (var j = 0; j < vm.frozenTubeArray[i].length; j++) {
                                if(data[k].columnsNumber == j+1){
                                    vm.frozenTubeArray[i][j].sampleClassificationId = data[k].sampleClassificationId;
                                    vm.frozenTubeArray[i][j].sampleTypeId = sampleTypeId;
                                }
                            }
                        }
                        for(var m = 0; m < vm.box.frozenTubeDTOS.length; m++){
                            if(vm.box.frozenTubeDTOS[m].tubeColumns == data[k].columnsNumber){
                                vm.box.frozenTubeDTOS[m].sampleClassification.id = data[k].sampleClassificationId;
                            }
                        }
                    }

                    vm.box.sampleClassificationId = "";
                }else{
                    if(vm.box.sampleClassification){
                        vm.box.sampleClassificationId = vm.projectSampleTypeOptions[0].sampleClassificationId;
                    }
                    for (var i = 0; i < vm.frozenTubeArray.length; i++) {
                        for (var j = 0; j < vm.frozenTubeArray[i].length; j++) {
                            if(vm.box.sampleClassification){
                                vm.frozenTubeArray[i][j].sampleClassificationId = vm.box.sampleClassification.id;
                            }
                            vm.frozenTubeArray[i][j].sampleTypeId = sampleTypeId;
                        }
                    }
                    for(var m = 0; m < vm.box.frozenTubeDTOS.length; m++){
                        if(vm.box.sampleClassification){
                            vm.box.frozenTubeDTOS[m].sampleClassification.id = vm.box.sampleClassification.id;
                        }
                    }
                }

                hotRegisterer.getInstance('my-handsontable').render();
            });
        }
        //取未装满的盒子
        function _fnIncompleteBox() {
            vm.incompleteBoxesList = [];
            IncompleteBoxService.query({frozenBoxCode:vm.box.frozenBoxCode,stockInCode:vm.entity.stockInCode},onIncompleteBoxesSuccess,onError);
        }

        function onIncompleteBoxesSuccess(data) {
            BioBankBlockUi.blockUiStop();
            if(data.length) {
                for (var i = 0; i < data.length; i++) {
                    _.forEach(data[i].stockInFrozenTubeList, function (tube) {
                        tube.frozenTubeId = tube.id;
                        tube.id = ""
                    });
                    var boxList = [];
                    //盒子编码太长时，用星号代替
                    if (data[i].frozenBoxCode.length > 10) {
                        data[i].copyBoxCode = _fnReplaceBoxCode(data[i].frozenBoxCode);
                    } else {
                        data[i].copyBoxCode = data[i].frozenBoxCode;
                    }
                    //增加多少样本数 +10
                    data[i].addTubeCount = 0;
                    if (data[i].sampleClassification) {
                        data[i].backColor = data[i].sampleClassification.backColor;
                        data[i].sampleTypeName = data[i].sampleClassification.sampleClassificationName;
                        vm.sampleTypeClassId = data[i].sampleClassification.sampleClassificationId || data[i].sampleClassification.id;
                        vm.sampleTypeClassCode = data[i].sampleClassification.sampleClassificationCode;
                    } else {
                        data[i].backColor = data[i].sampleType.backColor;
                        data[i].sampleTypeName = data[i].sampleType.sampleTypeName;
                    }

                    boxList.push(data[i]);


                    if (data[i].sampleClassification) {
                        vm.incompleteBoxesList.push({
                            sampleTypeId: data[i].sampleClassification.id || data[i].sampleClassification.sampleClassificationId,
                            sampleTypeCode: data[i].sampleClassification.sampleClassificationCode,
                            boxList: boxList,
                            tubesLen: data[i].stockInFrozenTubeList.length
                        });
                    } else {
                        vm.incompleteBoxesList.push({
                            sampleTypeId: data[i].sampleTypeId || data[i].sampleType.id,
                            sampleTypeCode: data[i].sampleTypeCode || data[i].sampleType.sampleTypeCode,
                            boxList: boxList,
                            tubesLen: data[i].stockInFrozenTubeList.length
                        });
                    }
                }
            }
            //返回多个盒子，取一个剩余量最多的盒子
            var array = angular.copy(vm.incompleteBoxesList);
            var obj = _.groupBy(array,'sampleTypeCode');
            vm.incompleteBoxesList = [];
            for(var code in obj){
                obj[code] = _.orderBy(obj[code],'tubesLen','asc');
                vm.incompleteBoxesList.push(obj[code][0])
            }
            setTimeout(function () {
                document.body.scrollTop = document.body.scrollHeight  - window.innerHeight;
            },300);
        }
        function onError(error) {
            toastr.error(error.data.message);
        }
        //盒子编码太长时，用星号代替
        function _fnReplaceBoxCode(code) {
            code = "***"+code.substring(code.length-10);
            return code;
        }
        //初始管子数
        function initFrozenTube(row,col) {
            for(var i = 0; i < +row; i++){
                vm.frozenTubeArray[i] = [];
                for(var j = 0;j < +col; j++){
                    vm.frozenTubeArray[i][j] = "";
                }
            }
        }
        function getTubeRowIndex(row) {
            if(row.charCodeAt(0) -65 > 7){
                return row.charCodeAt(0) -66;
            }else {
                return row.charCodeAt(0) -65;

            }
        }
        function getTubeColumnIndex(col) {
            return +col -1;
        }
        vm.boxList = [];

        //选中分装的冻存盒
        vm.obox = {
            stockInFrozenTubeList:[]
        };
        //选中分装盒内的样本数
        var tubeList = [];
        //已有样本
        var existedTubeList = [];

        //选中要分装样本盒
        vm.sampleBoxSelect = function (item,$event) {
            tubeList = [];
            // existedTubeList = [];
            vm.frozenBoxCode = item.frozenBoxCode;
            if(item.sampleClassification){
                vm.sampleTypeClassId = item.sampleClassification.id || item.sampleClassification.sampleClassificationId;
                vm.sampleTypeClassCode = item.sampleClassification.sampleClassificationCode;
            }
            vm.problemSamplyTypeCode = item.sampleType.sampleTypeCode;
            if(vm.frozenBoxCode){
                tubeList = item.stockInFrozenTubeList;
                // existedTubeList = _.concat(existedTubeList, item.stockInFrozenTubeList);
            }
            vm.obox = angular.copy(item);
            $($event.target).closest('ul').find('.box-selected').removeClass("box-selected");
            $($event.target).addClass("box-selected");
        };
        //分装操作
        vm.splitBox = function () {
            var rowCount = +vm.box.frozenBoxType.frozenBoxTypeRows;
            var colCount = +vm.box.frozenBoxType.frozenBoxTypeColumns;
            vm.obox.stockInFrozenTubeList = [];
            //初始100个管子或者80个管子
            for(var i = 0; i < rowCount; i++){
                tempTubeArray[i] = [];
                var rowNO = i > 7 ? i+1 : i;
                rowNO = String.fromCharCode(rowNO+65);
                for(var j = 0;j < colCount; j++){
                    tempTubeArray[i][j] = {
                        tubeColumns: j+1,
                        tubeRows: rowNO,
                        frozenBoxCode:'',
                        selectTubeCode:vm.obox.frozenBoxCode
                    };
                    vm.obox.stockInFrozenTubeList.push(tempTubeArray[i][j]);
                }
            }
            //把已有管子放进去
            for(var m =0 ; m < vm.obox.stockInFrozenTubeList.length; m++){
                for(var n = 0; n < tubeList.length;n++){
                    var tube = tubeList[n];
                    if(vm.obox.stockInFrozenTubeList[m].tubeRows == tube.tubeRows && vm.obox.stockInFrozenTubeList[m].tubeColumns == +tube.tubeColumns){
                        vm.obox.stockInFrozenTubeList[m] = tube;
                    }
                }
            }
            if(!selectList.length || !vm.frozenBoxCode ){
                modalInstance = $uibModal.open({
                    animation: true,
                    templateUrl: 'app/bizs/stock-in/stock-in-splittingBox-message-modal.html',
                    controller: 'SplittingBoxMessageController',
                    controllerAs:'vm',
                    backdrop:'static'
                });
                return;
            }
            //总管子数
            var tubeCount = colCount*rowCount;
            //剩余管子数
            var surplusCount =  tubeCount - vm.obox.countOfSample;
            //选中的被分装的管子数
            var selectCount = selectList.length;
            //分装到哪个盒子中的数量
            if( selectCount <= surplusCount){
                vm.obox.addTubeCount  += selectCount;
            }else{
                if(surplusCount != 0){
                    vm.obox.addTubeCount  = surplusCount;
                }
            }
            for(var i = 0; i < vm.incompleteBoxesList.length; i++){
                var incompleteBoxes = vm.incompleteBoxesList[i];
                for(var j = 0; j < incompleteBoxes.boxList.length;j++ ){
                    if(vm.obox.frozenBoxCode == incompleteBoxes.boxList[j].frozenBoxCode){
                        incompleteBoxes.boxList[j].addTubeCount = vm.obox.addTubeCount;
                    }
                }
            }
            //去除全选
            for(var i = 0; i < vm.frozenTubeArray.length; i++){
                for(var j = 0; j < vm.frozenTubeArray[i].length;j++){
                    vm.frozenTubeArray[i][j].selectedAll = false;
                }
            }
            //清空被分装的盒子数
            for(var k = 0; k < selectList.length; k++){
                vm.frozenTubeArray[getTubeRowIndex(selectList[k].tubeRows)][getTubeColumnIndex(selectList[k].tubeColumns)] = "";
            }

            hotRegisterer.getInstance('my-handsontable').render();
            //分装数据
            for(var i = 0; i < vm.obox.stockInFrozenTubeList.length; i++){
                if(!vm.obox.stockInFrozenTubeList[i].frozenBoxCode){
                    // 当盒子中管子剩余数为0时，自动添加第二个盒子
                    if(surplusCount){
                        if(selectList.length){
                            vm.obox.stockInFrozenTubeList[i].frozenBoxCode = vm.obox.stockInFrozenTubeList[i].selectTubeCode;
                            vm.obox.stockInFrozenTubeList[i].id = selectList[0].id;
                            delete vm.obox.stockInFrozenTubeList[i].selectTubeCode;
                            selectList.splice(0,1);
                        }else{
                            break;
                        }
                    }

                }
            }
            //删除空管子
            // var deleteIndexList = [];
            // for(var i = 0; i < vm.obox.stockInFrozenTubeList.length; i++){
            //     if(!vm.obox.stockInFrozenTubeList[i].frozenBoxCode){
            //         deleteIndexList.push(i);
            //     }
            // }
            // _.pullAt(vm.obox.stockInFrozenTubeList, deleteIndexList);
            _.remove(vm.obox.stockInFrozenTubeList,{"frozenBoxCode":""});
            vm.obox.countOfSample = vm.obox.stockInFrozenTubeList.length;
            tubeList = angular.copy(vm.obox.stockInFrozenTubeList);
            if(vm.obox.countOfSample == tubeCount && selectList.length){
                var frozenBox = {};
                frozenBox.frozenBoxTypeId= vm.obox.frozenBoxType.id;
                frozenBox.sampleTypeId= vm.obox.sampleType.id;
                frozenBox.sampleType= vm.obox.sampleType;
                if(vm.obox.sampleClassification || vm.obox.sampleClassificationId){
                    frozenBox.sampleClassificationId = vm.obox.sampleClassificationId || vm.obox.sampleClassification.id;
                }
                frozenBox.stockInFrozenTubeList= selectList;

                vm.addBoxModal(frozenBox,"1");
            }
            var obox = angular.copy(vm.obox);

            // if(!vm.boxList.length){
            vm.boxList.push(obox);
            // }else{
            //     for(var i = 0; i < vm.boxList.length; i++){
            //         if(obox.sampleClassification.id == vm.boxList[i].sampleClassification.id){
            //             if(vm.boxList.length > 1){
            //                 vm.boxList[1].stockInFrozenTubeList = obox.stockInFrozenTubeList;
            //
            //             }else{
            //                 vm.boxList[0].stockInFrozenTubeList = obox.stockInFrozenTubeList;
            //             }
            //             break;
            //         }else{
            //             vm.boxList.push(obox);
            //             break;
            //         }
            //     }
            // }
        };
        //保存分装结果
        var saveBoxList = [];
        vm.saveBox = function () {
            saveBoxList = [];
            for(var i = 0; i < vm.boxList.length; i++){
                var objBox = {};
                objBox.frozenBoxId = vm.boxList[i].frozenBoxId;
                objBox.frozenBoxCode = vm.boxList[i].frozenBoxCode;
                objBox.sampleTypeId = vm.boxList[i].sampleTypeId || vm.boxList[i].sampleType.id;
                objBox.frozenBoxTypeId = vm.boxList[i].frozenBoxTypeId || vm.boxList[i].frozenBoxType.id;
                objBox.memo = vm.boxList[i].memo;
                objBox.stockInFrozenTubeList = vm.boxList[i].stockInFrozenTubeList;
                if(vm.boxList[i].sampleClassification){
                    objBox.sampleClassificationId = vm.boxList[i].sampleClassification.sampleClassificationId || vm.boxList[i].sampleClassification.id;
                }
                saveBoxList.push(objBox);
            }
            // console.log(JSON.stringify(saveBoxList));
            BioBankBlockUi.blockUiStart();
            SplitedBoxService.saveSplit(vm.stockInCode,vm.box.frozenBoxCode,saveBoxList).success(function (data) {
                toastr.success("分装成功!");
                vm.dtOptions.isHeaderCompiled = false;
                vm.dtInstance.rerender();
                _splitABox(vm.box.id);
                vm.boxList = [];
                vm.frozenBoxCode = "";
                $(".box-selected").removeClass("box-selected");
            }).error(function (data) {
                vm.recover();
                BioBankBlockUi.blockUiStop();
                toastr.error(data.message);
            });
        };
        //复原
        vm.recover = function () {
            _fnTubeByBoxCode(vm.box.id);
            vm.boxList = [];
            tubeList = [];
            selectList = [];
            saveBoxList = [];
            vm.frozenBoxCode = "";

            $(".box-selected").removeClass("box-selected");
        };
        //关闭
        vm.closeBox = function () {

            modalInstance = $uibModal.open({
                animation: true,
                templateUrl: 'app/bizs/stock-in/stock-in-close-splittingBox-modal.html',
                controller: 'CloseSplittingBoxController',
                controllerAs:'vm',
                backdrop:'static',
                size:'sm',
                resolve: {
                    items: function () {
                        return {
                            status :2
                        };
                    }
                }
            });
            modalInstance.result.then(function (flag) {
                if(flag) {
                    SplitedBoxService.saveSplit(vm.stockInCode, vm.box.frozenBoxCode, vm.boxList).then(function (data) {

                        toastr.success("保存成功!");
                    });
                }
                vm.tableRender();
                vm.splittingBox = false;
            },function () {
                vm.tableRender();
            });
        };
        //全选
        vm.selectSampleAll = function () {
            for(var i = 0; i < vm.frozenTubeArray.length; i++){
                for(var j = 0; j < vm.frozenTubeArray[i].length;j++){
                    vm.frozenTubeArray[i][j].selectedAll = true;
                }
            }
            hotRegisterer.getInstance('my-handsontable').render();

        };
        //添加分装样本盒 1:第二个新盒子 2.新添第一个盒子
        vm.addBoxModal = function (box,status) {
            if(!box){
                box = {};
                box.sampleTypeId = vm.box.sampleType.id;
                box.sampleTypeCode = vm.box.sampleType.sampleTypeCode;
                // box.isMixed = vm.box.sampleType.isMixed;
                box.frozenBoxTypeId = vm.box.frozenBoxType.id;
                box.stockInFrozenTubeList = [];
                if(box.sampleTypeCode == "99"){
                    if(vm.projectSampleTypeOptions.length){
                        vm.sampleTypeClassId =  vm.projectSampleTypeOptions[0].sampleClassificationId;
                        vm.sampleTypeClassCode =  vm.projectSampleTypeOptions[0].sampleClassificationCode;
                    }
                }
            }
            modalInstance = $uibModal.open({
                animation: true,
                templateUrl: 'app/bizs/stock-in/add-box-modal.html',
                controller: 'AddBoxModalController',
                controllerAs:'vm',
                size:'lg',
                backdrop:'static',
                resolve: {
                    items: function () {
                        return {
                            projectId:vm.entity.projectId,
                            box :box || {stockInFrozenTubeList:[]},
                            incompleteBoxes: vm.incompleteBoxesList,
                            isMixed:vm.box.sampleType.isMixed,
                            sampleTypeId:box.sampleTypeId,
                            sampleTypeCode:box.sampleTypeCode,
                            sampleTypeClassId:vm.sampleTypeClassId || vm.box.sampleClassificationId,
                            sampleTypeClassCode:vm.sampleTypeClassCode || vm.box.sampleClassificationId,
                            frozenBoxTypeId:box.frozenBoxTypeId,
                            frozenBoxType:vm.box.frozenBoxType,
                            status:status || "2"
                        };
                    }
                }
            });

            modalInstance.result.then(function (data) {
                if(data){
                    // selectList = [];
                    //添加分装后的冻存盒，没有添加新的，有的话再添加相同的盒子，相同的最多添加2个
                    var index;
                    if(data.sampleClassificationId){
                        index = _.findIndex(vm.incompleteBoxesList,{sampleTypeId:+data.sampleClassificationId});
                    }else{
                        index = _.findIndex(vm.incompleteBoxesList,{sampleTypeId:data.sampleTypeId});
                    }
                    if(index == -1){
                        var boxTempList  = [];
                        boxTempList.push(data);
                        onIncompleteBoxesSuccess(boxTempList);
                    }else{
                        //盒子编码太长时，用星号代替
                        if(data.frozenBoxCode.length > 10){
                            data.copyBoxCode = _fnReplaceBoxCode(data.frozenBoxCode);
                        }else{
                            data.copyBoxCode = data.frozenBoxCode;
                        }
                        data.addTubeCount = data.countOfSample;
                        data.countOfSample = 0;
                        //无样本分类情况
                        if(data.sampleClassification){
                            data.backColor = data.sampleClassification.backColor;
                            data.sampleClassificationName = data.sampleClassification.sampleClassificationName;
                            data.sampleClassificationId = data.sampleClassification.sampleClassificationId;
                            for(var i = 0; i < vm.incompleteBoxesList.length; i++){
                                if(vm.incompleteBoxesList[i].sampleTypeId == data.sampleClassificationId){
                                    if(vm.incompleteBoxesList[i].boxList.length < 2 ){
                                        vm.incompleteBoxesList[i].boxList.push(data);
                                        vm.boxList.push(data);
                                    }

                                }
                            }
                        }else{
                            data.backColor = data.sampleType.backColor;
                            data.sampleTypeName = data.sampleType.sampleTypeName;
                            data.sampleTypeId = data.sampleType.id;
                            for(var i = 0; i < vm.incompleteBoxesList.length; i++){
                                if(vm.incompleteBoxesList[i].sampleTypeId == data.sampleTypeId){
                                    if(vm.incompleteBoxesList[i].boxList.length < 2 ){
                                        vm.incompleteBoxesList[i].boxList.push(data);
                                        vm.boxList.push(data);
                                    }

                                }
                            }
                        }

                        tubeList = [];
                        vm.frozenBoxCode = "";
                        $(".box-selected").removeClass("box-selected");
                    }
                }else{

                }

            },function (data) {
                //复原被分装的剩余管子数
                for(var k = 0; k < selectList.length; k++){
                    var rowIndex = getTubeRowIndex(selectList[k].tubeRows);
                    var colIndex = getTubeColumnIndex(selectList[k].tubeColumns);
                    vm.frozenTubeArray[rowIndex][colIndex] = selectList[k];
                }
                // $timeout(function(){
                hotRegisterer.getInstance('my-handsontable').render();
                // },500);
            });
        };
        function openCalendar (date) {
            vm.datePickerOpenStatus[date] = true;
        }
    }
    function RescindPutAwayModalController($uibModalInstance) {
        var vm = this;
        vm.ok = function () {
            $uibModalInstance.close(true);
        };
        vm.cancel = function () {
            $uibModalInstance.dismiss('cancel');
        };
    }
})();
