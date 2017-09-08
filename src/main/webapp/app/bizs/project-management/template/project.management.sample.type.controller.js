/**
 * Created by gaokangkang on 2017/9/6.
 */
(function() {
    'use strict';

    angular
        .module('bioBankApp')
        .controller('ProjectManagementSampleTypeController', ProjectManagementSampleTypeController);

    ProjectManagementSampleTypeController.$inject = ['$scope','$state'];

    function ProjectManagementSampleTypeController($scope,$state) {
        var vm = this;
        vm.color = "#fff";
        vm.sampleTypeItem = [
            {name:"aaa"}
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
                vm.sampleTypeItem.push({name:"aaa"});
            }

        }

        function _initFrozenTube() {
            vm.settings = {
                colHeaders : ['1','2','3','4','5','6','7','8','9','10'],
                rowHeaders : ['A','B','C','D','E','F','G','H','J','K'],
                data:vm.frozenTubeArray,
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




    }
})();
