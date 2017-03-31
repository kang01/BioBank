(function() {
    'use strict';
    angular
        .module('bioBankApp')
        .factory('FrozenTube', FrozenTube);

    FrozenTube.$inject = ['$resource', 'DateUtils'];

    function FrozenTube ($resource, DateUtils) {
        var resourceUrl =  'api/frozen-tubes/:id';

        return $resource(resourceUrl, {}, {
            'query': { method: 'GET', isArray: true},
            'get': {
                method: 'GET',
                transformResponse: function (data) {
                    if (data) {
                        data = angular.fromJson(data);
                        data.dob = DateUtils.convertDateTimeFromServer(data.dob);
                        data.visitDate = DateUtils.convertDateTimeFromServer(data.visitDate);
                    }
                    return data;
                }
            },
            'update': { method:'PUT' }
        });
    }
})();
