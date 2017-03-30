/**
 * Created by gaokangkang on 2017/3/14.
 */
(function() {
    'use strict';

    angular
        .module('bioBankApp')
        .controller('TransportRecordNewController', TransportRecordNewController);

    TransportRecordNewController.$inject = ['$scope','hotRegisterer','TransportRecordService','DTOptionsBuilder','DTColumnBuilder','$uibModal','$state','entity','FindFrozenBoxAndTubeByBoxService',
        'SampleTypeService','AlertService','FrozenBoxTypesService','FrozenBoxByIdService','EquipmentService','AreasByEquipmentIdService','SupportacksByAreaIdService','ProjectService','ProjectSitesByProjectIdService'];

    function TransportRecordNewController($scope,hotRegisterer,TransportRecordService,DTOptionsBuilder,DTColumnBuilder,$uibModal,$state,entity,FindFrozenBoxAndTubeByBoxService,
                                          SampleTypeService,AlertService,FrozenBoxTypesService,FrozenBoxByIdService,EquipmentService,AreasByEquipmentIdService,SupportacksByAreaIdService,ProjectService,ProjectSitesByProjectIdService) {
        var vm = this;
        vm.datePickerOpenStatus = {};
        vm.transportRecord = entity; //转运记录
        vm.frozenTubeArray = [];//初始管子数据二位数组
        vm.openCalendar = openCalendar; //时间
        vm.importFrozenStorageBox = importFrozenStorageBox; //导入冻存盒
        vm.someClickHandler = someClickHandler; //点击冻存盒的表格行
        vm.transportRecord.transhipDate = new Date(entity.transhipDate);
        vm.transportRecord.receiveDate = new Date(entity.receiveDate);
        var boxStr;
        for(var i = 0; i < vm.transportRecord.frozenBoxDTOList; i++){
            boxStr = vm.transportRecord.frozenBoxDTOList[i].frozenBoxCode
            // FindFrozenBoxAndTubeByBoxService.query({})
        }
        var size = 10;
        var initFrozenTube = function (size) {
            for(var i = 0; i < size; i++){
                vm.frozenTubeArray[i] = [];
                for(var j = 0;j < size; j++){
                    vm.frozenTubeArray[i][j] = "";
                }
            }
        };
        initFrozenTube(size);

        var htm; //渲染管子表格
        vm.myCustomRenderer = function(hotInstance, td, row, col, prop, value, cellProperties) {
            // Handsontable.renderers.TextRenderer.apply(this, arguments);
            // console.log(value);
            var tube = {
                frozenTubeCode:'',
                status:1,
                memo:'',
                tubeRows:row,
                tubeColumns:col,
                sampleTypeCode:'',//样本类型,
            };
            td.style.position = 'relative';

            // if(typeof value == "object"){
            //     if(Object.keys(value).length != 0){
            //         tube = value
            //
            //     }
            //
            // }
            if(value != ""){
                tube.frozenTubeCode = value
            }
            // if(value.sampleTempCode){
            //     tube.frozenTubeCode = '',
            //     tube.status = '1',
            //     tube.memo = '',
            //     tube.sampleTempCode = value.sampleTempCode;
            //     tube.tubeRows = row;
            //     tube.tubeColumns = col;
            // }
            // if(tube.sampleTypeCode){
            //     changeSampleType(tube.sampleTypeCode,td);
            // }

            htm = "<div ng-if='tube.frozenTubeCode'>"+tube.frozenTubeCode+"</div>"+
                "<div id='microtubesId' style='display: none'>"+tube.frozenTubeCode+"</div>" +
                "<div id='microtubesStatus' style='display: none'>"+tube.status+"</div>"+
                "<div id='microtubesRemark' style='display: none'>"+tube.memo+"</div>"+
                "<div id='microtubesRow' style='display: none'>"+tube.tubeRows+"</div>"+
                "<div id='microtubesCol' style='display: none'>"+tube.tubeColumns+"</div>"+
                "<div ng-if="+tube.memo+" class='triangle-topright' style='position: absolute;top:0;right: 0;'></div>"

            td.innerHTML = htm;
            // console.log(JSON.stringify(vm.frozenTubeArray))

        };
        //修改样本类型
        function changeSampleType(code,td) {
            //血浆
            if(code == 'S_TYPE_00001' || code == 'S_TYPE_00002'){
                td.style.backgroundColor = 'rgba(204,153,255,0.3)';
            }
            //白细胞
            if(code == 'S_TYPE_00003'){
                td.style.backgroundColor = 'rgba(255,255,255,0.3)';
            }
            //白细胞灰
            if(code == 'S_TYPE_00004'){
                td.style.backgroundColor = 'rgba(192,192,192,0.3)';
            }
            //血浆绿
            if(code == 'S_TYPE_00005' || code == 'S_TYPE_00006'){
                td.style.backgroundColor = 'rgba(0,255,0,0.3)';
            }
            //血清
            if(code == 'S_TYPE_00007' || code == 'S_TYPE_00008'){
                td.style.backgroundColor = 'rgba(255,0,0,0.3)';
            }
            //血清
            if(code == 'S_TYPE_00009' || code == 'S_TYPE_00010'){
                td.style.backgroundColor = 'rgba(255,255,0,0.3)';
            }
        }
        var remarkArray;//批注
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
                    if(vm.frozenTubeArray[row][col] == ""){
                        domArray.push({tubeRows:row+1,tubeColumns:col+1});
                    }else{
                        domArray.push(vm.frozenTubeArray[row][col]);
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
                            vm.frozenTubeArray[row][col].status = 2;
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
                            vm.frozenTubeArray[row][col].status = 3;
                        }
                        //空孔
                        if(microtubes.status == 3){
                            var txt = '<div class="abnormal" style="position:absolute;top:0;bottom:0;left:0;right:0;border:3px solid red;"></div>';
                            $(this.getCell(row,col)).append(txt);
                            this.getCell(row,col).style.backgroundColor = '';
                            // this.getCell(row,col).style.border = '2px solid red';
                            microtubesStatusDom.text('4');
                            vm.frozenTubeArray[row][col].status = 4;
                        }
                        //异常
                        if(microtubes.status == 4){
                            this.getCell(row,col).style.backgroundColor = operateColor;
                            // this.getCell(row,col).style.outline = '';
                            $(".abnormal").remove();
                            microtubesStatusDom.text('1');
                            vm.frozenTubeArray[row][col].status = 1;
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
                    console.log(JSON.stringify(vm.frozenTubeArray))
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
                // console.log(source);
                if(source == 'edit'){

                    hotRegisterer.getInstance('my-handsontable').render()
                    return;
                }
            },
            // cells:function (row,col,prop) {
            //     console.log(row)
            // }
        };

        var loadAll = function () {
            SampleTypeService.query({},onSampleTypeSuccess, onError);
            FrozenBoxTypesService.query({},onFrozenBoxTypeSuccess, onError);
            EquipmentService.query({},onEquipmentSuccess, onError);
            ProjectService.query({},onProjectSuccess, onError)
        };
        loadAll();
        function onFrozenBoxTypeSuccess(data) {
            vm.frozenBoxTypeOptions = data;
        }
        function onSampleTypeSuccess(data) {
            vm.sampleTypeOptions = data;
        }
        function onEquipmentSuccess(data) {
            vm.frozenBoxPlaceOptions = data;
        }
        //项目编码
        function onProjectSuccess(data) {
            vm.projectOptions = data;
        }
        function onError(error) {
            AlertService.error(error.data.message);
        }
        vm.projectConfig = {
            valueField:'id',
            labelField:'projectName',
            maxItems: 1,
            onChange:function(value){
                ProjectSitesByProjectIdService.query({id:value},onProjectSitesSuccess,onError)
            }
        };
        vm.projectSitesConfig = {
            valueField:'id',
            labelField:'projectSiteName',
            maxItems: 1
        };
        //盒子类型 17:10*10 18:8*8
        var arr1 = [];
        var arr2 = [];
        vm.typeConfig = {
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
        vm.sampleTypeConfig = {
            valueField:'sampleTypeCode',
            labelField:'sampleTypeName',
            maxItems: 1,
            onChange:function (value) {
                var countRows = hotRegisterer.getInstance('my-handsontable').countRows();
                var countCols = hotRegisterer.getInstance('my-handsontable').countCols();
                for(var i = 0; i < countRows; i++){
                    for(var j = 0; j <countCols; j++){
                        if(vm.frozenTubeArray[i][j] == ''){
                            vm.frozenTubeArray[i][j] = {
                                frozenTubeCode:'',
                                status:1,
                                memo:'',
                                tubeRows:'',
                                tubeColumns:'',
                                sampleTypeCode:''
                            }
                        }

                        //血浆-红
                        if(value == 'S_TYPE_00001'){
                            vm.frozenTubeArray[i][j].sampleTypeCode = 'S_TYPE_00001';
                        }
                        if(value == 'S_TYPE_00002'){
                            vm.frozenTubeArray[i][j].sampleTypeCode = 'S_TYPE_00002';
                        }
                        //白细胞-白
                        if(value == 'S_TYPE_00003'){
                            vm.frozenTubeArray[i][j].sampleTypeCode = 'S_TYPE_00003';
                            // hotRegisterer.getInstance('my-handsontable').getCell(i,j).style.backgroundColor = 'rgba(255,255,255,0.3)';
                        }
                        //白细胞-灰
                        if(value == 'S_TYPE_00004'){
                            vm.frozenTubeArray[i][j].sampleTypeCode = 'S_TYPE_00004';
                            // hotRegisterer.getInstance('my-handsontable').getCell(i,j).style.backgroundColor = 'rgba(192,192,192,0.3)';
                        }
                        //血浆-绿1
                        if(value == 'S_TYPE_00005'){
                            vm.frozenTubeArray[i][j].sampleTypeCode = 'S_TYPE_00005';
                            // hotRegisterer.getInstance('my-handsontable').getCell(i,j).style.backgroundColor = 'rgba(0,255,0,0.3)';
                        }
                        //血浆-绿2
                        if( value == 'S_TYPE_00006'){
                            vm.frozenTubeArray[i][j].sampleTypeCode = 'S_TYPE_00006';
                            // hotRegisterer.getInstance('my-handsontable').getCell(i,j).style.backgroundColor = 'rgba(0,255,0,0.3)';
                        }
                        //血清1
                        if(value == 'S_TYPE_00007'){
                            vm.frozenTubeArray[i][j].sampleTypeCode = 'S_TYPE_00007';
                            // hotRegisterer.getInstance('my-handsontable').getCell(i,j).style.backgroundColor = 'rgba(255,0,0,0.3)';
                        }
                        //血清2
                        if(value == 'S_TYPE_00008'){
                            vm.frozenTubeArray[i][j].sampleTypeCode = 'S_TYPE_00008';
                            // hotRegisterer.getInstance('my-handsontable').getCell(i,j).style.backgroundColor = 'rgba(255,255,0,0.3)';
                        }
                        //尿1
                        if(value == 'S_TYPE_00009'){
                            vm.frozenTubeArray[i][j].sampleTypeCode = 'S_TYPE_00009';
                            // hotRegisterer.getInstance('my-handsontable').getCell(i,j).style.backgroundColor = 'rgba(255,255,0,0.3)';
                        }
                        //尿2
                        if(value == 'S_TYPE_00010'){
                            vm.frozenTubeArray[i][j].sampleTypeCode = 'S_TYPE_00010';
                        }
                        //99
                        if(value == 'S_TYPE_00011'){
                            vm.frozenTubeArray[i][j].sampleTypeCode = 'S_TYPE_00011';
                        }

                    }
                }
                hotRegisterer.getInstance('my-handsontable').render()
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
        //设备
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

        function onProjectSitesSuccess(data) {
            vm.projectSitesOptions = data;
        }
        //区域
        function onAreaSuccess(data) {
            vm.frozenBoxAreaOptions = data;

        }
        //架子
        function onShelfSuccess(data) {
            vm.frozenBoxShelfOptions = data;

        }
        //区域
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

                vm.frozenTubeArray[row1-1][col1-1] = domArray[0];
                vm.frozenTubeArray[row1-1][col1-1].tubeRows = row1;
                vm.frozenTubeArray[row1-1][col1-1].tubeColumns = col1;
                vm.frozenTubeArray[row-1][col-1] = domArray[1];
                vm.frozenTubeArray[row-1][col-1].tubeRows = row;
                vm.frozenTubeArray[row-1][col-1].tubeColumns = col;

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
                        for(var i = 0; i < vm.frozenTubeArray.length; i++){
                            for(var j = 0; j < vm.frozenTubeArray[i].length; j++){
                                    if(selectedItem.remarkArray.frozenTubeCode == ''){
                                        vm.frozenTubeArray[i][j].memo = selectedItem.remarkArray[i][j].memo;
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

            });
            modalInstance.result.then(function (data) {
                console.log(JSON.stringify(data));
                vm.transportRecord.frozenBoxDTOList = data;
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
            // for(var i = 0; i < vm.transportRecord.frozenBoxDTOList.length; i++){
            //     if(vm.transportRecord.frozenBoxDTOList[i].frozenBoxCode = ){
            //
            //     }
                // for(var j = 0; j < vm.transportRecord.frozenBoxDTOList[i].frozenTubeDTOS; j++){
                //
                // }
            }/
            console.log(JSON.stringify(vm.transportRecord));
            // TransportRecordService.save(vm.transportRecord, onSaveSuccess, onSaveError);
        // };
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
            DTColumnBuilder.newColumn('frozenBoxCode').withTitle('冻存盒号')
        ];
        function someClickHandler(td,boxInfo) {
            $(td).closest('table').find('.highLight').removeClass("highLight");
            $(td).addClass('highLight');
            initFrozenTube(10);
            vm.box = boxInfo;
            var frozenTube = vm.box.frozenTubeDTOS;
            if(frozenTube.frozenTubeCode){
                for(var i = 0; i < frozenTube.length; i++){
                    vm.frozenTubeArray[frozenTube[i].tubeRows-1][frozenTube[i].tubeColumns-1] = frozenTube[i];
                }
                // vm.transportRecord.frozenBoxDTOList[0].frozenTubeDTOS = frozenTube;
            }else{
                for(var i = 0; i < vm.frozenTubeArray.length; i++){
                    for (var j = 0; j <vm.frozenTubeArray[i].length; j++){
                        vm.frozenTubeArray[i][j] = {
                            frozenTubeCode:'',
                            status:1,
                            memo:'',
                            tubeRows:i,
                            tubeColumns:j,
                            sampleTypeCode:'',//样本类型,
                        };
                        vm.frozenTubeArray[i][j].sampleTempCode = vm.box.frozenBoxCode + "-r" + i+1 + "c" + j+1;
                    }
                }
                vm.transportRecord.frozenBoxDTOList[0].frozenTubeDTOS = vm.frozenTubeArray;

            }
            // vm.transportRecord.frozenBoxDTOList[0].frozenTubeDTOS.push(frozenTube[i]);
            // console.log(JSON.stringify(vm.frozenTubeArray));
            hotRegisterer.getInstance('my-handsontable').render();
            // FrozenBoxByIdService.get({id:boxInfo.id},frozenBoxSuccess,onError());


        }
        function frozenBoxSuccess(data) {
            vm.box = data;
            var frozenTube = data.frozenTubeDTOS;
            for(var i = 0; i < frozenTube.length; i++){
                vm.frozenTubeArray[frozenTube[i].tubeRows-1][frozenTube[i].tubeColumns-1] = frozenTube[i];
            }
            // hotRegisterer.getInstance('my-handsontable').loadData(vm.frozenTubeArray);
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
