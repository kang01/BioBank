/**
 * Created by gaokangkang on 2017/3/10.
 */
(function() {
    'use strict';

    angular
        .module('bioBankApp')
        .controller('TransportRecordController', TransportRecordController);

    TransportRecordController.$inject = ['$scope', '$compile', 'Principal', 'TransportRecordService', 'ParseLinks', 'AlertService', '$state', 'pagingParams', 'paginationConstants', 'JhiLanguageService','DTOptionsBuilder','DTColumnBuilder'];

    function TransportRecordController($scope, $compile, Principal, TransportRecordService, ParseLinks, AlertService, $state, pagingParams, paginationConstants, JhiLanguageService,DTOptionsBuilder,DTColumnBuilder) {
        var vm = this;

        vm.authorities = ['ROLE_USER', 'ROLE_ADMIN'];
        vm.currentAccount = null;
        // vm.loadAll = loadAll;
        vm.users = [];
        vm.page = 1;
        vm.totalItems = null;
        // vm.clear = clear;
        vm.links = null;
        // vm.loadPage = loadPage;
        vm.predicate = pagingParams.predicate;
        vm.reverse = pagingParams.ascending;
        vm.itemsPerPage = paginationConstants.itemsPerPage;
        // vm.transition = transition;
        vm.datePickerOpenStatus = {};
        vm.openCalendar = openCalendar;

        vm.dtInstance = {};
        vm.persons = {};
        vm.message = '';
        vm.edit = edit;
        // vm.loadAll();


        // function loadAll () {
        //     TransportRecordService.query({}, onSuccess, onError);
        // }

        // function onSuccess(data, headers) {
            // vm.links = ParseLinks.parse(headers('link'));
            // vm.totalItems = headers('X-Total-Count');
            // vm.queryCount = vm.totalItems;
            // vm.page = pagingParams.page;
            // vm.transportRecordList = data;
        // }

        function onError(error) {
            AlertService.error(error.data.message);
        }

        vm.dtOptions = DTOptionsBuilder.fromFnPromise(function () {
            return TransportRecordService.query().$promise;
        })
            .withPaginationType('full_numbers')
            .withOption('createdRow', createdRow)
            .withColumnFilter({
                aoColumns: [{
                    type: 'text',
                    width:50
                }, {
                    type: 'text',
                    bRegex: true,
                    bSmart: true
                }, {
                    type: 'text',
                    bRegex: true,
                    bSmart: true
                }, {
                    type: 'text',
                    bRegex: true,
                    bSmart: true
                }, {
                    type: 'text',
                    bRegex: true,
                    bSmart: true
                }, {
                    type: 'text',
                    bRegex: true,
                    bSmart: true
                }, {
                    type: 'select',
                    bRegex: false,
                    width:50,
                    values: ['1', '2', '3', '4', '5']
                }, {
                    type: 'select',
                    bRegex: false,
                    width:50,
                    values: ['Yoda', 'Titi', 'Kyle', 'Bar', 'Whateveryournameis']
                }]
            });
        vm.dtColumns = [
            DTColumnBuilder.newColumn('projectCode').withTitle('项目点').notSortable(),
            DTColumnBuilder.newColumn('projectSiteCode').withTitle('项目编号').notSortable(),
            DTColumnBuilder.newColumn('transhipDate').withTitle('转运日期').notSortable(),
            DTColumnBuilder.newColumn('transhipState').withTitle('样本类型').notSortable(),
            DTColumnBuilder.newColumn('transhipReceive').withTitle('接收人').notSortable(),
            DTColumnBuilder.newColumn('receiveDate').withTitle('接收日期').notSortable(),
            DTColumnBuilder.newColumn('sampleSatisfaction').withTitle('满意度').notSortable(),
            DTColumnBuilder.newColumn('status').withTitle('状态').notSortable(),
            DTColumnBuilder.newColumn(null).withTitle('操作').notSortable()
                .renderWith(actionsHtml)
        ];
        function createdRow(row, data, dataIndex) {
            // Recompiling so we can bind Angular directive to the DT
            $compile(angular.element(row).contents())($scope);
        }
        // function headerCallback(header) {
        //     if (!vm.headerCompiled) {
        //         // Use this headerCompiled field to only compile header once
        //         vm.headerCompiled = true;
        //         $compile(angular.element(header).contents())($scope);
        //     }
        // }
        function edit(person) {
            vm.message = 'You are trying to edit the row: ' + JSON.stringify(person);
            // Edit some data and call server to make changes...
            // Then reload the data so that DT is refreshed
            vm.dtInstance.reloadData();
        }
        function actionsHtml(data, type, full, meta) {
            vm.persons[data.id] = data;
            return '<button type="button" class="btn btn-warning" ui-sref="transport-record-edit({id:vm.persons.id})">' +
                '   <i class="fa fa-edit"></i>' +
                '</button>&nbsp;'
        }
        function openCalendar (date) {
            vm.datePickerOpenStatus[date] = true;
        }
    }
})();
