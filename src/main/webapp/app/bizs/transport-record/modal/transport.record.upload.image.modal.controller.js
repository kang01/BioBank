/**
 * Created by gaokangkang on 2017/8/8.
 */
(function() {
    'use strict';

    angular
        .module('bioBankApp')
        .controller('transportUploadImageModalCtrl', transportUploadImageModalCtrl);

    transportUploadImageModalCtrl.$inject = ['$scope','$uibModalInstance','$uibModal','toastr','items','TransportRecordService'];

    function transportUploadImageModalCtrl($scope,$uibModalInstance,$uibModal,toastr,items,TransportRecordService) {
        var vm = this;
        var transportId = items.transportId;
        vm.imagesArray = [];
        vm.reader = new FileReader();   //创建一个FileReader接口
        vm.thumb = {
            imgSrc:'content/images/nosee1.png'
        };
        vm.entity = angular.copy(items.imgData);
        //1：新建 2：编辑
        vm.status = items.status;
        //单次提交图片的函数
        vm.img_upload = function(files) {
            if(files){
                vm.file = files;
                vm.guid = (new Date()).valueOf();   //通过时间戳创建一个随机数，作为键名使用
                vm.reader.readAsDataURL(files);  //FileReader的方法，把图片转成base64
                vm.reader.onload = function(ev) {
                    $scope.$apply(function(){
                        vm.thumb = {
                            imgSrc : ev.target.result ||  'content/images/nosee1.png' //接收base64
                        };

                        var img = new Image();
                        img.src = vm.thumb.imgSrc;
                        var dataURL;
                        img.onload = function(){
                            var canvas = document.getElementById("canvas");
                            var context = canvas.getContext("2d");
                            var width = img.width;
                            var height = img.height;
                            var startWithPos;
                            var startHeightPos;
                            if(width - height > 0){
                                var w = width*140/height;
                                canvas.width = w;
                                canvas.height = 140;
                                startWithPos = (canvas.width-140)/2;
                            }else{
                                var h = height*140/width;
                                canvas.width = 140;
                                canvas.height = h;
                                startHeightPos = (canvas.height-140)/2;
                            }
                            context.drawImage(img,0,0,canvas.width,canvas.height);
                            dataURL  = canvas.toDataURL("image/png");

                            if(dataURL){
                                var img1 = new Image();
                                img1.src = dataURL;
                                img1.onload = function(){
                                    var canvas1 = document.getElementById("canvas1");
                                    canvas1.width = 140;
                                    canvas1.height = 140;
                                    var context1 = canvas1.getContext("2d");
                                    if(startWithPos){
                                        context1.drawImage(img1,startWithPos,0,140,140,0,0,140,140);
                                    }else{
                                        context1.drawImage(img1,0,startHeightPos,140,140,0,0,140,140);
                                    }
                                    var dataURL1  = canvas1.toDataURL("image/png");
                                    // vm.imagesArray.push(dataURL1)
                                    vm.thumb.imgSrc = dataURL1 ;
                                    $scope.$apply();
                                }
                            }
                        };
                    });

                };

            }

        };



        vm.ok = function () {
            //新建
            if(vm.status == '1'){
                if(vm.file){
                    var obj = {};
                    obj.fileTitle = vm.entity.fileTitle;
                    obj.smallImage = vm.thumb.imgSrc;
                    obj.description = vm.entity.description;
                    var fb = new FormData();
                    fb.append('attachment', angular.toJson(obj));
                    fb.append('file', vm.file);
                    TransportRecordService.uploadTransportRecord(transportId,fb).success(function (data) {
                        toastr.success("上传成功!");
                        $uibModalInstance.close();
                    }).error(function (data) {
                        toastr.error(data.message);
                    });
                }
            }else{
                //编辑
                TransportRecordService.editTransportRecordFile(vm.entity).success(function (data) {
                    toastr.success("编辑成功!");
                    $uibModalInstance.close();
                }).error(function (data) {
                    toastr.error(data.message);
                });
            }
        };
        vm.cancel = function () {
            $uibModalInstance.dismiss('cancel');
        };
    }
})();
