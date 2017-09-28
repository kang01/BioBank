/**
 * Created by gaokangkang on 2017/9/5.
 */
(function () {
    'use strict';

    angular
        .module('bioBankApp')
        .controller('EquipmentManagementController', EquipmentManagementController);

    EquipmentManagementController.$inject = ['$scope'];

    function EquipmentManagementController($scope) {
        var vm = this;
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
                //window高
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
        $(".equipment-ul").delegate("li","click",function(){
            $(".equipment-ul li").removeClass("equipment-selected");
            console.log(this);
            $(this).addClass("equipment-selected");

        });
        vm.equipmentType = {
            code:""
        };
        $(".equipment-list-body").delegate(".equipment-box","click",function () {
            $(".equipment-list-body .equipment-box").removeClass("equipment-selected");
            $(this).addClass("equipment-selected");

            vm.equipmentType.code = "1";
            $scope.$apply();
        });

    }
})();
