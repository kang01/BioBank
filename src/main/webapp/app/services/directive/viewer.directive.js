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
                imagesData:"=",
                uploadFile: "&",
                delFile: "&"
            },
            replace: false,
            templateUrl:'app/bizs/transport-record/template/viewer-images-template.html',
            link: function(scope, element, attrs) {
                var viewer = null;

                scope.$watch('imagesData',function (newValue,oldValue) {
                    if (!newValue || !newValue.length){
                        return;
                    }

                    if(viewer /* && viewer.ready */){
                        viewer.update();
                    } else {
                        setTimeout(function (data) {
                            var dom = element[0];
                            viewer = new Viewer(dom, {
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
                        },500);
                    }
                });
            },
            controller: ImagesViewerController,
            controllerAs: 'vm'
        };
    }

    function ImagesViewerController($scope) {
        var vm = this;
        // $scope.$watch('imagesData',function () {
        //     vm.transportRecordUploadInfo = $scope.imagesData;
        // });
        vm.uploadFile = function (status,item) {
            $scope.uploadFile({status:2,imgData:item});
        };
        vm.delUploadInfo = function (item) {
            $scope.delFile({id:item.id});
        }

    }
})();
