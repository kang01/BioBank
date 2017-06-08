(function() {
    'use strict';
    angular
        .module('bioBankApp')
        .factory('TranshipTube', TranshipTube);

    TranshipTube.$inject = ['$resource'];

    function TranshipTube ($resource) {
        var resourceUrl =  'api/tranship-tubes/:id';

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
