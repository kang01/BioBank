(function() {
    'use strict';
    angular
        .module('bioBankApp')
        .factory('CheckType', CheckType);

    CheckType.$inject = ['$resource'];

    function CheckType ($resource) {
        var resourceUrl =  'api/check-types/:id';

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
