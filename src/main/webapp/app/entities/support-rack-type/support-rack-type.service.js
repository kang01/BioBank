(function() {
    'use strict';
    angular
        .module('bioBankApp')
        .factory('SupportRackType', SupportRackType);

    SupportRackType.$inject = ['$resource'];

    function SupportRackType ($resource) {
        var resourceUrl =  'api/support-rack-types/:id';

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
