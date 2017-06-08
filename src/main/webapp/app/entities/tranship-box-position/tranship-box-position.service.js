(function() {
    'use strict';
    angular
        .module('bioBankApp')
        .factory('TranshipBoxPosition', TranshipBoxPosition);

    TranshipBoxPosition.$inject = ['$resource'];

    function TranshipBoxPosition ($resource) {
        var resourceUrl =  'api/tranship-box-positions/:id';

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
