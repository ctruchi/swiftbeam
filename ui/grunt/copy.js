'use strict';

module.exports = {
    default: {
        files: [
            {
                expand: true,
                dot: true,
                cwd: '<%= conf.src %>',
                src: [
                    '<%= conf.static %>',
                    '<%= conf.html %>'
                ],
                dest: '<%= conf.dist %>/'
            }
        ]
    }
};