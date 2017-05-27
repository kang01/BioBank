/**
 * Created by gaokangkang on 2017/5/12.
 * 任务中装盒操作
 */
(function() {
    'use strict';

    angular
        .module('bioBankApp')
        .controller('TaskBoxInModalController', TaskBoxInModalController);

    TaskBoxInModalController.$inject = ['$scope','$compile','$uibModalInstance','$uibModal','items','DTOptionsBuilder','DTColumnBuilder','PlanService'];

    function TaskBoxInModalController($scope,$compile,$uibModalInstance,$uibModal,items,DTOptionsBuilder,DTColumnBuilder,PlanService) {
        var vm = this;
        vm.allInFlag = items.allInFlag;
        var boxInTubes = items.boxInTubes;
        console.log(JSON.stringify(boxInTubes));
        var tubes = [];
        for(var i = 0; i < boxInTubes.length; i++){
            var tube = {
                id:boxInTubes[i].id,
                sampleTypeName:boxInTubes[i].sampleType.sampleTypeName,
                sampleCode:boxInTubes[i].sampleTempCode
            }
            tubes.push(tube)
        }

        vm.tempBoxInstance = {};
        vm.sampleInstance = {};

        vm.tempBoxOptions = DTOptionsBuilder.newOptions()
            .withOption('info', false)
            .withOption('paging', false)
            .withOption('sorting', false)
            .withOption('searching', false)
            .withScroller()
            .withOption('scrollY', 398)
        var titleHtml = '<input type="checkbox" ng-model="vm.selectAll" ng-click="vm.toggleAll()">';
        vm.tempBoxColumns = [
            // DTColumnBuilder.newColumn("").withOption("width", "30").withTitle(titleHtml).notSortable().renderWith(_fnRowSelectorRender),
            DTColumnBuilder.newColumn('frozenBoxCode').withTitle('临时盒编码'),
            DTColumnBuilder.newColumn('sampleOfCount').withTitle('盒内样本数'),
            DTColumnBuilder.newColumn('id').notVisible()
        ];
        function _fnRowSelectorRender(data, type, full, meta) {
            vm.selected[full.id] = false;
            var html = '';
            html = '<input type="checkbox" ng-model="vm.selected[' + full.id + ']" ng-click="vm.toggleOne(vm.selected)">';
            return html;
        }

        //待装盒样本
        vm.selected = {};
        vm.selectAll = false;
        // 处理盒子选中状态
        vm.toggleAll = function (selectAll, selectedItems) {
            selectedItems = vm.selected;
            selectAll = vm.selectAll;
            var arrayId = [];
            for (var id in selectedItems) {
                arrayId.push(id)
                if (selectedItems.hasOwnProperty(id)) {
                    selectedItems[id] = selectAll;
                }
            }
            vm.strBoxIds = _.join(arrayId,",");
            if(!selectAll){
                vm.strBoxIds = "";
            }
        };

        vm.toggleOne = function (selectedItems) {
            // console.log(JSON.stringify(selectedItems))
            var arrayId = [];
            for (var id in selectedItems) {
                if(selectedItems[id]){
                    arrayId.push(id)
                }
            }
            vm.strBoxIds = _.join(arrayId,",");
            for (var id in selectedItems) {
                if (selectedItems.hasOwnProperty(id)) {
                    if(!selectedItems[id]) {
                        vm.selectAll = false;
                        return;
                    }
                }
            }
            vm.selectAll = true;


        };
        vm.sampleOptions = DTOptionsBuilder.newOptions()
            .withOption('info', false)
            .withOption('paging', false)
            .withOption('sorting', false)
            .withOption('searching', false)
            .withScroller()
            .withOption('scrollY', 398);
        var titleHtml = '<input type="checkbox" ng-model="vm.selectAll" ng-click="vm.toggleAll()">';
        vm.sampleColumns = [
            DTColumnBuilder.newColumn("").withOption("width", "30").withTitle(titleHtml).notSortable().renderWith(_fnRowSampleSelectorRender),
            DTColumnBuilder.newColumn('sampleCode').withTitle('样本编码'),
            DTColumnBuilder.newColumn('sampleTypeName').withTitle('样本类型'),
            DTColumnBuilder.newColumn('id').withTitle('预装位置'),
            DTColumnBuilder.newColumn('sampleTypeName').withTitle('标签'),
            DTColumnBuilder.newColumn("").withTitle('操作').notSortable().renderWith(actionsHtml),
            DTColumnBuilder.newColumn('id').notVisible()
        ];
        function _fnRowSampleSelectorRender(data, type, full, meta) {
            vm.selected[full.id] = false;
            var html = '';
            html = '<input type="checkbox" ng-model="vm.selected[' + full.id + ']" ng-click="vm.toggleOne(vm.selected)">';
            return html;
        }
        function actionsHtml(data, type, full, meta) {
            return '<button type="button" class="btn btn-warning btn-xs" >' +
                '   <i class="fa  fa-long-arrow-up"></i>' +
                '</button>&nbsp;'+
                '<button type="button" class="btn btn-warning btn-xs" >' +
                '   <i class="fa fa-long-arrow-down"></i>' +
                '</button>&nbsp;'
        }

        setTimeout(function () {
            vm.sampleOptions.withOption('data', tubes);
            vm.sampleInstance.rerender();
        },500);



        vm.yes = function () {
            vm.allInFlag = true;
        };
        vm.ok = function () {
            $uibModalInstance.close();
        };
        vm.cancel = function () {
            $uibModalInstance.dismiss('cancel');
        };


    }
})();
