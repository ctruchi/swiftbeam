(function (app) {
    'use strict';

    app.service('ShowService', function(Restangular) {
        return {
            findAll: function () {
                return Restangular.all('show').getList().$object;
            }
        };
    });
})(angular.module('sb.service'));