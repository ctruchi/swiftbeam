'use strict';

module.exports = {
    default: {
        files: [
            {
                dot: true,
                src: [
                    '<%= conf.temp %>',
                    '<%= conf.dist %>'
                ]
            }
        ]
    }
};