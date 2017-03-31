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
        if(vm.transportRecord.transhipDate){
            vm.transportRecord.transhipDate = new Date(entity.transhipDate);
        }
        if(vm.transportRecord.receiveDate){
            vm.transportRecord.receiveDate = new Date(entity.receiveDate);
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
            td.style.position = 'relative';
            if(value == ""){
                value= {};
                value.sampleCode = "";
                value.status = "3003",//冻存管状态3001：正常，3002：空管，3003：空孔；3004：异常
                value.tubeRows = row,
                value.tubeColumns = col,
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
            if(value.sampleTypeCode){
                changeSampleType(value.sampleTypeCode,td);
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
            valueField:'sampleTypeCode',
            labelField:'sampleTypeName',
            maxItems: 1,
            onChange:function (value) {
                for(var i =0; i <  vm.frozenTubeArray.length; i++){
                    for(var j = 0; j < vm.frozenTubeArray[i].length; j++){
                        vm.frozenTubeArray[i][j].sampleTypeCode = value;
                        for(var k = 0; k < vm.sampleTypeOptions.length; k++){
                            if(vm.frozenTubeArray[i][j].sampleTypeCode == vm.sampleTypeOptions[k].sampleTypeCode){
                                vm.frozenTubeArray[i][j].sampleTypeName = vm.sampleTypeOptions[k].sampleTypeName;
                                vm.frozenTubeArray[i][j].sampleTypeId = vm.sampleTypeOptions[k].id;
                            }
                        }
                    }
                }
                for(var m =0;m < vm.transportRecord.frozenBoxDTOList.length; m++){
                    for(var n = 0; n < vm.sampleTypeOptions.length; n++){
                        if(vm.transportRecord.frozenBoxDTOList[m].sampleTypeCode == vm.sampleTypeOptions[n].sampleTypeCode){
                            vm.transportRecord.frozenBoxDTOList[m].sampleTypeName = vm.sampleTypeOptions[n].sampleTypeName;
                            vm.transportRecord.frozenBoxDTOList[m].sampleTypeId = vm.sampleTypeOptions[n].id
                        }

                    }

                }

                // var countRows = hotRegisterer.getInstance('my-handsontable').countRows();
                // var countCols = hotRegisterer.getInstance('my-handsontable').countCols();
                // for(var i = 0; i < countRows; i++){
                //     for(var j = 0; j <countCols; j++){
                // if(vm.frozenTubeArray[i][j] == ''){
                //     vm.frozenTubeArray[i][j] = {
                //         frozenTubeCode:'',
                //         status:1,
                //         memo:'',
                //         tubeRows:'',
                //         tubeColumns:'',
                //         sampleTypeCode:''
                //     }
                // }

                // vm.frozenTubeArray[i][j].sampleTypeName = value;
                // //血浆-红
                // if(value == 'S_TYPE_00001'){
                //     vm.frozenTubeArray[i][j].sampleTypeCode = 'S_TYPE_00001';
                //     vm.frozenTubeArray[i][j].sampleTypeId = value;
                // }
                // if(value == 'S_TYPE_00002'){
                //     vm.frozenTubeArray[i][j].sampleTypeCode = 'S_TYPE_00002';
                //     vm.frozenTubeArray[i][j].sampleTypeId = value;
                //
                // }
                // //白细胞-白
                // if(value == 'S_TYPE_00003'){
                //     vm.frozenTubeArray[i][j].sampleTypeCode = 'S_TYPE_00003';
                //     vm.frozenTubeArray[i][j].sampleTypeId = value;
                //
                //     // hotRegisterer.getInstance('my-handsontable').getCell(i,j).style.backgroundColor = 'rgba(255,255,255,0.3)';
                // }
                // //白细胞-灰
                // if(value == 'S_TYPE_00004'){
                //     vm.frozenTubeArray[i][j].sampleTypeCode = 'S_TYPE_00004';
                //     vm.frozenTubeArray[i][j].sampleTypeId = value;
                //
                //     // hotRegisterer.getInstance('my-handsontable').getCell(i,j).style.backgroundColor = 'rgba(192,192,192,0.3)';
                // }
                // //血浆-绿1
                // if(value == 'S_TYPE_00005'){
                //     vm.frozenTubeArray[i][j].sampleTypeCode = 'S_TYPE_00005';
                //     vm.frozenTubeArray[i][j].sampleTypeId = value;
                //
                //     // hotRegisterer.getInstance('my-handsontable').getCell(i,j).style.backgroundColor = 'rgba(0,255,0,0.3)';
                // }
                // //血浆-绿2
                // if( value == 'S_TYPE_00006'){
                //     vm.frozenTubeArray[i][j].sampleTypeCode = 'S_TYPE_00006';
                //     vm.frozenTubeArray[i][j].sampleTypeId = value;
                //
                //     // hotRegisterer.getInstance('my-handsontable').getCell(i,j).style.backgroundColor = 'rgba(0,255,0,0.3)';
                // }
                // //血清1
                // if(value == 'S_TYPE_00007'){
                //     vm.frozenTubeArray[i][j].sampleTypeCode = 'S_TYPE_00007';
                //     vm.frozenTubeArray[i][j].sampleTypeId = value;
                //
                //     // hotRegisterer.getInstance('my-handsontable').getCell(i,j).style.backgroundColor = 'rgba(255,0,0,0.3)';
                // }
                // //血清2
                // if(value == 'S_TYPE_00008'){
                //     vm.frozenTubeArray[i][j].sampleTypeCode = 'S_TYPE_00008';
                //     vm.frozenTubeArray[i][j].sampleTypeId = value;
                //
                //     // hotRegisterer.getInstance('my-handsontable').getCell(i,j).style.backgroundColor = 'rgba(255,255,0,0.3)';
                // }
                // //尿1
                // if(value == 'S_TYPE_00009'){
                //     vm.frozenTubeArray[i][j].sampleTypeCode = 'S_TYPE_00009';
                //     vm.frozenTubeArray[i][j].sampleTypeId = value;
                //
                //     // hotRegisterer.getInstance('my-handsontable').getCell(i,j).style.backgroundColor = 'rgba(255,255,0,0.3)';
                // }
                // //尿2
                // if(value == 'S_TYPE_00010'){
                //     vm.frozenTubeArray[i][j].sampleTypeCode = 'S_TYPE_00010';
                //     vm.frozenTubeArray[i][j].sampleTypeId = value;
                //
                // }
                // //99
                // if(value == 'S_TYPE_00011'){
                //     vm.frozenTubeArray[i][j].sampleTypeCode = 'S_TYPE_00011';
                //     vm.frozenTubeArray[i][j].sampleTypeId = value;
                //
                // }

                //     }
                // }
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
                vm.transportRecord.frozenBoxDTOList = data;
                for(var k = 0; k < vm.transportRecord.frozenBoxDTOList.length; k++){
                    for(var i = 0; i < vm.frozenTubeArray.length; i++){
                        for (var j = 0; j <vm.frozenTubeArray[i].length; j++){

                            vm.frozenTubeArray[i][j] = {
                                sampleCode: "",//样本编码
                                sampleTypeCode: "",//样本类型编码
                                sampleTypeId: "",//样本类型ID
                                sampleTypeName: "",//样本类型名称
                                sampleTempCode:vm.transportRecord.frozenBoxDTOList[k].frozenBoxCode + "-r" + i+1 + "c" + j+1,
                                frozenBoxCode:vm.transportRecord.frozenBoxDTOList[k].frozenBoxCode,//盒子编码
                                status: "3003",//状态
                                tubeColumns: j,//列数
                                tubeRows: i,//行数
                                memo: "",//备注
                                errorType: "",//错误类型
                                // frozenTubeCode: "",//冻存管编码
                                // frozenTubeTypeCode: "",//冻存管类型编码
                                // frozenTubeTypeId: '',//冻存管类型ID
                                // frozenTubeTypeName: "",//冻存管类型名称
                                isModifyPostition: "0003",//是否修改位置'否:0003 是：0002',
                                isModifyState: "0003",//是否修改状态'否:0003 是：0002',

                                // frozenTubeVolumnsUnit:"",//冻存管容量单位
                                // sampleUsedTimes:"",//冻存盒已使用次数
                                // sampleUsedTimesMost:"",//冻存盒最多使用次数
                                // frozenTubeVolumns:""//冻存管容量
                            };
                        }
                    }
                    vm.transportRecord.frozenBoxDTOList[k].frozenTubeDTOS = vm.frozenTubeArray
                }
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
            vm.box = angular.copy(boxInfo);
            var frozenTube = vm.box.frozenTubeDTOS;
            if(frozenTube.sampleCode){
                for(var i = 0; i < frozenTube.length; i++){
                    vm.frozenTubeArray[frozenTube[i].tubeRows-1][frozenTube[i].tubeColumns-1] = frozenTube[i];
                }
            }else{
                vm.frozenTubeArray = frozenTube;

            }
            hotRegisterer.getInstance('my-handsontable').render();
        }
        // function frozenBoxSuccess(data) {
        //     vm.box = data;
        //     var frozenTube = data.frozenTubeDTOS;
        //     for(var i = 0; i < frozenTube.length; i++){
        //         vm.frozenTubeArray[frozenTube[i].tubeRows-1][frozenTube[i].tubeColumns-1] = frozenTube[i];
        //     }
        //     // hotRegisterer.getInstance('my-handsontable').loadData(vm.frozenTubeArray);
        //     hotRegisterer.getInstance('my-handsontable').render();
        // }
        function rowCallback(nRow, aData, iDisplayIndex, iDisplayIndexFull) {
            $('td', nRow).unbind('click');
            $('td', nRow).bind('click', function() {
                var td = this;
                $scope.$apply(function() {
                    vm.someClickHandler(td,aData);
                });
            });

            if (vm.box && vm.box.frozenBoxCode == aData.frozenBoxCode){
                $('td', nRow).addClass('rowLight');
            }
            return nRow;
        }

    }
})();
