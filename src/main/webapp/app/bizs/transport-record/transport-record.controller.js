/**
 * Created by gaokangkang on 2017/3/10.
 */
(function() {
    'use strict';

    angular
        .module('bioBankApp')
        .controller('TransportRecordController', TransportRecordController);

    TransportRecordController.$inject = ['$scope', '$compile', 'Principal', 'TransportRecordService', 'ParseLinks', 'AlertService', '$state', 'pagingParams', 'paginationConstants', 'JhiLanguageService','DTOptionsBuilder','DTColumnBuilder','TranshipNewEmptyService'];

    function TransportRecordController($scope, $compile, Principal, TransportRecordService, ParseLinks, AlertService, $state, pagingParams, paginationConstants, JhiLanguageService,DTOptionsBuilder,DTColumnBuilder,TranshipNewEmptyService) {
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
        vm.add = add;

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
        function add() {
            TranshipNewEmptyService.save({},onTranshipNewEmptyService,onError)
        }
        function onTranshipNewEmptyService(data) {
            console.log(data.id);
            // vm.transhipId = data.id;
            // vm.transhipCode = data.transhipCode;
            $state.go('transport-record-new',{transhipId : data.id,transhipCode : data.transhipCode});
        }
        function onError(error) {
            AlertService.error(error.data.message);
        }

        vm.dtInstanceCallback = function(instance){
            vm.dtInstance = instance;
        };

        vm.searchSomething = function(){
            var table = vm.dtInstance.DataTable;
            table
                .column( 0 )
                .search( "hello" )
                .draw();
        };

        vm.dtOptions = DTOptionsBuilder.fromSource({
                "url": 'api/res/tranships',
                "dataSrc": "data"
            })
            .withOption('sServerMethod','POST')
            .withOption('processing',true)
            .withOption('serverSide',true)
            // .withOption('sAjaxSource', 'api/res/tranships')
            .withFnServerData(function ( sSource, aoData, fnCallback, oSettings ) {
                var data = {};
                for(var i=0; aoData && i<aoData.length; ++i){
                    var oData = aoData[i];
                    data[oData.name] = oData.value;
                }
                var jqDt = this;
                TransportRecordService.getJqDataTableValues(data, oSettings).then(function (res){
                    var json = res.data;
                    var error = json.error || json.sError;
                    if ( error ) {
                        jqDt._fnLog( oSettings, 0, error );
                    }
                    oSettings.json = json;
                    fnCallback( json );
                }).catch(function(res){
                    console.log(res);

                    var ret = jqDt._fnCallbackFire( oSettings, null, 'xhr', [oSettings, null, oSettings.jqXHR] );

                    if ( $.inArray( true, ret ) === -1 ) {
                        if ( error == "parsererror" ) {
                            jqDt._fnLog( oSettings, 0, 'Invalid JSON response', 1 );
                        }
                        else if ( res.readyState === 4 ) {
                            jqDt._fnLog( oSettings, 0, 'Ajax error', 7 );
                        }
                    }

                    jqDt._fnProcessingDisplay( oSettings, false );
                });
            })


            .withPaginationType('full_numbers')
            .withOption('createdRow', createdRow)
            .withColumnFilter({
                aoColumns: [{
                    type: 'text',
                    width:50,
                    iFilterLength:3
                }, {
                    type: 'text',
                    bRegex: true,
                    bSmart: true,
                    iFilterLength:3
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
                    values: ['1', '2', '3', '4', '5','6','7','8','9','10']
                }, {
                    type: 'select',
                    bRegex: false,
                    width:50,
                    values: ['1001', '1002', '1003', '1004']
                }]
            });

        vm.dtColumns = [
            // DTColumnBuilder.newColumn('id').withTitle('id').notVisible(),
            DTColumnBuilder.newColumn('projectSiteCode').withTitle('项目点'),
            DTColumnBuilder.newColumn('projectCode').withTitle('项目编号'),
            DTColumnBuilder.newColumn('transhipDate').withTitle('转运日期'),
            DTColumnBuilder.newColumn('receiver').withTitle('接收人'),
            DTColumnBuilder.newColumn('receiveDate').withTitle('接收日期'),
            DTColumnBuilder.newColumn('sampleSatisfaction').withTitle('满意度'),
            DTColumnBuilder.newColumn('transhipState').withTitle('状态'),
            DTColumnBuilder.newColumn("").withTitle('操作').notSortable().renderWith(actionsHtml)
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
            return '<button type="button" class="btn btn-warning" ui-sref="transport-record-edit({id:'+ full.id +'})">' +
                '   <i class="fa fa-edit"></i>' +
                '</button>&nbsp;'
        }
        function openCalendar (date) {
            vm.datePickerOpenStatus[date] = true;
        }
    }
})();
