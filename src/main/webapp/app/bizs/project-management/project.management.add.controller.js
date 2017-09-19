/**
 * Created by gaokangkang on 2017/8/31.
 */
(function() {
    'use strict';

    angular
        .module('bioBankApp')
        .controller('ProjectManagementAddController', ProjectManagementAddController);

    ProjectManagementAddController.$inject = ['$scope','$state'];

    function ProjectManagementAddController($scope,$state) {
        var vm = this;
        vm.titles = [
            {id:1,name:"项目信息",className:"active"},
            {id:2,name:"项目样本分类",className:""},
            {id:3,name:"项目点",className:""}
        ];
        vm.color = "#fff";
        vm.nextStep = _fnNextStep;
        vm.preStep = _fnPreStep;
        //下一步
        function _fnNextStep() {
            var id = 0;
            _.forEach(vm.titles,function (title) {
                if(title.id != vm.titles.length){
                    if(title.className == "active"){
                        title.className = "finish";
                        id = title.id +1;
                    }

                }
                if(title.id == id){
                    title.className = 'active';
                    if(title.id == 1){
                        $state.go("project-management-info")
                    }
                    if(title.id == 2){
                        $state.go("project-management-sample-type")
                    }
                    if(title.id == 3){
                        $state.go("project-management-sites")
                    }
                    if(title.id == 4){
                        $state.go("project-management-equipment")
                    }
                }
            });

        }
        //上一步
        function _fnPreStep() {
            var titles = _.orderBy(vm.titles,'id','desc');
            _.forEach(titles,function (title) {
                if(title.className == "finish"){
                    title.className = 'active';
                    var titles1 = _.orderBy(vm.titles,'id','asc');
                    titles1[title.id].className = '';
                    if(title.id == 1){
                        $state.go("project-management-info")
                    }
                    if(title.id == 2){
                        $state.go("project-management-sample-type")
                    }
                    if(title.id == 3){
                        $state.go("project-management-sites")
                    }
                    if(title.id == 4){
                        $state.go("project-management-equipment")
                    }
                    return false;
                }
            });

        }

    }
})();
