'use strict';

module.exports = {
    default: {
        files: [
            {
                expand: true,
                cwd: '<%= conf.src %>',
                src: '<%= conf.images %>',
                dest: '<%= conf.dist %>'
            }
        ]
    }
};