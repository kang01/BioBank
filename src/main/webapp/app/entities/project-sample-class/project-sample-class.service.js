(function() {
    'use strict';
    angular
        .module('bioBankApp')
        .factory('ProjectSampleClass', ProjectSampleClass);

    ProjectSampleClass.$inject = ['$resource'];

    function ProjectSampleClass ($resource) {
        var resourceUrl =  'api/project-sample-classes/:id';

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
