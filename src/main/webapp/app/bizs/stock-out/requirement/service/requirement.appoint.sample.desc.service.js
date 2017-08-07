/**
 * Created by gaokangkang on 2017/8/3.
 */
(function() {
    'use strict';
    angular
        .module('bioBankApp')
        .factory('RequirementAppointSampleDescService', RequirementAppointSampleDescService);

    RequirementAppointSampleDescService.$inject = ['$resource', 'DateUtils'];

    function RequirementAppointSampleDescService ($resource, DateUtils) {
        var resourceUrl =  'api/stock-out-required-samples/stockOutRequirement/:requirementId';
        return $resource(resourceUrl, {}, {
            'query': { method: 'GET', isArray: true},
            'get': {
                method: 'GET',
                transformResponse: function (data) {
                    if (data) {
                        data = angular.fromJson(data);
                        data.dob = DateUtils.convertDateTimeFromServer(data.dob);
                        data.visitDate = DateUtils.convertDateTimeFromServer(data.visitDate);
                    }
                    return data;
                }
            },
            'update': { method:'PUT' }
        });
    }
})();
