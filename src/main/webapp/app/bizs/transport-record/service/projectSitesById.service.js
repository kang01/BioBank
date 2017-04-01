/**
 * Created by gaokangkang on 2017/3/28.
 */
(function() {
    'use strict';
    angular
        .module('bioBankApp')
        .factory('ProjectSitesByProjectIdService', ProjectSitesByProjectIdService);

    ProjectSitesByProjectIdService.$inject = ['$resource'];

    function ProjectSitesByProjectIdService ($resource) {
        var resourceUrl =  'api/projectSitesByProjectId/:id';

        return $resource(resourceUrl, {}, {
            'query': { method: 'GET', isArray: true},
            'get': {
                method: 'GET',
                transformResponse: function (data) {
                    if (data) {
                        data = angular.fromJson(data);
                    }
                    return data;
                }
            },
            'update': { method:'PUT' }
        });
    }
})();
