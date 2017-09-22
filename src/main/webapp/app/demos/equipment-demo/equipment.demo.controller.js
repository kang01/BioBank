/**
 * Created by gaokangkang on 2017/9/21.
 */
(function() {
    'use strict';

    angular
        .module('bioBankApp')
        .controller('DemoEquipmentController', DemoEquipmentController);

    DemoEquipmentController.$inject = ['$scope', '$compile', 'Principal', 'StockInService', 'ParseLinks', 'AlertService', '$state'];

    function DemoEquipmentController($scope, $compile, Principal, StockInService, ParseLinks, AlertService, $state) {
        var vm = this;
        vm.xy ={
            position:0
        };
        vm.posList = [];

        $scope.$watch('vm.xy.position',function (newValue,oldValue) {
            vm.posList.push(vm.xy.position);
        });
        vm.empty = function () {
            vm.posList = [];
        };


        function _fnInitWH() {
            //获取左侧区域
            var $areaList = $(".area-demo");
            var $areaPanel = $(".area-panel");
            //获取右侧区域
            var $equipmentList = $(".equipment-list");
            var $equipmentPanel = $(".equipment-panel");
            //获取左侧区域宽度，永远不变
            var leftWidth = $areaPanel.outerWidth();
            function _fnSetWH(){
                //window宽高
                var winWidth = window.innerWidth;
                var winHeight = $(window).height();
                //设置区域高度
                $areaList.css("height",winHeight- 190);
                //获取区域宽高
                var leftHeight = $areaList.outerHeight();

                //设置右侧区域宽高
                // var rightWidth = winWidth - leftWidth - 40;
                // console.log(leftWidth,rightWidth,winWidth,winHeight);
                $equipmentList.css("height",leftHeight);
                // $equipmentPanel.css("width",rightWidth);
            }
            _fnSetWH();

            $(window).resize(function() {
                _fnSetWH();
            });
        }
        _fnInitWH();
    }
})();
