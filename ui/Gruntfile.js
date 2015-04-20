'use strict';

module.exports = function (grunt) {

    /**
     * Define every path used in grunt tasks.
     */
    var conf = {
        src: 'app',
        dist: 'dist',
        temp: 'tmp',

        css: {
            src: [
                'main.scss'
            ],
            dir: 'styles'
        },
        js: [
            'scripts/**/*.js'
        ],
        images: [
            'images/**/*.{png,jpeg,jpg,gif,svg}'
        ],
        html: [
            '*.html',
            'modules/**/*.html'
        ],
        'static': [
            '*.{ico,txt}',
            '.htaccess',
            'fonts/**/*'
        ]
    };

    /**
     * Load files from grunt folder.
     * A single file must exist for each task (i.e one concat.js, one usemin.js, ...)
     */
    require('load-grunt-config')(grunt, {
        data: {
            conf: conf
        }
    });
};
