/**
 * Created by gaokangkang on 2017/8/23.
 * 添加临时盒
 */
(function() {
    'use strict';

    angular
        .module('bioBankApp')
        .controller('TaskTempBoxModalController', TaskTempBoxModalController);

    TaskTempBoxModalController.$inject = ['$scope','$compile','$uibModalInstance','$uibModal','toastr','items','DTOptionsBuilder','DTColumnBuilder','TaskService','FrozenBoxTypesService','BioBankBlockUi','BoxCodeIsRepeatService','BioBankDataTable'];

    function TaskTempBoxModalController($scope,$compile,$uibModalInstance,$uibModal,toastr,items,DTOptionsBuilder,DTColumnBuilder,TaskService,FrozenBoxTypesService,BioBankBlockUi,BoxCodeIsRepeatService,BioBankDataTable) {
        var vm = this;
        var taskId = items.taskId;
        var tempBox = items.tempBox;
        //临时盒list
        var boxList = [];
        vm.tempBoxInstance = {};
        vm.selectBox = {
            frozenTubeDTOS:[]
        };
        vm.box = {
            frozenBoxCode:null,
            frozenBoxTypeId:null,
            frozenBoxTypeRows:null,
            frozenBoxTypeColumns:null,
            sampleCount:null,
            sampleOutCount:null,
            startPos:null,
            frozenTubeDTOS:[]
        };
        //添加临时盒子
        vm.addNewTempBox = _fnAddNewTempBox;
        //选择管子位置
        vm.selectTubePos = _fnSelectTubePos;

        _fnLoadTempBox();
        _fnLoadBoxType();

        //临时盒子
        function _fnLoadTempBox() {
            //临时盒子
            TaskService.queryTempBoxes(taskId).success(function (data) {
                for(var i = 0; i < data.length; i++){
                    data[i].sampleCount = null;
                    boxList.push(data[i]);
                }
                if(tempBox.frozenBoxCode){
                    var len = _.filter(boxList,{frozenBoxCode:tempBox.frozenBoxCode}).length;
                    if(!len){
                        boxList.push(tempBox);
                    }
                }
                vm.tempBoxOptions.withOption('data', boxList);
            });
        }
        //盒类型
        function _fnLoadBoxType() {
            //盒子类型
            FrozenBoxTypesService.query({},onFrozenBoxTypeSuccess, onError);
            function onFrozenBoxTypeSuccess(data) {
                vm.frozenBoxTypeOptions = _.orderBy(data, ['id'], ['asc']);
                vm.box.frozenBoxTypeId = vm.frozenBoxTypeOptions[0].id;
                vm.box.frozenBoxTypeRows = vm.frozenBoxTypeOptions[0].frozenBoxTypeRows;
                vm.box.frozenBoxTypeColumns = vm.frozenBoxTypeOptions[0].frozenBoxTypeColumns;
            }
            //盒子类型
            vm.boxTypeConfig = {
                valueField:'id',
                labelField:'frozenBoxTypeName',
                maxItems: 1,
                onChange:function(value){
                    var boxType = _.find(vm.frozenBoxTypeOptions, {id:+value});
                    vm.box.frozenBoxTypeId = value;
                    vm.box.frozenBoxTypeRows = boxType.frozenBoxTypeRows;
                    vm.box.frozenBoxTypeColumns = boxType.frozenBoxTypeColumns;
                }
            };
        }
        //添加临时盒子
        function _fnAddNewTempBox() {
            //判断库里有没有冻存盒
            BoxCodeIsRepeatService.getByCode(vm.frozenBoxCode).then(function (data) {
                vm.isRepeat = data;
                if (vm.isRepeat){
                    toastr.error("冻存盒编码已存在!");
                    vm.frozenBoxCode = "";
                    return;
                }
                var len = _.filter(boxList,{frozenBoxCode:vm.frozenBoxCode}).length;
                if(len === 0){
                    vm.box.frozenBoxCode = vm.frozenBoxCode;
                    boxList.push(angular.copy(vm.box));
                    vm.tempBoxOptions.withOption('data', boxList);
                    vm.tempBoxInstance.rerender();
                    vm.frozenBoxCode = "";
                }else{
                    toastr.error("冻存盒编码已存在!");
                }

            });
        }
        //选择管子位置
        //dom片段
        var fragment = document.createDocumentFragment();
        function _fnSelectTubePos(e) {
            //有子元素清空
            if( $(".pos-content li")){
                $(".pos-content").empty();
            }
            var $posContent = $(".pos-content");
            $posContent.css({"display":"block"});
            //遍历冻存盒的盒类型，生成二维的位置矩阵

            for(var i = 0, row = vm.selectBox.frozenBoxTypeRows; i < row; i++){
                var rowIndex = String.fromCharCode('A'.charCodeAt(0) + i);
                var $div = fragment.appendChild(document.createElement('div'));
                if(i > 7){
                    rowIndex = String.fromCharCode('A'.charCodeAt(0) + i+1);
                }
                for(var j = 0, col = vm.selectBox.frozenBoxTypeColumns; j < col; j++){
                    var $li = $("<li class='allow-pos'/>");
                    var colIndex = j+1;
                    //给每个li赋值行列的数值
                    $li.text(rowIndex+colIndex);
                    //选择的临时的盒子，临时盒中管子中的位置不能再被选择
                    if(vm.selectBox.frozenTubeDTOS.length){

                        var len =  _.filter(vm.selectBox.frozenTubeDTOS,{pos:$li.text()}).length;
                        var tubeId =  _.find(vm.selectBox.frozenTubeDTOS,{pos:$li.text()}).id;
                        if(len && tubeId){
                            $li.removeClass("allow-pos");
                            $li.addClass("pos-disabled")
                        }
                    }
                    //预装位置与容器内位置相同时，标记一下
                    if($li.text() == vm.pos){
                        $li.css({"border":"1px solid red"})
                    }
                    $li.appendTo($div);

                }
            }
            $posContent.append(fragment);
            //每个li的宽度、高度
            var liW = $(".pos-content li:first").outerWidth()+4;
            var liH = $(".pos-content li:first").outerHeight()+4;
            //动态的给层赋值宽高
            $posContent.width(liW*vm.selectBox.frozenBoxTypeColumns);
            $posContent.height(liH*vm.selectBox.frozenBoxTypeRows);
            //让容器出现在框的上面
            var h = $posContent.outerHeight();
            $posContent.css({top:-h});

            //防止冒泡
            stopPropagation(e);
            function stopPropagation(e) {
                if (e.stopPropagation)
                    e.stopPropagation();
                else
                    //只支持Ie
                    e.cancelBubble = true;
            }
            //委托给能被选择位置点击事件
            $posContent.delegate("li.allow-pos","click",function(){
                vm.pos = $(this).text();
                $scope.$apply();
            });

        }
        //捕获body的点击事件
        $(document).bind('click',function(){
            $('.pos-content').css('display','none');
        });



        //临时盒子
        vm.tempBoxOptions = BioBankDataTable.buildDTOption("BASIC", 280)
            .withOption('rowCallback', rowCallback)
            .withOption('createdRow', createdRow);
        vm.tempBoxColumns = [
            DTColumnBuilder.newColumn('frozenBoxCode').withTitle('临时盒编码'),
            DTColumnBuilder.newColumn('sampleCount').withTitle('盒内样本数')
        ];
        var sampleTotalCount;
        function createdRow(row, data, dataIndex) {
            sampleTotalCount =  data.frozenBoxTypeRows * data.frozenBoxTypeColumns;
            var sampleOutCount = data.frozenTubeDTOS.length - (_.filter(data.frozenTubeDTOS,{sampleTempCode:""}).length);
            var str = sampleOutCount+"/"+sampleTotalCount;
            $('td:eq(1)', row).html(str);
            $compile(angular.element(row).contents())($scope);
        }
        function rowCallback(nRow, oData, iDisplayIndex, iDisplayIndexFull)  {
            $('td', nRow).unbind('click');
            $(nRow).bind('click', function() {
                var tr = this;
                rowClickHandler(tr,oData);
            });
            return nRow;
        }
        function rowClickHandler(tr,data) {
            $(tr).closest('table').find('.rowLight').removeClass("rowLight");
            $(tr).addClass('rowLight');
            vm.selectBox = data;
            // vm.box.frozenBoxTypeId = vm.selectBox.frozenBoxTypeId;
            // vm.box.frozenBoxTypeRows = vm.selectBox.frozenBoxTypeRows;
            // vm.box.frozenBoxTypeColumns = vm.selectBox.frozenBoxTypeColumns;
            var tubesInTable = [];
            _reloadTubesForTable(vm.selectBox);
            function _reloadTubesForTable(box){
                var row = +box.frozenBoxTypeRows;
                var col = +box.frozenBoxTypeColumns;
                for (var i=0; i < row; ++i){
                    var pos = {tubeRows: String.fromCharCode('A'.charCodeAt(0) + i), tubeColumns: 1 + ""};
                    if(i > 7){
                        pos.tubeRows = String.fromCharCode('A'.charCodeAt(0) + i+1);
                    }
                    var tubes = [];
                    for (var j = 0; j < col; ++j){
                        pos.tubeColumns = j + 1 + "";
                        var tubeInBox = _.filter(box.frozenTubeDTOS, pos)[0];
                        var tube = _createTubeForTableCell(tubeInBox, i, j + 1, pos);
                        tubes.push(tube);
                    }
                    tubesInTable.push(tubes);
                }
                vm.selectBox.frozenTubeDTOS =  _.flatten(tubesInTable);

            }
            function _createTubeForTableCell(tubeInBox, rowNO, colNO, pos){
                var tube = {
                    id: null,
                    sampleCode: "",
                    sampleTempCode: "",
                    tubeRows: pos.tubeRows,
                    tubeColumns: pos.tubeColumns,
                    rowNO: rowNO,
                    colNO: colNO
                };
                if (tubeInBox){
                    tube.id = tubeInBox.id;
                    tube.sampleCode = tubeInBox.sampleCode;
                    tube.sampleTempCode = tubeInBox.sampleTempCode;
                }

                return tube;
            }
            vm.pos = "";
            //获取选定的临时盒子的空位置
            if(vm.selectBox.frozenTubeDTOS.length){
                _.forEach(vm.selectBox.frozenTubeDTOS,function (tube) {
                    tube.pos = tube.tubeRows+tube.tubeColumns;
                    if(!tube.id && !vm.pos){
                        vm.pos = tube.pos;
                    }
                });
            }
            $scope.$apply();

        }

        vm.ok = function () {
            vm.selectBox.startPos = vm.pos;
            $uibModalInstance.close(vm.selectBox);
        };
        vm.cancel = function () {
            $uibModalInstance.dismiss('cancel');
        };
        function onError(error) {
            BioBankBlockUi.blockUiStop();
            toastr.error(error.data.message);
        }
    }
})();
