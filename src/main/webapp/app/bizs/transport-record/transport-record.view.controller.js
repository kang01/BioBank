/**
 * Created by gaokangkang on 2017/8/25.
 */
(function() {
    'use strict';

    angular
        .module('bioBankApp')
        .controller('TransportRecordViewController', TransportRecordViewController);

    TransportRecordViewController.$inject = ['$scope','$q','MasterData','hotRegisterer','SampleService','TranshipInvalidService','DTOptionsBuilder','DTColumnBuilder','$uibModal','$state','$stateParams','toastr','entity','frozenBoxByCodeService','TransportRecordService','TranshipSaveService','TranshipBoxService',
        'SampleTypeService','FrozenBoxTypesService','FrozenBoxByIdService','EquipmentService','AreasByEquipmentIdService','SupportacksByAreaIdService','ProjectService','ProjectSitesByProjectIdService','TranshipBoxByCodeService','TranshipStockInService','FrozenBoxDelService','SampleUserService','TrackNumberService',
        'BioBankBlockUi','Principal'];
    function TransportRecordViewController($scope,$q,MasterData,hotRegisterer,SampleService,TranshipInvalidService,DTOptionsBuilder,DTColumnBuilder,$uibModal,$state,$stateParams,toastr,entity,frozenBoxByCodeService,TransportRecordService,TranshipSaveService,TranshipBoxService,
                                          SampleTypeService,FrozenBoxTypesService,FrozenBoxByIdService,EquipmentService,AreasByEquipmentIdService,SupportacksByAreaIdService,ProjectService,ProjectSitesByProjectIdService,TranshipBoxByCodeService,TranshipStockInService,FrozenBoxDelService,SampleUserService,TrackNumberService,
                                          BioBankBlockUi,Principal) {

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
                    var queryBox = frozenBoxByCodeService.get({code:boxes[i].frozenBoxCode}).$promise;
                    querys.push(queryBox);
                    if (querys.length >= 10 || len == i + 1){
                        $q.all(querys).then(function(datas){
                            var boxesDetail = _.map(datas, function(res){
                                if(res.data){
                                    return res.data;
                                }
                                return res;

                            });
                            vm.boxList = boxesDetail;
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
                    var $boxTitleLeft = $("<div  class='col-md-4 box-title-left pl-0'/>");
                    var $boxTitleRight = $("<div class='col-md-8 box-title-right pr-0 text-right'></div>");
                    var $iconExpand = $("<i name='extend' class='fa fa-expand'></i>");
                    var $iconCompress = $("<i name='compress' class='fa fa-compress'></i>");
                    $boxTitleLeft.text(box.frozenBoxCode);
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
                            $li.css({"width":"122","height":"44","line-height":"20px"});
                            var liW = Math.floor((document.body.clientWidth-104)/colCount);
                            $li.width(liW);
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
                                    $tubeMemo.mouseover(function (e) {
                                        var liW = $li.outerWidth();
                                        var offsetLeft = $(e.target)[0].offsetParent.offsetLeft + liW;
                                        var hoverDivW  = $(".hoverdiv").outerWidth();
                                        var bodyW = document.body.clientWidth;
                                        // var iW = $(e.target).width();
                                        console.log(bodyW);
                                        var pageX;
                                        if(offsetLeft + hoverDivW > bodyW){
                                            pageX = offsetLeft - hoverDivW - 10;//pageX() 属性是鼠标指针的位置，相对于文档的左边缘。
                                        }else{
                                            pageX =offsetLeft;
                                        }
                                        var pageY = $(e.target)[0].offsetParent.offsetTop ;//pageY() 属性是鼠标指针的位置，相对于文档的上边缘。
                                        $(".hoverdiv").text(tube.memo);
                                        $(".hoverdiv").css({
                                                            "position":"absolute",
                                                            "z-index":"1",
                                                            "display": "block",
                                                            "left": pageX,
                                                            "top": pageY
                                                        });
                                    });
                                    $tubeMemo.mouseout(function (e) {
                                        $(".hoverdiv").css({"display":"none"});
                                    });
                                }
                                // 管子颜色
                                var backColor =  tube.backColorForClass ? tube.backColorForClass : tube.backColor;
                                $li.css({"background":backColor});
                                //详情
                                if(status == '2'){
                                    // console.log($(this).closest(".transport-box-body"));
                                    // $(".transport-box-body").empty();

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
                        var boxCode = $box.children('.transport-box-title').text();
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

            }

        }


    }
})();
