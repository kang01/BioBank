/**
 * Created by gaokangkang on 2017/8/30.
 * 报表
 */
(function() {
    'use strict';

    angular
        .module('bioBankApp')
        .controller('ReportsController', ReportsController)
        .controller('SelectDateModalController', SelectDateModalController)
        .controller('ReportEnlargedModalController', ReportEnlargedModalController)
        .controller('SelectDateModalController', SelectDateModalController);

    ReportsController.$inject = ['$scope','$uibModal','ReportService','ProjectService','EquipmentAllService','MasterData','AreasByEquipmentIdService','SampleTypeService','SupportacksByAreaIdService','RequirementService'];
    ReportEnlargedModalController.$inject = ['$uibModalInstance','$uibModal','items','ReportService'];
    SelectDateModalController.$inject = ['$uibModalInstance'];
    function ReportsController($scope,$uibModal,ReportService,ProjectService,EquipmentAllService,MasterData,AreasByEquipmentIdService,SampleTypeService,SupportacksByAreaIdService,RequirementService) {
        var vm = this;
        vm.dto = {
            frozenBoxCodeStr:[]
        };
        var projectIds = [];
        var projectIdStr = "";
        var modalInstance;

        vm.searchShow = _fnSearchShow;
        vm.close = _fnClose;
        vm.empty = _fnEmpty;
        vm.search = _fnSearch;
        //放大
        vm.enlarged = _fnEnlarged;

        var startDate = moment().subtract(3, 'months').format("YYYY-MM-DD");
        var endDate = moment().format("YYYY-MM-DD");
        vm.type = 'M';
        vm.typeContent = '月视图';
        vm.heightStyle = "height200";
        vm.screenSize = "small";
        vm.left = "-100";
        vm.zoom = 1.8;
        var searchForm;

        // vm.projectSiteSampleCountFlag = false;
        // vm.sampleTypeSampleCountFlag = false;
        // vm.sexSampleCountFlag = false;
        // vm.diseaseTypeSampleCountFlag = false;
        // vm.daySampleCountFlag = false;
        // vm.citySampleCountFlag = false;
        vm.mapStatus = "市";
        vm.geoIndex = 10;


        function _init() {
            vm.daySampleCountFlag = true;
            vm.citySampleCountFlag = true;
            vm.ageSampleCountFlag = true;
            //样本流向
            function _fnQueryEveyDaySampleCount(){
                ReportService.queryEveyDaySampleCount(startDate,endDate,vm.type,searchForm).success(function (data) {
                    vm.daySampleCountData = data;
                    var len = _.filter(vm.daySampleCountData,{countOfInSample:0,countOfOutSample:0}).length;
                    if(len == vm.daySampleCountData.length || !vm.daySampleCountData.length){
                        vm.daySampleCountFlag = true;
                    }else{
                        vm.daySampleCountFlag = false;
                    }
                })
            }
            //全国城市样本分布
            function _fnQueryCitySampleCount(){
                ReportService.queryCitySampleCount(searchForm).success(function (data) {
                    vm.citySampleCountData = data;
                    var len = _.filter(vm.citySampleCountData,{countOfSample:0}).length;
                    if(len == vm.citySampleCountData.length){
                        vm.citySampleCountFlag = true;
                    }else{
                        vm.citySampleCountFlag = false;
                    }
                })
            }
            //全国省份样本分布
            function _fnQueryProvinceSampleCount() {
                ReportService.queryProvinceSampleCount(searchForm).success(function (data) {
                    vm.citySampleCountData = data;
                    // var len = _.filter(vm.citySampleCountData,{countOfSample:0}).length;
                    // if(len == vm.citySampleCountData.length){
                    //     vm.citySampleCountFlag = true;
                    // }else{
                    //     vm.citySampleCountFlag = false;
                    // }
                })
            }
            //项目点样本分布
            function _fnQueryProjectSiteSampleCount(mapStatus){
                ReportService.queryProjectSiteSampleCount(searchForm).success(function (data) {
                    vm.projectSiteSampleCountData = data;
                    if(mapStatus){
                        vm.citySampleCountData = data;
                    }

                    if(vm.projectSiteSampleCountData.length){
                        vm.projectSiteSampleCountFlag = true;
                    }else{
                        vm.projectSiteSampleCountFlag = false;
                    }
                })
            }
            //样本类型样本分布
            function _fnQuerySampleTypeCount(){
                ReportService.querySampleTypeCount(searchForm).success(function (data) {
                    vm.sampleTypeSampleCountData = data;
                    if(vm.sampleTypeSampleCountData.length){
                        vm.sampleTypeSampleCountFlag = true;
                    }else{
                        vm.sampleTypeSampleCountFlag = false;
                    }
                })
            }
            //根据性别统计样本量
            function _fnQuerySexSampleCount(){
                ReportService.querySexSampleCount(searchForm).success(function (data) {
                    vm.sexSampleCountData = data;
                    if(vm.sexSampleCountData.length){
                        vm.sexSampleCountFlag = true;
                    }else{
                        vm.sexSampleCountFlag = false;
                    }
                })
            }
            //根据疾病类型统计样本量
            function _fnQueryDiseaseTypeSampleCount(){
                ReportService.queryDiseaseTypeSampleCount(searchForm).success(function (data) {
                    vm.diseaseTypeSampleCountData = data;
                    if(vm.diseaseTypeSampleCountData.length){
                        vm.diseaseTypeSampleCountFlag = true;
                    }else{
                        vm.diseaseTypeSampleCountFlag = false;
                    }
                })
            }
            //根据年龄统计不同年龄段的样本量
            function _fnQueryAgeSampleCount(){
                ReportService.queryAgeSampleCount(searchForm).success(function (data) {
                    vm.ageSampleCountData = data;
                    var len = _.filter(vm.ageSampleCountData,{countOfSample:0}).length;
                    if(len == vm.ageSampleCountData.length){
                        vm.ageSampleCountFlag = true;
                    }else{
                        vm.ageSampleCountFlag = false;
                    }
                })
            }

            searchForm = angular.toJson(vm.dto);
            //每天的出入库样本量
            _fnQueryEveyDaySampleCount();
            //全国样本分布
            _fnQueryCitySampleCount();
            //全国样本分布
            // _fnQueryProvinceSampleCount();
            //项目点样本分布
            _fnQueryProjectSiteSampleCount();
            //样本类型样本量分布
            _fnQuerySampleTypeCount();
            //按性别样本量分布
            _fnQuerySexSampleCount();
            //按疾病类型的样本量分布
            _fnQueryDiseaseTypeSampleCount();
            //按年龄段的样本量分布
            _fnQueryAgeSampleCount();
            vm.selectDate = function () {
                vm.typeContent = '日期';
                vm.searchType();
            };
            //切换类型
            vm.searchType = function () {
                if(vm.typeContent == '日期'){
                    modalInstance = $uibModal.open({
                        animation: true,
                        templateUrl: 'selectDateModal.html',
                        controller: 'SelectDateModalController',
                        backdrop:'static',
                        controllerAs: 'vm',
                        size:'sm'
                    });
                    modalInstance.result.then(function (stockInOut) {
                        startDate = moment(stockInOut.startDate).format("YYYY-MM-DD");
                        endDate = moment(stockInOut.endDate).format("YYYY-MM-DD");
                        vm.typeContent = startDate + "到"+ endDate;
                        _fnQueryEveyDaySampleCount();
                    });
                }else{
                    _fnQueryEveyDaySampleCount();
                }

            };

            vm.querySampleCount = function () {
                if(vm.mapStatus === "市"){
                    _fnQueryCitySampleCount();
                }else if(vm.mapStatus === "省"){
                    _fnQueryProvinceSampleCount();
                }else if(vm.mapStatus === "项目点"){
                    _fnQueryProjectSiteSampleCount(vm.mapStatus);
                }

            }
        }
        function _searchCondition() {
            //获取项目
            ProjectService.query({},onProjectSuccess, onError);
            function onProjectSuccess(data) {
                vm.projectOptions = data;
            }
            //设备
            EquipmentAllService.query({},onEquipmentSuccess, onError);
            function onEquipmentSuccess(data) {
                vm.frozenBoxPlaceOptions = data;
            }
            vm.equipmentOptions = [
                {value:"1",label:"冰箱"},
                {value:"2",label:"液氮罐"}
            ];
            vm.shelvesOptions = [
                {value:"1",label:"4*6"},
                {value:"2",label:"6*4"}
            ];
            //样本状态
            vm.statusOptions = MasterData.frozenTubeStatus;
            //库存状态
            vm.frozenTubeStatusOptions = MasterData.frozenBoxStatus;
            //疾病类型
            vm.diseaseTypeOptions = MasterData.diseaseType;
            SampleTypeService.querySampleType().success(function (data) {
                vm.sampleTypeOptions = _.orderBy(data,['sampleTypeName','asc']);
                _.remove(vm.sampleTypeOptions,{sampleTypeCode:"99"});
            });
            vm.sexOptions = MasterData.sexDict;
            vm.projectConfig = {
                valueField:'id',
                labelField:'projectName',
                searchField:'projectName',
                onChange:function(value){
                    projectIdStr = _.join(value, ',');
                    projectIds = value;
                    if(vm.dto.sampleTypeId){
                        _fnQueryProjectSampleClass(projectIdStr,vm.dto.sampleTypeId);
                    }
                }
            };
            //盒子位置
            vm.frozenBoxPlaceConfig = {
                valueField:'id',
                labelField:'equipmentCode',
                maxItems: 1,
                onChange:function (value) {
                    if(value){
                        AreasByEquipmentIdService.query({id:value},onAreaSuccess, onError);
                    }else{
                        vm.frozenBoxAreaOptions = [
                            {id:"",areaCode:""}
                        ];
                        vm.dto.areaId = "";
                        vm.frozenBoxShelfOptions = [
                            {id:"",supportRackCode:""}
                        ];
                        vm.dto.shelvesId = "";
                        $scope.$apply();
                    }
                }
            };
            function onAreaSuccess(data) {
                vm.frozenBoxAreaOptions = data;
                vm.frozenBoxAreaOptions.push({id:"",areaCode:""});
                if(vm.frozenBoxAreaOptions.length){
                    vm.dto.areaId = "";
                    vm.dto.shelvesId = "";
                    // vm.dto.areaId = vm.frozenBoxAreaOptions[0].id;
                    SupportacksByAreaIdService.query({id:vm.dto.areaId},onShelfSuccess, onError);
                }
            }
            vm.frozenBoxAreaConfig = {
                valueField:'id',
                labelField:'areaCode',
                maxItems: 1,
                onChange:function (value) {
                    if(value){
                        SupportacksByAreaIdService.query({id:value},onShelfSuccess, onError);
                    }else{
                        vm.frozenBoxShelfOptions = [
                            {id:"",supportRackCode:""}
                        ];
                        vm.dto.shelvesId = "";
                        $scope.$apply();
                    }
                }
            };
            //架子
            function onShelfSuccess(data) {
                vm.frozenBoxShelfOptions = data;
                vm.frozenBoxShelfOptions.push({id:"",supportRackCode:""});
                vm.dto.shelvesId = "";
            }
            vm.frozenBoxShelfConfig = {
                valueField:'id',
                labelField:'supportRackCode',
                maxItems: 1,
                onChange:function (value) {
                }
            };
            //设备类型
            vm.equipmentConfig = {
                valueField:'value',
                labelField:'label',
                maxItems: 1,
                onChange:function(value){
                }
            };
            //架子类型
            vm.shelvesConfig = {
                valueField:'value',
                labelField:'label',
                maxItems: 1,
                onChange:function(value){
                }
            };
            vm.statusConfig = {
                valueField:'id',
                labelField:'name',
                maxItems: 1,
                onChange:function(value){
                }
            };
            vm.frozenTubeStatusConfig = {
                valueField:'value',
                labelField:'label',
                maxItems: 1,
                onChange:function(value){
                }
            };
            vm.diseaseTypeConfig = {
                valueField:'id',
                labelField:'name',
                maxItems: 1,
                onChange:function (value) {
                }
            };
            vm.boxCodeConfig = {
                create: true,
                persist:false,
                onChange: function(value){
                    vm.dto.frozenBoxCodeStr = value;
                }
            };
            vm.sampleCodeConfig = {
                create: true,
                persist:false,
                onChange: function(value){
                    vm.dto.sampleCodeStr = value;
                }
            };
            //样本类型
            vm.sampleTypeConfig = {
                valueField:'id',
                labelField:'sampleTypeName',
                maxItems: 1,
                onChange:function (value) {
                    if(projectIdStr){
                        _fnQueryProjectSampleClass(projectIdStr,value);
                    }
                }
            };
            vm.sexConfig = {
                valueField:'type',
                labelField:'name',
                maxItems: 1,
                onChange:function (value) {

                }
            };
            //样本分类
            function _fnQueryProjectSampleClass(projectIds,sampleTypeId) {
                RequirementService.queryRequirementSampleClasses(projectIds,sampleTypeId).success(function (data) {
                    vm.sampleClassOptions = data;
                    if(vm.sampleClassOptions.length){
                        vm.dto.sampleClassificationId = vm.sampleClassOptions[0].sampleClassificationId;
                    }
                });
            }
            vm.sampleClassConfig = {
                valueField:'sampleClassificationId',
                labelField:'sampleClassificationName',
                maxItems: 1,
                onChange:function (value) {
                }
            };

            function onError() {

            }
        }
        _init();
        _searchCondition();

        //搜索
        function _fnSearchShow() {
            vm.checked = true;
        }
        function _fnClose() {
            vm.checked = false;
        }
        function _fnEmpty() {
            vm.dto = {};
            vm.dto.frozenBoxCodeStr = [];
            vm.dto.projectCodeStr = [];
            vm.arrayBoxCode = [];
            vm.arraySampleCode = [];
            vm.projectCodeStr = [];
        }
        function _fnSearch() {
            vm.dto.projectCodeStr = [];
            if(projectIds.length){
                for(var i = 0; i < projectIds.length; i++){
                    var projectCode = _.find(vm.projectOptions,{id:+projectIds[i]}).projectCode;
                    vm.dto.projectCodeStr.push(projectCode)
                }
            }
            vm.checked = false;
            _init();
        }
        function _fnEnlarged(status) {
            console.log(vm.mapStatus);
            var reportData;
            switch (status){
                case 1: reportData = vm.daySampleCountData;break;
                case 2: reportData = vm.citySampleCountData;break;
                case 3: reportData = vm.projectSiteSampleCountData;break;
                case 4: reportData = vm.sampleTypeSampleCountData;break;
                case 5: reportData = vm.sexSampleCountData;break;
                case 6: reportData = vm.diseaseTypeSampleCountData;break;
                case 7: reportData = vm.ageSampleCountData;break;
            }
            modalInstance = $uibModal.open({
                animation: true,
                templateUrl: 'app/bizs/reports/modal/report-enlarged-modal.html',
                controller: 'ReportEnlargedModalController',
                backdrop:'static',
                controllerAs: 'vm',
                size:'90',
                resolve:{
                    items:function () {
                        return{
                            reportData:reportData,
                            status:status,
                            mapStatus : vm.mapStatus || null
                        };
                    }
                }
            });
            modalInstance.result.then(function () {


            });
        }


    }
    function ReportEnlargedModalController($uibModalInstance,$uibModal,items,ReportService) {
        var vm = this;
        //样本流向分布图
        var startDate = moment().subtract(3, 'months').format("YYYY-MM-DD");
        var endDate = moment().format("YYYY-MM-DD");
        vm.type = 'M';
        vm.typeContent = '月视图';
        vm.heightStyle = "height450";
        vm.screenSize = "big";
        var modalInstance;
        //城市样本分布图
        vm.left = "25%";
        vm.zoom = 1.2;
        vm.status = items.status;
        vm.dto = {
            frozenBoxCodeStr:[]
        };
        var searchForm = angular.toJson(vm.dto);
        vm.mapStatus = "市";
        vm.geoIndex = 10;
        vm.mapStatus = items.mapStatus;
        vm.reportData = items.reportData;

        //切换类型
        vm.searchType = function () {
            if(vm.typeContent == '日期'){
                modalInstance = $uibModal.open({
                    animation: true,
                    templateUrl: 'selectDateModal.html',
                    controller: 'SelectDateModalController',
                    backdrop:'static',
                    controllerAs: 'vm',
                    size:'sm'
                });
                modalInstance.result.then(function (stockInOut) {
                    startDate = moment(stockInOut.startDate).format("YYYY-MM-DD");
                    endDate = moment(stockInOut.endDate).format("YYYY-MM-DD");
                    vm.typeContent = startDate + "到"+ endDate;
                    _fnQueryEveyDaySampleCount();
                });
            }else{
                _fnQueryEveyDaySampleCount();
            }
        };
        //样本流向
        function _fnQueryEveyDaySampleCount(){
            ReportService.queryEveyDaySampleCount(startDate,endDate,vm.type,searchForm).success(function (data) {
                vm.reportData = data;
            })
        }
        //全国城市样本分布
        function _fnQueryCitySampleCount(){
            ReportService.queryCitySampleCount(searchForm).success(function (data) {
                vm.reportData = data;
            })
        }
        //全国省份样本分布
        function _fnQueryProvinceSampleCount() {
            ReportService.queryProvinceSampleCount(searchForm).success(function (data) {
                vm.reportData = data;
            })
        }
        //项目点样本分布
        function _fnQueryProjectSiteSampleCount(){
            ReportService.queryProjectSiteSampleCount(searchForm).success(function (data) {
                vm.reportData = data;
            })
        }

        vm.querySampleCount = function () {
            if(vm.mapStatus == "市"){
                _fnQueryCitySampleCount();
            }else if(vm.mapStatus == "省"){
                _fnQueryProvinceSampleCount();
            }else if(vm.mapStatus == "项目点"){
                _fnQueryProjectSiteSampleCount();
            }

        };

        vm.cancel = function () {
            $uibModalInstance.dismiss('cancel');
        };
        vm.ok = function () {
            $uibModalInstance.close();
        };
    }
    function SelectDateModalController($uibModalInstance) {
        var vm = this;
        vm.stockInOut = {
            startDate:new Date(moment().subtract(3, 'months')),
            endDate:new Date()
        };
        vm.datePickerOpenStatus = {};
        vm.openCalendar = openCalendar; //时间
        function openCalendar (date) {
            vm.datePickerOpenStatus[date] = true;
        }

        vm.cancel = function () {
            $uibModalInstance.dismiss('cancel');
        };
        vm.ok = function () {
            $uibModalInstance.close(vm.stockInOut);
        };
    }
})();
