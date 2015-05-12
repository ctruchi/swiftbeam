(function (app) {
    'use strict';

    app.controller('ShowController', function (ShowService, $stateParams) {
        angular.extend(this, {
           show: ShowService.get($stateParams.id)
        });
    });
})(angular.module('sb.show'));