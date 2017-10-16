(function() {
    'use strict';

    angular
        .module('bioBankApp')
        .config(alertServiceConfig);

    alertServiceConfig.$inject = ['AlertServiceProvider'];

    function alertServiceConfig(AlertServiceProvider) {
        // set below to true to make alerts look like toast
        AlertServiceProvider.showAsToast(false);
    }

    angular
        .module('bioBankApp')
        .config(toastrServiceConfig);

    toastrServiceConfig.$inject = ['toastrConfig'];
    function toastrServiceConfig(toastrConfig){
        angular.extend(toastrConfig, {
            onShown: function(event){
                if (event.iconClass == "toast-error"){
                }
            },
            templates: {
                toast: 'app/components/alert/toast-template.html',
                progressbar: 'directives/progressbar/progressbar.html'
            },
        });
    }
})();
