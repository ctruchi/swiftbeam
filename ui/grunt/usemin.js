'use strict';

module.exports = {
    html: {
        files: [
            {
                expand: true,
                dot: true,
                cwd: '<%= conf.dist %>',
                src: [
                    '<%= conf.html %>'
                ]
            }
        ]
    },
    css: '<%= conf.dist %>/<%= conf.css.dir %>/**/*.css',
    js: '<%= conf.dist %>/**/*.js',
    options: {
        assetsDirs: '<%= conf.dist %>',
        patterns: {
            html: [
                [/ng-include="'([^']+)'"/gm, 'Update the HTML with the new image filenames for ng-include']
            ],
            css: [
                [
                    /['(](?:\.\.\/)+([^')]+)[')]/gm,
                    'Update relative references'
                ]
            ]
        }
    }
};