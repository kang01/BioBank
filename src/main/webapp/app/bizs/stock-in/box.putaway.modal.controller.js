/**
 * Created by gaoyankang on 2017/4/4.
 */
(function() {
    'use strict';

    angular
        .module('bioBankApp')
        .controller('BoxPutAwayModalController', BoxPutAwayModalController)

    BoxPutAwayModalController.$inject = ['hotRegisterer','DTOptionsBuilder','DTColumnBuilder','$uibModalInstance','$uibModal','items','frozenBoxByCodeService','AlertService'];

    function BoxPutAwayModalController(hotRegisterer,DTOptionsBuilder,DTColumnBuilder,$uibModalInstance,$uibModal,items,frozenBoxByCodeService,AlertService) {

        var vm = this;
        vm.frozenTubeArray = [];
        vm.items = items;
        var htm;

        vm.dtOptions = DTOptionsBuilder.newOptions()
            .withOption('searching', false)
            .withOption('paging', false)
            .withOption('sorting', false)
            .withScroller()
            .withOption('deferRender', true)
            .withOption('scrollY', 300);

        // vm.load = function () {
        //     frozenBoxByCodeService.get({code:'1213243543'},onFrozenSuccess,onError);
        // };
        // function onFrozenSuccess(data) {
        //     vm.box =  data
        //     for(var k = 0; k < vm.box.frozenTubeDTOS.length; k++){
        //         var tube = vm.box.frozenTubeDTOS[k];
        //         vm.frozenTubeArray[getTubeRowIndex(tube.tubeRows)][getTubeColumnIndex(tube.tubeColumns)] = tube;
        //     }
        // }
        function onError(error) {
            AlertService.error(error.data.message);
        }
        // vm.load();
        var size = 4;
        function initFrozenTube(size) {
            for(var i = 0; i < size; i++){
                vm.frozenTubeArray[i] = [];
                for(var j = 0;j < size; j++){
                    vm.frozenTubeArray[i][j] = "";
                }
            }
        }
        initFrozenTube(size);
        function getTubeRowIndex(row) {
            return row.charCodeAt(0) -65;
        }
        function getTubeColumnIndex(col) {
            return +col -1;
        }
        vm.settings ={
            colHeaders : ['1','2','3','4'],
            rowHeaders : ['A','B','C','D'],
            minRows: 4,
            minCols: 4,
            data:vm.frozenTubeArray,
            renderer:customRenderer,
            fillHandle:false,
            stretchH: 'all',
            editor: false,
            onAfterSelectionEnd:function (row, col, row2, col2) {
            },
            enterMoves:function () {
                var hotMoves = hotRegisterer.getInstance('my-handsontable');
                var selectedCol = hotMoves.getSelected()[1];
                if(selectedCol + 1 < hotMoves.countCols()){
                    return{row:0,col:1}
                } else{
                    return{row:1,col:-selectedCol}
                }
            },
            cells: function (row, col, prop) {
                var cellProperties = {};

                // if (hot2.getDataAtRowProp(row, prop) === 'Nissan') {
                //     cellProperties.editor = false;
                // } else {
                //     cellProperties.editor = 'text';
                // }
                //
                // return cellProperties;
            }
        };
        function customRenderer(hotInstance, td, row, col, prop, value, cellProperties) {
            if(value != ""){
                console.log(value.sampleCode);
                htm = "<div ng-if='value.sampleCode'>"+value.sampleCode+"</div>"+
                    "<div id='microtubesId' style='display: none'>"+value.sampleCode+"</div>" +
                    "<div id='microtubesStatus' style='display: none'>"+value.status+"</div>"+
                    "<div id='microtubesRemark' style='display: none'>"+value.memo+"</div>"+
                    "<div id='microtubesRow' style='display: none'>"+value.tubeRows+"</div>"+
                    "<div id='microtubesCol' style='display: none'>"+value.tubeColumns+"</div>"+
                    "<div ng-if="+value.memo+" class='triangle-topright' style='position: absolute;top:0;right: 0;'></div>"
            }else{
                htm = ""
            }
            td.style.backgroundColor = 'yellow';
            td.style.position = 'relative';


            td.innerHTML = htm;
            // console.log(JSON.stringify(value))
        }
        vm.cancel = function () {
            $uibModalInstance.dismiss('cancel');
        };
        vm.ok = function () {
            $uibModalInstance.close();
        };
    }
})();
