'use strict';

module.exports = {
    livereload: {
        options: {
            livereload: true
        },
        files: [
            '<%= conf.src %>/index.html',
            '<%= conf.src %>/modules/**/*',
            '<%= conf.src %>/styles/**/*.scss'
        ]
    },
    sass: {
        files: [
            '<%= conf.src %>/styles/**/*.scss'
        ],
        tasks: [
            'style'
        ]
    }
};
