(function (app) {
    'use strict';

    app.controller('HomeController', function (ShowService) {
        angular.extend(this, {
            shows: ShowService.findAll()
        });
    });
})(angular.module('sb.home'));
