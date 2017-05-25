/**
 * Created by gaokangkang on 2017/5/12.
 * 出库申请列表
 */
(function() {
    'use strict';

    angular
        .module('bioBankApp')
        .controller('requirementListController', requirementListController);

    requirementListController.$inject = ['$scope','$compile','$state','toastr','DTOptionsBuilder','DTColumnBuilder','RequirementService'];

    function requirementListController($scope,$compile,$state,toastr,DTOptionsBuilder,DTColumnBuilder,RequirementService) {
        var vm = this;
        vm.authorities = ['ROLE_USER', 'ROLE_ADMIN'];
        vm.dtInstance = {};
        vm.add = _fnAdd;
        function _fnAdd() {
            RequirementService.saveRequirementEmpty().success(function (data) {
                $state.go('requirement-new',{applyId : data.id,applyCode : data.applyCode});
            })

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
                    },{
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
                    type: 'text',
                    bRegex: false,
                    width:50
                }, {
                    type: 'text',
                    bRegex: false,
                    width:50
                }, {
                    type: 'select',
                    bRegex: true,
                    width:50,
                    values: [
                        {value:'1101',label:"进行中"},
                        {value:"1102",label:"待批准"},
                        {value:"1103",label:"已批准"},
                        {value:"1104",label:"已作废"}
                    ]
                }]
            });

        vm.dtColumns = [
            DTColumnBuilder.newColumn("").withTitle('').withOption('width', '10px').notSortable().renderWith(extraHtml),
            DTColumnBuilder.newColumn('applyCode').withTitle('申请单号').withOption('width', '100px'),
            DTColumnBuilder.newColumn('delegateName').withTitle('委托方').withOption('width', '220px'),
            DTColumnBuilder.newColumn('applyPersonName').withTitle('委托人').withOption('width', '100px'),
            DTColumnBuilder.newColumn('applyTime').withTitle('需求日期').withOption('width', '120px'),
            DTColumnBuilder.newColumn('purposeOfSample').withTitle('用途').withOption('width', '220px'),
            DTColumnBuilder.newColumn('countOfSample').withTitle('样本需求量').withOption('width', '90px'),
            DTColumnBuilder.newColumn('sampleTypes').withTitle('样本类型').withOption('width', '100px'),
            DTColumnBuilder.newColumn('status').withTitle('状态').withOption('width', '50px'),
            DTColumnBuilder.newColumn("").withTitle('操作').notSortable().renderWith(actionsHtml).withOption('width', '100px'),
            DTColumnBuilder.newColumn('id').notVisible()
        ];
        //列表中字段替换
        function createdRow(row, data, dataIndex) {
            var status = '';
            switch (data.status){
                case '1101': status = '进行中';break;
                case '1102': status = '待批准';break;
                case '1103': status = '已批准';break;
                case '1104': status = '已作废';break;
            }
            $('td:eq(8)', row).html(status);
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
                RequirementService.copyRequirementList(row.data().id).then(function (data) {
                    // console.log(JSON.stringify(data.data))
                    row.child(format(data.data)).show();
                    $(tr).addClass('shown');
                });

            }
        }
        function format ( items ) {
            var html =  $('<table class="table" style="width: 100%"><tbody></tbody></table>');
            for(var i = 0; i < items.length; i++){
                var tr = $("<tr />").html(

                    "<td style='display: none'>"+items[i].id+"</td>"+
                    "<td style='width: 2%'> </td>"+
                    "<td style='width: 13%'>"+items[i].applyCode+"</td>"+
                    "<td style='width: 17%'>"+items[i].delegateName+"</td>"+
                    "<td style='width: 10%'>"+items[i].applyPersonName+"</td>"+
                    "<td style='width: 13%'>"+items[i].applyTime+"</td>"+
                    "<td style='width: 17%'>"+items[i].purposeOfSample+"</td>"+
                    "<td style='width: 10%'>"+items[i].countOfSample+"</td>"+
                    "<td style='width: 12%'>"+items[i].sampleTypes+"</td>"+
                    "<td style='width: 10%'>"+statusShow(items[i].status)  +"</td>"+
                    "<td ><button  class='btn btn-warning addApplyId'><i class='fa fa-edit'></i></button></td>"
                    );
                $(".addApplyId", tr).click(function(){
                   var applyId = $(tr)[0].childNodes[0].innerText;
                    $state.go('requirement-edit',{applyId:applyId});
                });
                html.append(tr);

            }

            return html;
        }
        function statusShow(staus) {
            var statusVal = "";
            switch (staus){
                case '1101': statusVal = '进行中';break;
                case '1102': statusVal = '待批准';break;
                case '1103': statusVal = '已批准';break;
                case '1104': statusVal = '已作废';break;
            }
            return statusVal

        }
        function actionsHtml(data, type, full, meta) {
            return '<button type="button" class="btn btn-warning" ui-sref="requirement-edit({applyId:'+ full.id +'})">' +
                '   <i class="fa fa-edit"></i>' +
                '</button>&nbsp;'+ '<button ng-if="'+full.status+'== 1103" type="button" class="btn btn-warning" ui-sref="requirement-edit({applyId:'+ full.id +',addApplyFlag:1})">' +
                '附加' +
                '</button>&nbsp;';
        }
        function extraHtml(data, type, full, meta) {
            return '<div class="details-control"></div>'
        }


        setTimeout(function () {
            vm.dtInstance.rerender();
        },500)
    }
})();
