/**
 * Created by gaokangkang on 2017/3/28.
 * 区域下的架子
 */
(function() {
    'use strict';
    angular
        .module('bioBankApp')
        .factory('SupportacksByAreaIdService', SupportacksByAreaIdService);

    SupportacksByAreaIdService.$inject = ['$resource'];

    function SupportacksByAreaIdService ($resource) {
        var resourceUrl =  'api/supportacksByAreaId/:id';

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
