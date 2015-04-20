'use strict';

module.exports = {
    files: {
        expand: true,
        cwd: '<%= conf.dist %>',
        src: [
            '<%= conf.images %>',
            '<%= conf.css.dir %>/**/*.css',
            '<%= conf.js %>'
        ]
    }
};