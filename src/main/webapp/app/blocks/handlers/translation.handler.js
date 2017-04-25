(function() {
    'use strict';

    angular
        .module('bioBankApp')
        .factory('translationHandler', translationHandler);

    translationHandler.$inject = ['$rootScope', '$window', '$state', '$translate', 'DTDefaultOptions'];

    function translationHandler($rootScope, $window, $state, $translate, DTDefaultOptions) {
        return {
            initialize: initialize,
            updateTitle: updateTitle
        };

        function initialize() {
            // if the current translation changes, update the window title
            var translateChangeSuccess = $rootScope.$on('$translateChangeSuccess', function() {
                updateTitle();
            });

            $rootScope.$on('$destroy', function () {
                if(angular.isDefined(translateChangeSuccess) && translateChangeSuccess !== null){
                    translateChangeSuccess();
                }
            });

            var tableLang = {
                'emptyTable': '没有数据',
                'loadingRecords': '加载中...',
                'processing': '查询中...',
                'search': '搜索:',
                'lengthMenu': '每页 _MENU_ 条',
                'zeroRecords': '没有数据',
                'paginate': {
                    'first':      '首页',
                    'last':       '末页',
                    'next':       '下一页',
                    'previous':   '上一页'
                },
                'info': '第 _PAGE_ 页 / 总 _PAGES_ 页',
                'infoEmpty': '没有数据',
                'infoFiltered': '(过滤后总行数 _MAX_ 条)'
            };
            DTDefaultOptions.setLanguage(tableLang);
        }

        // update the window title using params in the following
        // precedence
        // 1. titleKey parameter
        // 2. $state.$current.data.pageTitle (current state page title)
        // 3. 'global.title'
        function updateTitle(titleKey) {
            if (!titleKey && $state.$current.data && $state.$current.data.pageTitle) {
                titleKey = $state.$current.data.pageTitle;
            }
            $translate(titleKey || 'global.title').then(function (title) {
                $window.document.title = title;
            });
        }
    }
})();
