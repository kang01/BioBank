/**
 * Created by gaokangkang on 2017/3/14.
 */
(function() {
    'use strict';

    angular
        .module('bioBankApp')
        .controller('TransportRecordNewController', TransportRecordNewController);

    TransportRecordNewController.$inject = ['$scope','hotRegisterer','TransportRecordService','DTOptionsBuilder','DTColumnBuilder','$uibModal','$state','entity','frozenBoxByCodeService',
        'SampleTypeService','AlertService','FrozenBoxTypesService','FrozenBoxByIdService','EquipmentService','AreasByEquipmentIdService','SupportacksByAreaIdService','ProjectService','ProjectSitesByProjectIdService'];

    function TransportRecordNewController($scope,hotRegisterer,TransportRecordService,DTOptionsBuilder,DTColumnBuilder,$uibModal,$state,entity,frozenBoxByCodeService,
                                          SampleTypeService,AlertService,FrozenBoxTypesService,FrozenBoxByIdService,EquipmentService,AreasByEquipmentIdService,SupportacksByAreaIdService,ProjectService,ProjectSitesByProjectIdService) {
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

        if(vm.transportRecord.transhipDate){
            vm.transportRecord.transhipDate = new Date(entity.transhipDate);
        }
        if(vm.transportRecord.receiveDate){
            vm.transportRecord.receiveDate = new Date(entity.receiveDate);
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
                value.status = "3003";//冻存管状态3001：正常，3002：空管，3003：空孔；3004：异常
                value.tubeRows = getTubeRows(row);
                value.tubeColumns = getTubeColumns(col);
                value.memo = ""
            }
            // else{
            //     var tube = {};
            //     tube.sampleCode = value;
            //     tube.status = "3003",
            //     tube.tubeRows = row,
            //     tube.tubeColumns = col,
            //     tube.memo = ""
            //     value = tube;
            // }
            //样本类型
            if(value.sampleTypeId){
                changeSampleType(value.sampleTypeId,td);
            }
            //样本状态 status3001：正常，3002：空管，3003：空孔；3004：异常
            if(value.status){
                changeSampleStatus(value.status,row,col,td,cellProperties)
            }

            htm = "<div ng-if='value.sampleCode'>"+value.sampleCode+"</div>"+
                "<div id='microtubesId' style='display: none'>"+value.sampleCode+"</div>" +
                "<div id='microtubesStatus' style='display: none'>"+value.status+"</div>"+
                "<div id='microtubesRemark' style='display: none'>"+value.memo+"</div>"+
                "<div id='microtubesRow' style='display: none'>"+value.tubeRows+"</div>"+
                "<div id='microtubesCol' style='display: none'>"+value.tubeColumns+"</div>"+
                "<div ng-if="+value.memo+" class='triangle-topright' style='position: absolute;top:0;right: 0;'></div>"

            td.innerHTML = htm;
            console.log(JSON.stringify(vm.frozenTubeArray))

        };
        //修改样本类型
        function changeSampleType(sampleTypeId,td) {
            //血浆
            if(sampleTypeId == 5 || sampleTypeId == 39){
                td.style.backgroundColor = 'rgba(204,153,255,0.3)';
            }
            //白细胞
            if(sampleTypeId == 6){
                td.style.backgroundColor = 'rgba(255,255,255,0.3)';
            }
            //白细胞灰
            if(sampleTypeId == 7){
                td.style.backgroundColor = 'rgba(192,192,192,0.3)';
            }
            //血浆绿
            if(sampleTypeId == 8 || sampleTypeId == 40){
                td.style.backgroundColor = 'rgba(0,255,0,0.3)';
            }
            //血清-红
            if(sampleTypeId == 9 || sampleTypeId == 10){
                td.style.backgroundColor = 'rgba(255,0,0,0.3)';
            }
            //尿-黄
            if(sampleTypeId == 11 || sampleTypeId == 38){
                td.style.backgroundColor = 'rgba(255,255,0,0.3)';
            }
        }
        //修改样本状态正常、空管、空孔、异常
        function changeSampleStatus(sampleStatus,row,col,td,cellProperties) {
            operateColor = td.style.backgroundColor;
            //正常
            if(sampleStatus == 3001){

            }
            //空管
            if(sampleStatus == 3002){
                td.style.background = 'linear-gradient(to right,'+operateColor+',50%,rgba(0,0,0,1)';
            }
            //空孔
            if(sampleStatus == 3003){
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

                    hotRegisterer.getInstance('my-handsontable').render()
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
                    console.log(JSON.stringify(selectedItem));
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
        var loadAll = function () {
            SampleTypeService.query({},onSampleTypeSuccess, onError);
            FrozenBoxTypesService.query({},onFrozenBoxTypeSuccess, onError);
            EquipmentService.query({},onEquipmentSuccess, onError);
            ProjectService.query({},onProjectSuccess, onError)
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
                console.log(value);
                var countRows = hotRegisterer.getInstance('my-handsontable').countRows();
                var countCols = hotRegisterer.getInstance('my-handsontable').countCols();

                if(value == 18){
                    // arr = vm.frozenTubeArray.splice(countRows-2,2);
                    // console.log(arr);
                    for(var i = 0; i < countRows; i++){
                        //     for(var j = 0; j <countCols; j++){
                        arr1 = vm.frozenTubeArray[i].splice(countRows-2,2);
                        //     }
                    }
                    arr2 = vm.frozenTubeArray.splice(countRows-2,2);
                    console.log(JSON.stringify(arr2+"$$$$$$$$$$$$$$$"));


                }else{

                    for(var i = 0; i < countRows; i++) {
                        vm.frozenTubeArray[i].push({});
                    }
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
                hotRegisterer.getInstance('my-handsontable').render()
            }
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
                AreasByEquipmentIdService.query({id:value},onAreaSuccess, onError)
                for(var i = 0; i < vm.frozenBoxPlaceOptions.length; i++){
                    if(value == vm.frozenBoxPlaceOptions[i].id){
                        vm.box.equipmentCode = vm.frozenBoxPlaceOptions[i].equipmentCode
                    }
                }
            }
        };

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

        function onError(error) {
            AlertService.error(error.data.message);
        }



        vm.dtInstance = {};
        //导入冻存盒
        var modalInstance;
        function importFrozenStorageBox() {
            modalInstance = $uibModal.open({
                animation: true,
                templateUrl: 'app/admin/transport-record/frozen-storage-box-modal.html',
                controller: 'FrozenStorageBoxModalController',
                controllerAs:'vm',
                size:'lg'

            });
            modalInstance.result.then(function (data) {
                // vm.transportRecord.frozenBoxDTOList = data;

                vm.dtOptions = DTOptionsBuilder.newOptions()
                    .withOption('data', data)
                    .withOption('info', false)
                    .withOption('paging', false)
                    .withOption('sorting', false)
                    .withScroller()
                    .withOption('scrollY', 450)
                    .withOption('rowCallback', rowCallback);

                vm.dtInstance.rerender();
            });
        }
        //保存
        vm.saveRecord = function () {

            for(var i = 0; i < vm.transportRecord.frozenBoxDTOList.length; i++){
                vm.transportRecord.frozenBoxDTOList[i].frozenTubeDTOS = [];
                vm.transportRecord.frozenBoxDTOList[i].rowsInShelf = vm.boxLocation.charAt(0);
                vm.transportRecord.frozenBoxDTOList[i].columnsInShelf = vm.boxLocation.charAt(1);
                for(var k = 0; k < vm.frozenTubeArray.length; k++){
                    for (var j = 0; j <vm.frozenTubeArray[k].length; j++){
                        if(vm.transportRecord.frozenBoxDTOList[i].frozenBoxCode == vm.frozenTubeArray[k][j].frozenBoxCode){
                            var tubeArray = angular.copy(vm.frozenTubeArray);
                            tubeArray[k][j].tubeColumns = j+1;
                            tubeArray[k][j].tubeRows = String.fromCharCode(k+65);
                            vm.transportRecord.frozenBoxDTOList[i].frozenTubeDTOS.push(tubeArray[k][j]);
                        }
                    }
                }
            }

            console.log(JSON.stringify(vm.transportRecord));
            TransportRecordService.save(vm.transportRecord, onSaveSuccess, onSaveError);
        };
        function openCalendar (date) {
            vm.datePickerOpenStatus[date] = true;
        }
        function onSaveSuccess () {
            $state.go('transport-record');
        }
        function onSaveError () {

        }

        //盒子信息
        vm.dtOptions = DTOptionsBuilder.newOptions()
            .withOption('data', vm.transportRecord.frozenBoxDTOList)
            .withOption('paging', false)
            .withOption('sorting', false)
            .withOption('rowCallback', rowCallback);
        vm.dtColumns = [
            DTColumnBuilder.newColumn('frozenBoxCode').withTitle('冻存盒号')
        ];
        //点击冻存盒行
        function someClickHandler(td,boxInfo) {
            $(td).closest('table').find('.rowLight').removeClass("rowLight");
            $(td).addClass('rowLight');
            frozenBoxByCodeService.get({code:boxInfo.frozenBoxCode},onFrozenSuccess,onError);
        }
        function onFrozenSuccess(data) {
            vm.box = data;
            for(var k = 0; k < vm.box.frozenTubeResponseList.length; k++){
                var tube = vm.box.frozenTubeResponseList[k];
                vm.frozenTubeArray[getTubeRowIndex(tube.tubeRows)][getTubeColumnIndex(tube.tubeColumns)] = tube;
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
})();
