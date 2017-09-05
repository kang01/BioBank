/**
 * Created by gaokangkang on 2017/9/1.
 */
(function () {
    'use strict';

    angular
        .module('bioBankApp')
        .factory('ReportService', ReportService);

    ReportService.$inject = ['$http'];
    function ReportService($http) {
        var API_HOST = "http://10.24.200.13:8601/api";
        var service = {
            //获取全国样本分布
            queryCitySampleCount:_fnQueryCitySampleCount,
            //项目点样本分布
            queryProjectSiteSampleCount:_fnQueryProjectSiteSampleCount,
            //每天的样本流向
            queryEveyDaySampleCount:_fnQueryEveyDaySampleCount,
            //根据样本类型获取样本数
            querySampleTypeCount:_fnQuerySampleTypeCount,
            //根据性别统计样本量
            querySexSampleCount:_fnQuerySexSampleCount,
            //根据疾病类型统计样本量
            queryDiseaseTypeSampleCount:_fnQueryDiseaseTypeSampleCount,
            //根据年龄统计不同年龄段的样本量
            queryAgeSampleCount:_fnQueryAgeSampleCount
        };
        function _fnQueryCitySampleCount(params) {
            return $http.post(API_HOST+'/sample-flowing-report/project-site-location-samples/city?searchForm='+params);
        }
        function _fnQueryProjectSiteSampleCount(params) {
            return $http.post(API_HOST+'/sample-flowing-report/project-site-samples?searchForm='+params);
        }
        function _fnQueryEveyDaySampleCount(beginDate,endDate,searchType) {
            return $http.post(API_HOST+'/sample-flowing-report/stock-in-out/'+beginDate+'/'+endDate+'/searchType/'+searchType);
        }
        function _fnQuerySampleTypeCount(params) {
            return $http.post(API_HOST+'/sample-flowing-report/sample-type?searchForm='+params);
        }
        function _fnQuerySexSampleCount(params) {
            return $http.post(API_HOST+'/sample-flowing-report/gender?searchForm='+params);
        }
        function _fnQueryDiseaseTypeSampleCount(params) {
            return $http.post(API_HOST+'/sample-flowing-report/disease-type?searchForm='+params);
        }
        function _fnQueryAgeSampleCount(params) {
            return $http.post(API_HOST+'/sample-flowing-report/age?searchForm='+params);
        }
        return service;
    }
})();
