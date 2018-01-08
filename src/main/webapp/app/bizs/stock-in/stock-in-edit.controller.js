/**
 * Created by zhuyu on 2017/3/14.
 */
(function() {
    'use strict';

    angular
        .module('bioBankApp')
        .controller('StockInEditController', StockInEditController)
        .controller('RescindPutAwayModalController', RescindPutAwayModalController);

    StockInEditController.$inject = ['$scope','blockUIConfig','EquipmentAllService','BioBankBlockUi','$state','SupportacksByAreaIdService', '$compile','toastr','hotRegisterer','DTOptionsBuilder','DTColumnBuilder','$uibModal','BioBankDataTable',
        'entity','AreasByEquipmentIdService','StockInBoxService','StockInBoxByCodeService','SplitedBoxService','ProjectSitesByProjectIdService',
        'SampleTypeService','SampleService','IncompleteBoxService','RescindPutAwayService','MasterData','ProjectService','SampleUserService','Principal','StockInInputService'];
    RescindPutAwayModalController.$inject = ['$uibModalInstance'];
    function StockInEditController($scope,blockUIConfig,EquipmentAllService,BioBankBlockUi,$state,SupportacksByAreaIdService,$compile,toastr,hotRegisterer,DTOptionsBuilder,DTColumnBuilder,$uibModal,BioBankDataTable,
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
        vm.frozenBox = {};
        var modalInstance;
        vm.checked = false;
        vm.selectedDataFlag = false;
        vm.search = _fnSearch;
        vm.empty = _fnEmpty;
        var _scrollTop;

        vm.initStockInBoxesTable = _initStockInBoxesTable;
        //编辑转到分装
        vm.editToSpiltTube = _fnEditToSpiltTube;
        //绘制盒子列表
        vm.tableRender = function () {
            _.forEach(vm.stockInBox, function(box) {
                if(vm.box){
                    if(box.id == vm.box.id){
                        box.countOfSample = vm.box.countOfSample;
                        box.frozenBoxCode1D = vm.box.frozenBoxCode1D;
                        box.isSplit = vm.box.isSplit;
                    }
                }

            });
            vm.dtOptions.withOption("data",vm.stockInBox);
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
            vm.statusOptions.push({value:"2002",label:"待分装"})
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
                _.remove(vm.sampleTypeOptions,{sampleTypeCode:"98"});
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
        //盒子位置
        function _positionInit() {
            var equipmentCode;
            var areaCode;
            var supportRackCode;
            //设备
            EquipmentAllService.query({},onEquipmentSuccess, onError);
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
        //当前用户
        function _fnQueryUser() {
            Principal.identity().then(function(account) {
                vm.account = account;
                if(vm.account.login != 'admin'){
                    vm.entity.receiveName = vm.account.login;
                }
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
            // _initStockInBoxesTable();
        }
        _initStockInInfo();
        _initStockInBoxesTable();

        function _getSplitingTubeTableCtrl(){
            return hotRegisterer.getInstance('my-handsontable');
        }
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

            vm.dtOptions = BioBankDataTable.buildDTOption("BASIC", 252, 10)
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
                // 设置默认排序
                .withOption('order', [])
                // 指定数据加载方法
                // .withFnServerData(_fnServerData)
                // 每行的渲染
                .withOption('createdRow', _fnCreatedRow)
                .withOption('headerCallback', function(header) {
                    $compile(angular.element(header).contents())($scope);
                })
                .withOption('drawCallback',function () {
                    $(".dataTables_scrollBody")[0].style.height = 'auto';
                    $(".dataTables_scrollBody")[0].style.maxHeight = '400px';
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
        //初始化盒子列表
        function _fnQueryBoxList() {
            var data = {};
            data["stockInCode"] = vm.stockInCode;
            data["draw"] = 1;
            data["length"] = -1;
            StockInBoxService.getJqDataTableValues(data).success(function (res){
                vm.stockInBox = res.data;
                vm.dtOptions.withOption('data',vm.stockInBox);
                vm.dtInstance.rerender();
                _isStockInFinish();

            });
        }
        function _fnServerData() {
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
                BioBankBlockUi.blockUiStop();
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
            // var transportCode = '';
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
            // if(data.transhipCode){
            //     transportCode = data.transhipCode;
            // }else{
            //     transportCode = null;
            // }
            // $('td:eq(2)', row).html(transportCode);
            $('td:eq(10)', row).html(isSplit ? '需要分装' : '');
            $('td:eq(11)', row).html(status);
            $compile(angular.element(row).contents())($scope);
        }
        function _fnActionButtonsRender(data, type, full, meta) {
            var buttonHtml = "";
            if (full.status == "2002"){
                // if(full.sampleTypeCode != '99'){
                //     buttonHtml +='<button type="button" class="btn btn-xs btn-error" ng-click="vm.editBox(\''+ full.id +'\')">' +
                //         '   <i class="fa fa-edit"></i> 编辑 ' +
                //         '</button>&nbsp;';
                // }
                buttonHtml +='<button type="button" class="btn btn-xs btn-error" ng-click="vm.editBox(\''+ full.id +'\')">' +
                    '   <i class="fa fa-edit"></i> 编辑 ' +
                    '</button>&nbsp;';
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
                    null,
                    {type: 'text',bRegex: true,bSmart: true,iFilterLength:3},
                    {type: 'text',bRegex: true,bSmart: true,iFilterLength:3},
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
                DTColumnBuilder.newColumn("").withOption("width", "20").withTitle(titleHtml).withOption('searchable',false).notSortable().renderWith(_fnRowSelectorRender),
                DTColumnBuilder.newColumn('orderNO').withTitle('序号').withOption("width", "30"),
                DTColumnBuilder.newColumn('transhipCode').withTitle('转运编码').withOption("width", "100"),
                DTColumnBuilder.newColumn('projectSiteCode').withTitle('项目点').withOption("width", "60"),
                DTColumnBuilder.newColumn('frozenBoxCode').withTitle('冻存盒号').withOption("width", "90"),
                DTColumnBuilder.newColumn('frozenBoxCode1D').withTitle('一维编码').withOption("width", "100"),
                DTColumnBuilder.newColumn('sampleTypeName').withOption("width", "60").withTitle('样本类型'),
                DTColumnBuilder.newColumn('sampleClassificationName').withOption("width", "120").withTitle('样本分类'),
                DTColumnBuilder.newColumn('position').withOption("width", "auto").withTitle('冻存位置'),
                DTColumnBuilder.newColumn('countOfSample').withOption("width", "50").withTitle('样本量'),
                DTColumnBuilder.newColumn('isSplit').withOption("width", "60").withTitle('是否分装'),
                DTColumnBuilder.newColumn('status').withOption("width", "60").withTitle('状态'),
                DTColumnBuilder.newColumn("").withOption("width", "120").withTitle('操作').withOption('searchable',false).notSortable().renderWith(_fnActionButtonsRender)
            ];

            return columns;
        }




        // 冻存盒号是否可以编辑，编辑盒子时，盒子信息无法编辑，新增盒子，可以编辑
        vm.editFlag = false;
        //编辑
        function _editBox(stockInBoxId) {
            BioBankBlockUi.blockUiStart();


            StockInInputService.queryEditStockInBox(stockInBoxId).success(function (data) {
                BioBankBlockUi.blockUiStop();
                vm.box = {};
                vm.box = data;
                vm.editFlag = true;
                vm.showFlag = true;
                vm.splittingBox = false;
                _changeBodyScrollTop();
            }).error(function (data) {
                toastr.error(data.message);
            });
        }
        //分装、编辑时，body滚动条要滚到底部
        function _changeBodyScrollTop() {
            setTimeout(function () {
                _scrollTop = document.body.scrollHeight  - window.innerHeight - 30;
                document.documentElement.scrollTop = _scrollTop;
            },500);
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
                templateUrl: 'app/bizs/stock-in/modal/box-putaway-modal.html',
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
                _fnSearch();
                //编辑
                vm.showFlag = false;
                //分装
                vm.splittingBox = false;
                _fnQueryBoxList();
                // vm.dtInstance.rerender();
            });
        }
        //撤销上架
        function _rescindInShelf(boxCode) {
            modalInstance = $uibModal.open({
                animation: true,
                templateUrl: 'app/bizs/stock-in/modal/rescind-putaway-modal.html',
                controller: 'RescindPutAwayModalController',
                controllerAs:'vm',
                backdrop:'static'
            });
            modalInstance.result.then(function (data) {
                RescindPutAwayService.rescindPutAway(vm.entity.stockInCode,boxCode).then(function (data) {
                    vm.dtOptions.isHeaderCompiled = false;
                    _fnQueryBoxList();
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
                            storeKeeper1: vm.entity.receiveName,
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

        //盒子初始化
        _fnQueryBoxList();

        vm.frozenTubeArray = [];//初始管子的单元格
        vm.incompleteBoxesList = []; //分装后的样本类型盒子，未装满样本的盒子
        var tempTubeArray = [];//选中未满样本盒子的临时数据，需要操作管子
        var selectTubeList = [];//选择单元格的管子数据
        //根据盒子编码取管子
        function _fnTubeByBoxCode(stockInBoxId) {
            BioBankBlockUi.blockUiStart();
            blockUIConfig.autoBlock = false;
            StockInInputService.queryEditStockInBox(stockInBoxId).success(function (data) {
                blockUIConfig.autoBlock = true;
                // vm.tableRender();
                vm.box =  data;
                //绘制样本
                _fnDrawTube(vm.box);
                //取未满盒子
                _fnIncompleteBox();
            });
        }
        //编辑切换到分装
        function _fnEditToSpiltTube() {
            //绘制样本
            _fnDrawTube(vm.box);
            //取未满盒子
            _fnIncompleteBox();
        }
        //绘制样本
        function _fnDrawTube(frozenBox) {
            if(!frozenBox.frozenTubeDTOS.length){
                vm.splittingBox = false;
            }else{
                vm.splittingBox = true;
            }

            //获取样本分类
            _fnQueryProjectSampleClass(vm.entity.projectId,frozenBox.sampleType.id,frozenBox.sampleType.isMixed);
            var minCols = +frozenBox.frozenBoxType.frozenBoxTypeColumns;
            var minRows = +frozenBox.frozenBoxType.frozenBoxTypeRows;
            var tubesInTable = [];
            var colHeaders = [];
            var rowHeaders = [];
            for(var i = 0; i < minRows; i++){
                var pos = {tubeRows: String.fromCharCode('A'.charCodeAt(0) + i), tubeColumns: 1 + ""};
                if(i > 7){
                    pos.tubeRows = String.fromCharCode('A'.charCodeAt(0) + i+1);
                }
                rowHeaders.push(pos.tubeRows);
                var tubes = [];
                for(var j = 0; j < minCols;j++){
                    pos.tubeColumns = j + 1 + "";
                    if (colHeaders.length < minCols){
                        colHeaders.push(pos.tubeColumns);
                    }
                    var tubeInBox = _.filter(frozenBox.frozenTubeDTOS, pos)[0];
                    var tube = _createTubeForTableCell(tubeInBox, frozenBox, pos);
                    tubes.push(tube);
                }
                tubesInTable.push(tubes);
            }
            vm.frozenTubeArray = tubesInTable;


            setTimeout(function () {
                var tableCtrl = _getSplitingTubeTableCtrl();
                var tableSettings = tableCtrl.getSettings();
                var tableWrapper = tableCtrl.container;
                tableWrapper = $(tableWrapper).closest(".panel-body");
                var maxWidth = tableWrapper.innerWidth();
                var rowHeaderWidth = +tableSettings.rowHeaderWidth;
                var colWidths = (maxWidth-rowHeaderWidth)/colHeaders.length;
                tableSettings.minCols = minCols;
                tableSettings.minRows = minRows;
                tableSettings.rowHeaders = rowHeaders;
                tableSettings.colHeaders = colHeaders;
                tableSettings.columnHeaderHeight = rowHeaderWidth;
                // tableSettings.colWidths = colWidths;
                tableSettings.width = maxWidth;
                tableCtrl.updateSettings(tableSettings);
                tableCtrl.loadData(vm.frozenTubeArray);
                tableCtrl.render();
                vm.boxStr = JSON.stringify(vm.frozenTubeArray);
                _changeBodyScrollTop();
            },500);
        }

        var customRenderer = function (hotInstance, td, row, col, prop, value, cellProperties) {
            if(value){
                if (cellProperties.readOnly){
                    $(td).addClass('htDimmed');
                    $(td).addClass('htReadOnly');

                } else {
                    $(td).removeClass('htDimmed');
                    $(td).removeClass('htReadOnly');
                }
                if(value.memo && value.memo != " "){
                    cellProperties.comment = {value:value.memo};
                }
                if(value.sampleCode == null){
                    value.sampleCode = "";
                }
                //样本类型
                if(value.backColorForClass){
                    td.style.backgroundColor = value.backColorForClass;
                }else{
                    td.style.backgroundColor = value.backColor;
                }
                if (value.frontColor){
                    td.style.color = value.frontColor;
                }
                //样本类型
                // if(value.sampleClassificationId){
                //     SampleService.changeSampleType(value.sampleClassificationId,td,vm.projectSampleTypeOptions,1);
                // }else{
                //     if(vm.sampleTypeOptions){
                //         SampleService.changeSampleType(value.sampleTypeId,td,vm.sampleTypeOptions,2);
                //     }
                // }
                //样本状态 status3001：正常，3002：空管，3003：空孔；3004：异常 3005:销毁
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
                $div = $("<div  class='tube-status'/>").html(value.status).appendTo(td);
                // $div = $("<div id='microtubesStatus'/>").html(value.status).hide().appendTo(td);
                // if(value.selectedAll){
                //     if(value.sampleCode || value.sampleTempCode) {
                //         selectTubeList.push(value);
                //     }
                //     $('<div class="temp selected-sample-color"/>').appendTo(td);
                // }
            }else {
                $(td).html("");
            }
            td.style.position = 'relative';


            // td.innerHTML = htm;
        };
        var selectedTubesArray = [];
        function changeSampleStatus(sampleStatus,row,col,td,cellProperties) {
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

        }
        vm.splitingTbueTableCtrl = {};
        vm.settings ={
            colHeaders : ['1','2','3','4','5','6','7','8','9','10'],
            rowHeaders : ['A','B','C','D','E','F','G','H','J','K'],
            height:490,
            minRows: 10,
            minCols: 10,
            fillHandle:false,
            stretchH: 'all',
            wordWrap:true,
            rowHeaderWidth: 25,
            colWidths: 60,
            columnHeaderHeight:25,
            editor: false,
            outsideClickDeselects:true,
            multiSelect: true,
            comments: true,
            renderer:customRenderer,
            onAfterSelectionEnd:function (row, col, row2, col2) {
                var tableCtrl = _getTableCtrl();
                if (!tableCtrl){
                    return;
                }
                var cellData = tableCtrl.getDataAtCell(row, col);
                if (cellData && cellData.flag == 2) {
                    return;
                }
                var pos = {row :row,col:col,row2:row2,col2:col2};
                //ctrl键
                if(window.event && window.event.ctrlKey){
                    _fnSelectTubesData(this,pos);
                }else{
                    $(".temp").remove();
                    selectTubeList = [];
                    _fnSelectTubesData(this,pos);
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
            },
            afterInit: function(){
                vm.splitingTbueTableCtrl = this;
                $(window).resize(function (event){
                    var tableCtrl = _getSplitingTubeTableCtrl();
                    var tableSettings = tableCtrl.getSettings();
                    var tableWrapper = tableCtrl.container;
                    tableWrapper = $(tableWrapper).closest(".panel-body");
                    var maxWidth = tableWrapper.innerWidth();
                    var rowHeaderWidth = +tableSettings.rowHeaderWidth;
                    tableSettings.columnHeaderHeight = rowHeaderWidth;
                    tableSettings.width = maxWidth;
                    tableCtrl.updateSettings(tableSettings);
                });
            }
        };
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
                cellProperties.disableVisualSelection = true;
                // 该单元格只读
                cellProperties.readOnly = true;
                // 该单元格的样式
                cellProperties.className = 'a00';
                // cellProperties.readOnlyCellClassName = 'htDimmed';
            }

            return cellProperties;
        }
        //选择单元格数据
        function _fnSelectTubesData(td,pos) {
            selectedTubesArray = td.getData(pos.row,pos.col,pos.row2,pos.col2);
            for(var m = 0; m < selectedTubesArray.length; m++){
                for (var n = 0; n < selectedTubesArray[m].length; n++){
                    if(selectedTubesArray[m][n].sampleCode || selectedTubesArray[m][n].sampleTempCode) {
                        selectTubeList.push(selectedTubesArray[m][n]);
                    }
                }
            }
            var txt = '<div class="temp selected-sample-color"></div>';
            var start1,end1,start2,end2;
            var row = pos.row;
            var row2 = pos.row2;
            var col = pos.col;
            var col2 = pos.col2;
            if(row >= row2){
                start1 = row2;
                end1 = row;
            }else{
                start1 = row;
                end1 = row2;
            }
            if(col >= col2){
                start2 = col2;
                end2 = col;
            }else{
                start2 = col;
                end2 = col2;
            }
            for(var i = start1;i <= end1; i++){
                for(var j = start2;  j <= end2;j++) {
                    if($(td.getCell(i,j))[0].childElementCount !=3){
                        $(td.getCell(i,j)).append(txt);
                    }
                }
            }
            // var start1,end1,start2,end2;
            // if(selectTubeArrayIndex[0] > selectTubeArrayIndex[2]){
            //     start1 = selectTubeArrayIndex[2];
            //     end1 = selectTubeArrayIndex[0];
            // }else{
            //     start1 = selectTubeArrayIndex[0];
            //     end1 = selectTubeArrayIndex[2];
            // }
            // if(selectTubeArrayIndex[1] > selectTubeArrayIndex[3]){
            //     start2 = selectTubeArrayIndex[3];
            //     end2 = selectTubeArrayIndex[1];
            // }else{
            //     start2 = selectTubeArrayIndex[1];
            //     end2 = selectTubeArrayIndex[3];
            // }
            // for(var i = start1;i <= end1; i++){
            //     for(var j = start2;  j <= end2;j++) {
            //         if($(td.getCell(i,j))[0].childElementCount !=3){
            //             $(td.getCell(i,j)).append(txt);
            //         }
            //     }
            // }

        }
        // 创建一个对象用于管子Table的控件
        function _createTubeForTableCell(tubeInBox, box, pos){
            var tube = {
                id: null,
                sampleCode: null,
                sampleTempCode: null,
                sampleType:box.sampleType,
                sampleTypeId: box.sampleType.id,
                sampleTypeCode: box.sampleType.sampleTypeCode,
                sampleTypeName: box.sampleType.sampleTypeName,
                backColorForClass:box.backColorForClass,
                backColor:box.backColor,
                frontColor:box.frontColor,
                frozenBoxId: box.id,
                frozenBoxCode: box.frozenBoxCode,
                status: null,
                memo: null,
                flag:null,
                tubeRows: pos.tubeRows,
                tubeColumns: pos.tubeColumns
            };
            if(box.sampleClassification){
                tube.sampleClassification = box.sampleClassification;
                tube.sampleClassificationId = box.sampleClassification.id;
                tube.sampleClassificationName = box.sampleClassification.sampleClassificationName;
                tube.sampleClassificationCode = box.sampleClassification.sampleClassificationCode;
                tube.backColorForClass = box.sampleClassification.backColor;
            }
            if (tubeInBox){
                tube.id = tubeInBox.id;
                tube.frozenTubeId =  tubeInBox.frozenTubeId;
                tube.sampleCode = tubeInBox.sampleCode;
                tube.sampleTempCode = tubeInBox.sampleTempCode;

                tube.sampleTypeId = tubeInBox.sampleTypeId;
                tube.sampleTypeCode = tubeInBox.sampleTypeCode;
                tube.sampleTypeName = tubeInBox.sampleTypeName;
                tube.backColor = tubeInBox.backColor;

                tube.sampleClassificationId = tubeInBox.sampleClassificationId;
                tube.sampleClassificationName = tubeInBox.sampleClassificationName;
                tube.sampleClassificationCode = tubeInBox.sampleClassificationCode;
                tube.backColorForClass = tubeInBox.backColorForClass;

                tube.status = tubeInBox.status;
                tube.memo = tubeInBox.memo;
                tube.flag = tubeInBox.flag;
                if(tubeInBox.sampleClassification){
                    tube.sampleClassification = box.sampleClassification;
                    tube.sampleClassificationId = tubeInBox.sampleClassificationId;
                    tube.sampleClassificationName = tubeInBox.sampleClassificationName;
                    tube.sampleClassificationCode = tubeInBox.sampleClassificationCode;
                    tube.backColorForClass = tubeInBox.backColorForClass;
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
                    // for(var k = 0; k < data.length; k++){
                    //     for (var i = 0; i < vm.frozenTubeArray.length; i++) {
                    //         for (var j = 0; j < vm.frozenTubeArray[i].length; j++) {
                    //             if(data[k].columnsNumber == j+1 && !vm.frozenTubeArray[i][j].sampleCode){
                    //                 vm.frozenTubeArray[i][j].sampleClassificationId = data[k].sampleClassificationId;
                    //                 vm.frozenTubeArray[i][j].sampleClassificationCode = data[k].sampleClassificationCode;
                    //                 vm.frozenTubeArray[i][j].sampleTypeId = sampleTypeId;
                    //             }
                    //         }
                    //     }
                    //     for(var m = 0; m < vm.box.frozenTubeDTOS.length; m++){
                    //         if(vm.box.frozenTubeDTOS[m].tubeColumns == data[k].columnsNumber && !vm.box.frozenTubeDTOS[m].sampleCode){
                    //             vm.box.frozenTubeDTOS[m].sampleClassificationId = data[k].sampleClassificationId;
                    //             vm.box.frozenTubeDTOS[m].sampleClassificationCode = data[k].sampleClassificationCode;
                    //         }
                    //     }
                    // }

                    vm.box.sampleClassificationId = null;
                }else{
                    if(vm.box.sampleClassification){
                        vm.box.sampleClassificationId = vm.projectSampleTypeOptions[0].sampleClassificationId;
                        vm.box.sampleClassificationCode = vm.projectSampleTypeOptions[0].sampleClassificationCode;
                    }
                    for (var i = 0; i < vm.frozenTubeArray.length; i++) {
                        for (var j = 0; j < vm.frozenTubeArray[i].length; j++) {
                            if(vm.box.sampleClassification){
                                vm.frozenTubeArray[i][j].sampleClassificationId = vm.box.sampleClassification.id;
                                vm.frozenTubeArray[i][j].sampleClassificationCode = vm.box.sampleClassification.sampleClassificationCode;
                            }
                            vm.frozenTubeArray[i][j].sampleTypeId = sampleTypeId;
                        }
                    }
                    for(var m = 0; m < vm.box.frozenTubeDTOS.length; m++){
                        if(vm.box.sampleClassification){
                            vm.box.frozenTubeDTOS[m].sampleClassificationId = vm.box.sampleClassification.id;
                            vm.box.frozenTubeDTOS[m].sampleClassificationCode = vm.box.sampleClassification.sampleClassificationCode;
                        }
                    }
                }

                hotRegisterer.getInstance('my-handsontable').render();
            });
        }
        //获取未满冻存盒
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
                        tube.id = "";
                        tube.flag = "2";
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
                        data[i].sampleClassificationName = data[i].sampleClassification.sampleClassificationName;
                        data[i].sampleTypeName1 = data[i].sampleClassification.sampleClassificationName;
                        vm.sampleTypeClassId = data[i].sampleClassification.sampleClassificationId || data[i].sampleClassification.id;
                        vm.sampleTypeClassCode = data[i].sampleClassification.sampleClassificationCode;
                    } else {
                        data[i].backColor = data[i].backColor || data[i].sampleType.backColor;
                        data[i].sampleTypeName = data[i].sampleTypeName || data[i].sampleType.sampleTypeName;
                        data[i].sampleTypeName1 = data[i].sampleTypeName || data[i].sampleType.sampleTypeName;
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
            vm.incompleteBoxesList  = _.orderBy(vm.incompleteBoxesList, ['sampleTypeCode'], ['asc']);
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

        //选中要分装样本盒
        vm.sampleBoxSelect = function (item,$event) {
            tubeList = [];
            vm.obox = angular.copy(item);
            if(vm.boxList.length){
                for(var i =0,len = vm.boxList.length; i < len; i++){
                    if(vm.obox.frozenBoxCode == vm.boxList[i].frozenBoxCode){
                        vm.obox.stockInFrozenTubeList = vm.boxList[i].stockInFrozenTubeList;
                        tubeList = vm.boxList[i].stockInFrozenTubeList;
                    }
                }
            }else{
                tubeList = vm.obox.stockInFrozenTubeList;
            }
            // tubeList = [];
            vm.frozenBoxCode = vm.obox.frozenBoxCode;
            if(vm.obox.sampleClassification){
                vm.sampleTypeClassId = vm.obox.sampleClassification.id || vm.obox.sampleClassification.sampleClassificationId;
                vm.sampleTypeClassCode = vm.obox.sampleClassification.sampleClassificationCode;
            }
            vm.problemSamplyTypeCode = vm.obox.sampleTypeCode || vm.obox.sampleType.sampleTypeCode;
            // if(vm.frozenBoxCode){
            //     tubeList = item.stockInFrozenTubeList;
            // }

            // vm.obox.stockInFrozenTubeList = _.each(vm.obox.stockInFrozenTubeList, function(t){ t.tubeColumns = +t.tubeColumns});
            $($event.target).closest('ul').find('.box-selected').removeClass("box-selected");
            $($event.target).addClass("box-selected");
        };
        //双击显示未满冻存盒详情
        vm.selectAndOpenTargetBox = function (item, $event){
            // if(item.stockInFrozenTubeList.length){
            //     vm.obox = angular.copy(item);
            // }

            // tubeList = vm.obox.stockInFrozenTubeList;
            var rowCount = +vm.obox.frozenBoxTypeRows;
            var colCount = +vm.obox.frozenBoxTypeColumns;
            //绘制新管子集合，并且把盒子中已有管子放入这个管子集合
            _fnDrawSplitTube(rowCount,colCount);
            //显示盒子详情
            vm.frozenBox = vm.obox;
            vm.frozenBox.stockInFrozenTubeList1 = _.each(vm.obox.stockInFrozenTubeList, function(t){ t.tubeColumns = +t.tubeColumns});
            vm.boxDetailFlag = true;
        };
        //绘制新管子集合，并且把盒子中已有管子放入这个管子集合
        function _fnDrawSplitTube(rowCount,colCount) {

            vm.obox.stockInFrozenTubeList = [];

            //初始管子个数
            for(var i = 0; i < rowCount; i++){
                tempTubeArray[i] = [];
                var rowNO = i > 7 ? i+1 : i;
                rowNO = String.fromCharCode(rowNO+65);
                for(var j = 0;j < colCount; j++){
                    tempTubeArray[i][j] = {
                        tubeColumns: j+1,
                        tubeRows: rowNO,
                        frozenBoxCode:"",
                        frozenTubeId:"",
                        sampleCode:null,
                        id:"",
                        selectTubeCode:vm.obox.frozenBoxCode,
                        flag:"1"
                    };
                    vm.obox.stockInFrozenTubeList.push(tempTubeArray[i][j]);

                }
            }
            // if(!tubeList.length){
            //     _.forEach(vm.obox.stockInFrozenTubeList,function (box) {
            //         box.isNewSampleFlag = true;
            //     })
            // }
            //把已有管子放进去
            for(var m =0 ; m < vm.obox.stockInFrozenTubeList.length; m++){
                for(var n = 0; n < tubeList.length;n++){
                    var tube = tubeList[n];
                    if(vm.obox.stockInFrozenTubeList[m].tubeRows == tube.tubeRows && vm.obox.stockInFrozenTubeList[m].tubeColumns == +tube.tubeColumns){
                        vm.obox.stockInFrozenTubeList[m] = tube;
                    }
                }
            }
        }
        //分装操作
        vm.splitBox = function () {
            if(vm.box.sampleClassification || vm.box.sampleType.sampleTypeCode == "99"){
                if(vm.problemSamplyTypeCode != "97"){
                    for(var i = 0; i< selectTubeList.length; i++){
                        if(vm.sampleTypeClassCode != selectTubeList[i].sampleClassificationCode){
                            toastr.error("被分装的样本分类必须跟要分装的盒子的分类要一致！");
                            return;
                        }
                    }
                }
            }
            var rowCount = +vm.box.frozenBoxType.frozenBoxTypeRows;
            var colCount = +vm.box.frozenBoxType.frozenBoxTypeColumns;
            //绘制新管子集合，并且把盒子中已有管子放入这个管子集合
            _fnDrawSplitTube(rowCount,colCount);
            //判断选中的分装管子或者盒子
            if(!selectTubeList.length || !vm.frozenBoxCode ){
                toastr.error("请选择被分装的冻存管或者请选择要分装到的冻存盒!");
                return;
            }
            //总管子数
            var tubeCount = colCount*rowCount;
            //剩余管子数
            var surplusCount =  tubeCount - vm.obox.countOfSample;
            //选中的被分装的管子数
            var selectCount = selectTubeList.length;
            //分装到哪个盒子中的数量
            if( selectCount <= surplusCount){
                vm.obox.addTubeCount  += selectCount;
            }else{
                if(surplusCount != 0){
                    vm.obox.addTubeCount  += surplusCount;
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
            //清空被分装的盒子数
            for(var k = 0; k < selectTubeList.length; k++){
                vm.frozenTubeArray[getTubeRowIndex(selectTubeList[k].tubeRows)][getTubeColumnIndex(selectTubeList[k].tubeColumns)] = {
                    id: null,
                    sampleCode: null,
                    sampleTempCode: null,
                    sampleTypeId: selectTubeList[k].sampleTypeId,
                    sampleTypeCode: selectTubeList[k].sampleTypeCode,
                    sampleClassificationCode: selectTubeList[k].sampleClassificationCode,
                    sampleClassificationId: selectTubeList[k].sampleClassificationId,
                    backColor:vm.box.backColor,
                    backColorForClass:vm.box.backColorForClass,
                    frozenBoxId: vm.box.id,
                    frozenBoxCode: vm.box.frozenBoxCode,
                    status: "3001",
                    memo: null,
                    tubeRows:selectTubeList[k].tubeRows,
                    tubeColumns:selectTubeList[k].tubeColumns
                };


            }
            var tableCtrl = _getSplitingTubeTableCtrl();
            tableCtrl.loadData(vm.frozenTubeArray);
            // hotRegisterer.getInstance('my-handsontable').render();
            //分装数据
            for(var i = 0; i < vm.obox.stockInFrozenTubeList.length; i++){
                if(!vm.obox.stockInFrozenTubeList[i].frozenBoxCode){
                    // 当盒子中管子剩余数为0时，自动添加第二个盒子
                    if(surplusCount){
                        if(selectTubeList.length){
                            vm.obox.stockInFrozenTubeList[i].frozenBoxCode = vm.obox.stockInFrozenTubeList[i].selectTubeCode;
                            vm.obox.stockInFrozenTubeList[i].id = selectTubeList[0].id;
                            vm.obox.stockInFrozenTubeList[i].frozenTubeId = selectTubeList[0].frozenTubeId;
                            vm.obox.stockInFrozenTubeList[i].sampleCode = selectTubeList[0].sampleCode || selectTubeList[0].sampleTempCode;
                            vm.obox.stockInFrozenTubeList[i].status = selectTubeList[0].status;
                            vm.obox.stockInFrozenTubeList[i].memo = selectTubeList[0].memo;
                            vm.obox.stockInFrozenTubeList[i].sampleTypeId = selectTubeList[0].sampleTypeId;
                            vm.obox.stockInFrozenTubeList[i].sampleTypeCode = selectTubeList[0].sampleTypeCode;
                            vm.obox.stockInFrozenTubeList[i].sampleClassificationId = selectTubeList[0].sampleClassificationId;
                            vm.obox.stockInFrozenTubeList[i].sampleClassificationCode = selectTubeList[0].sampleClassificationCode;
                            vm.obox.stockInFrozenTubeList[i].backColor = selectTubeList[0].backColor;
                            vm.obox.stockInFrozenTubeList[i].backColorForClass = selectTubeList[0].backColorForClass;
                            vm.obox.stockInFrozenTubeList[i].flag = "3";
                            delete vm.obox.stockInFrozenTubeList[i].selectTubeCode;
                            selectTubeList.splice(0,1);
                        }else{
                            break;
                        }
                    }

                }
            }
            //删除空管子
            _.remove(vm.obox.stockInFrozenTubeList,{"flag":"1"});

            tubeList = angular.copy(vm.obox.stockInFrozenTubeList);

            //分装后的样本量
            vm.obox.countOfSample = vm.obox.stockInFrozenTubeList.length;
            //分装的盒子已满，添加第二个盒子
            if(vm.obox.countOfSample == tubeCount && selectTubeList.length){
                vm.boxList = [];
                var frozenBox = {};
                frozenBox.frozenBoxTypeId= vm.obox.frozenBoxType.id;
                frozenBox.sampleTypeId= vm.obox.sampleType.id;
                frozenBox.sampleType= vm.obox.sampleType;
                if(vm.obox.sampleClassification || vm.obox.sampleClassificationId){
                    frozenBox.sampleClassificationId = vm.obox.sampleClassificationId || vm.obox.sampleClassification.id;
                }
                frozenBox.stockInFrozenTubeList = selectTubeList;

                vm.addBoxModal(frozenBox,"1");
            }
            var obox = angular.copy(vm.obox);

            var len = _.filter(vm.boxList,{frozenBoxCode:obox.frozenBoxCode}).length;

            if(!len){
                vm.boxList.push(obox);
            }else{
                _.forEach(vm.boxList,function (box) {
                    if(box.frozenBoxCode == obox.frozenBoxCode){
                        //新增的样本
                        var addSampleArray = _.filter(obox.stockInFrozenTubeList,{flag:"2"});
                        if(addSampleArray.length){
                            for(var i = 0,addSampleCount = addSampleArray.length; i < addSampleCount; i++){
                                var len = _.filter(box.stockInFrozenTubeList,{id:addSampleArray[i].id}).length;
                                if(!len){
                                    box.stockInFrozenTubeList.push(addSampleArray[i]);
                                }

                            }
                        }
                    }
                });
            }
            //绘制新管子集合，并且把盒子中已有管子放入这个管子集合
            _fnDrawSplitTube(rowCount,colCount);

            vm.frozenBox.stockInFrozenTubeList1 = _.each(vm.obox.stockInFrozenTubeList, function(t){ t.tubeColumns = +t.tubeColumns});
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
        //添加分装样本盒 1:第二个新盒子 2.新添第一个盒子
        vm.addBoxModal = function (box,status) {
            if(vm.boxList.length){
                toastr.error("有未满的冻存盒，无法添加新冻存盒！请您先进行保存分装操作!");
                return;
            }
            if(!box){
                box = {};
                box.sampleTypeId = vm.box.sampleType.id;
                box.sampleTypeCode = vm.box.sampleType.sampleTypeCode;
                box.sampleTypeName = vm.box.sampleType.sampleTypeName;
                // box.isMixed = vm.box.sampleType.isMixed;
                box.frozenBoxTypeId = vm.box.frozenBoxType.id;
                box.stockInFrozenTubeList = [];
                if(box.sampleTypeCode == "99"){
                    if(vm.projectSampleTypeOptions.length){
                        vm.sampleTypeClassId =  vm.projectSampleTypeOptions[0].sampleClassificationId;
                        vm.sampleTypeClassCode =  vm.projectSampleTypeOptions[0].sampleClassificationCode;
                        vm.sampleTypeClassName =  vm.projectSampleTypeOptions[0].sampleClassificationName;
                    }
                }
            }
            modalInstance = $uibModal.open({
                animation: true,
                templateUrl: 'app/bizs/stock-in/modal/add-box-modal.html',
                controller: 'AddBoxModalController',
                controllerAs:'vm',
                size:'lg',
                backdrop:'static',
                resolve: {
                    items: function () {
                        return {
                            projectId:vm.entity.projectId,
                            stockInCode:vm.entity.stockInCode,
                            box :box || {stockInFrozenTubeList:[]},
                            incompleteBoxes: vm.incompleteBoxesList,
                            isMixed:vm.box.sampleType.isMixed,
                            sampleTypeId:box.sampleTypeId,
                            sampleTypeName:box.sampleTypeName,
                            sampleTypeCode:box.sampleTypeCode,
                            sampleTypeClassId:vm.sampleTypeClassId || vm.box.sampleClassificationId,
                            sampleTypeClassName:vm.sampleTypeClassName || vm.box.sampleClassificationName,
                            sampleTypeClassCode:vm.sampleTypeClassCode || vm.box.sampleClassificationCode,
                            frozenBoxTypeId:box.frozenBoxTypeId,
                            frozenBoxType:vm.box.frozenBoxType,
                            status:status || "2"
                        };
                    }
                }
            });

            modalInstance.result.then(function (data) {
                _.forEach(data.stockInFrozenTubeList,function (tube) {
                    if(status == 1){
                        tube.flag = "2";
                    }
                });
                if(data){
                    if(!data.sampleTypeCode){
                        data.sampleTypeCode = data.sampleType.sampleTypeCode;
                        data.sampleTypeName = data.sampleType.sampleTypeName;
                    }
                    //添加分装后的冻存盒，没有添加新的，有的话再添加相同的盒子，相同的最多添加2个
                    var index;
                    if(data.sampleClassificationCode){
                        index = _.findIndex(vm.incompleteBoxesList,{sampleTypeCode:data.sampleClassificationCode});

                    }else{
                        index = _.findIndex(vm.incompleteBoxesList,{sampleTypeCode:data.sampleTypeCode});
                    }
                    if(!vm.boxList.length){
                        if(index != -1){
                            _.remove(vm.incompleteBoxesList,{sampleTypeCode:data.sampleClassificationCode});
                            index = -1;
                        }
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

                        if(data.sampleClassification){
                            data.backColor = data.sampleClassification.backColor;
                            data.sampleTypeName1 = data.sampleClassification.sampleClassificationName;
                            data.sampleClassificationId = data.sampleClassification.id || data.sampleClassification.sampleClassificationId;
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
                            data.sampleTypeName1 = data.sampleType.sampleTypeName;
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
                        vm.incompleteBoxesList  = _.orderBy(vm.incompleteBoxesList, ['sampleTypeCode'], ['asc']);
                    }
                }else{

                }

            },function (data) {
                //复原被分装的剩余管子数
                for(var k = 0; k < selectTubeList.length; k++){
                    var rowIndex = getTubeRowIndex(selectTubeList[k].tubeRows);
                    var colIndex = getTubeColumnIndex(selectTubeList[k].tubeColumns);
                    vm.frozenTubeArray[rowIndex][colIndex] = selectTubeList[k];
                }
                // $timeout(function(){
                hotRegisterer.getInstance('my-handsontable').render();
                // },500);
            });
        };
        //保存分装结果
        var saveBoxList = [];
        vm.saveBox = function (status) {
            saveBoxList = [];
            for(var i = 0; i < vm.boxList.length; i++){
                var objBox = {
                    frozenBoxId : vm.boxList[i].frozenBoxId,
                    frozenBoxCode : vm.boxList[i].frozenBoxCode,
                    frozenBoxCode1D : vm.boxList[i].frozenBoxCode1D,
                    sampleTypeId : vm.boxList[i].sampleTypeId || vm.boxList[i].sampleType.id,
                    sampleTypeCode : vm.boxList[i].sampleTypeCode || vm.boxList[i].sampleType.sampleTypeCode,
                    sampleTypeName : vm.boxList[i].sampleTypeName || vm.boxList[i].sampleType.sampleTypeName,
                    frozenBoxTypeId : vm.boxList[i].frozenBoxTypeId || vm.boxList[i].frozenBoxType.id,
                    memo : vm.boxList[i].memo,
                    countOfSample : vm.boxList[i].countOfSample,
                    addTubeCount : vm.boxList[i].addTubeCount,
                    stockInFrozenTubeList : vm.boxList[i].stockInFrozenTubeList,
                    sampleClassificationId : null,
                    sampleClassificationCode : null,
                    sampleClassificationName : null
                };
                if(vm.boxList[i].sampleClassification){
                    objBox.sampleClassificationId = vm.boxList[i].sampleClassification.sampleClassificationId || vm.boxList[i].sampleClassification.id;
                    objBox.sampleClassificationCode = vm.boxList[i].sampleClassification.sampleClassificationCode || vm.boxList[i].sampleClassification.sampleClassificationCode;
                    objBox.sampleClassificationName = vm.boxList[i].sampleClassification.sampleClassificationName || vm.boxList[i].sampleClassification.sampleClassificationName;
                }

                saveBoxList.push(objBox);
            }

            //保存完更新入库盒子列表
            function _updateBoxList(data){

                //新增盒子到列表
                var newBoxArray = [];
                newBoxArray = _fnNewStockInListOfBox();

                //更改分装到冻存盒中的样本数
                _fnUpdateSampleCount();

                //更改被分装的盒子的样本数
                _fnSplitBoxSampleCount();

                //新增盒子记录与已有盒子记录合并
                if(newBoxArray.length){
                    var array = angular.copy(vm.stockInBox);
                    vm.stockInBox = _.concat(newBoxArray, array);
                }
                //更新新增盒子的入库盒子ID
                _fnUpdateBoxListId(data);

                vm.dtOptions.withOption('data',vm.stockInBox);
            }

            //新增盒子到列表
            function _fnNewStockInListOfBox() {
                var newBoxArray = [];
                _.forEach(saveBoxList,function (tube) {
                    var len = _.filter(vm.stockInBox,{frozenBoxCode:tube.frozenBoxCode}).length;
                    if(!len){
                        var tube1 = {
                            id:null,
                            countOfSample:tube.addTubeCount,
                            status:"2002",
                            frozenBoxCode:tube.frozenBoxCode,
                            frozenBoxCode1D:tube.frozenBoxCode1D,
                            position:null,
                            isSplit:0,
                            projectSiteCode:null,
                            orderNO:null,
                            transhipCode:null,
                            sampleClassificationCode:tube.sampleClassificationCode,
                            sampleClassificationName:tube.sampleClassificationName,
                            sampleTypeCode:tube.sampleTypeCode,
                            sampleTypeName:tube.sampleTypeName
                        };

                        newBoxArray.push(tube1);
                    }
                });
                return newBoxArray
            }
            // 更改分装到冻存盒中的样本数
            function _fnUpdateSampleCount() {
                for(var i = 0; i < saveBoxList.length;i++){
                    for(var j = 0; j < vm.stockInBox.length; j++){
                        if(saveBoxList[i].frozenBoxCode == vm.stockInBox[j].frozenBoxCode){
                            var len = 0;
                            var stockInFrozenTube = saveBoxList[i].stockInFrozenTubeList;
                            // len = stockInFrozenTube.length -  (_.filter(stockInFrozenTube,{id:""}).length);
                            len = _.filter(stockInFrozenTube,{flag:"3"}).length;

                            vm.stockInBox[j].countOfSample += len;
                        }
                    }
                }
            }
            //更改被分装的盒子的样本数
            function _fnSplitBoxSampleCount() {
                var tubes = _.flattenDeep(angular.copy(vm.frozenTubeArray));
                var notEmptyTubes = [];
                var notEmptyTubeLength;
                for(var k = 0; k < tubes.length; k++){
                    if(tubes[k].sampleCode || tubes[k].sampleTempCode){
                        notEmptyTubes.push(tubes[k]);
                    }
                }
                notEmptyTubeLength = notEmptyTubes.length;
                if(notEmptyTubes.length){
                    _.forEach(vm.stockInBox,function (tube) {
                        if(tube.frozenBoxCode == notEmptyTubes[0].frozenBoxCode){
                            tube.countOfSample = notEmptyTubeLength;
                        }
                    });
                }

            }
            // 更新新增盒子的入库盒子ID
            function _fnUpdateBoxListId(data) {
                for(var m = 0; m < vm.stockInBox.length; m++){
                    for(var n = 0; n < data.length; n++){
                        if(vm.stockInBox[m].frozenBoxCode == data[n].frozenBoxCode){
                            vm.stockInBox[m].id = data[n].id;
                        }
                    }
                }
            }
            vm.box.frozenTubeDTOS = _.flattenDeep(angular.copy(vm.frozenTubeArray));

            BioBankBlockUi.blockUiStart();
            SplitedBoxService.saveSplit(vm.stockInCode,vm.box.frozenBoxCode,saveBoxList).success(function (data) {
                toastr.success("分装成功!");

                //保存完更新入库盒子列表
                _updateBoxList(data);
                _fnDrawTube(vm.box);
                //取未满盒子
                _fnIncompleteBox();
                //当被分装的盒子别全部分装完，关闭分装界面
                var len = _.filter(vm.obox.stockInFrozenTubeList,{frozenBoxCode:""}).length;
                if(!len){
                    vm.splittingBox = false;
                }
                //分装切换编辑保存
                if(status == 3){
                    vm.editFlag = true;
                    vm.showFlag = true;
                    vm.splittingBox = false;
                }
                vm.boxList = [];
                vm.frozenBoxCode = "";
                vm.boxDetailFlag = false;
                $(".box-selected").removeClass("box-selected");
            }).error(function (data) {
                vm.recover();
                BioBankBlockUi.blockUiStop();
                toastr.error(data.message);
            });
        };
        //关闭未满冻存盒详情
        vm.closeBoxDesc = function () {
            vm.frozenBox.stockInFrozenTubeList1 = [];
            vm.frozenBoxCode = "";
            //关闭盒子详情
            vm.boxDetailFlag = false;
        };
        //查看已分装的冻存盒详情
        vm.viewSplitBoxDesc = function () {
            document.documentElement.scrollTop = 0;
            modalInstance = $uibModal.open({
                animation: true,
                templateUrl: 'app/bizs/stock-in/modal/split.box-desc-modal.html',
                controller: 'SplitBoxDescModalController',
                controllerAs:'vm',
                size:'lg w-1200',
                backdrop:'static',
                resolve: {
                    items: function () {
                        return {
                            box:vm.frozenBox
                        }
                    }
                }
            });

            modalInstance.result.then(function (data) {

            },function (data) {
                document.documentElement.scrollTop = _scrollTop;
            });
        } ;
        function _fnRecoverInit(){
            vm.boxList = [];
            tubeList = [];
            selectTubeList = [];
            saveBoxList = [];
            vm.frozenBoxCode = "";
            //盒子详情
            vm.frozenBox.stockInFrozenTubeList1 = [];
            vm.boxDetailFlag = false;
            $(".box-selected").removeClass("box-selected");
        }
        //复原
        vm.recover = function () {
            _fnTubeByBoxCode(vm.box.id);
            _fnRecoverInit();
        };
        //关闭
        vm.closeBox = function () {
            var boxStr = JSON.stringify(vm.frozenTubeArray);
            if(boxStr == vm.boxStr){
                _fnRecoverInit();
                vm.splittingBox = false;
            }else{
                modalInstance = $uibModal.open({
                    animation: true,
                    templateUrl: 'app/bizs/stock-in/modal/stock-in-close-splittingBox-modal.html',
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
                    vm.saveBox();
                },function () {
                    _fnRecoverInit();
                    vm.splittingBox = false;
                });
            }

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
        //分装时，编辑box
        vm.editBoxOperate = function () {
            tubeList = [];
            selectTubeList = [];
            saveBoxList = [];
            vm.frozenBoxCode = "";
            //盒子详情
            vm.frozenBox.stockInFrozenTubeList1 = [];
            vm.boxDetailFlag = false;
            $(".box-selected").removeClass("box-selected");

            var boxStr = JSON.stringify(vm.frozenTubeArray);
            if(boxStr == vm.boxStr){
                vm.box.frozenTubeDTOS = _.flattenDeep(angular.copy(vm.frozenTubeArray));
                vm.editFlag = true;
                vm.showFlag = true;
                vm.splittingBox = false;
            }else{
                modalInstance = $uibModal.open({
                    animation: true,
                    templateUrl: 'app/bizs/stock-in/modal/stock-in-close-splittingBox-modal.html',
                    controller: 'CloseSplittingBoxController',
                    controllerAs:'vm',
                    backdrop:'static',
                    size:'sm',
                    resolve: {
                        items: function () {
                            return {
                                status :3
                            };
                        }
                    }
                });
                modalInstance.result.then(function (flag) {
                    if(flag) {
                        //分装切换编辑保存
                        vm.saveBox(3);
                    }
                    // vm.editFlag = true;
                    // vm.showFlag = true;
                    // vm.splittingBox = false;
                },function () {
                    vm.editFlag = true;
                    vm.showFlag = true;
                    vm.splittingBox = false;
                    _changeBodyScrollTop();
                });
            }

        };

        function openCalendar (date) {
            vm.datePickerOpenStatus[date] = true;
        }

        // 获取控制实体
        function _getTableCtrl(){
            vm.TableCtrl = hotRegisterer.getInstance('my-handsontable');
            return vm.TableCtrl;
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
