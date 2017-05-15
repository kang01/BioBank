/**
 * Created by gaokangkang on 2017/5/12.
 * 出库申请列表
 */
(function() {
    'use strict';

    angular
        .module('bioBankApp')
        .controller('requirementListController', requirementListController);

    requirementListController.$inject = ['$scope','$compile','$state','DTOptionsBuilder','DTColumnBuilder','RequirementService'];

    function requirementListController($scope,$compile,$state,DTOptionsBuilder,DTColumnBuilder,RequirementService) {
        var vm = this;
        vm.authorities = ['ROLE_USER', 'ROLE_ADMIN'];
        vm.dtInstance = {};
        vm.add = _fnAdd;
        function _fnAdd() {
            $state.go('requirement-new');
        }
        vm.dtOptions = DTOptionsBuilder.newOptions()
            .withOption('processing',true)
            .withOption('serverSide',true)
            .withFnServerData(function ( sSource, aoData, fnCallback, oSettings ) {
                var data = {};
                for(var i=0; aoData && i<aoData.length; ++i){
                    var oData = aoData[i];
                    data[oData.name] = oData.value;
                }
                var jqDt = this;
                RequirementService.queryRequirementList(data, oSettings).then(function (res){
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
            .withOption('rowCallback', rowCallback)
            .withColumnFilter({
                aoColumns: [
                    {
                    },
                    {
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
                    values: [
                        {value:"10",label:"非常满意"},
                        {value:"9",label:"较满意"},
                        {value:"8",label:"满意"},
                        {value:"7",label:"有少量空管"},
                        {value:"6",label:"有许多空管"},
                        {value:"5",label:"有大量空管"},
                        {value:"4",label:"有少量空孔"},
                        {value:"3",label:"有少量错位"},
                        {value:"2",label:"有大量错位"},
                        {value:"1",label:"非常不满意"}
                    ]
                }, {
                    type: 'select',
                    bRegex: true,
                    width:50,
                    values: [
                        {value:'1001',label:"进行中"},
                        {value:"1002",label:"待入库"},
                        {value:"1003",label:"已入库"},
                        {value:"1004",label:"已作废"}
                    ]
                }]
            });

        vm.dtColumns = [
            DTColumnBuilder.newColumn("").withTitle('').withOption('width', '50px').notSortable().renderWith(extraHtml),
            DTColumnBuilder.newColumn('projectSiteCode').withTitle('项目点'),
            DTColumnBuilder.newColumn('projectCode').withTitle('项目编号'),
            DTColumnBuilder.newColumn('transhipDate').withTitle('转运日期'),
            DTColumnBuilder.newColumn('receiver').withTitle('接收人'),
            DTColumnBuilder.newColumn('receiveDate').withTitle('接收日期'),
            DTColumnBuilder.newColumn('sampleSatisfaction').withTitle('满意度'),
            DTColumnBuilder.newColumn('transhipState').withTitle('状态'),
            DTColumnBuilder.newColumn("").withTitle('操作').notSortable().renderWith(actionsHtml)
        ];
        //列表中字段替换
        function createdRow(row, data, dataIndex) {
            var transhipState = '';
            var sampleSatisfaction = '';
            switch (data.transhipState){
                case '1001': transhipState = '进行中';break;
                case '1002': transhipState = '待入库';break;
                case '1003': transhipState = '已入库';break;
                case '1004': transhipState = '已作废';break;
            }
            switch (data.sampleSatisfaction){
                case 1: sampleSatisfaction = '非常不满意';break;
                case 2: sampleSatisfaction = '有大量错位';break;
                case 3: sampleSatisfaction = '有少量错位';break;
                case 4: sampleSatisfaction = '有少量空孔';break;
                case 5: sampleSatisfaction = '有大量空管';break;
                case 6: sampleSatisfaction = '有许多空管';break;
                case 7: sampleSatisfaction = '有少量空管';break;
                case 8: sampleSatisfaction = '满意';break;
                case 9: sampleSatisfaction = '较满意';break;
                case 10: sampleSatisfaction = '非常满意';break;
            }
            $('td:eq(5)', row).html(sampleSatisfaction);
            $('td:eq(6)', row).html(transhipState);
            $compile(angular.element(row).contents())($scope);
        }
        //展开
        function rowCallback(nRow, oData)  {
            $('td', nRow).unbind('click');
            $('td:first',nRow).css("cursor","pointer");
            $('td:first', nRow).bind('click',function () {
                $scope.$apply(function () {
                    extraClickHandler(nRow);
                })
            });
        }
        function extraClickHandler(tr) {
            var row =  vm.dtInstance.DataTable.row(tr);
            if ( row.child.isShown() ) {
                // This row is already open - close it
                row.child.hide();
                $(tr).removeClass('shown');
            }
            else {
                // Open this row
                row.child(format(row.data())).show();
                $(tr).addClass('shown');
            }
        }
        function format ( d ) {
            return '<table cellpadding="5" cellspacing="0" border="0" style="padding-left:50px;">'+
                '<tr>'+
                '<td>projectCode:</td>'+
                '<td>'+d.projectCode+'</td>'+
                '</tr>'+
                '<tr>'+
                '<td>receiver:</td>'+
                '<td>'+d.receiver+'</td>'+
                '</tr>'+
                '<tr>'+
                '<td>transhipCode:</td>'+
                '<td>'+d.transhipCode+'</td>'+
                '</tr>'+
                '</table>';
        }
        function actionsHtml(data, type, full, meta) {
            return '<button type="button" class="btn btn-warning" ui-sref="transport-record-edit({id:'+ full.id +'})">' +
                '   <i class="fa fa-edit"></i>' +
                '</button>&nbsp;'
        }
        function extraHtml(data, type, full, meta) {
            return '<div class="details-control"></div>'
        }
    }
})();
