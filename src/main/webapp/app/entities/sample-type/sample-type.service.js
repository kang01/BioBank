(function() {
    'use strict';
    angular
        .module('bioBankApp')
        .factory('SampleType', SampleType);

    SampleType.$inject = ['$resource'];

    function SampleType ($resource) {
        var resourceUrl =  'api/sample-types/:id';

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
