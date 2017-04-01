/**
 * Created by gaokangkang on 2017/3/28.
 * 项目
 */
(function() {
    'use strict';
    angular
        .module('bioBankApp')
        .factory('ProjectService', ProjectService);

    ProjectService.$inject = ['$resource'];

    function ProjectService ($resource) {
        var resourceUrl =  'api/projects/all/:id';

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
