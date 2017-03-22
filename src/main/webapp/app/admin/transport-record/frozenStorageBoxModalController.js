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
        this.dtOptions = DTOptionsBuilder.fromSource('app/admin/transport-record/data.json')
            .withPaginationType('full_numbers')
            .withOption('searching', false);
        this.dtColumns = [
            DTColumnBuilder.newColumn('id').withTitle('ID'),
            DTColumnBuilder.newColumn('firstName').withTitle('First name'),
            DTColumnBuilder.newColumn('lastName').withTitle('Last name').notVisible()
        ];

        this.cancel = function () {
            $uibModalInstance.dismiss('cancel');
        };
        this.ok = function () {
            $uibModalInstance.close();
        };
    }
})();
