/**
 * Created by zhuyu on 2017/3/31.
 */
(function() {
    'use strict';

    angular
        .module('bioBankApp')
        .controller('DemoWebCamController', DemoWebCamController);

    DemoWebCamController.$inject = ['$scope', '$compile', 'Principal', 'StockInService', 'ParseLinks', 'AlertService', '$state'];

    function DemoWebCamController($scope, $compile, Principal, StockInService, ParseLinks, AlertService, $state) {
        var vm = this;
        var localMediaStream  = null;
        var video = null;
        var canvas = null;
        var image = null;
        var ctx = null;

        vm.run = function(){
            video = $(".videoElement")[0];
            canvas = $(".canvasElement")[0];
            image = $(".imageElement")[0];
            ctx = canvas.getContext('2d');

            var hdConstraints = {
                video: {
                    mandatory: {
                        minWidth: 1920,
                        minHeight: 1080
                    }
                }
            };

            navigator.getUserMedia = navigator.getUserMedia || navigator.webkitGetUserMedia || navigator.mozGetUserMedia || navigator.msGetUserMedia || navigator.oGetUserMedia;

            if (navigator.getUserMedia) {
                // navigator.getUserMedia(hdConstraints, handleVideo, videoError);
                navigator.getUserMedia(hdConstraints, handleVideo, videoError);
            }

            function handleVideo(stream) {
                video.src = window.URL.createObjectURL(stream);
                localMediaStream = stream;
                setTimeout(function () {
                    canvas.width = video.videoWidth;
                    canvas.height = video.videoHeight;
                    image.width = video.videoWidth;
                    image.height = video.videoHeight;
                }, 100);
            }

            function videoError(e) {
                // do something
            }
        };

        vm.snapshot = function(){
            if(localMediaStream){
                ctx.drawImage(video,0,0);
                setTimeout(function(){
                    image.src = canvas.toDataURL('image/webp');
                }, 100);
                // image.reload();
            }
        };
    }
})();
