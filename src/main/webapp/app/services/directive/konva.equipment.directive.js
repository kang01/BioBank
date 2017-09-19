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
            var height = 600;
            //舞台
            var stage = new Konva.Stage({
                container: 'container1',
                width: width,
                height: height
            });
            //背景层
            var backgroundLayer = new Konva.Layer();
            //区域层
            var areaLayer = new Konva.Layer();
            //拖拽层
            var dragLayer = new Konva.Layer();
            dragLayer.x(stage.getWidth() / 4);
            dragLayer.y(-200);
            dragLayer.red("rgba(0,0,0,0.5)");
            var listLayer = new Konva.Layer();

            //组
            var group = new Konva.Group({
                width: 80,
                height: 80
            });
            var areaLeft = stage.getWidth() / 2 -100;

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
                },
                {
                    width:100,
                    height:100,
                    left:210,
                    top:100
                },
                {
                    width:100,
                    height:100,
                    left:310,
                    top:100
                },
                {
                    width:100,
                    height:100,
                    left:10,
                    top:210
                },
                {
                    width:100,
                    height:100,
                    left:110,
                    top:210
                },
                {
                    width:100,
                    height:100,
                    left:210,
                    top:210
                },
                {
                    width:100,
                    height:100,
                    left:310,
                    top:210
                },
                {
                    width:100,
                    height:100,
                    left:areaLeft,
                    top:100
                },
                {
                    width:100,
                    height:100,
                    left:areaLeft+110,
                    top:100
                },
                {
                    width:100,
                    height:100,
                    left:areaLeft+210,
                    top:100
                },
                {
                    width:100,
                    height:100,
                    left:areaLeft+310,
                    top:100
                },
                {
                    width:100,
                    height:100,
                    left:areaLeft,
                    top:210
                },
                {
                    width:100,
                    height:100,
                    left:areaLeft+110,
                    top:210
                },
                {
                    width:100,
                    height:100,
                    left:areaLeft+210,
                    top:210
                },
                {
                    width:100,
                    height:100,
                    left:areaLeft+310,
                    top:210
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


                var imageObj = new Image();
                imageObj.src = 'https://www.w3.org/Icons/SVG/svg-logo.svg';
                imageObj.onload = function() {
                    var image = new Konva.Image({
                        x:  data.left,
                        y: data.top,
                        image: imageObj,
                        width: data.width,
                        height: data.height
                    });
                    image.moveTo(group);
                    group.add(rect);

                    areaLayer.add(group);
                    areaLayer.batchDraw();
                };
            }
            for(var n = 0; n < areaData.length; n++) {
                addArea(areaLayer,areaData[n]);
            }


            //设备
            function addEquipment(layer,stage,left) {
                var equipment = new Konva.Rect({
                    x: left,
                    y: stage.getHeight() / 2,
                    stroke: '#555',
                    strokeWidth: 0,
                    fill: 'red',
                    width: 40,
                    height: 40,
                    shadowColor: 'black',
                    shadowBlur: 10,
                    shadowOffset: [10, 10],
                    shadowOpacity: 0.2,
                    cornerRadius: 2,
                    draggable: true
                });
                layer.add(equipment);


                var startPos = {};
                equipment.on('dragstart', function(evt) {
                    if (!startPos.x){
                        startPos = evt.target.getClientRect();
                    }
                });

                equipment.on('dragmove',function (evt) {
                    group.find('Rect').each(function( rect, index ){
                        rect.fill('rgba(0,0,0,0)')
                    });

                    var mousePos = stage.getPointerPosition();
                    var shape = areaLayer.getIntersection(mousePos);
                    if(shape && shape.className == 'Rect'){
                        shape.fill('rgba(0,0,0,0.3)');
                    }
                    areaLayer.batchDraw();
                });

                equipment.on('dragend', function(evt) {
                    var endPos = {};
                    var mousePos = stage.getPointerPosition();
                    var shape = areaLayer.getIntersection(mousePos);

                    if(!shape){
                        endPos = evt.target.getClientRect();

                        var rangeX1 = startPos.x;
                        var rangeY1 = startPos.y;

                        var rangeX2 = endPos.x;
                        var rangeY2 = endPos.y;

                        var offsetPos = {x:rangeX1 -rangeX2,y:rangeY1 - rangeY2};
                        var self = this;
                        self.move(offsetPos);
                    }else{

                        endPos = evt.target.getClientRect();
                        var offsetPos = shape.getClientRect();
                        var rangeX1 = offsetPos.x+offsetPos.width / 2.0;
                        var rangeY1 = offsetPos.y+offsetPos.height / 2.0;
                        var rangeX2 = endPos.x+endPos.width / 2.0;
                        var rangeY2 = endPos.y+endPos.height / 2.0;
                        var offsetPos1 = {x:rangeX1 -rangeX2,y:rangeY1 - rangeY2};
                        startPos = {x:rangeX1-endPos.width/2.0,y:rangeY1 - endPos.height / 2.0};
                        this.move(offsetPos1)
                    }
                    group.find('Rect').each(function( rect, index ){
                        rect.fill('rgba(0,0,0,0)')
                    });
                    dragLayer.batchDraw();
                    areaLayer.batchDraw();
                });
            }
            var equipmentLeft = stage.getWidth() / 2;
            for(var m = 0; m < 5; m++) {
                addEquipment(dragLayer, stage,equipmentLeft);
                 equipmentLeft += 45;
            }


            //区域层
            areaLayer.add(group);


            stage.add(areaLayer);
            stage.add(dragLayer);






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
