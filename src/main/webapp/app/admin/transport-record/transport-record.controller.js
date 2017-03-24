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

        // vm.dtOptions = DTOptionsBuilder.fromSource('api/res/tranships')
        vm.dtOptions = DTOptionsBuilder.fromSource(function( data, callback, oSettings ){
            var jqDT = this;
            console.log(jqDT, jqDT._fnLog);
            TransportRecordService.getJqDataTableValues(data, oSettings).then(function (res){
                var json = res.data;
                var error = json.error || json.sError;
                // if ( error ) {
                //     _fnLog( oSettings, 0, error );
                // }
                oSettings.json = json;
                callback( json );
            });


            // var baseAjax = {
            //     "data": data,
            //     "success": function (json) {
            //         var error = json.error || json.sError;
            //         if ( error ) {
            //             _fnLog( oSettings, 0, error );
            //         }
            //
            //         oSettings.json = json;
            //         callback( json );
            //     },
            //     "dataType": "json",
            //     "cache": false,
            //     "type": oSettings.sServerMethod,
            //     "error": function (xhr, error, thrown) {
            //         var ret = _fnCallbackFire( oSettings, null, 'xhr', [oSettings, null, oSettings.jqXHR] );
            //
            //         if ( $.inArray( true, ret ) === -1 ) {
            //             if ( error == "parsererror" ) {
            //                 _fnLog( oSettings, 0, 'Invalid JSON response', 1 );
            //             }
            //             else if ( xhr.readyState === 4 ) {
            //                 _fnLog( oSettings, 0, 'Ajax error', 7 );
            //             }
            //         }
            //
            //         _fnProcessingDisplay( oSettings, false );
            //     }
            // };
        })
        // vm.dtOptions = DTOptionsBuilder.fromFnPromise(function() {
        //     return $resource('data.json').query().$promise;
        // })
            .withOption('sServerMethod','POST')
            .withOption('processing',true)
            .withOption('serverSide',true)
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
        // vm.dtOptions = DTOptionsBuilder.newOptions()
        //     .withOption('ajax',{
        //         url:'api/temp/tranships',
        //         type:'get'
        //     })
        //     .withDataProp('data')
        //     .withOption('processing',true)
        //     .withOption('serverSide',true)
        //     .withPaginationType('full_number');
        vm.dtColumns = [
            DTColumnBuilder.newColumn('projectCode').withTitle('项目点').notSortable(),
            DTColumnBuilder.newColumn('projectSiteCode').withTitle('项目编号').notSortable(),
            DTColumnBuilder.newColumn('transhipDate').withTitle('转运日期').notSortable(),
            DTColumnBuilder.newColumn('receiver').withTitle('接收人').notSortable(),
            DTColumnBuilder.newColumn('receiveDate').withTitle('接收日期').notSortable(),
            DTColumnBuilder.newColumn('sampleSatisfaction').withTitle('满意度').notSortable(),
            DTColumnBuilder.newColumn('transhipState').withTitle('状态').notSortable(),
            DTColumnBuilder.newColumn(null).withTitle('操作').notSortable()
                .renderWith(actionsHtml)
        ];
        function createdRow(row, data, dataIndex) {
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
