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

        // 获取元素
        var oDiv = document.querySelector('#box');
        var x = 30;
        var y = -60;
        oDiv.onmousedown = function (ev) {
            var event = window.event || ev;
            var disY = event.clientX - y;
            var disX = event.clientY - x;

            document.onmousemove = function (ev) {
                var event = window.event || ev;
                // 计算偏移角度
                x = event.clientY - disX;
                y = event.clientX - disY;
                oDiv.style.transform = 'perspective(8000px) rotateY(' + y + 'deg) rotateX(201deg)'
            };
            document.onmouseup = function () {
                document.onmousemove = null;
            };
            return false;
        };


    }
})();
