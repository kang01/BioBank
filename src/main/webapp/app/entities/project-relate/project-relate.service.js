(function() {
    'use strict';
    angular
        .module('bioBankApp')
        .factory('ProjectRelate', ProjectRelate);

    ProjectRelate.$inject = ['$resource'];

    function ProjectRelate ($resource) {
        var resourceUrl =  'api/project-relates/:id';

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
