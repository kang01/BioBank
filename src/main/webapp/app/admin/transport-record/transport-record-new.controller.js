/**
 * Created by gaokangkang on 2017/3/14.
 */
(function() {
    'use strict';

    angular
        .module('bioBankApp')
        .controller('TransportRecordNewController', TransportRecordNewController);

    TransportRecordNewController.$inject = ['dataFactory','DTOptionsBuilder','DTColumnBuilder','$uibModal'];

    function TransportRecordNewController(dataFactory,DTOptionsBuilder,DTColumnBuilder,$uibModal) {
        var vm = this;
        vm.dtOptions = DTOptionsBuilder.fromSource('app/admin/transport-record/data.json')
            .withPaginationType('full_numbers')
            .withOption('info', false)
            .withOption('paging', false);
        vm.dtColumns = [
            DTColumnBuilder.newColumn('firstName').withTitle('First name').notSortable()
        ];
        // var data1 = [];
        var tArray = new Array();
        for(var k=0;k<10;k++){
            tArray[k]=new Array();
            for(var j=0;j<10;j++){
                tArray[k][j]=null;
            }
        }
        tArray[5][6] = {id:"123456",name:"kk"};
        vm.minSpareRows = 0 ;
        vm.minSpareCols = 0;
        vm.rowHeaders = true;
        vm.colHeaders = true;
        vm.colHeaders = ['1','2','3','4','5','6','7','8','9','10'];
        vm.rowHeaders = ['A','B','C','D','E','F','G','H','I','J'];
        vm.db = {
            items: tArray
        };
        vm.onAfterSelectionEnd = function(row, col, row2, col2) {

            //获取选中的坐标
            console.log(this.getSelected())
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
            console.log(JSON.stringify(this.getValue()))
            // console.log(this.selectCell(row,col))
        };
        vm.myCustomRenderer = function(hotInstance, td, row, col, prop, value, cellProperties) {
            if(row === 5 && col === 6){
                this.style.backgroundColor = 'yellow';
            }

        }

        vm.settings = {
            contextMenu:true,
            // contextMenu: [
            //     'row_above', 'row_below', 'remove_row'
            // ]
        };


        var modalInstance;
        function importBox() {
            modalInstance = $uibModal.open({
                animation: true,
                templateUrl: 'app/admin/transport-record/frozen-storage-box-modal.html',
                controller: 'FrozenStorageBoxModalController',
                backdrop:'static'
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
        // vm.importBox = function(item,type){
        //
        // };


    }
})();
