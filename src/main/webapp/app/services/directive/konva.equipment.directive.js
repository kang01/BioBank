/**
 * Created by gaokangkang on 2017/9/13.
 */
(function()  {
    'use  strict';

    angular
        .module('bioBankApp')
        .directive('konvaEquipment',  konvaEquipment);

    konvaEquipment.$inject  =  [];

    function  konvaEquipment()  {
        var  directive  =  {
            restrict:  'EA',
            scope  :  {
                data: "=",
                heightStyle: "="
            },
            template: '<div id="container1" style="width:100%;height: 600px;border:1px solid #aaa;"></div>',
            link:  linkFunc
        };

        return  directive;

        function  linkFunc(scope,  element,  attrs)  {
            var width = window.innerWidth;
            var height = window.innerHeight;
            var stage = new Konva.Stage({
                container: 'container1',
                width: width,
                height: height
            });
            var layer = new Konva.Layer();
            //矩形
            var rect = new Konva.Rect({
                x: 500,
                y: 200,
                width: 100,
                height: 50,
                fill: 'red',
                stroke: 'black',
                strokeWidth: 4,
                draggable: true

            });
            // add cursor styling
            rect.on('mouseover', function() {
                document.body.style.cursor = 'pointer';
            });
            rect.on('mouseout', function() {
                document.body.style.cursor = 'default';
            });
            //圆形
            var circle = new Konva.Circle({
                x: stage.getWidth() / 2,
                y: stage.getHeight() / 2,
                radius: 70,
                fill: 'red',
                stroke: 'black',
                strokeWidth: 4
            });
            //文本
            var simpleText = new Konva.Text({
                x: stage.getWidth() / 2,
                y: 15,
                text: 'Simple Text',
                fontSize: 30,
                fontFamily: 'Calibri',
                fill: 'green'
            });
            simpleText.setOffset({
                x: simpleText.getWidth() / 2
            });
            var complexText = new Konva.Text({
                x: 20,
                y: 60,
                text: 'COMPLEX TEXT\n\nAll the world\'s a stage, and all the men and women merely players. They have their exits and their entrances.',
                fontSize: 18,
                fontFamily: 'Calibri',
                fill: '#555',
                width: 300,
                padding: 20,
                align: 'center'
            });
            var rect1 = new Konva.Rect({
                x: 20,
                y: 60,
                stroke: '#555',
                strokeWidth: 5,
                fill: '#ddd',
                width: 300,
                height: complexText.getHeight(),
                shadowColor: 'black',
                shadowBlur: 10,
                shadowOffset: [10, 10],
                shadowOpacity: 0.2,
                cornerRadius: 10
            });

            var textpath = new Konva.TextPath({
                x: 100,
                y: 200,
                fill: '#333',
                fontSize: 16,
                fontFamily: 'Arial',
                text: 'All the world\'s a stage, and all the men and women merely players.',
                data: 'M10,10 C0,0 10,150 100,100 S300,150 400,50'
            });
            var text = new Konva.Text({
                text: 'Text Shadow!',
                fontFamily: 'Calibri',
                fontSize: 40,
                x: 20,
                y: 20,
                stroke: 'red',
                strokeWidth: 2,
                shadowColor: 'black',
                shadowBlur: 0,
                shadowOffset: {x : 10, y : 10},
                shadowOpacity: 0.5
            });

            var star;
            for (var i = 0; i < 10; i++) {
                star = new Konva.Star({
                    x : stage.width() * Math.random(),
                    y : stage.height() * Math.random(),
                    fill : "blue",
                    numPoints :10,
                    innerRadius : 20,
                    outerRadius : 25,
                    draggable: true,
                    name : 'star ' + i,
                    shadowOffsetX : 5,
                    shadowOffsetY : 5
                });
                layer.add(star);
            }
            layer.draw();

            var tween = null;
            function addStar(layer, stage) {
                var scale = Math.random();
                var star1 = new Konva.Star({
                    x: Math.random() * stage.getWidth(),
                    y: Math.random() * stage.getHeight(),
                    numPoints: 5,
                    innerRadius: 30,
                    outerRadius: 50,
                    fill: '#89b717',
                    opacity: 0.8,
                    draggable: true,
                    scale: {
                        x : scale,
                        y : scale
                    },
                    rotation: Math.random() * 180,
                    shadowColor: 'black',
                    shadowBlur: 10,
                    shadowOffset: {
                        x : 5,
                        y : 5
                    },
                    shadowOpacity: 0.6,
                    startScale: scale
                });
                layer.add(star1);
            }
            var dragLayer = new Konva.Layer();
            for(var n = 0; n < 10; n++) {
                addStar(layer, stage);
            }



            layer.add(rect);
            layer.add(circle);
            layer.add(simpleText);
            layer.add(rect1);
            layer.add(complexText);
            layer.add(textpath);
            layer.add(text);


            stage.add(layer);
            stage.add(dragLayer);


            stage.on('mousedown', function(evt) {
                var shape = evt.target;
                shape.moveTo(dragLayer);
                stage.draw();
                shape.startDrag();
            });
            stage.on('mouseup', function(evt) {
                var shape = evt.target;
                shape.moveTo(layer);
                stage.draw();
            });
            stage.on('dragstart', function(evt) {
                var shape = evt.target;
                if (tween) {
                    tween.pause();
                }
                shape.setAttrs({
                    shadowOffset: {
                        x: 15,
                        y: 15
                    },
                    scale: {
                        x: shape.getAttr('startScale') * 1.2,
                        y: shape.getAttr('startScale') * 1.2
                    }
                });
            });
            stage.on('dragend', function(evt) {
                var shape = evt.target;
                tween = new Konva.Tween({
                    node: shape,
                    duration: 0.5,
                    easing: Konva.Easings.ElasticEaseOut,
                    scaleX: shape.getAttr('startScale'),
                    scaleY: shape.getAttr('startScale'),
                    shadowOffsetX: 5,
                    shadowOffsetY: 5
                });
                tween.play();
            });


            var scaleBy = 1.01;
            window.addEventListener('wheel', function(e) {
                e.preventDefault();
            var oldScale = stage.scaleX();
            var mousePointTo = {
                x: stage.getPointerPosition().x / oldScale - stage.x() / oldScale,
                y: stage.getPointerPosition().y / oldScale - stage.y() / oldScale,
            };
            var newScale = e.deltaY > 0 ? oldScale * scaleBy : oldScale / scaleBy;
            stage.scale({ x: newScale, y: newScale });
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
