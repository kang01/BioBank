(function() {
    'use strict';
    angular
        .module('bioBankApp')
        .factory('FrozenBoxType', FrozenBoxType);

    FrozenBoxType.$inject = ['$resource'];

    function FrozenBoxType ($resource) {
        var resourceUrl =  'api/frozen-box-types/:id';

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
