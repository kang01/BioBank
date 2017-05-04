(function() {
    'use strict';
    angular
        .module('bioBankApp')
        .factory('SampleClassification', SampleClassification);

    SampleClassification.$inject = ['$resource'];

    function SampleClassification ($resource) {
        var resourceUrl =  'api/sample-classifications/:id';

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
