(function() {
    'use strict';
    angular
        .module('bioBankApp')
        .factory('SupportRack', SupportRack);

    SupportRack.$inject = ['$resource'];

    function SupportRack ($resource) {
        var resourceUrl =  'api/support-racks/:id';

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
