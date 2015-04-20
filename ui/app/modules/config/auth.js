(function () {
    'use strict';

    angular.module('swiftbeam').config(function ($authProvider) {
        $authProvider.loginUrl = '/api/auth/login';
        $authProvider.signupUrl = '/api/user';
    });
})();