(function (app) {
    app.config(function ($stateProvider) {
        $stateProvider.state('app.home', {
            url: '/home',
            views: {
                '': {templateUrl: 'modules/home/home.html'},
                'header': {templateUrl: 'modules/common/header.html'}
            }
        });
    });
})(angular.module('sb.home', ['ui.router']));