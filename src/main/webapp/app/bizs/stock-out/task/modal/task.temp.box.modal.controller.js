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

        _fnLoadTempBox();
        _fnLoadBoxType();

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
            if(vm.selectBox.frozenTubeDTOS.length >= sampleTotalCount){
                vm.pos = "";
                $scope.$apply();
                return
            }
            if(vm.selectBox.frozenTubeDTOS.length){
                var len = vm.selectBox.frozenTubeDTOS.length-1;
                var tubes = _.orderBy(vm.selectBox.frozenTubeDTOS, ['tubeRows','tubeColumns'], ['asc','asc']);
                var row =(tubes[len].tubeRows).toUpperCase();
                var col =+tubes[len].tubeColumns+1;
                vm.pos = row + col;
            }else{
                vm.pos = tempBox.frozenBoxCode ? tempBox.startPos : "A1";
            }

            // _FnPreassemble(vm.selectedTubes);
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
