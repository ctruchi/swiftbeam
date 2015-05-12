(function (app) {
    'use strict';

    app.service('ShowService', function(Restangular) {
        return {
            findAll: function () {
                return Restangular.all('show').getList().$object;
            },
            get: function (showId) {
                return Restangular.all('show').get(showId).$object;
            }
        };
    });
})(angular.module('sb.service'));