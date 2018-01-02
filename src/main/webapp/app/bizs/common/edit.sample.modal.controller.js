/**
 * Created by gaokangkang on 2017/12/29.
 */
(function() {
    'use strict';

    angular
        .module('bioBankApp')
        .controller('EditSampleModal', EditSampleModal);

    EditSampleModal.$inject = ['$uibModalInstance','DTColumnBuilder','BioBankDataTable','BioBankSelectize','items','toastr','SampleTypeService','MasterMethod','ProjectSitesByProjectIdService','MasterData','StockInInputService'];

    function EditSampleModal($uibModalInstance,DTColumnBuilder,BioBankDataTable,BioBankSelectize,items,toastr,SampleTypeService,MasterMethod,ProjectSitesByProjectIdService,MasterData,StockInInputService) {
        var vm = this;
        vm.entity = {
            memo:null,
            status:null,
            sampleVolumns:null,
            sampleTypeId:null,
            sampleClassificationId:null,
            label1:null,
            label2:null,
            label3:null,
            label4:null
        };

        var _selectedSample = items.selectedSample;
        var _projectId = items.obj.projectId;
        var _projectCode = items.obj.projectCode;
        var _projectName = items.obj.projectName;
        vm.singleMultipleFlag = items.obj.singleMultipleFlag;
        vm.entity.sampleTypeId = items.obj.sampleTypeId;
        vm.entity.sampleClassificationId = items.obj.sampleClassificationId;
        vm.isMixed = items.obj.isMixed;
        vm.projectName = _projectCode+","+_projectName;
        if(_selectedSample.length == 1){
            vm.entity.sampleCode = _selectedSample[0].sampleCode;
            vm.entity.memo = _selectedSample[0].memo;
            vm.entity.status = _selectedSample[0].status;
            vm.entity.sampleVolumns = _selectedSample[0].sampleVolumns;
        }

        _loadInitializeDataFromServer();
        _loadDataTable();

        // 加载页面上的初始化数据
        function _loadInitializeDataFromServer(){


            //冻存管状态
            vm.tubeStatusOptions = MasterData.frozenTubeStatus;
            _.remove(vm.tubeStatusOptions,{id:"3005"});
            //样本分类
            MasterMethod.querySampleType().success(function (data) {
                vm.sampleTypeOptions = _.orderBy(data, ['sampleTypeCode'], ['desc']);
                _.remove(vm.sampleTypeOptions,{sampleTypeCode:"99"});
            });
            //样本类型
            MasterMethod.querySampleClass(_projectId,vm.entity.sampleTypeId).success(function (data) {
                vm.sampleClassOptions = data;
            });
            //状态
            vm.tubeStatusConfig = {
                valueField:'id',
                labelField:'name',
                maxItems: 1,
                onChange:function(value){}
            };
            //样本类型
            vm.sampleTypeConfig = {
                valueField:'id',
                labelField:'sampleTypeName',
                maxItems: 1,
                onChange:function (value) {
                    vm.sampleClassSelectize.clearOptions();
                    vm.box.sampleClassificationId = null;
                    if(value){
                        var sampleTypeCode = _.find(vm.sampleTypeOptions,{'id':+value}).sampleTypeCode;
                        //获取样本分类
                        // _querySampleClass(value);
                        // //RNA：大橘盒 DNA：96孔板
                        // _changeBoxType(sampleTypeCode,vm.boxTypeOptions,vm.boxTypeInstance);

                    }
                }
            };
            //样本分类
            vm.sampleClassConfig = {
                valueField:'sampleClassificationId',
                labelField:'sampleClassificationName',
                maxItems: 1,
                onInitialize:function (initialize) {
                    vm.sampleClassSelectize = initialize;
                },
                onChange:function (value) {
                }
            };
            //标签
            var _tagSelectize = {
                create : true,
                persist:false,
                clearMaxItemFlag : true
            };
            vm.TagConfig = BioBankSelectize.buildSettings(_tagSelectize);


        }
        //选中的样本的DataTable
        function _loadDataTable() {
            vm.sampleInstance = {};
            vm.sampleColumns = [
                DTColumnBuilder.newColumn('sampleCode').withOption("width", "50").notSortable().withOption('searchable',false).withTitle('序号'),
                DTColumnBuilder.newColumn('sampleCode').withTitle('冻存管编码').notSortable()
            ];
            vm.sampleOptions = BioBankDataTable.buildDTOption("BASIC", 316, 10)
                .withOption('rowCallback', rowCallback);
            function rowCallback(nRow, oData, iDisplayIndex, iDisplayIndexFull)  {
                $('td:first', nRow).html(iDisplayIndex+1);
                return nRow;
            }
            vm.sampleOptions.withOption("data",_selectedSample);
        }
        //标签
        function _updateTagsData() {
            var tags1 = [];
            var tags2 = [];

            _.forEach(vm.tags,function (tag,i) {
                if(i < 3){
                    tags1.push(tag);
                }else{
                    tags2.push(tag);
                }
            });
            _.forEach(tags1,function (tag,i) {
                if(tag){
                    var index = i+1;
                    vm.entity["label"+index] = tag;
                }
            });
            if(tags2.length){
                vm.entity.label4 = _.join(tags2,",");
            }

        }


        function onError() {

        }

        vm.ok = function () {
            _updateTagsData();
            $uibModalInstance.close(vm.entity);
        };
        vm.cancel = function () {
            $uibModalInstance.dismiss('cancel');
        };
    }
})();