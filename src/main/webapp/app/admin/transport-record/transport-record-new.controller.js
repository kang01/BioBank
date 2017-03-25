/**
 * Created by gaokangkang on 2017/3/14.
 */
(function() {
    'use strict';

    angular
        .module('bioBankApp')
        .controller('TransportRecordNewController', TransportRecordNewController);

    TransportRecordNewController.$inject = ['hotRegisterer','TransportRecordService','DTOptionsBuilder','DTColumnBuilder','$uibModal','$state'];

    function TransportRecordNewController(hotRegisterer,TransportRecordService,DTOptionsBuilder,DTColumnBuilder,$uibModal,$state) {
        var vm = this;
        vm.datePickerOpenStatus = {};
        vm.transportRecord = {};
        vm.openCalendar = openCalendar;
        //导入冻存盒
        vm.importFrozenStorageBox = importFrozenStorageBox;
        //
        vm.transportRecord.boxList = [
            {
                id:"1",
                code:"000001",
                boxType:'1',
                sampleType:'1',
                boxStorageLocation:'F1-01.S01.R01.A1',
                boxStatus:'1',
                remark:'盒子正常',
                microtubesList:[
                    {id:'1',number:'12345678909',color:'rgba(204, 102, 204,0.3)',status:'1',remark:'',row:1,col:2},
                    {id:'25',number:'12345678909',color:'rgba(153, 153, 153,0.3)',status:'1',remark:'',row:1,col:3},
                    {id:'27',number:'12345678909',color:'rgba(204, 102, 204,0.3)',status:'1',remark:'',row:2,col:4},
                    {id:'28',number:'12345678909',color:'rgba(204, 102, 204,0.3)',status:'1',remark:'',row:3,col:6},
                    {id:'11',number:'12345678901',color:'rgba(0, 204, 102,0.3)',status:'1',remark:'',row:1,col:6},
                    {id:'21',number:'12345678902',color:'rgba(204, 102, 204,0.3)',status:'1',remark:'',row:2,col:1},
                    {id:'22',number:'12345678903',color:'rgba(0, 204, 102,0.3)',status:'1',remark:'',row:2,col:6},
                    {id:'23',number:'12345678904',color:'rgba(204, 102, 204,0.3)',status:'1',remark:'123456',row:3,col:5}
                ]
            },
            {id:"2",code:"000002",boxType:'1',sampleType:'1',boxStorageLocation:'F1-01.S01.R01.A1',boxStatus:'1',remark:'盒子正常'},
            {id:"3",code:"000003",boxType:'1',sampleType:'1',boxStorageLocation:'F1-01.S01.R01.A1',boxStatus:'1',remark:'盒子正常'}
        ];
        vm.dtOptions = DTOptionsBuilder.newOptions()
            .withOption('info', false)
            .withOption('paging', false);
        var tArray = new Array();
        for(var k=0;k<10;k++){
            tArray[k]=new Array();
            for(var j=0;j<10;j++){
                tArray[k][j] = "";

            }
        }
        var microtubesList = vm.transportRecord.boxList[0].microtubesList;
        for(var i = 0; i < microtubesList.length; i++){
            tArray[microtubesList[i].row][microtubesList[i].col] = microtubesList[i]
        }
        //自定义初始化单元格
        vm.myCustomRenderer = function(hotInstance, td, row, col, prop, value, cellProperties) {
            if(typeof value != "object"){
                console.log(value)
            }
            td.style.backgroundColor = value.color;
            td.style.position = 'relative';
            if(vm.flagStatus || vm.exchangeFlag || vm.remarkFlag) {
                cellProperties.readOnly = true;
            }else{
                cellProperties.readOnly = false;
            }
            if(value.number){
                var htm = "<div>"+value.number+"</div>"+
                    "<div id='microtubesId' style='display: none'>"+value.id+"</div>" +
                    "<div id='microtubesStatus' style='display: none'>"+value.status+"</div>"+
                    "<div id='microtubesRemark' style='display: none'>"+value.remark+"</div>"+
                    "<div id='microtubesRow' style='display: none'>"+value.row+"</div>"+
                    "<div id='microtubesCol' style='display: none'>"+value.col+"</div>"+
                    "<div ng-if="+value.remark+" class='triangle-topright ' style='position: absolute;top:0;right: 0;'></div>"
            }else{
                var htm = value
             }

            td.innerHTML = htm;

        };
        vm.db = {
            items: tArray
        };

        vm.settings = {
            colHeaders : ['1','2','3','4','5','6','7','8','9','10'],
            rowHeaders : ['A','B','C','D','E','F','G','H','I','J'],
            data:vm.db.items,
            colWidths:100,
            renderer:vm.myCustomRenderer,
            fillHandle:false,
            comments: true,
            stretchH: 'all',
            cell: [
                {col: 1, row: 1, comment: 'Hello Comment'}
            ],
            onAfterInit: function () {
            },
            onAfterSelectionEnd:function (row, col, row2, col2) {
                if(window.event.ctrlKey){
                    vm.exchangeFlag = true;
                    vm.remarkFlag = true;
                    var txt = '<div class="temp" style="position:absolute;top:0;bottom:0;left:0;right:0;border:2px solid #5292F7;"></div>';
                    $(this.getCell(row,col)).append(txt);
                    domArray.push({row:row,col:col,dom:this.getCell(row,col),value:this.getValue(row,col),color:this.getCell(row,col).style.backgroundColor});

                    console.log(JSON.stringify(domArray))
                }else{
                    domArray = [];
                    $(".temp").remove();
                }
                if(vm.operateStatus == 1) {
                    if(vm.flagStatus){
                        var microtubesIdDom = $(this.getCell(row, col)).find("#microtubesId");
                        var microtubesStatusDom = $(this.getCell(row, col)).find("#microtubesStatus");

                        if(microtubes.id != microtubesIdDom.text()){
                            microtubes.status = microtubesStatusDom.text();
                            microtubes.id = microtubesIdDom.text();
                        }else{

                        }

                        if(microtubes.status == 1){
                            microtubesStatusDom.text('2');
                            for(var i = 0; i < microtubesList.length; i++){
                                if(microtubesList[i].row == row && microtubesList[i].col == col){
                                    microtubesList[i].status = 2;
                                    operateColor =  microtubesList[i].color;
                                    this.getCell(row,col).style.background = 'linear-gradient(to right,'+operateColor+',rgba(0,0,0,1)';
                                }
                            }


                        }
                        //空管
                        if(microtubes.status == 2){
                            this.getCell(row,col).style.background = '';
                            this.getCell(row,col).style.backgroundColor = '#ffffff';
                            microtubesStatusDom.text('3');
                        }
                        if(microtubes.status == 3){
                            var txt = '<div class="abnormal" style="position:absolute;top:0;bottom:0;left:0;right:0;border:3px solid red;"></div>';
                            $(this.getCell(row,col)).append(txt);
                            this.getCell(row,col).style.backgroundColor = '';
                            // this.getCell(row,col).style.outline = '2px solid red';
                            microtubesStatusDom.text('4');
                            for(var i = 0; i < microtubesList.length; i++){
                                if(microtubesList[i].row == row && microtubesList[i].col == col){
                                    microtubesList[i].status = 4;
                                }
                            }
                        }
                        //异常
                        if(microtubes.status == 4){
                            this.getCell(row,col).style.backgroundColor = operateColor;
                            // this.getCell(row,col).style.outline = '';
                            $(".abnormal").remove();
                            microtubesStatusDom.text('1');
                            for(var i = 0; i < microtubesList.length; i++){
                                if(microtubesList[i].row == row && microtubesList[i].col == col){
                                    this.getCell(row,col).style.backgroundColor =  microtubesList[i].color;
                                    microtubesList[i].status = 1;
                                }
                            }
                        }
                        microtubes.id = $(this.getCell(row, col)).find("#microtubesId").text();
                        microtubes.status = $(this.getCell(row, col)).find("#microtubesStatus").text();
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
            }
        };

        //operateStatus 1.状态 2:换位 3.批注
        vm.operateStatus;
        //单元格操作的数据
        var domArray = [];
        var operateColor;
        //管子
        var microtubes = {};

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
            hotRegisterer.getInstance('my-handsontable').render();
            //开启换位
            if(vm.exchangeFlag && domArray.length == 2){
                hotRegisterer.getInstance('my-handsontable').setDataAtCell(domArray[0].row,domArray[0].col,domArray[1].value);
                hotRegisterer.getInstance('my-handsontable').setDataAtCell(domArray[1].row,domArray[1].col,domArray[0].value);
                hotRegisterer.getInstance('my-handsontable').getCell(domArray[0].row,domArray[0].col).style.backgroundColor = domArray[1].color;
                hotRegisterer.getInstance('my-handsontable').getCell(domArray[1].row,domArray[1].col).style.backgroundColor = domArray[0].color;
                domArray = [];
                vm.exchangeFlag = false;
            }else{
               console.log("只能选择两个进行交换！");
                domArray = [];
            }

        };
        //批注
        vm.remarkFlag = false;
        vm.microtubesRemark = function () {
            if(vm.remarkFlag){
                    modalInstance = $uibModal.open({
                        animation: true,
                        templateUrl: 'app/admin/transport-record/microtubes-remark-modal.html',
                        controller: 'microtubesRemarkModalController',
                        backdrop:'static',
                        controllerAs: 'vm',
                        resolve: {
                            items: function () {
                                return {
                                    domArray :domArray
                                }
                            }
                        }

                    });
                    modalInstance.result.then(function (selectedItem) {

                        console.log(JSON.stringify(selectedItem));
                        for(var i = 0; i< microtubesList.length; i++){
                            for(var j =0; j < selectedItem.domArray.length;j++){
                               if(microtubesList[i].id == selectedItem.domArray[j].value.id){
                                   microtubesList[i].remark = selectedItem.domArray[j].remark;
                               }
                            }
                        }
                        hotRegisterer.getInstance('my-handsontable').render();
                    });
            }
        };

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
            modalInstance.result.then(function () {

            });
        }
        //保存
        this.saveRecord = function () {
            console.log(JSON.stringify(vm.transportRecord));
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

    }
})();
