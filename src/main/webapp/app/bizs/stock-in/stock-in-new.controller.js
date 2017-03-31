/**
 * Created by zhuyu on 2017/3/14.
 */
(function() {
    'use strict';

    angular
        .module('bioBankApp')
        .controller('StockInNewController', StockInNewController);

    StockInNewController.$inject = ['$scope','hotRegisterer','StockInService','DTOptionsBuilder','DTColumnBuilder','$uibModal','$state','entity','frozenBoxByCodeService',
        'SampleTypeService','AlertService','FrozenBoxTypesService','FrozenBoxByIdService','EquipmentService','AreasByEquipmentIdService','SupportacksByAreaIdService','ProjectService','ProjectSitesByProjectIdService'];

    function StockInNewController($scope,hotRegisterer,StockInService,DTOptionsBuilder,DTColumnBuilder,$uibModal,$state,entity,frozenBoxByCodeService,
                                          SampleTypeService,AlertService,FrozenBoxTypesService,FrozenBoxByIdService,EquipmentService,AreasByEquipmentIdService,SupportacksByAreaIdService,ProjectService,ProjectSitesByProjectIdService) {
        var vm = this;

    }
})();
