/**
 * Created by gaokangkang on 2017/7/31.
 */
(function()  {
    'use  strict';

    angular
        .module('bioBankApp')
        .directive('viewer',  viewer)
        .controller('ImagesViewerController',ImagesViewerController);

    viewer.$inject  =  [];
    ImagesViewerController.$inject  =  ['$scope'];

    function  viewer()  {
        return {
            restrict: 'A',
            scope:{
                imagesData:"="
            },
            replace: false,
            templateUrl:'app/bizs/transport-record/template/viewer-images-template.html',
            link: function(scope, element, attrs) {


                var viewer = new Viewer(document.getElementById('dowebok'), {
                    url: 'data-original'

                    // build: function() {
                    //     alert('build');
                    // },
                    //
                    // built: function() {
                    //     alert('built');
                    // },
                    //
                    // show: function() {
                    //     alert('show');
                    // },
                    // shown: function() {
                    //     alert('shown');
                    // },
                    //
                    // hide: function() {
                    //     alert('hide');
                    // },
                    //
                    // hidden: function() {
                    //     alert('hidden');
                    // },
                    //
                    // view: function() {
                    //     alert('view');
                    // },
                    //
                    // viewed: function() {
                    //     alert('viewed');
                    // }
                });
            },
            controller: ImagesViewerController,
            controllerAs: 'vm'
        };
    }

    function ImagesViewerController($scope) {
        var vm = this;
        $scope.$watch('imagesData',function () {
            // console.log($scope.imagesData);
            vm.image = $scope.imagesData
        });

    }
})();
