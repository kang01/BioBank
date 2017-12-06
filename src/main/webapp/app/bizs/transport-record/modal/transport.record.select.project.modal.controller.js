//选择项目、项目点
(function() {
    'use strict';

    angular
        .module('bioBankApp')
        .controller('SelectProjectModalController', SelectProjectModalController);

    SelectProjectModalController.$inject = ['$uibModalInstance','ProjectService','ProjectSitesByProjectIdService'];

    function SelectProjectModalController($uibModalInstance,ProjectService,ProjectSitesByProjectIdService) {
        var vm = this;
        vm.project = {};
        _init();
        function _init() {
            //项目
            ProjectService.query({},onProjectSuccess, onError);

            //项目编码
            function onProjectSuccess(data)  {
                vm.projectOptions = data;
            }

        }
        // 项目点
        function onProjectSitesSuccess(data) {
            vm.projectSitesOptions = data;
        }

        function onError(error) {
            // toastr.error(error.data.message);
        }
        vm.projectConfig = {
            valueField:'id',
            labelField:'projectName',
            maxItems: 1,
            searchField:'projectName',
            onChange:function(value){
                if(value){
                    ProjectSitesByProjectIdService.query({id:value},onProjectSitesSuccess,onError);
                }
            }
        };
        //项目点
        vm.projectSitesConfig = {
            valueField:'id',
            labelField:'projectSiteName',
            searchField:'projectSiteName',
            maxItems: 1,
            onChange:function (value) {
                // for(var i = 0; i < vm.projectSitesOptions.length;i++){
                //     if(value == vm.projectSitesOptions[i].id){
                //         vm.transportRecord.projectSiteCode = vm.projectSitesOptions[i].projectSiteCode;
                //         vm.transportRecord.projectSiteName = vm.projectSitesOptions[i].projectSiteName;
                //     }
                // }
            }
        };




        this.cancel = function () {
            $uibModalInstance.dismiss('cancel');
        };
        this.ok = function () {
            $uibModalInstance.close(vm.project);
        };
    }
})();
