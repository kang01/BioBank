(function() {
    'use strict';

    angular
        .module('bioBankApp')
        .factory('globalEventHandler', globalEventHandler);

    globalEventHandler.$inject = ['$rootScope', '$state', '$sessionStorage', '$translate', 'JhiLanguageService', 'translationHandler', '$window', '$document'];

    function globalEventHandler($rootScope, $state, $sessionStorage, $translate, JhiLanguageService, translationHandler, $window, $document) {
        return {
            initialize: initialize
        };

        function initialize() {
            $document.bind('keyup', function (e) {
                $rootScope.$broadcast('bbis:global:keyup', e);
            });
        }
    }
})();
