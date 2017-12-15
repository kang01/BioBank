/**
 * Created by gaokangkang on 2017/5/12.
 * 出库申请列表
 */
(function () {
    'use strict';

    angular
        .module('bioBankApp')
        .controller('requirementListController', requirementListController)
        .controller('ConfirmModalController', ConfirmModalController);

    requirementListController.$inject = ['$scope', '$compile', '$state', 'toastr', 'DTOptionsBuilder', 'DTColumnBuilder','MasterData', 'RequirementService','$uibModal','BioBankDataTable'];
    ConfirmModalController.$inject = ['$uibModalInstance'];
    function requirementListController($scope, $compile, $state, toastr, DTOptionsBuilder, DTColumnBuilder,MasterData, RequirementService,$uibModal,BioBankDataTable) {
        var vm = this;
        var modalInstance;
        vm.authorities = ['ROLE_USER', 'ROLE_ADMIN'];
        vm.dtInstance = {};
        vm.add = _fnAdd;
        function _fnAdd() {
            RequirementService.saveRequirementEmpty().success(function (data) {
                $state.go('requirement-edit', {applyId: data.id, applyCode: data.applyCode});
            });

        }
        function _fnStockOutSeach(sSource, aoData, fnCallback, oSettings) {
            var data = {};
            for (var i = 0; aoData && i < aoData.length; ++i) {
                var oData = aoData[i];
                data[oData.name] = oData.value;
            }
            var jqDt = this;
            RequirementService.queryRequirementList(data, oSettings).then(function (res) {
                var json = res.data;
                var error = json.error || json.sError;
                if (error) {
                    jqDt._fnLog(oSettings, 0, error);
                }
                oSettings.json = json;
                fnCallback(json);
            }).catch(function (res) {
                console.log(res);

                var ret = jqDt._fnCallbackFire(oSettings, null, 'xhr', [oSettings, null, oSettings.jqXHR]);

                if ($.inArray(true, ret) === -1) {
                    if (error == "parsererror") {
                        jqDt._fnLog(oSettings, 0, 'Invalid JSON response', 1);
                    }
                    else if (res.readyState === 4) {
                        jqDt._fnLog(oSettings, 0, 'Ajax error', 7);
                    }
                }

                jqDt._fnProcessingDisplay(oSettings, false);
            });
        }
        var t;
        vm.dtOptions = BioBankDataTable.buildDTOption("NORMALLY", null, 10)
            .withOption('order', [['1', 'desc']])
            .withOption('serverSide', true)
            .withFnServerData(function (sSource, aoData, fnCallback, oSettings) {

                if(!oSettings.oPreviousSearch.sSearch){
                    _fnStockOutSeach(sSource, aoData, fnCallback, oSettings);
                }else{
                    if(t){
                        clearTimeout(t);
                    }
                    t=setTimeout(function () {
                        _fnStockOutSeach(sSource, aoData, fnCallback, oSettings);
                    },2000);
                }
            })
            .withOption('createdRow', createdRow)
            .withColumnFilter({
                aoColumns: [
                    null,
                    {
                        type: 'text',
                        width: 50,
                        iFilterLength: 3
                    }, {
                        type: 'text',
                        bRegex: true,
                        bSmart: true,
                        iFilterLength: 3
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
                        width: 50
                    }, {
                        type: 'text',
                        bRegex: false,
                        width: 50
                    }, {
                        type: 'text',
                        bRegex: false,
                        width: 50
                    }, {
                        type: 'select',
                        bRegex: true,
                        width: 50,
                        values: [
                            {value: '1101', label: "进行中"},
                            {value: "1102", label: "待批准"},
                            {value: "1103", label: "已批准"},
                            {value: "1190", label: "已作废"}
                        ]
                    }]
            });

        vm.dtColumns = [
            DTColumnBuilder.newColumn("").withTitle('').withOption('width', '10px')
                .withOption('searchable',false).notSortable().renderWith(extraHtml),
            DTColumnBuilder.newColumn('applyCode').withTitle('申请单号').withOption('width', '100px'),
            DTColumnBuilder.newColumn('delegateName').withTitle('委托方').withOption('width', '240px'),
            DTColumnBuilder.newColumn('applyPersonName').withTitle('委托人').withOption('width', '80px'),
            DTColumnBuilder.newColumn('applyTime').withTitle('需求日期').withOption('width', '170px'),
            DTColumnBuilder.newColumn('purposeOfSample').withTitle('用途').withOption('width', 'auto'),
            DTColumnBuilder.newColumn('countOfSample').withTitle('样本量').withOption('width', '60px'),
            DTColumnBuilder.newColumn('countOfStockSample').withTitle('满足样本量').withOption('width', '110px'),
            DTColumnBuilder.newColumn('sampleTypes').withTitle('样本类型').withOption('width', '80px'),
            DTColumnBuilder.newColumn('status').withTitle('状态').withOption('width', '70px'),
            DTColumnBuilder.newColumn("").withTitle('操作').withOption('searchable',false).notSortable().renderWith(actionsHtml).withOption('width', '80px'),
            DTColumnBuilder.newColumn('id').notVisible()
        ];
        //列表中字段替换
        function createdRow(row, data, dataIndex) {
            var status = MasterData.getStatus(data.status);
            // switch (data.status) {
            //     case '1101':
            //         status = '进行中';
            //         break;
            //     case '1102':
            //         status = '待批准';
            //         break;
            //     case '1103':
            //         status = '已批准';
            //         break;
            //     case '1190':
            //         status = '已作废';
            //         break;
            // }
            $('td:eq(9)', row).html(status);

            $('td', row).unbind('click');
            $('td:first', row).css("cursor", "pointer");
            if (data.levelNo) {
                var levelNo = data.levelNo;
                $('td:first', row).bind('click', function () {
                    extraClickHandler(row,levelNo);
                });
            }

            $compile(angular.element(row).contents())($scope);
        }

        //展开
        function rowCallback(nRow, oData) {

        }
        function actionsHtml(data, type, full, meta) {
            if(full.status == '1190'){
                return '';
            }
            if (full.status != '1103') {
                return '<a  type="button" class="btn btn-default btn-xs" ui-sref="requirement-edit({applyId:' + full.id + '})">' +
                    '<i class="fa fa-edit"></i>' +
                    '</a>';
            } else {
                return '<a  type="button" class="btn btn-default btn-xs" ui-sref="requirement-view({applyId:' + full.id + ',viewFlag:1})">' +
                    '<i class="fa fa-eye"></i>' +
                    '</a>&nbsp;' +
                    '<a  type="button" class="btn btn-default btn-xs" ng-click="vm.additionApply(' + full.id + ')">' +
                    '附加' +
                    '</a>';
            }



        }
        function extraClickHandler(tr,levelNo) {
            var row = vm.dtInstance.DataTable.row(tr);
            if (row.child.isShown()) {
                // This row is already open - close it
                row.child.hide();
                $(tr).removeClass('shown');
            }
            else {
                if(levelNo == 1){
                    // Open this row
                    RequirementService.queryCopyRequirementList(row.data().id).then(function (res) {
                        row.child(format(tr, res.data)).show();
                        var child = row.child();
                        $("td:eq(0)", child).addClass('p-0 child-color');
                        $(tr).addClass('shown');
                    });
                }else{
                    RequirementService.queryStairRequirementListBySecondId(row.data().id).then(function (res) {
                        row.child(format(tr, res.data)).show();
                        var child = row.child();
                        $("td:eq(0)", child).addClass('p-0 parent-color');
                        $(tr).addClass('shown');
                    });
                }


            }
        }

        function format(parentRow, items) {
            var html = $('<table class="table table-operate dataTable mt-0" style="width: 100%;"><tbody></tbody></table>');
            for (var i = 0; i < items.length; i++) {
                var tr = $("<tr />").html(
                    "<td data-id="+ (items[i].id ||' ') +"> </td>" +
                    "<td>" + (items[i].applyCode||' ') + "</td>" +
                    "<td>" + (items[i].delegateName||' ') + "</td>" +
                    "<td>" + (items[i].applyPersonName||' ') + "</td>" +
                    "<td>" + (items[i].applyTime||' ') + "</td>" +
                    "<td>" + (items[i].purposeOfSample||' ') + "</td>" +
                    "<td>" + (items[i].countOfSample||' ') + "</td>" +
                    "<td>" + (items[i].countOfStockSample||' ') + "</td>" +
                    "<td>" + (items[i].sampleTypes||' ') + "</td>" +
                    "<td>" + statusShow(items[i].status) + "</td>"
                );
                if (items[i].status == '1101') {
                    $(tr).append("<td ><button  class='addApplyId btn  btn-default btn-xs'><i class='fa fa-edit'></i></button></td>");
                } else if (items[i].status == '1103') {
                    $(tr).append("<td ><button  class='viewApplyId btn btn-default btn-xs'><i class='fa fa-eye'></i></button></td>");
                }

                //附加
                $(".addApplyId", tr).click(function () {
                    var tr = $(this).closest('tr');
                    var applyId = tr.find('td:eq(0)').data('id');
                    $state.go('requirement-additionApply', {applyId: applyId,viewFlag:2});
                });
                //查看页面
                $(".viewApplyId", tr).click(function () {
                    var tr = $(this).closest('tr');
                    var applyId = tr.find('td:eq(0)').data('id');
                    $state.go('requirement-view', {applyId: applyId,viewFlag:4});
                });
                $("td", tr).each(function (index, td){
                    var sourceTd = $('td', parentRow).eq(index);
                    $(td).copyCSS(sourceTd, ['width']);
                });
                html.append(tr);

            }
            return html;
        }

        function statusShow(status) {
            var statusVal = MasterData.getStatus(status);
            // switch (staus) {
            //     case '1101':
            //         statusVal = '进行中';
            //         break;
            //     case '1102':
            //         statusVal = '待批准';
            //         break;
            //     case '1103':
            //         statusVal = '已批准';
            //         break;
            //     case '1190':
            //         statusVal = '已作废';
            //         break;
            // }
            return statusVal;

        }
        vm.additionApply = _fnAdditionApply;

        function _fnAdditionApply(requirementId) {
            modalInstance = $uibModal.open({
                animation: true,
                templateUrl: 'app/bizs/stock-out/requirement/modal/requirement-confirm-modal.html',
                controller: 'ConfirmModalController',
                controllerAs:'vm'
            });

            modalInstance.result.then(function (data) {
                RequirementService.addApplyRequirement(requirementId).success(function (data) {
                    vm.status = data.status;
                    $state.go("requirement-additionApply",{applyId:data.id,viewFlag:3});
                });
            });

        }
        function extraHtml(data, type, full, meta) {
            var html = '';
            //有下一级
            if (full.levelNo == 1) {
                html = '<div class="details-control"></div>';
            }
            if (full.levelNo == 2) {
                html = '<div style="color:#0066FF;margin-left: 4px;"><i class="fa fa-paperclip"></i></div>';
            }
            return html;
        }
    }
    function ConfirmModalController($uibModalInstance) {
        var vm = this;
        vm.ok = function () {
            $uibModalInstance.close(true);
        };
        vm.cancel = function () {
            $uibModalInstance.dismiss('cancel');
        };
    }
})();
