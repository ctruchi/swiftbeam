(function () {
    'use strict';

    angular.module('swiftbeam').config(function ($stateProvider, $urlRouterProvider) {
        $urlRouterProvider.otherwise("/app/home");
        $stateProvider
            .state('app', {
                url: "/app",
                abstract: true,
                template: "<header ui-view='header'></header><ui-view></ui-view>"
            });
    });
})();
