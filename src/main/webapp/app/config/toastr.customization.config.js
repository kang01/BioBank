/**
 * Created by gaokangkang on 2017/4/28.
 */
(function() {
    'use strict';

    angular
        .module('bioBankApp')
        .config(toastrConfig);

    toastrConfig.$inject = ['toastrConfig'];

    function toastrConfig(toastrConfig) {
        angular.extend(toastrConfig,{
            closeButton:true,
            progressBar:true,
            timeOut:1500
        });
    }
})();
