/**
 * Created by gaokangkang on 2017/3/14.
 */
(function() {
    'use strict';

    angular
        .module('bioBankApp')
        .controller('TransportRecordNewController', TransportRecordNewController);

    TransportRecordNewController.$inject = ['hotRegisterer','dataFactory','DTOptionsBuilder','DTColumnBuilder','$uibModal'];

    function TransportRecordNewController(hotRegisterer,dataFactory,DTOptionsBuilder,DTColumnBuilder,$uibModal) {
        var vm = this;
        vm.datePickerOpenStatus = {};
        vm.openCalendar = openCalendar;
        vm.importFrozenStorageBox = importFrozenStorageBox;

        vm.dtOptions = DTOptionsBuilder.fromSource('app/admin/transport-record/data.json')
            .withPaginationType('full_numbers')
            .withOption('info', false)
            .withOption('paging', false);
        vm.dtColumns = [
            DTColumnBuilder.newColumn('firstName').withTitle('First name').notSortable()
        ];
        var data1 = [
            [
                {id:'1',number:'12345678909',color:'rgba(204, 102, 204,0.3)',status:'1',remark:''},
                {id:'25',number:'12345678909',color:'rgba(153, 153, 153,0.3)',status:'1',remark:''},
                {id:'27',number:'12345678909',color:'rgba(204, 102, 204,0.3)',status:'1',remark:''},
                {id:'28',number:'12345678909',color:'rgba(204, 102, 204,0.3)',status:'1',remark:''}
            ],
            [
                {id:'11',number:'12345678901',color:'rgba(0, 204, 102,0.3)',status:'1',remark:''},
                {id:'21',number:'12345678902',color:'rgba(204, 102, 204,0.3)',status:'1',remark:''},
                {id:'22',number:'12345678903',color:'rgba(0, 204, 102,0.3)',status:'1',remark:''},
                {id:'23',number:'12345678904',color:'rgba(204, 102, 204,0.3)',status:'1',remark:''}
            ]
        ];
        var tArray = new Array();
        for(var k=0;k<10;k++){
            tArray[k]=new Array();
            for(var j=0;j<10;j++){
                tArray[k][j] = {};

            }
        }
        for(var i = 0; i < data1.length; i++){
            for(var j = 0; j < data1[i].length;j++){
                tArray[i][j] = data1[i][j]
            }
        }
        // tArray = data1
        //自定义初始化单元格
        vm.myCustomRenderer = function(hotInstance, td, row, col, prop, value, cellProperties) {
            // if(row == 1 && col == 1){
                td.style.backgroundColor = value.color;
            // }
            // if(row === 3 && col === 3){
            //     td.style.backgroundColor = 'rgba(153, 153, 153,0.3)';
            // }
            // if(row === 4 && col === 4){
            //     td.style.backgroundColor = 'rgba(0, 204, 102,0.3)';
            // }
            // if( row === 2 &&  col===6 ){
            //     td.style.backgroundColor = 'rgba(255, 255, 0,0.3)';
            // }
            var htm = "<div>"+value.number+"</div>"+
                "<div id='microtubesId' style='display: none'>"+value.id+"</div>" +
                "<div id='microtubesStatus' style='display: none'>"+value.status+"</div>"+
                "<div id='microtubesRemark' style='display: none'>"+value.remark+"</div>";
            td.innerHTML = htm;

        };


        vm.minSpareRows = 0 ;
        vm.minSpareCols = 0;
        vm.rowHeaders = true;
        vm.colHeaders = true;
        vm.colHeaders = ['1','2','3','4','5','6','7','8','9','10'];
        vm.rowHeaders = ['A','B','C','D','E','F','G','H','I','J'];
        vm.db = {
            items: tArray
        };
        //operateStatus 1.状态 2:换位 3.批注
        var operateStatus;
        //单元格操作的数据
        var domArray = [];
        var operateColor;
        var microtubes = {};
        //样本状态
        // var microtubesStatus;
        // var microtubesId;
        //管子备注
        // var microtubesRemark;
        vm.onAfterSelectionEnd = function(row, col, row2, col2) {

            //获取选中的坐标
            // console.log(this.getSelected())
            // vm.myCustomRenderer(row,col)
            // console.log(this.getSelectedRange())
            // console.log(this.getData(row,col))
            // console.log(this.getCellMeta(row,col).instance.getValue())
            //获得列头
            // console.log(this.getColHeader(col));
            //获得列宽
            // console.log(this.getColWidth(col))
            // console.log(this.getCopyableData(row,col));
            //获得一个字符串从开始位置到结束位置
            // console.log(this.getCopyableText(row,col,row2,col2));
            //获得一数组 矩阵数组
            // console.log(JSON.stringify(this.getData(row,col,row2,col2)));
            //获取一列的数据数组
            // console.log(this.getDataAtProp(col));
            //获取一行的数据数组
            // console.log(this.getDataAtRow(row));
            //获取数据类型
            // console.log(this.getDataType(row,col,row2,col2))
            //获取所有的行数
            // console.log(this.getInstance().countRows())
            //获得单元格的值
            // console.log(JSON.stringify(this.getCopyableData(row,col,row2,col2)))
            // console.log(this.selectCell(row,col))
            // 根据行列索引获取单元格的属性对象
            // console.log(this.getValue(row,col))
            // selectVal = this.getValue(row,col);
            // console.log(selectVal)
            //根据单元格的行列索引获取单元格的渲染函数
            // console.log(this.getCellRenderer(row,col))
            // this.setCellMeta(row,col,"aaaaaa")
            // this.setDataAtCell(row,col,"aaaaaa")
            //获得元素
            // console.log(this.getCell(row,col).style.backgroundColor);

            //修改样本状态microtubesStatus 1正常 2.空管 3.空孔 4.异常
            if(operateStatus == 1) {
                if(flagStatus){
                    if(microtubes.id != $(this.getCell(row, col)).find("#microtubesId").text()){
                        microtubes.status = $(this.getCell(row, col)).find("#microtubesStatus").text();
                        microtubes.id = $(this.getCell(row, col)).find("#microtubesId").text();
                        // operateColor =  this.getCell(row, col).style.backgroundColor;
                    }else{

                    }

                    if(microtubes.status == 1){
                        operateColor =  this.getCell(row, col).style.backgroundColor;
                        this.getCell(row,col).style.background = 'linear-gradient(to right,'+operateColor+',rgba(0,0,0,1)';
                        $(this.getCell(row, col)).find("#microtubesStatus").text('2');
                    }
                    //空管
                    if(microtubes.status == 2){
                        this.getCell(row,col).style.background = '';
                        this.getCell(row,col).style.backgroundColor = '#ffffff';
                        $(this.getCell(row, col)).find("#microtubesStatus").text('3');
                    }
                    if(microtubes.status == 3){
                        this.getCell(row,col).style.backgroundColor = '';
                        this.getCell(row,col).style.outline = '2px solid red';
                        $(this.getCell(row, col)).find("#microtubesStatus").text('4');
                    }
                    //异常
                    if(microtubes.status == 4){
                        this.getCell(row,col).style.backgroundColor = operateColor;
                        this.getCell(row,col).style.outline = '';
                        $(this.getCell(row, col)).find("#microtubesStatus").text('1');
                    }
                    microtubes.id = $(this.getCell(row, col)).find("#microtubesId").text();
                    microtubes.status = $(this.getCell(row, col)).find("#microtubesStatus").text();
                }
            }
            //换位
            if(operateStatus == 2){
                if(exchangeFlag){
                    exchangeCount ++;
                    // console.log(count);
                    if(exchangeCount <= 3){
                        this.getCell(row,col).style.outline = '2px solid #5292F7';
                        domArray.push({row:row,col:col,dom:this.getCell(row,col),value:this.getValue(row,col),color:this.getCell(row,col).style.backgroundColor});
                    }
                    console.log(JSON.stringify(domArray))
                }
            }
            // 批注
            if(operateStatus == 3){
                if(remarkFlag){
                    microtubes.id = $(this.getCell(row, col)).find("#microtubesId").text();
                    microtubes.remark = $(this.getCell(row, col)).find("#microtubesRemark").text();
                    console.log( microtubes.id);

                    if(microtubes.id != 'undefined'){
                        modalInstance = $uibModal.open({
                            animation: true,
                            templateUrl: 'app/admin/transport-record/microtubes-remark-modal.html',
                            controller: 'microtubesRemarkModalController',
                            backdrop:'static',
                            controllerAs: 'vm',
                            resolve: {
                                items: function () {
                                    return {
                                        id :microtubes.id,
                                        remark :microtubes.remark
                                    }
                                }
                            }

                        });
                        modalInstance.result.then(function (selectedItem) {
                            console.log(JSON.stringify(selectedItem))
                        });
                    }
                }


            }

        };

        //修改样本状态 正常、空管、空孔、异常
        var flagStatus = false;
        this.editStatus = function () {
            operateStatus = 1;
            if(flagStatus){
                flagStatus = false;
            }else{
                flagStatus = true;
            }
        };
        //换位
        var exchangeFlag = false;
        var exchangeCount = 0;
        this.exchange = function () {
            operateStatus = 2;
            //开启换位
            if(exchangeFlag){
                exchangeCount = 0;
                exchangeFlag = false;
                hotRegisterer.getInstance('my-handsontable').setDataAtCell(domArray[0].row,domArray[0].col,domArray[1].value);
                hotRegisterer.getInstance('my-handsontable').setDataAtCell(domArray[1].row,domArray[1].col,domArray[0].value);
                hotRegisterer.getInstance('my-handsontable').getCell(domArray[0].row,domArray[0].col).style.backgroundColor = domArray[1].color;
                hotRegisterer.getInstance('my-handsontable').getCell(domArray[1].row,domArray[1].col).style.backgroundColor = domArray[0].color;
                domArray = [];
            }else{
                exchangeFlag = true;
                exchangeCount = 1
            }

        };
        //批注
        var remarkFlag = false;
        this.microtubesRemark = function () {
            operateStatus = 3;
            if(remarkFlag){
                remarkFlag = false;
            }else{
                remarkFlag = true;
            }
        };



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
        function openCalendar (date) {
            vm.datePickerOpenStatus[date] = true;
        }

    }
})();
