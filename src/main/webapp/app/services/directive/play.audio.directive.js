/**
 * Created by gaokangkang on 2017/7/6.
 */
(function()  {
    'use  strict';

    angular
        .module('bioBankApp')
        .directive('playAudio',  playAudio);

    playAudio.$inject  =  ["ngAudio", "$timeout"];
    function playAudio(ngAudio, $timeout) {

        function audioPlay(audioType, audioUrl){
            var audio = audioes[audioType];
            if (!audio && audioUrl && audioUrl.length > 0){
                audio = ngAudio.load(audioUrl)
            }
            if (audio){
                audio.play();
            }
        }

        var audioes = {
            "toast-error": ngAudio.load("/content/audio/error.mp3"),
        };
        return {
            restrict: 'E',
            scope: {
                playMethod: '@',
                audioType: '@',
                audioUrl: '@'
            },
            link: function (scope, elem, attrs) {
                switch (scope.playMethod){
                    case "AUTO":
                        $timeout(audioPlay(scope.audioType, scope.audioUrl), 200);
                        break;
                    default:
                        $(elem).click(function(){
                            audioPlay(scope.audioType, scope.audioUrl);
                        });
                        break;
                }
            }
        }
    }


})();
