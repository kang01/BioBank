/**
 * Created by gaokangkang on 2017/3/14.
 */
(function() {
    'use strict';

    angular
        .module('bioBankApp')
        .controller('FrozenStorageBoxModalController', FrozenStorageBoxModalController);

    FrozenStorageBoxModalController.$inject = ['DTOptionsBuilder','DTColumnBuilder','$uibModalInstance','$uibModal'];

    function FrozenStorageBoxModalController(DTOptionsBuilder,DTColumnBuilder,$uibModalInstance,$uibModal) {
        this.dtOptions = DTOptionsBuilder.newOptions()
            .withOption('searching', false);
        // this.dtColumns = [
        //     DTColumnBuilder.newColumn('id').withTitle('冻存盒号'),
        //     DTColumnBuilder.newColumn('firstName').withTitle('状态'),
        //     DTColumnBuilder.newColumn('lastName').withTitle('样本类型').notVisible(),
        //     DTColumnBuilder.newColumn('lastName').withTitle('样本数').notVisible(),
        //     DTColumnBuilder.newColumn('lastName').withTitle('是否分装').notVisible()
        // ];

        this.cancel = function () {
            $uibModalInstance.dismiss('cancel');
        };
        this.ok = function () {
            $uibModalInstance.close();
        };
    }
})();
