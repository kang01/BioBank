/**
 * Created by gaokangkang on 2018/1/2.
 */
(function()  {
    'use  strict';

    angular
        .module('bioBankApp')
        .directive('bbEquipmentUsageState',  bbEquipmentUsageState);

    bbEquipmentUsageState.$inject  =  ['$parse','$compile'];
    controllerFun.$inject  =  ['$scope'];

    function  bbEquipmentUsageState($parse,$compile)  {
        return {
            restrict: 'EAC',
            replace:false,
            scope  :  {
                equipments : '=equipments'
            },
            templateUrl: 'app/services/directive/template/equipment-usage-state.html',
            link: linkFun,
            controller: controllerFun,
            controllerAs: 'vm'
        };
    }

    function controllerFun($scope) {
        var equipments = $scope.equipments;
        $scope.$watch('equipments',function () {
            $scope.areas = _.groupBy(equipments, 'areaCode');
            $scope.draw();
        });
        $(window).resize(function () {
            // setTimeout(function () {
                $scope.draw();
            // },500)

        })

    }
    function linkFun(scope, elm, attr, controller, transcludeFn) {
        var vm = scope;
        vm.draw = _draw;
        function _draw() {
            var $equipmentDiv = $(".equipment-usage-state",elm);
            //创建一个文档片段，虚拟的节点对象
            var fragment = document.createDocumentFragment();
            for(var key in vm.areas){
                var areaDom =  _createAreaDom(key,vm.areas,$equipmentDiv);
                fragment.append(areaDom[0]);
            }
            $equipmentDiv.html("");
            $equipmentDiv.append(fragment);

        }
    }
    //生成区域DOM
    function _createAreaDom(key,areas,$equipmentDiv) {
        var shelves = _.groupBy(areas[key], 'shelvesCode');
        var areaDom = $("<div class='area'/>");
        var areaDomLeft = $("<div class='area-left' style='width: 60px'/>");
        var areaDomRight = $("<div class='area-right'/>");
        //创建一个文档片段，虚拟的节点对象
        var fragment = document.createDocumentFragment();
        //架子个数
        var shelfCounts = Object.keys(shelves).length;
        //左侧区域宽度
        var areaLeftW = areaDomLeft.outerWidth();
        //设备的宽度
        var equipmentW = $equipmentDiv.outerWidth();
        //右侧区域的高度
        var areaRightW = equipmentW - areaLeftW;

        areaDomRight.width(areaRightW);
        areaDomLeft.html(key);

        var shelfW;
        // 架子数量大于15个折行
        if(shelfCounts > 15){
            shelfW = areaRightW/15;
            // shelfDom.width(areaRightW/15);

        }else{
            shelfW = areaRightW/shelfCounts;
            // shelfDom.width(areaRightW/shelfCounts);
        }
        //几行数据
        var areaRowCount =  Math.ceil(shelfCounts/15);

        for(var key in shelves){
            var shelvesDom =  _createShelvesDom(key,shelves,areaRowCount,shelfW,areaDomLeft);
            fragment.append(shelvesDom[0]);
        }
        areaDomRight.append(fragment);

        areaDom.append(areaDomLeft);
        areaDom.append(areaDomRight);

        return areaDom;
    }
    //创建架子DOM
    function _createShelvesDom(key,shelves,areaRowCount,shelfW,areaDomLeft) {
        var usedCount = parseInt(shelves[key][0].countOfUsed);
        var restCount = parseInt(shelves[key][0].countOfRest);
        var totalCount =  usedCount + restCount;
        var shelfDom = $("<span class='shelf' style='height: 60px'/>");
        shelfDom.html(key+"<br>"+usedCount+"/"+totalCount);
        shelfDom.width(shelfW);

        //单个架子的高度
        var shelfH = shelfDom.outerHeight();
        //右侧架子区域的高度（不同行不同高度）
        var areaH = shelfH*areaRowCount;

        areaDomLeft.css({"height":areaH,"line-height":areaH+"px"});

        return shelfDom;
    }
})();
