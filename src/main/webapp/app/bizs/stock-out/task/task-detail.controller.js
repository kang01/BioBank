/**
 * Created by gaokangkang on 2017/5/12.
 * 出库申请详情页面
 */
(function() {
    'use strict';

    angular
        .module('bioBankApp')
        .controller('TaskDetailController', TaskDetailController);

    TaskDetailController.$inject = ['$scope','$uibModal','hotRegisterer'];

    function TaskDetailController($scope,$uibModal,hotRegisterer) {
        var vm = this;
        var modalInstance;
        //异常、撤销
        vm.abnormalModal = _fnAbnormalModal;
        //未出库样本、已出库样本批注
        vm.commentModal = _fnCommentModal;
        //装盒
        vm.boxInModal = _fnBoxInModal;
        //出库
        vm.taskStockOutModal = _fnTaskStockOutModal;
        //待出库样本
        _initSampleDetailsTable();
        function _initSampleDetailsTable() {
            vm.sampleDetailsTableSettings = {
                // 点击表格外部时，表格内选项仍然能够选中
                outsideClickDeselectsCache: false,
                outsideClickDeselects: false,

                // 表格内单元格只能单选
                multiSelect: false,

                // 显示行和列的Title
                showColHeaders: true,
                showRowHeaders: true,

                // 默认每列的宽度
                colWidths: 30,
                // 行头的宽度
                rowHeaderWidth: 30,

                // 默认行列Title
                rowHeaders: ['A','B','C','D','E','F','G','H','J','K'],
                colHeaders: ['1','2','3','4','5','6','7','8','9','10'],

                // 默认显示的行和列的数量
                minRows: 10,
                minCols: 10,

                // 默认显示的数据
                data: [["","","",""]],

                // 默认表格占用的宽高，超过范围显示滚动条
                // width: 584,
                // height: 380,
                // 是否自动拉伸单元格
                stretchH: 'all',
                //右下角拖拽
                fillHandle:false,
                editor: false,
                // 单元格的渲染函数
                renderer:_customRenderer
            }
            // 渲染单元格
            function _customRenderer(hotInstance, td, row, col, prop, value, cellProperties) {
                // var hot = _getSampleDetailsTableCtrl();
                // var cellData = value;
                // if (hot){
                //     cellData = hot.getDataAtCell(row, col);
                // }
                var $div = $("<div/>");
                // if (value){
                    $div.html("7895461331")
                        .data("frozenBoxId", "123465");
                        // .data("frozenBoxCode", value.frozenBoxCode)
                        // .data("columnsInShelf", value.columnsInShelf)
                        // .data("rowsInShelf", value.rowsInShelf)
                        // .data("countOfSample", value.countOfSample);
                // }

                $(td).html("");
                $(td).append($div);
            }
        }

        //异常、撤销
        function _fnAbnormalModal() {
            modalInstance = $uibModal.open({
                animation: true,
                templateUrl: 'app/bizs/stock-out/task/modal/abnormal-recall-modal.html',
                controller: 'AbnormalRecallModalController',
                controllerAs:'vm',
                size:'lg',
                resolve: {
                    items: function () {
                        return {
                        }
                    }
                }
            });

            modalInstance.result.then(function (data) {

            });
        }
        //未出库样本、已出库样本批注
        function _fnCommentModal() {
            modalInstance = $uibModal.open({
                animation: true,
                templateUrl: 'app/bizs/stock-out/task/modal/comment-modal.html',
                controller: 'TaskCommentModalController',
                controllerAs:'vm',
                size:'lg',
                resolve: {
                    items: function () {
                        return {
                        }
                    }
                }
            });

            modalInstance.result.then(function (data) {

            });
        }
        //装盒
        function _fnBoxInModal() {
            modalInstance = $uibModal.open({
                animation: true,
                templateUrl: 'app/bizs/stock-out/task/modal/box-in-modal.html',
                controller: 'TaskBoxInModalController',
                controllerAs:'vm',
                size:'lg',
                resolve: {
                    items: function () {
                        return {
                        }
                    }
                }
            });

            modalInstance.result.then(function (data) {

            });
        }
        //出库
        function _fnTaskStockOutModal() {
            modalInstance = $uibModal.open({
                animation: true,
                templateUrl: 'app/bizs/stock-out/task/modal/box-in-modal.html',
                controller: 'TaskBoxInModalController',
                controllerAs:'vm',
                size:'lg',
                resolve: {
                    items: function () {
                        return {
                        }
                    }
                }
            });

            modalInstance.result.then(function (data) {

            });
        }

        // 获取上架位置列表的控制实体
        function _getSampleDetailsTableCtrl(){
            vm.sampleDetailsTableCtrl = hotRegisterer.getInstance('sampleDetailsTable');
            return vm.sampleDetailsTableCtrl;
        }
    }
})();
