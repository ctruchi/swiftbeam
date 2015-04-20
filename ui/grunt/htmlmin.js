'use strict';

module.exports = {
    default: {
        options: {
            collapseWhitespace: true,
            collapseBooleanAttributes: true,
            removeCommentsFromCDATA: true,
            removeOptionalTags: true
        },
        files: [
            {
                expand: true,
                cwd: '<%= conf.dist %>',
                src: '<%= conf.html %>',
                dest: '<%= conf.dist %>'
            }
        ]
    }
};