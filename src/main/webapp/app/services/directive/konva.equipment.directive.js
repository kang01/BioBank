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
                data: "="
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
                    y: -90,
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
                // var mousePos = stage.getPointerPosition();
                // var x = mousePos.x;
                // var y = mousePos.y;
                // scope.xy.position = x + "," + y;
                // scope.$apply();
                // console.log(scope.xy);
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





































            // var width = window.innerWidth;
            // var height = 600;
            // //舞台
            // var stage = new Konva.Stage({
            //     container: 'container1',
            //     width: width,
            //     height: height
            // });
            // //背景层
            // var backgroundLayer = new Konva.Layer();
            // //区域层
            // var areaLayer = new Konva.Layer();
            // //拖拽层
            // var dragLayer = new Konva.Layer();
            //
            // var listLayer = new Konva.Layer();
            //
            // var tooltipLayer = new Konva.Layer();
            //
            // var tooltipText = new Konva.Text({
            //     text: '啊啊啊啊啊啊啊啊啊啊啊啊啊啊啊啊啊啊啊啊',
            //     fontSize: 18,
            //     fontFamily: 'Calibri',
            //     fill: '#555',
            //     width: 300,
            //     padding: 20,
            //     align: 'center',
            //     visible: false
            // });
            // var tooltipRect = new Konva.Rect({
            //     stroke: '#555',
            //     strokeWidth: 2,
            //     fill: '#ddd',
            //     width: 300,
            //     height: tooltipText.getHeight(),
            //     shadowColor: 'black',
            //     shadowBlur: 10,
            //     shadowOffset: [10, 10],
            //     shadowOpacity: 0.2,
            //     cornerRadius: 2,
            //     visible: false
            // });
            // dragLayer.x(stage.getWidth() / 4);
            // dragLayer.y(-200);
            // dragLayer.red("rgba(0,0,0,0.5)");
            //
            // var line = new Konva.Line({
            //     x: stage.getWidth() / 2 + 400,
            //     y: 0,
            //     points: [ 0, 0,0, 0, 0, stage.getHeight() ],
            //     tension: 1,
            //     stroke: '#aaa'
            // });
            // //组
            // var group = new Konva.Group({
            //     width: 80,
            //     height: 80
            // });
            // var areaLeft = stage.getWidth() / 2 -100;
            //
            // var areaData = [
            //     {
            //         width:100,
            //         height:100,
            //         left:10,
            //         top:100
            //     },
            //     {
            //         width:100,
            //         height:100,
            //         left:110,
            //         top:100
            //     },
            //     {
            //         width:100,
            //         height:100,
            //         left:210,
            //         top:100
            //     },
            //     {
            //         width:100,
            //         height:100,
            //         left:310,
            //         top:100
            //     },
            //     {
            //         width:100,
            //         height:100,
            //         left:10,
            //         top:210
            //     },
            //     {
            //         width:100,
            //         height:100,
            //         left:110,
            //         top:210
            //     },
            //     {
            //         width:100,
            //         height:100,
            //         left:210,
            //         top:210
            //     },
            //     {
            //         width:100,
            //         height:100,
            //         left:310,
            //         top:210
            //     },
            //     {
            //         width:100,
            //         height:100,
            //         left:areaLeft,
            //         top:100
            //     },
            //     {
            //         width:100,
            //         height:100,
            //         left:areaLeft+110,
            //         top:100
            //     },
            //     {
            //         width:100,
            //         height:100,
            //         left:areaLeft+210,
            //         top:100
            //     },
            //     {
            //         width:100,
            //         height:100,
            //         left:areaLeft+310,
            //         top:100
            //     },
            //     {
            //         width:100,
            //         height:100,
            //         left:areaLeft,
            //         top:210
            //     },
            //     {
            //         width:100,
            //         height:100,
            //         left:areaLeft+110,
            //         top:210
            //     },
            //     {
            //         width:100,
            //         height:100,
            //         left:areaLeft+210,
            //         top:210
            //     },
            //     {
            //         width:100,
            //         height:100,
            //         left:areaLeft+310,
            //         top:210
            //     }
            // ];
            //
            // //区域
            // function addArea(layer,data) {
            //     var rect = new Konva.Rect({
            //         x: data.left,
            //         y: data.top,
            //         // stroke: '#555',
            //         // strokeWidth: 2,
            //         // fill: '#ddd',
            //         width: data.width,
            //         height: data.height
            //         // shadowColor: 'black',
            //         // shadowBlur: 0,
            //         // shadowOffset: [10, 10],
            //         // shadowOpacity: 0.8,
            //         // cornerRadius: 10
            //     });
            //
            //
            //     var imageObj = new Image();
            //     imageObj.src = 'https://www.w3.org/Icons/SVG/svg-logo.svg';
            //     imageObj.onload = function() {
            //         var image = new Konva.Image({
            //             x:  data.left,
            //             y: data.top,
            //             image: imageObj,
            //             width: data.width,
            //             height: data.height
            //         });
            //         image.moveTo(group);
            //         group.add(rect);
            //
            //         areaLayer.add(group);
            //         areaLayer.batchDraw();
            //     };
            // }
            // for(var n = 0; n < areaData.length; n++) {
            //     addArea(areaLayer,areaData[n]);
            // }
            //
            //
            // //设备
            // function addEquipment(layer,stage,left) {
            //     var equipment = new Konva.Rect({
            //         x: left,
            //         y: stage.getHeight() / 2,
            //         stroke: '#555',
            //         strokeWidth: 0,
            //         fill: 'red',
            //         width: 40,
            //         height: 40,
            //         shadowColor: 'black',
            //         shadowBlur: 10,
            //         shadowOffset: [10, 10],
            //         shadowOpacity: 0.2,
            //         cornerRadius: 2,
            //         draggable: true
            //     });
            //     layer.add(equipment);
            //
            //
            //     var startPos = {};
            //     equipment.on('dragstart', function(evt) {
            //         if (!startPos.x){
            //             startPos = evt.target.getClientRect();
            //         }
            //     });
            //
            //     equipment.on('dragmove',function (evt) {
            //         tooltipText.hide();
            //         tooltipRect.hide();
            //         tooltipLayer.batchDraw();
            //
            //         group.find('Rect').each(function( rect, index ){
            //             rect.fill('rgba(0,0,0,0)')
            //         });
            //
            //         var mousePos = stage.getPointerPosition();
            //         var shape = areaLayer.getIntersection(mousePos);
            //         if(shape && shape.className == 'Rect'){
            //             shape.fill('rgba(0,0,0,0.3)');
            //         }
            //         areaLayer.batchDraw();
            //     });
            //
            //     equipment.on('dragend', function(evt) {
            //         var endPos = {};
            //         var mousePos = stage.getPointerPosition();
            //         var shape = areaLayer.getIntersection(mousePos);
            //
            //         if(!shape){
            //             endPos = evt.target.getClientRect();
            //
            //             var rangeX1 = startPos.x;
            //             var rangeY1 = startPos.y;
            //
            //             var rangeX2 = endPos.x;
            //             var rangeY2 = endPos.y;
            //
            //             var offsetPos = {x:rangeX1 -rangeX2,y:rangeY1 - rangeY2};
            //             var self = this;
            //             self.move(offsetPos);
            //         }else{
            //
            //             endPos = evt.target.getClientRect();
            //             var offsetPos = shape.getClientRect();
            //             var rangeX1 = offsetPos.x+offsetPos.width / 2.0;
            //             var rangeY1 = offsetPos.y+offsetPos.height / 2.0;
            //             var rangeX2 = endPos.x+endPos.width / 2.0;
            //             var rangeY2 = endPos.y+endPos.height / 2.0;
            //             var offsetPos1 = {x:rangeX1 -rangeX2,y:rangeY1 - rangeY2};
            //             startPos = {x:rangeX1-endPos.width/2.0,y:rangeY1 - endPos.height / 2.0};
            //             this.move(offsetPos1)
            //         }
            //         group.find('Rect').each(function( rect, index ){
            //             rect.fill('rgba(0,0,0,0)')
            //         });
            //         dragLayer.batchDraw();
            //         areaLayer.batchDraw();
            //     });
            //
            //     equipment.on("mousemove", function(){
            //         console.log(this);
            //         var mousePos = stage.getPointerPosition();
            //         var tooltipRectWidth = tooltipRect.width();
            //         var stageWidth = stage.width();
            //         if(mousePos.x + tooltipRectWidth > stageWidth-30){
            //             tooltipRect.position({
            //                 x : mousePos.x - tooltipRectWidth,
            //                 y : mousePos.y + 5
            //             });
            //             tooltipText.position({
            //                 x : mousePos.x - tooltipRectWidth,
            //                 y : mousePos.y + 5
            //             });
            //         }else{
            //             tooltipText.position({
            //                 x : mousePos.x + 5,
            //                 y : mousePos.y + 5
            //             });
            //             tooltipRect.position({
            //                 x : mousePos.x + 5,
            //                 y : mousePos.y + 5
            //             });
            //         }
            //
            //         tooltipText.text("Cyan Triangle");
            //         tooltipText.show();
            //         tooltipRect.show();
            //         tooltipLayer.batchDraw();
            //     });
            //     equipment.on("mouseout", function(){
            //         tooltipText.hide();
            //         tooltipRect.hide();
            //         tooltipLayer.draw();
            //     });
            // }
            // var equipmentLeft = stage.getWidth() / 2;
            // for(var m = 0; m < 5; m++) {
            //     addEquipment(dragLayer, stage,equipmentLeft);
            //      equipmentLeft += 45;
            // }
            //
            //
            // tooltipLayer.add(tooltipRect);
            // tooltipLayer.add(tooltipText);
            // //区域层
            // areaLayer.add(group);
            // listLayer.add(line);
            //
            // stage.add(listLayer);
            // stage.add(areaLayer);
            // stage.add(dragLayer);
            // stage.add(tooltipLayer);






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
