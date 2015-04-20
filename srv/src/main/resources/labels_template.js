angular.module('sb.i18n', ['pascalprecht.translate'])
    .config(function ($translateProvider) {
        $translateProvider.translations('fr', {LABELS});

        $translateProvider.preferredLanguage('fr');
    });
