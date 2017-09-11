/**
 * Created by gaokangkang on 2017/9/6.
 */
(function() {
    'use strict';

    angular
        .module('bioBankApp')
        .controller('ProjectManagementSampleTypeController', ProjectManagementSampleTypeController);

    ProjectManagementSampleTypeController.$inject = ['$scope','$state','SampleTypeService'];

    function ProjectManagementSampleTypeController($scope,$state,SampleTypeService) {
        var vm = this;
        vm.color = "#fff";
        vm.sampleTypeItem = [
            {id:1,name:"aaa"}
        ];
        vm.createSampleType = _fnCreateSampleType;
        vm.changeColor = _fnChangeColor;

        function _fnChangeColor(color) {
            vm.obj = {
                "background-color" : color
            };
        }
        function _fnCreateSampleType() {
            if(vm.sampleTypeItem.length < 11){
                vm.sampleTypeItem.push({id:vm.sampleTypeItem.length,name:"aaa"});
            }

        }

        function _initFrozenTube() {
            vm.settings = {
                colHeaders : ['1','2','3','4','5','6','7','8','9','10'],
                rowHeaders : ['A','B','C','D','E','F','G','H','J','K'],
                data:[
                    ['', 'Kia', 'Nissan', 'Toyota', 'Honda', 'Mazda', 'Ford'],
                    ['2012', 10, 11, 12, 13, 15, 16],
                    ['2013', 10, 11, 12, 13, 15, 16],
                    ['2014', 10, 11, 12, 13, 15, 16],
                    ['2015', 10, 11, 12, 13, 15, 16],
                    ['2016', 10, 11, 12, 13, 15, 16]
                ],
                minRows: 10,
                minCols: 10,
                fillHandle:false,
                stretchH: 'all',
                autoWrapCol:true,
                wordWrap:true,
                colWidths: 80,
                rowHeaderWidth: 30,
                editor: 'tube',
                multiSelect: true,
                comments: true
            };
        }
        _initFrozenTube();
        _fnQuerySampleType();
        //获取样本类型
        function _fnQuerySampleType() {
            SampleTypeService.querySampleType().success(function (data) {
                vm.sampleTypeOptions = _.orderBy(data, ['sampleTypeId'], ['asc']);

            });
        }
        function _fnQueryProjectSampleClass(projectId,sampleTypeId) {
            SampleTypeService.queryProjectSampleClasses(projectId,sampleTypeId).success(function (data) {
                vm.projectSampleTypeOptions = data;
            });
        }
        vm.sampleTypeConfig = {
            valueField:'id',
            labelField:'sampleTypeName',
            maxItems: 1,
            onChange:function (value) {
                _fnQueryProjectSampleClass('31',value)
            }
        };
        vm.projectSampleTypeConfig = {
            valueField:'sampleClassificationId',
            labelField:'sampleClassificationName',
            maxItems: 1,
            onChange:function (value) {
            }
        };



    }
})();
