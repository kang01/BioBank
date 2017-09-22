/**
 * Created by gaokangkang on 2017/9/13.
 */
(function()  {
    'use  strict';

    angular
        .module('bioBankApp')
        .directive('konvaEquipmentDemo',  konvaEquipmentDemo);

    konvaEquipmentDemo.$inject  =  [];

    function  konvaEquipmentDemo()  {
        var  directive  =  {
            restrict:  'EA',
            scope  :  {
                data: "=",
                xy: "="
            },
            template: '<div id="container1"></div>',
            link:  linkFunc
        };

        return  directive;

        function  linkFunc(scope,  element,  attrs)  {
            var height = 3310;
            //舞台
            var stage = new Konva.Stage({
                container: 'container1',
                width: 943,
                height: height
            });
            //背景层
            var backgroundLayer = new Konva.Layer();

            //样本库平面图1
            var imageObj1 = new Image();
            imageObj1.src = 'content/images/样本库平面图-1.svg';
            imageObj1.onload = function() {
                var width = (3600*imageObj1.width)/imageObj1.height;
                var image1 = new Konva.Image({
                    x:  0,
                    y: -100,
                    image: imageObj1,
                    width: width,
                    height: 3600
                });
                backgroundLayer.add(image1);
                stage.add(backgroundLayer);
            };

            function writeMessage(message) {
                text.setText(message);
                // backgroundLayer.draw();
                stage.batchDraw();
            }
            stage.on('click', function() {
                var mousePos = stage.getPointerPosition();
                var x = mousePos.x;
                var y = mousePos.y;
                scope.xy.position = x + "," + y;
                scope.$apply();
                console.log(scope.xy);
            });
            stage.on('mouseout', function() {
                writeMessage('Mouseout triangle');
            });
            stage.on('mousemove', function() {
                var mousePos = stage.getPointerPosition();
                var x = mousePos.x;
                var y = mousePos.y;
                writeMessage('x: ' + x + ', y: ' + y);
            });
            var text = new Konva.Text({
                x: 10,
                y: 10,
                fontFamily: 'Calibri',
                fontSize: 24,
                text: '',
                fill: 'black'
            });
            backgroundLayer.add(text);






            var scaleBy = 1.25;
            window.addEventListener('wheel', function (e) {
                e.preventDefault();
                var oldScale = stage.scaleX();
                var mousePointTo = {
                    x: stage.getPointerPosition().x / oldScale - stage.x() / oldScale,
                    y: stage.getPointerPosition().y / oldScale - stage.y() / oldScale
                };
                var newScale = e.deltaY > 0 ? oldScale * scaleBy : oldScale / scaleBy;
                stage.scale({x: newScale, y: newScale});
                var newPos = {
                    x: -(mousePointTo.x - stage.getPointerPosition().x / newScale) * newScale,
                    y: -(mousePointTo.y - stage.getPointerPosition().y / newScale) * newScale
                };
                stage.position(newPos);
                stage.batchDraw();
            });


    }
    }
})();
