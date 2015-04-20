(function () {
    'use strict';

    angular.module('swiftbeam').config(function (RestangularProvider) {
        RestangularProvider.setBaseUrl('/api');

        RestangularProvider.setErrorInterceptor(function (response, deferred, responseHandler) {
            console.log(response);
        });
    });

})();