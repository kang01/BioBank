/**
 * Created by gaokangkang on 2017/3/14.
 */
(function() {
    'use strict';

    angular
        .module('bioBankApp')
        .controller('TransportRecordNewController', TransportRecordNewController);

    TransportRecordNewController.$inject = ['$scope','hotRegisterer','TransportRecordService','DTOptionsBuilder','DTColumnBuilder','$uibModal','$state','SampleTypeService','AlertService','FrozenBoxTypesService','FrozenBoxByIdService'];

    function TransportRecordNewController($scope,hotRegisterer,TransportRecordService,DTOptionsBuilder,DTColumnBuilder,$uibModal,$state,SampleTypeService,AlertService,FrozenBoxTypesService,FrozenBoxByIdService) {
        var vm = this;
        vm.datePickerOpenStatus = {};
        vm.transportRecord = {};
        vm.openCalendar = openCalendar;
        vm.importFrozenStorageBox = importFrozenStorageBox; //导入冻存盒
        vm.someClickHandler = someClickHandler;
        var microtubesList;
        vm.transportRecord =
            {
                "effectiveSampleNumber": 2,
                "emptyHoleNumber": 12,
                "emptyTubeNumber": 11,
                "frozenBoxDTOList": [
                ],
                "frozenBoxNumber": 10,
                "memo": "",
                "projectCode": "P_00001",
                "projectId": 1,
                "projectName": "心血管高危筛查项目",
                "projectSiteCode": "P_SITE_00001",
                "projectSiteId": 2,
                "projectSiteName": "心血管高危筛查项目天坛医院项目点",
                "receiveDate": new Date("2017-03-25"),
                "receiver":"张主任",
                "sampleNumber":20,
                "sampleSatisfaction": 1,
                "trackNumber": "1222'",
                "transhipBatch": "232323",
                "transhipDate": new Date("2017-03-25"),
                "status": "1",
                "transhipState": "1002"
            };
        var tArray = new Array();
        var size = 10;
        var init = function (size) {
            for(var k=0; k < size; k++){
                tArray[k] = new Array();
                for(var j=0;j < size; j++){
                    tArray[k][j] = {};
                }
            }
            // microtubesList = vm.transportRecord.frozenBoxDTOList[0].frozenTubeDTOS;
            // for(var i = 0; i < microtubesList.length; i++){
            //     tArray[microtubesList[i].tubeRows-1][microtubesList[i].tubeColumns-1] = microtubesList[i]
            // }
            // vm.db = {
            //     items: tArray
            // };

        };
        init(size);
        var htm;
        vm.myCustomRenderer = function(hotInstance, td, row, col, prop, value, cellProperties) {
            // Handsontable.renderers.TextRenderer.apply(this, arguments);

            if(value.sampleTempCode){
                htm = value.sampleTempCode
            }else{
                if(Object.keys(value).length == 0){
                    console.log(value)
                    htm = ""
                }else{
                    if(value.status == 1){
                        td.style.backgroundColor = value.backColor;
                    }
                    if(value.status == 2){
                        td.style.background = 'linear-gradient(to right,'+value.backColor+',50%,rgba(0,0,0,1)';
                    }
                    if(value.status == 3){

                    }
                    td.style.position = 'relative';
                    if(vm.flagStatus || vm.exchangeFlag || vm.remarkFlag) {
                        cellProperties.readOnly = true;
                    }else{
                        cellProperties.readOnly = false;
                    }
                    if(value.frozenTubeCode){
                        htm = "<div>"+value.frozenTubeCode+"</div>"+
                            "<div id='microtubesId' style='display: none'>"+value.frozenTubeCode+"</div>" +
                            "<div id='microtubesStatus' style='display: none'>"+value.status+"</div>"+
                            "<div id='microtubesRemark' style='display: none'>"+value.memo+"</div>"+
                            "<div id='microtubesRow' style='display: none'>"+value.tubeRows+"</div>"+
                            "<div id='microtubesCol' style='display: none'>"+value.tubeColumns+"</div>"+
                            "<div ng-if="+value.memo+" class='triangle-topright' style='position: absolute;top:0;right: 0;'></div>"
                    }else{
                        htm = value
                    }
                }

            }


            td.innerHTML = htm;

        };
        var remarkArray;
        vm.settings = {
            colHeaders : ['1','2','3','4','5','6','7','8','9','10'],
            rowHeaders : ['A','B','C','D','E','F','G','H','I','J'],
            data:tArray,
            // colWidths:100,
            renderer:vm.myCustomRenderer,
            fillHandle:false,
            comments: true,
            stretchH: 'all',
            cell: [
                {col: 1, row: 1, comment: 'Hello Comment'}
            ],
            onAfterInit: function () {
                // hotRegisterer.getInstance('my-handsontable').loadData(tArray);
            },
            onAfterSelectionEnd:function (row, col, row2, col2) {
                remarkArray = this.getData(row,col,row2,col2);
                vm.remarkFlag = true;
                if(window.event.ctrlKey){
                    vm.exchangeFlag = true;
                    var txt = '<div class="temp" style="position:absolute;top:0;bottom:0;left:0;right:0;border:2px dotted #5292F7;"></div>';
                    $(this.getCell(row,col)).append(txt);
                    if(tArray[row][col] == ""){
                        domArray.push({tubeRows:row+1,tubeColumns:col+1});
                    }else{
                        domArray.push(tArray[row][col]);
                    }

                    console.log(JSON.stringify(domArray))
                }else{
                    domArray = [];
                    $(".temp").remove();
                }
                //管子
                var microtubes = {};
                if(vm.operateStatus == 1) {
                    if(vm.flagStatus){
                        var microtubesIdDom = $(this.getCell(row, col)).find("#microtubesId");
                        var microtubesStatusDom = $(this.getCell(row, col)).find("#microtubesStatus");
                        //
                        if(microtubes.id != microtubesIdDom.text()){
                            microtubes.status = microtubesStatusDom.text();
                            microtubes.id = microtubesIdDom.text();
                        }else{

                        }
                        //正常
                        if(microtubes.status == 1){
                            microtubesStatusDom.text('2');
                            tArray[row][col].status = 2;
                            // for(var i = 0; i < microtubesList.length; i++){
                            //     if(microtubesList[i].tubeRows-1 == row && microtubesList[i].tubeColumns-1 == col){
                            //         microtubesList[i].status = 2;
                            //         microtubesList[i].background = 'linear-gradient(to right,'+operateColor+',50%,rgba(0,0,0,1)';
                            //         operateColor =  microtubesList[i].backColor;
                            //         this.getCell(row,col).style.background = 'linear-gradient(to right,'+operateColor+',50%,rgba(0,0,0,1)';
                            //     }
                            // }


                        }
                        //空管
                        if(microtubes.status == 2){
                            this.getCell(row,col).style.background = '';
                            this.getCell(row,col).style.backgroundColor = '#ffffff';
                            microtubesStatusDom.text('3');
                            // microtubesList[i].status = 3
                            tArray[row][col].status = 3;
                        }
                        //空孔
                        if(microtubes.status == 3){
                            var txt = '<div class="abnormal" style="position:absolute;top:0;bottom:0;left:0;right:0;border:3px solid red;"></div>';
                            $(this.getCell(row,col)).append(txt);
                            this.getCell(row,col).style.backgroundColor = '';
                            // this.getCell(row,col).style.border = '2px solid red';
                            microtubesStatusDom.text('4');
                            tArray[row][col].status = 4;
                        }
                        //异常
                        if(microtubes.status == 4){
                            this.getCell(row,col).style.backgroundColor = operateColor;
                            // this.getCell(row,col).style.outline = '';
                            $(".abnormal").remove();
                            microtubesStatusDom.text('1');
                            tArray[row][col].status = 1;
                            // for(var i = 0; i < microtubesList.length; i++){
                            //     if(microtubesList[i].tubeRows-1 == row && microtubesList[i].tubeColumns-1 == col){
                            //         this.getCell(row,col).style.backgroundColor =  microtubesList[i].backColor;
                            //         microtubesList[i].status = 1;
                            //     }
                            // }
                        }
                        microtubes.id = $(this.getCell(row, col)).find("#microtubesId").text();
                        microtubes.status = $(this.getCell(row, col)).find("#microtubesStatus").text();
                        hotRegisterer.getInstance('my-handsontable').render();
                    }
                    console.log(JSON.stringify(tArray))
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
                console.log(source);
                if(source == 'loadData'){
                    return;
                }
            }
        };

        var loadAll = function () {
            SampleTypeService.query({},onSampleTypeSuccess, onError);
            FrozenBoxTypesService.query({},onFrozenBoxTypeSuccess, onError)
        };
        loadAll();
        function onFrozenBoxTypeSuccess(data) {
            vm.frozenBoxTypeOptions = data;
        }
        function onSampleTypeSuccess(data) {
            vm.sampleTypeOptions = data;
        }
        function onError(error) {
            AlertService.error(error.data.message);
        }
        //盒子类型
        vm.typeConfig = {
            valueField:'id',
            labelField:'frozenBoxTypeName',
            maxItems: 1,
            onChange:function(value){
                var countRows = hotRegisterer.getInstance('my-handsontable').countRows();
                var countCols = hotRegisterer.getInstance('my-handsontable').countCols();

                for(var i = 0; i < countRows; i++){
                    for(var j = 8; j <countCols; j++){
                        if(value == 18){
                            hotRegisterer.getInstance('my-handsontable').getCell(i,j).style.backgroundColor = 'rgba(51,51,51,0.3)';
                        }else{
                            hotRegisterer.getInstance('my-handsontable').getCell(i,j).style.backgroundColor = '#fff'
                        }
                    }
                }
               // if(value == 18){
               //     size = 8;
               //     // console.log(hotRegisterer.getInstance('my-handsontable'))
               //     // hotRegisterer.getInstance('my-handsontable').render()
               // }else{
               //     size = 10;
               // }
               //  init(size);
               //  hotRegisterer.getInstance('my-handsontable').render()
            }
        };
        //样本类型
        vm.sampleTypeConfig = {
            valueField:'sampleTypeCode',
            labelField:'sampleTypeName',
            maxItems: 1,
            onChange:function (value) {
                var countRows = hotRegisterer.getInstance('my-handsontable').countRows();
                var countCols = hotRegisterer.getInstance('my-handsontable').countCols();
                for(var i = 0; i < countRows; i++){
                    for(var j = 0; j <countCols; j++){
                        //血浆-红
                        if(value == 'S_TYPE_00001' || value == 'S_TYPE_00002'){
                            hotRegisterer.getInstance('my-handsontable').getCell(i,j).style.backgroundColor = 'rgba(204,153,255,0.3)';
                        }
                        //白细胞-白
                        if(value == 'S_TYPE_00003'){
                            hotRegisterer.getInstance('my-handsontable').getCell(i,j).style.backgroundColor = 'rgba(255,255,255,0.3)';
                        }
                        //白细胞-灰
                        if(value == 'S_TYPE_00004'){
                            hotRegisterer.getInstance('my-handsontable').getCell(i,j).style.backgroundColor = 'rgba(192,192,192,0.3)';
                        }
                        //血浆-绿
                        if(value == 'S_TYPE_00005'|| value == 'S_TYPE_00006'){
                            hotRegisterer.getInstance('my-handsontable').getCell(i,j).style.backgroundColor = 'rgba(0,255,0,0.3)';
                        }
                        //血清
                        if(value == 'S_TYPE_00007'|| value == 'S_TYPE_00008'){
                            hotRegisterer.getInstance('my-handsontable').getCell(i,j).style.backgroundColor = 'rgba(255,0,0,0.3)';
                        }
                        //尿
                        if(value == 'S_TYPE_00009'|| value == 'S_TYPE_00010'){
                            hotRegisterer.getInstance('my-handsontable').getCell(i,j).style.backgroundColor = 'rgba(255,255,0,0.3)';
                        }
                        //99
                        // if(value == 'S_TYPE_00011'){
                        //     hotRegisterer.getInstance('my-handsontable').getCell(i,j).style.backgroundColor = 'rgba(255,255,0,0.3)';
                        // }
                    }
                }

            }
        };
        vm.statusOptions = [
            {id:"1",name:"进行中"},
            {id:"2",name:"待入库"},
            {id:"3",name:"已入库"},
            {id:"4",name:"已作废"}
        ];
        vm.statusConfig = {
            valueField:'id',
            labelField:'name',
            maxItems: 1

        };

        //operateStatus 1.状态 2:换位 3.批注
        vm.operateStatus;
        //单元格操作的数据
        var domArray = [];
        var operateColor;


        //修改样本状态 正常、空管、空孔、异常
        vm.flagStatus = false;
        vm.editStatus = function () {
            vm.operateStatus = 1;
            // hotRegisterer.getInstance('my-handsontable').render();
        };
        //换位
        vm.exchangeFlag = false;
        var exchangeCount = 0;
        vm.exchange = function () {
            if(vm.exchangeFlag && domArray.length == 2){
                var row = domArray[0].tubeRows;
                var col = domArray[0].tubeColumns;
                var row1 = domArray[1].tubeRows;
                var col1 = domArray[1].tubeColumns;

                tArray[row1-1][col1-1] = domArray[0];
                tArray[row1-1][col1-1].tubeRows = row1;
                tArray[row1-1][col1-1].tubeColumns = col1;
                tArray[row-1][col-1] = domArray[1];
                tArray[row-1][col-1].tubeRows = row;
                tArray[row-1][col-1].tubeColumns = col;

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
        vm.microtubesRemark = function () {
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
                        for(var i = 0; i < tArray.length; i++){
                            for(var j = 0; j < tArray[i].length; j++){
                                    if(selectedItem.remarkArray.frozenTubeCode == ''){
                                        tArray[i][j].memo = selectedItem.remarkArray[i][j].memo;
                                    }
                            }
                        }
                        hotRegisterer.getInstance('my-handsontable').render();
                    });
            }
        };
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
                // backdrop:'static'
                // resolve: {
                //     items: function () {
                //         return {
                //             adviceData:item,
                //             type:type
                //         }
                //     }
                // }

            });
            modalInstance.result.then(function (data) {
                console.log(JSON.stringify(data));
                vm.transportRecord.frozenBoxDTOList = data;
                // vm.dtOptions = DTOptionsBuilder.newOptions()

                vm.dtOptions = DTOptionsBuilder.newOptions()
                    .withOption('data', vm.transportRecord.frozenBoxDTOList)
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
        this.saveRecord = function () {
            // console.log(JSON.stringify(vm.transportRecord));
            TransportRecordService.save(vm.transportRecord, onSaveSuccess, onSaveError);
        };
        function openCalendar (date) {
            vm.datePickerOpenStatus[date] = true;
        }
        function onSaveSuccess () {
            // $state.go('transport-record');
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
            DTColumnBuilder.newColumn('projectSiteCode').withTitle('冻存盒号')
        ];
        function someClickHandler(td,boxInfo) {
            $(td).closest('table').find('.highLight').removeClass("highLight");
            $(td).addClass('highLight');
            console.log(boxInfo);
            init(10);
            vm.box = boxInfo;
            microtubesList = vm.box.frozenTubeDTOS;
            if(microtubesList.length){
                for(var i = 0; i < microtubesList.length; i++){
                    tArray[microtubesList[i].tubeRows-1][microtubesList[i].tubeColumns-1] = microtubesList[i];

                }
            }else{
                for(var i = 0; i < tArray.length; i++){
                    for (var j = 0; j <tArray[i].length; j++){
                        tArray[i][j].sampleTempCode = vm.box.projectSiteCode + "-r" + i + "c" + j;
                    }
                }
            }
            vm.transportRecord.frozenBoxDTOList[0].frozenTubeDTOS = tArray;
            // console.log(JSON.stringify(tArray))
            hotRegisterer.getInstance('my-handsontable').render();
            // FrozenBoxByIdService.get({id:boxInfo.id},frozenBoxSuccess,onError());


        }
        function frozenBoxSuccess(data) {
            vm.box = data;
            microtubesList = data.frozenTubeDTOS;
            for(var i = 0; i < microtubesList.length; i++){
                tArray[microtubesList[i].tubeRows-1][microtubesList[i].tubeColumns-1] = microtubesList[i];
            }
            hotRegisterer.getInstance('my-handsontable').loadData(tArray);
            hotRegisterer.getInstance('my-handsontable').render();
        }
        function rowCallback(nRow, aData, iDisplayIndex, iDisplayIndexFull) {
            $('td', nRow).unbind('click');
            $('td', nRow).bind('click', function() {
                var td = this;
                $scope.$apply(function() {
                    vm.someClickHandler(td,aData);
                });
            });
            return nRow;
        }

    }
})();
