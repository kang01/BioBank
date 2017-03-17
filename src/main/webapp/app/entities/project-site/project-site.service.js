(function() {
    'use strict';
    angular
        .module('bioBankApp')
        .factory('ProjectSite', ProjectSite);

    ProjectSite.$inject = ['$resource'];

    function ProjectSite ($resource) {
        var resourceUrl =  'api/project-sites/:id';

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
