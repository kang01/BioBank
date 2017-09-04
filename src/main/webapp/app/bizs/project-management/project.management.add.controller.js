/**
 * Created by gaokangkang on 2017/8/31.
 */
(function() {
    'use strict';

    angular
        .module('bioBankApp')
        .controller('ProjectManagementAddController', ProjectManagementAddController);

    ProjectManagementAddController.$inject = ['$scope'];

    function ProjectManagementAddController($scope) {
        var vm = this;
        vm.titles = [
            {id:1,name:"项目信息",className:"active"},
            {id:2,name:"项目样本分类",className:""},
            {id:3,name:"项目点",className:""}
        ];
        vm.nextStep = _fnNextStep;
        vm.preStep = _fnPreStep;
        //下一步
        function _fnNextStep() {
            var id = 0;
            _.forEach(vm.titles,function (title) {
                if(title.id != 3){
                    if(title.className == "active"){
                        title.className = "finish";
                        id = title.id +1;
                    }

                }
                if(title.id == id){
                    title.className = 'active'
                }
            })
        }
        //上一步
        function _fnPreStep() {
            var titles = _.orderBy(vm.titles,'id','desc');
            _.forEach(titles,function (title) {
                if(title.className == "finish"){
                    title.className = 'active';
                    var titles1 = _.orderBy(vm.titles,'id','asc');
                    titles1[title.id].className = '';
                    return false;
                }
            });

        }

    }
})();
