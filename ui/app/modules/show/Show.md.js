(function (app) {
    app.config(function ($stateProvider) {
        $stateProvider.state('app.show', {
            url: '/show/:id',
            views: {
                '': {templateUrl: 'modules/show/show.html'},
                'header': {templateUrl: 'modules/common/header.html'}
            }
        });
    });

})(angular.module('sb.show', ['ui.router']));