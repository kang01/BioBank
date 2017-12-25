/**
 * Created by gaokangkang on 2017/8/25.
 */
(function() {
    'use strict';

    angular
        .module('bioBankApp')
        .controller('TransportRecordViewController', TransportRecordViewController);

    TransportRecordViewController.$inject = ['$q','MasterData','entity','TranshipBoxByCodeService','TransportRecordService'];
    function TransportRecordViewController($q,MasterData,entity,TranshipBoxByCodeService,TransportRecordService) {

        var vm = this;
        vm.transportRecord = entity; //转运记录
        vm.boxList = [];
        init();

        function init() {
            //转运状态
            vm.transportStatus = MasterData.getStatus(vm.transportRecord.transhipState);
            _fnLoadBox();
            //获取盒子
            function _fnLoadBox() {
                TranshipBoxByCodeService.queryBoxDetail(vm.transportRecord.transhipCode).then(function (res) {
                    _fnQueryBoxes(res.data)
                });
            }
            //获取盒子跟管子
            function _fnQueryBoxes(boxes) {
                var querys = [];
                for(var i = 0 , len = boxes.length; i < len; i++){
                    // var queryBox = frozenBoxByCodeService.get({code:boxes[i].frozenBoxCode}).$promise;
                    var queryBox = TransportRecordService.queryViewBoxTubeDes(vm.transportRecord.transhipCode,boxes[i].frozenBoxCode);
                    querys.push(queryBox);
                    if (querys.length >= 10 || len == i + 1){
                        $q.all(querys).then(function(datas){
                            var boxesDetail = _.map(datas, function(res){
                                if(res.data){
                                    vm.boxList.push(res.data);
                                    return res.data;
                                }
                                vm.boxList.push(res);
                                return res;

                            });

                            createBoxDom(boxesDetail)
                        });
                        querys = [];
                    }
                }
            }
            //画盒子

            function createBoxDom(boxes) {
                //dom片段
                var fragment = document.createDocumentFragment();
                var $boxes = $(".transport-boxes");
                _.forEach(boxes,function (box) {
                    //盒子dom
                    var $box = $("<div class='transport-box'/>");
                    //盒子title
                    var $title = $("<div class='transport-box-title row m-0'></div>");
                    var $boxTitleLeft = $("<div  class='col-md-11 box-title-left pl-0'/>");
                    var $boxTitleRight = $("<div class='col-md-1 box-title-right pr-0 text-right'></div>");
                    var $iconExpand = $("<i name='extend' class='fa fa-expand'></i>");
                    var $iconCompress = $("<i name='compress' class='fa fa-compress'></i>");
                    var status = MasterData.getFrozenBoxStatus(box.status);
                    var sampleTypeName = box.sampleClassificationName ? box.sampleClassificationName : box.sampleTypeName;
                    $boxTitleLeft.html("<span class='box-code'>"+box.frozenBoxCode+"</span><span>&nbsp;"+sampleTypeName+"&nbsp;</span><span>"+status+"</span>");

                    // if(box.sampleClassificationName){
                    //     // $boxTitleLeft.text(box.frozenBoxCode+" "+box.sampleClassificationName+" "+status);
                    // }else{
                    //     $boxTitleLeft.text("<span class='box-code'>"+box.frozenBoxCode+"&nbsp;</span><span>"+box.sampleTypeName+"&nbsp;</span><span>"+status+"</span>");
                    //     $boxTitleLeft.text(box.frozenBoxCode+" "+box.sampleTypeName+" "+status);
                    // }

                    //盒子body 需要画出来
                    var $boxBody = drawTube(box,1);
                    $box.width($boxBody.outerWidth());
                    //插入到第一个
                    $title.prepend($boxTitleLeft);
                    $iconExpand.appendTo($boxTitleRight);
                    $boxTitleRight.appendTo($title);
                    $title.appendTo($box);
                    $boxBody.appendTo($box);
                    fragment.appendChild($box[0]);
                    //收起、展开
                    $boxTitleRight.click(function () {
                        var className = $boxTitleRight.children("i").attr("name");
                        $boxTitleRight.empty();
                        $box.children(".transport-box-body").remove();
                        var $boxDetailBox = _fnOperate(className);
                        $boxDetailBox.appendTo($box);
                        $box.width($boxDetailBox.outerWidth());
                    });
                    function _fnOperate(className) {
                        var $boxDetailBox;
                        //展开
                        if(className == "extend"){
                            $iconCompress.appendTo($boxTitleRight);
                            $boxDetailBox = drawTube(box,2);
                            setTimeout(function () {
                                var offsetTop = $box[0].offsetTop;
                                document.body.scrollTop =  offsetTop;
                            },500);
                        }else{
                            //合起
                            $iconExpand.appendTo($boxTitleRight);
                            //盒子dom
                            $boxDetailBox = drawTube(box,1);

                        }
                        return $boxDetailBox;
                    }
                });
                $boxes.append(fragment);

            }
            //画管子
            function drawTube(box,status) {
                var rowCount = box.frozenBoxTypeRows;
                var colCount = box.frozenBoxTypeColumns;
                var $boxBody = $("<div class='transport-box-body'/>");
                var $li;
                for(var i = 0; i < rowCount;i++){
                    var rowIndex = String.fromCharCode('A'.charCodeAt(0) + i);
                    var $divRow = $("<div />");

                    if(i > 7){
                        rowIndex = String.fromCharCode('A'.charCodeAt(0) + i+1);
                    }

                    for(var j = 0; j < colCount;j++){
                        $li = $("<li class='transport-tube'/>");
                        var colIndex = j+1;
                        var pos = rowIndex+colIndex;
                        //详情
                        if(status == '2'){
                            var liW = Math.floor((document.body.clientWidth-104)/colCount) -2;
                            $li.css({"width":liW,"height":"44","line-height":"20px"});

                        }else{
                            $li.width(30);
                            //给每个li赋值行列的数值
                            $li.text(pos);
                        }

                        //获取冻存盒的状态
                        _.forEach(box.frozenTubeDTOS,function (tube) {
                            tube.pos = tube.tubeRows + tube.tubeColumns;

                            if(pos == tube.pos) {
                                //状态
                                var $tubeStatus = $("<div class='tube-status'/>");
                                switch (tube.status) {
                                    case '3002':
                                        //空管
                                        $li.addClass("empty-tube-color");
                                        break;
                                    case '3003':
                                        //空孔
                                        $li.addClass("empty-hole-color");
                                        break;
                                    case '3004':
                                        //问题
                                        $li.addClass("error-tube-color");
                                        break;
                                }
                                $tubeStatus.appendTo($li);
                                //备注
                                if(tube.memo){
                                    var $tubeMemo = $("<div class='pos-memo triangle-topright'/>");
                                    $tubeMemo.appendTo($li);
                                    $li.mouseover(function (e) {
                                        var liW = $li.outerWidth();
                                        var offsetLeft = $(this)[0].offsetLeft + liW;
                                        var hoverDivW  = $(".hoverdiv").outerWidth();
                                        var bodyW = document.body.clientWidth;
                                        var pageX;
                                        if(offsetLeft + hoverDivW > bodyW){
                                            pageX = offsetLeft - hoverDivW - liW;//pageX() 属性是鼠标指针的位置，相对于文档的左边缘。
                                        }else{
                                            pageX =offsetLeft;
                                        }
                                        // $(e.target)[0].offsetParent
                                        var pageY = $(this)[0].offsetTop ;//pageY() 属性是鼠标指针的位置，相对于文档的上边缘。
                                        $(".hoverdiv").text(tube.memo);
                                        $(".hoverdiv").css({
                                                            "position":"absolute",
                                                            "z-index":"1",
                                                            "display": "block",
                                                            "left": pageX,
                                                            "top": pageY
                                                        });
                                    });
                                    $li.mouseout(function (e) {
                                        $(".hoverdiv").css({"display":"none"});
                                    });
                                }
                                // 管子颜色
                                var backColor =  tube.backColorForClass ? tube.backColorForClass : tube.backColor;
                                $li.css({"background":backColor});
                                //详情
                                if(status == '2'){
                                    var $sampleCode = $("<div class='text-ellipsis text-left'/>");
                                    var $sampleTypeName = $("<div  class='text-left'/>");
                                    var sampleCode = tube.sampleCode || tube.sampleTempCode;
                                    var sampleTypeName = tube.sampleClassificationName ? tube.sampleClassificationName : tube.sampleTypeName;
                                    $sampleCode.text(pos + " " +sampleCode).appendTo($li);
                                    $sampleCode.attr({"title":sampleCode});
                                    $sampleTypeName.text(sampleTypeName).appendTo($li);
                                    var liH = $li.outerHeight()+4;
                                    $boxBody.height(liH*rowCount+6);

                                }


                            }
                        });
                        //删除的临时冻存管
                        var len =  _.filter(box.frozenTubeDTOS,{"pos":pos}).length;
                        if(!len){
                            $li.text("");
                            $li.css({"background":"#ffffff"});
                        }
                        $li.appendTo($divRow);
                    }

                    $divRow.appendTo($boxBody);

                }
                var lW = $li.outerWidth()+4;
                $($boxBody).width(lW*colCount+6);
                return $boxBody;
            }

            //全部收起
            vm.packUp = function () {
                var $boxTitleRight = $(".box-title-right");
                for(var i = 0; i < $boxTitleRight.length;i++){
                    var className = $($boxTitleRight[i]).children("i").attr("name");
                    //展开的
                    if(className == 'compress'){
                        //icon图标
                        var $iconExpand = $("<i name='extend' class='fa fa-expand'></i>");
                        //展开的盒子
                        var compressBox = {};
                        var $box = $($boxTitleRight[i]).closest(".transport-box");
                        var boxCode = $box.find('.box-code').text();
                        compressBox = _.find(vm.boxList,{"frozenBoxCode":boxCode});
                        $($boxTitleRight[i]).empty();
                        $iconExpand.appendTo($boxTitleRight[i]);
                        $box.children(".transport-box-body").remove();
                        var $boxDetailBox = drawTube(compressBox,1);
                        $boxDetailBox.appendTo($box);
                        $box.width($boxDetailBox.outerWidth());

                    }
                }


                // console.log(JSON.stringify(compressBox))
                // var className = $boxTitleRight.children("i").attr("name");
                // _.forEach(compressBox,function (box) {
                //     var $boxDetailBox = drawTube(box,1);
                //     $box.width($boxDetailBox.outerWidth());
                // });
                //     var $box = $("<div class='transport-box'/>");
                //     var $boxTitleRight = $("<div class='col-md-8 box-title-right pr-0 text-right'></div>");
                //     var $iconExpand = $("<i name='extend' class='fa fa-expand'></i>");
                //     var className = $boxTitleRight.children("i").attr("name");
                //     $boxTitleRight.empty();
                //     $box.children(".transport-box-body").remove();
                //     //合起
                //     $iconExpand.appendTo($boxTitleRight);
                //     //盒子dom
                //     var $boxDetailBox = drawTube(box,1);
                //     $boxDetailBox.appendTo($box);
                //     $box.width($boxDetailBox.outerWidth());
                //     $boxes.append(fragment);
                // });

            };
            vm.searchBox = _fnSearchBox;
            //搜索冻存盒
            function _fnSearchBox() {

                if(!vm.boxCode){
                    $(".transport-boxes").empty();
                    createBoxDom(vm.boxList)
                }else{
                    //盒号长度大于3时，开始搜索
                    if(vm.boxCode.length > 3){
                        $(".transport-boxes").empty();
                        var box = _.filter(vm.boxList,function (data) {
                            if(_.startsWith(data.frozenBoxCode, vm.boxCode)){
                                return data;
                            }
                        });
                        createBoxDom(box)
                    }

                }

            }

        }


    }
})();
