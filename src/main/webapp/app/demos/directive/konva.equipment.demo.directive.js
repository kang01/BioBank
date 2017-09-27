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
                xy: "=",
                wh: "=",
                ht: "="
            },
            template: '<div id="container1"></div>',
            link:  linkFunc
        };

        return  directive;

        function  linkFunc(scope,  element,  attrs)  {
            // var height = 3310;
            var height = $("#container1").height();
            var width = $("#container1").width();
            console.log(scope.wh,scope.ht);
            //舞台
            var stage = new Konva.Stage({
                container: 'container1',
                width: scope.wh,
                height: scope.ht,
                stroke: '#666',
                fill: '#ddd',
                strokeWidth: 2
            });
            stage.transformsEnabled('all');
            //背景层
            var backgroundLayer = new Konva.Layer({
                draggable: true,
                skiptransform:false
            });

            // 区域层
            var areaLayer = new Konva.Layer();

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
                stage.add(areaLayer);
            };

            function writeMessage(message) {
                text.setText(message);
                // backgroundLayer.draw();
                stage.batchDraw();
            }
            stage.on('click', function() {
                console.log(this);
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



            var areaData = [
                {
                    width:100,
                    height:100,
                    left:10,
                    top:100
                },
                {
                    width:100,
                    height:100,
                    left:110,
                    top:100
                }
            ];
            //区域
            function addArea(layer,data) {
                var rect = new Konva.Rect({
                    x: data.left,
                    y: data.top,
                    // stroke: '#555',
                    // strokeWidth: 2,
                    // fill: '#ddd',
                    width: data.width,
                    height: data.height
                    // shadowColor: 'black',
                    // shadowBlur: 0,
                    // shadowOffset: [10, 10],
                    // shadowOpacity: 0.8,
                    // cornerRadius: 10
                });
                areaLayer.add(rect);
            }
            for(var n = 0; n < areaData.length; n++) {
                addArea(areaLayer,areaData[n]);
            }



            // var scaleBy = 1.25;
            // window.addEventListener('wheel', function (e) {
            //     e.preventDefault();
            //     var oldScale = stage.scaleX();
            //     var mousePointTo = {
            //         x: stage.getPointerPosition().x / oldScale - stage.x() / oldScale,
            //         y: stage.getPointerPosition().y / oldScale - stage.y() / oldScale
            //     };
            //     var newScale = e.deltaY > 0 ? oldScale * scaleBy : oldScale / scaleBy;
            //     stage.scale({x: newScale, y: newScale});
            //     var newPos = {
            //         x: -(mousePointTo.x - stage.getPointerPosition().x / newScale) * newScale,
            //         y: -(mousePointTo.y - stage.getPointerPosition().y / newScale) * newScale
            //     };
            //     stage.position(newPos);
            //     stage.batchDraw();
            // });


    }
    }
})();
