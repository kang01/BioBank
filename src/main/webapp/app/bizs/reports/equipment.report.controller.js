/**
 * Created by gaokangkang on 2018/1/2.
 */
(function() {
    'use strict';

    angular
        .module('bioBankApp')
        .controller('EquipmentReportController', EquipmentReportController);

    EquipmentReportController.$inject = ['$scope','ReportService'];

    function EquipmentReportController ($scope,ReportService) {
        var vm = this;
        vm.keys = [];
        vm.equipments = [];
        _queryEquipment();

        function _queryEquipment() {
            ReportService.queryEquipment().success(function (data) {
                var list = data;
                vm.equipmentList = _.groupBy(list, 'equipmentCode');
                for(var key in vm.equipmentList){
                    //只遍历对象自身的属性，而不包含继承于原型链上的属性。
                    if (vm.equipmentList.hasOwnProperty(key) === true){
                        vm.keys.push(key);
                        // vm.equipments.push(equipmentList[key]);
                    }
                }
                // console.log(JSON.stringify(vm.equipments))
            })

        }

    }
})();
