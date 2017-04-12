/**
 * Created by gaokangkang on 2017/3/14.
 */
(function() {
    'use strict';

    angular
        .module('bioBankApp')
        .controller('TransportRecordNewController', TransportRecordNewController)
        .controller('BoxInstanceCtrl',BoxInstanceCtrl);

    TransportRecordNewController.$inject = ['$scope','hotRegisterer','SampleService','TransportRecordService','DTOptionsBuilder','DTColumnBuilder','$uibModal','$state','$stateParams','entity','frozenBoxByCodeService','TranshipNewEmptyService','TranshipSaveService','TranshipBoxService',
        'SampleTypeService','AlertService','FrozenBoxTypesService','FrozenBoxByIdService','EquipmentService','AreasByEquipmentIdService','SupportacksByAreaIdService','ProjectService','ProjectSitesByProjectIdService','TranshipBoxByCodeService','TranshipStockInService'];
    BoxInstanceCtrl.$inject = ['$uibModalInstance'];
    function TransportRecordNewController($scope,hotRegisterer,SampleService,TransportRecordService,DTOptionsBuilder,DTColumnBuilder,$uibModal,$state,$stateParams,entity,frozenBoxByCodeService,TranshipNewEmptyService,TranshipSaveService,TranshipBoxService,
                                          SampleTypeService,AlertService,FrozenBoxTypesService,FrozenBoxByIdService,EquipmentService,AreasByEquipmentIdService,SupportacksByAreaIdService,ProjectService,ProjectSitesByProjectIdService,TranshipBoxByCodeService,TranshipStockInService) {
        var vm = this;
        vm.datePickerOpenStatus = {};
        vm.transportRecord = entity; //转运记录
        vm.frozenTubeArray = [];//初始管子数据二位数组
        var remarkArray;//批注
        vm.operateStatus;// 1.状态 2:换位 3.批注
        var domArray = [];//单元格操作的数据
        var operateColor;//单元格颜色
        vm.openCalendar = openCalendar; //时间
        vm.importFrozenStorageBox = importFrozenStorageBox; //导入冻存盒
        vm.someClickHandler = someClickHandler; //点击冻存盒的表格行
        vm.reImportFrozenBoxData = reImportFrozenBoxData;//重新导入
        vm.saveRecord = saveRecord; //保存记录
        vm.saveBox = saveBox;//保存盒子
        vm.loadBox = loadBox;
        if($stateParams.transhipId){
            vm.transportRecord.id = $stateParams.transhipId;
        }

        if($stateParams.transhipCode){
            vm.transportRecord.transhipCode = $stateParams.transhipCode;
        }
        var obox = {
            transhipId:vm.transportRecord.id,
            frozenBoxDTOList:[]
        };
        if(vm.transportRecord.transhipDate){
            vm.transportRecord.transhipDate = new Date(entity.transhipDate);
        }
        if(vm.transportRecord.receiveDate){
            vm.transportRecord.receiveDate = new Date(entity.receiveDate);
        }
        function loadBox() {
            if(vm.transportRecord.transhipCode){
                TranshipBoxByCodeService.query({code:vm.transportRecord.transhipCode},onBoxSuccess,onError)
            }
        }
        var size = 10;
        function initFrozenTube(size) {
            for(var i = 0; i < size; i++){
                vm.frozenTubeArray[i] = [];
                for(var j = 0;j < size; j++){
                    vm.frozenTubeArray[i][j] = "";
                }
            }
        }
        initFrozenTube(size);

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

        var htm; //渲染管子表格
        vm.myCustomRenderer = function(hotInstance, td, row, col, prop, value, cellProperties) {
            td.style.position = 'relative';
            if(value == ""){
                value= {};
                value.sampleCode = "";
                value.sampleTempCode = "";
                value.sampleTypeCode = 'S_TYPE_00001';
                // value.status = "3003";//冻存管状态3001：正常，3002：空管，3003：空孔；3004：异常
                value.tubeRows = getTubeRows(row);
                value.tubeColumns = getTubeColumns(col);
                value.memo = ""
            }
            //样本类型
            if(value.sampleTypeCode){
                SampleService.changeSampleType(value.sampleTypeCode,td);
            }
            //样本状态 status3001：正常，3002：空管，3003：空孔；3004：异常
            if(value.status){
                changeSampleStatus(value.status,row,col,td,cellProperties)
            }

            htm = "<div ng-if='value.sampleCode'>"+value.sampleCode+"</div>"+
                "<div  ng-if='value.sampleTempCode'>"+value.sampleTempCode+"</div>" +
                "<div  style='display: none'>"+value.sampleTypeCode+"</div>" +
                "<div id='microtubesStatus' style='display: none'>"+value.status+"</div>"+
                "<div id='microtubesRemark' style='display: none'>"+value.memo+"</div>"+
                "<div id='microtubesRow' style='display: none'>"+value.tubeRows+"</div>"+
                "<div id='microtubesCol' style='display: none'>"+value.tubeColumns+"</div>"+
                "<div ng-if="+value.memo+" class='triangle-topright' style='position: absolute;top:0;right: 0;'></div>"

            td.innerHTML = htm;
            // console.log(JSON.stringify(vm.frozenTubeArray))

        };
        //修改样本状态正常、空管、空孔、异常
        vm.normalCount = 0;
        vm.emptyCount = 0;
        function changeSampleStatus(sampleStatus,row,col,td,cellProperties) {

            operateColor = td.style.backgroundColor;
            //正常
            if(sampleStatus == 3001){
               vm.normalCount ++;
            }
            //空管
            if(sampleStatus == 3002){
                td.style.background = 'linear-gradient(to right,'+operateColor+',50%,rgba(0,0,0,1)';
            }
            //空孔
            if(sampleStatus == 3003){
                vm.emptyCount++;
                td.style.background = '';
                td.style.backgroundColor = '#ffffff';
            }
            //异常
            if(sampleStatus == 3004){
                // var dom = '<div class="abnormal" style="position:absolute;top:0;bottom:0;left:0;right:0;border:3px solid red;"></div>';
                // $(td).append(dom);
                td.style.backgroundColor = 'red';
                td.style.border = '3px solid red;margin:-3px';
            }
        }

        vm.settings = {
            colHeaders : ['1','2','3','4','5','6','7','8','9','10'],
            rowHeaders : ['A','B','C','D','E','F','G','H','I','J'],
            data:vm.frozenTubeArray,
            renderer:vm.myCustomRenderer,
            fillHandle:false,
            stretchH: 'all',
            onAfterSelectionEnd:function (row, col, row2, col2) {
                remarkArray = this.getData(row,col,row2,col2);
                vm.remarkFlag = true;
                if(window.event.ctrlKey){
                    vm.exchangeFlag = true;
                    var txt = '<div class="temp" style="position:absolute;top:0;bottom:0;left:0;right:0;border:2px dotted #5292F7;"></div>';
                    $(this.getCell(row,col)).append(txt);
                    domArray.push(vm.frozenTubeArray[row][col]);
                    // console.log(JSON.stringify(domArray))
                }else{
                    domArray = [];
                    $(".temp").remove();
                }
                //管子
                if(vm.operateStatus == 1) {
                    if(vm.flagStatus){
                        var tubeStatus = $(this.getCell(row, col)).find("#microtubesStatus").text();
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
                if (selectedRow + 1 < hotMoves.countRows()) {
                    return {row: 1, col: 0}
                }
                else {
                    return {row: -selectedRow, col: 1}
                }
            },
            afterChange:function (change,source) {
                if(source == 'edit'){

                    // hotRegisterer.getInstance('my-handsontable').render()
                    return;
                }
            }
            // cells:function (row,col,prop) {
            //     console.log(row)
            // }
        };

        //修改样本状态
        vm.flagStatus = false;
        vm.editStatus = function () {
            vm.operateStatus = 1;
        };
        //换位
        vm.exchangeFlag = false;
        vm.exchange = function () {
            if(vm.exchangeFlag && domArray.length == 2){
                var row = getTubeRowIndex(domArray[0].tubeRows);
                var col = getTubeColumnIndex(domArray[0].tubeColumns);
                var row1 = getTubeRowIndex(domArray[1].tubeRows);
                var col1 = getTubeColumnIndex(domArray[1].tubeColumns);

                vm.frozenTubeArray[row1][col1] = domArray[0];
                vm.frozenTubeArray[row1][col1].tubeRows = getTubeRows(row1);
                vm.frozenTubeArray[row1][col1].tubeColumns = getTubeColumns(col1);

                vm.frozenTubeArray[row][col] = domArray[1];
                vm.frozenTubeArray[row][col].tubeRows = getTubeRows(row);
                vm.frozenTubeArray[row][col].tubeColumns = getTubeColumns(col);

                domArray = [];
                vm.exchangeFlag = false;

            }else{
                console.log("只能选择两个进行交换！");
                domArray = [];
            }
            hotRegisterer.getInstance('my-handsontable').render();


        };
        //批注
        vm.remarkFlag = false;
        vm.tubeRemark = function () {
            if(vm.remarkFlag &&  remarkArray.length > 0){
                modalInstance = $uibModal.open({
                    animation: true,
                    templateUrl: 'app/admin/transport-record/microtubes-remark-modal.html',
                    controller: 'microtubesRemarkModalController',
                    backdrop:'static',
                    controllerAs: 'vm',
                    resolve: {
                        items: function () {
                            return {
                                remarkArray :remarkArray
                            }
                        }
                    }

                });
                modalInstance.result.then(function (selectedItem) {
                    // console.log(JSON.stringify(selectedItem));
                    // for(var i = 0; i < vm.frozenTubeArray.length; i++){
                    //     for(var j = 0; j < vm.frozenTubeArray[i].length; j++){
                    //             if(selectedItem[i][j].sampleCode != ''){
                    //                 vm.frozenTubeArray[i][j].memo = selectedItem.remarkArray[i][j].memo;
                    //             }
                    //     }
                    // }
                    hotRegisterer.getInstance('my-handsontable').render();
                });
            }
        };
        //重新导入
        function reImportFrozenBoxData(){
            var modalInstance = $uibModal.open({
                animation: true,
                templateUrl: 'reImportDataModal.html',
                size: 'sm',
                controller: 'BoxInstanceCtrl',
                controllerAs: 'ctrl'


            });
            modalInstance.result.then(function (flag) {

                //true:保存 false:不保存
                if(flag){

                }
                frozenBoxByCodeService.get({code:vm.box.frozenBoxCode},onFrozenSuccess,onError);
            }, function () {
            });
        }
        var loadAll = function () {
            SampleTypeService.query({},onSampleTypeSuccess, onError);//样本类型
            FrozenBoxTypesService.query({},onFrozenBoxTypeSuccess, onError);//盒子类型
            EquipmentService.query({},onEquipmentSuccess, onError);//设备
            ProjectService.query({},onProjectSuccess, onError)//项目
            loadBox()
        };
        loadAll();

        //盒子类型
        function onFrozenBoxTypeSuccess(data) {
            vm.frozenBoxTypeOptions = data;
        }
        //盒子类型 17:10*10 18:8*8
        var arr1 = [];
        var arr2 = [];
        vm.boxTypeConfig = {
            valueField:'id',
            labelField:'frozenBoxTypeName',
            maxItems: 1,
            onChange:function(value){
                var countRows = hotRegisterer.getInstance('my-handsontable').countRows();
                var countCols = hotRegisterer.getInstance('my-handsontable').countCols();
                console.log(countRows)
                if(value == 18){
                    if(countRows != 8){
                        for(var i = 0; i < countRows; i++){
                            arr1 = vm.frozenTubeArray[i].splice(countRows-2,2);
                        }
                    }

                }else{
                    // if(countRows < 10){
                        for(var i = 0; i < 2; i++) {
                            vm.frozenTubeArray[i].push("");
                        }
                    // }

                }


                // for(var i = 8; i < countRows; i++){
                //     for(var j = 0; j <countCols; j++){
                //         if(value == 18){
                //             hotRegisterer.getInstance('my-handsontable').getCell(i,j).style.backgroundColor = 'rgb(0,0,0)';
                //         }else{
                //             hotRegisterer.getInstance('my-handsontable').getCell(i,j).style.backgroundColor = '#fff'
                //         }
                //     }
                // }
                // if(value == 18){
                //     size = 8;
                //     // console.log(hotRegisterer.getInstance('my-handsontable'))
                //     // hotRegisterer.getInstance('my-handsontable').render()
                // }else{
                //     size = 10;
                // }
                //  init(size);
                hotRegisterer.getInstance('my-handsontable').render()
            }
        };

        //样本类型
        function onSampleTypeSuccess(data) {
            vm.sampleTypeOptions = data;
        }
        vm.sampleTypeConfig = {
            valueField:'id',
            labelField:'sampleTypeName',
            maxItems: 1,
            onChange:function (value) {
                if(value != 16){
                    for(var i =0; i <  vm.frozenTubeArray.length; i++){
                        for(var j = 0; j < vm.frozenTubeArray[i].length; j++){
                            vm.frozenTubeArray[i][j].sampleTypeId = value;
                            for(var k = 0; k < vm.sampleTypeOptions.length; k++){
                                if(vm.frozenTubeArray[i][j].sampleTypeId == vm.sampleTypeOptions[k].id){
                                    vm.frozenTubeArray[i][j].sampleTypeName = vm.sampleTypeOptions[k].sampleTypeName;
                                    vm.frozenTubeArray[i][j].sampleTypeCode = vm.sampleTypeOptions[k].sampleTypeCode;
                                }
                            }
                        }
                    }
                }else{
                    for(var i =0; i <  vm.frozenTubeArray.length; i++){
                        for(var j = 0; j < vm.frozenTubeArray[i].length; j++){
                            switch(j){
                                case 0:
                                    vm.frozenTubeArray[i][j].sampleTypeCode = 'S_TYPE_00001';
                                    break;
                                case 1:
                                    vm.frozenTubeArray[i][j].sampleTypeCode = 'S_TYPE_00002';
                                    break;
                                case 2:
                                    vm.frozenTubeArray[i][j].sampleTypeCode = 'S_TYPE_00003';
                                    break;
                                case 3:
                                    vm.frozenTubeArray[i][j].sampleTypeCode = 'S_TYPE_00004';
                                    break;
                                case 4:
                                    vm.frozenTubeArray[i][j].sampleTypeCode = 'S_TYPE_00005';
                                    break;
                                case 5:
                                    vm.frozenTubeArray[i][j].sampleTypeCode = 'S_TYPE_00006';
                                    break;
                                case 6:
                                    vm.frozenTubeArray[i][j].sampleTypeCode = 'S_TYPE_00007';
                                    break;
                                case 7:
                                    vm.frozenTubeArray[i][j].sampleTypeCode = 'S_TYPE_00008';
                                    break;
                                case 8:
                                    vm.frozenTubeArray[i][j].sampleTypeCode = 'S_TYPE_00009';
                                    break;
                                case 9:
                                    vm.frozenTubeArray[i][j].sampleTypeCode = 'S_TYPE_00010';
                                    break;
                            }
                        }}
                }

                hotRegisterer.getInstance('my-handsontable').render()
            }
        };
        if(vm.transportRecord.projectId){
            ProjectSitesByProjectIdService.query({id:vm.transportRecord.projectId},onProjectSitesSuccess,onError)
        }
        //项目编码
        function onProjectSuccess(data) {
            vm.projectOptions = data;
        }
        vm.projectConfig = {
            valueField:'id',
            labelField:'projectName',
            maxItems: 1,
            onChange:function(value){
                for(var i = 0; i < vm.projectOptions.length;i++){
                    if(value == vm.projectOptions[i].id){
                        vm.transportRecord.projectCode = vm.projectOptions[i].projectCode;
                        vm.transportRecord.projectName = vm.projectOptions[i].projectName
                    }
                }

                ProjectSitesByProjectIdService.query({id:value},onProjectSitesSuccess,onError)
            }
        };

        //项目点
        function onProjectSitesSuccess(data) {
            vm.projectSitesOptions = data;
        }
        vm.projectSitesConfig = {
            valueField:'id',
            labelField:'projectSiteName',
            maxItems: 1,
            onChange:function (value) {
                for(var i = 0; i < vm.projectSitesOptions.length;i++){
                    if(value == vm.projectSitesOptions[i].id){
                        vm.transportRecord.projectSiteCode = vm.projectSitesOptions[i].projectSiteCode
                        vm.transportRecord.projectSiteName = vm.projectSitesOptions[i].projectSiteName
                    }
                }
            }
        };
        // 满意度1-10
        vm.satisfactionOptions = [
            {id:"10",name:"非常满意"},
            {id:"9",name:"较满意"},
            {id:"8",name:"满意"},
            {id:"7",name:"有少量空管"},
            {id:"6",name:"有许多空管"},
            {id:"5",name:"有大量空管"},
            {id:"4",name:"有少量空孔"},
            {id:"3",name:"有少量错位"},
            {id:"2",name:"有大量错位"},
            {id:"1",name:"非常不满意"}
        ];
        vm.satisfactionConfig = {
            valueField:'id',
            labelField:'name',
            maxItems: 1
        };

        //转运状态
        vm.statusOptions = [
            {id:"1001",name:"进行中"},
            {id:"1002",name:"待入库"},
            {id:"1003",name:"已入库"},
            {id:"1004",name:"已作废"}
        ];
        vm.statusConfig = {
            valueField:'id',
            labelField:'name',
            maxItems: 1

        };
        //设备
        function onEquipmentSuccess(data) {
            vm.frozenBoxPlaceOptions = data;
        }
        vm.frozenBoxPlaceConfig = {
            valueField:'id',
            labelField:'equipmentCode',
            maxItems: 1,
            onChange:function (value) {
                AreasByEquipmentIdService.query({id:value},onAreaSuccess, onError);
                for(var i = 0; i < vm.frozenBoxPlaceOptions.length; i++){
                    if(value == vm.frozenBoxPlaceOptions[i].id){
                        vm.box.equipmentCode = vm.frozenBoxPlaceOptions[i].equipmentCode
                    }
                }
            }
        };
        //区域
        function onAreaSuccess(data) {
            vm.frozenBoxAreaOptions = data;

        }
        vm.frozenBoxAreaConfig = {
            valueField:'id',
            labelField:'areaCode',
            maxItems: 1,
            onChange:function (value) {
                for(var i = 0; i < vm.frozenBoxAreaOptions.length; i++){
                    if(value == vm.frozenBoxAreaOptions[i].id){
                        vm.box.areaCode = vm.frozenBoxAreaOptions[i].areaCode
                    }
                }
                SupportacksByAreaIdService.query({id:value},onShelfSuccess, onError)

            }
        };
        //架子
        function onShelfSuccess(data) {
            vm.frozenBoxShelfOptions = data;

        }
        vm.frozenBoxShelfConfig = {
            valueField:'id',
            labelField:'supportRackCode',
            maxItems: 1,
            onChange:function (value) {
                for(var i = 0; i < vm.frozenBoxShelfOptions.length; i++){
                    if(value == vm.frozenBoxShelfOptions[i].id){
                        vm.box.supportRackCode = vm.frozenBoxShelfOptions[i].areaCode
                    }
                }
            }
        };

        function onBoxSuccess(data) {
            vm.dtOptions = DTOptionsBuilder.newOptions()
                .withOption('data', data)
                .withOption('info', false)
                .withOption('paging', false)
                .withOption('sorting', false)
                .withScroller()
                .withOption('scrollY', 450)
                .withOption('rowCallback', rowCallback);
        }
        function onError(error) {
            AlertService.error(error.data.message);
        }
        //盒子信息
        vm.dtOptions = DTOptionsBuilder.newOptions()
            .withOption('paging', false)
            .withOption('sorting', false);
        vm.dtColumns = [
            DTColumnBuilder.newColumn('frozenBoxCode').withTitle('冻存盒号')
        ];


        vm.dtInstance = {};
        //导入冻存盒
        var modalInstance;
        function importFrozenStorageBox() {
            modalInstance = $uibModal.open({
                animation: true,
                templateUrl: 'app/bizs/transport-record/frozen-storage-box-modal.html',
                controller: 'FrozenStorageBoxModalController',
                controllerAs:'vm',
                size:'lg',
                resolve: {
                    items: function () {
                        return {
                            transhipId :vm.transportRecord.id
                        }
                    }
                }

            });
            modalInstance.result.then(function (data) {
                loadBox();
            });
        }
        //保存保存记录
        function saveRecord() {
            TranshipSaveService.update(vm.transportRecord,onSaveTranshipRecordSuccess,onError)
        }
        function onSaveTranshipRecordSuccess(data) {
            AlertService.success("保存转运记录成功");
        }

        function saveBox(){
            obox.frozenBoxDTOList = [];
            obox.frozenBoxDTOList.push(vm.box);
            obox.columnsInShelf = vm.boxRowCol.charAt(0);
            obox.rowsInShelf = vm.boxRowCol.charAt(vm.boxRowCol.length - 1);
            TranshipBoxService.update(obox,onSaveBoxSuccess,onError);
        }
        function openCalendar (date) {
            vm.datePickerOpenStatus[date] = true;
        }
        function onSaveBoxSuccess() {
            AlertService.success("保存冻存盒成功！");
        }
        function onSaveSuccess () {
            $state.go('transport-record');
        }
        function onSaveError () {

        }
        //转运入库
        vm.stockIn = function () {
            TranshipStockInService.saveStockIn(vm.transportRecord.transhipCode).then(function () {
                AlertService.success("入库成功！");
            })
        };
        // function onStockInSuccess(data) {
        //     AlertService.success("入库成功！");
        // }
        //点击冻存盒行
        var count = 0;
        function someClickHandler(td,boxInfo) {
            if(count == 0){
                $(td).closest('table').find('.rowLight').removeClass("rowLight");
                $(td).addClass('rowLight');
                frozenBoxByCodeService.get({code:boxInfo.frozenBoxCode},onFrozenSuccess,onError);

            }else{
                var modalInstance = $uibModal.open({
                    animation: true,
                    templateUrl: 'boxModal.html',
                    size: 'sm',
                    controller: 'BoxInstanceCtrl',
                    controllerAs: 'ctrl'


                });
                modalInstance.result.then(function (flag) {
                    $(td).closest('table').find('.rowLight').removeClass("rowLight");
                    $(td).addClass('rowLight');
                    //true:保存 false:不保存
                    if(flag){
                        saveBox()
                    }
                    frozenBoxByCodeService.get({code:boxInfo.frozenBoxCode},onFrozenSuccess,onError);
                }, function () {
                });
            }
            count++;
        }
        function onFrozenSuccess(data) {
            vm.box = data;
            if(vm.box.equipmentId){
                AreasByEquipmentIdService.query({id:vm.box.equipmentId},onAreaSuccess, onError);
            }
            if(vm.box.areaId){
                SupportacksByAreaIdService.query({id:vm.box.areaId},onShelfSuccess, onError)
            }
            if(vm.box.supportRackId){
                vm.boxRowCol =  vm.box.columnsInShelf + vm.box.rowsInShelf;
            }
            for(var k = 0; k < vm.box.frozenTubeDTOS.length; k++){
                var tube = vm.box.frozenTubeDTOS[k];
                vm.frozenTubeArray[getTubeRowIndex(tube.tubeRows)][getTubeColumnIndex(tube.tubeColumns)] = tube;
                vm.frozenTubeArray[getTubeRowIndex(tube.tubeRows)][getTubeColumnIndex(tube.tubeColumns)].frozenBoxCode = vm.box.frozenBoxCode
            }
            hotRegisterer.getInstance('my-handsontable').render();
        }
        function rowCallback(nRow, oData, iDisplayIndex, iDisplayIndexFull)  {
            $('td', nRow).unbind('click');
            $('td', nRow).bind('click', function() {
                var td = this;
                $scope.$apply(function() {
                    vm.someClickHandler(td,oData);
                });
            });
            if (vm.box && vm.box.frozenBoxCode == oData.frozenBoxCode){
                $('td', nRow).addClass('rowLight');
            }
            return nRow;
        }

    }
    function BoxInstanceCtrl($uibModalInstance) {
        var ctrl = this;
        ctrl.ok = function () {
            $uibModalInstance.close(true);
        };
        ctrl.unSave = function () {
            $uibModalInstance.close(false);
        };
        ctrl.cancel = function () {
            $uibModalInstance.dismiss('cancel');
        };
    }
})();
