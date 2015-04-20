'use strict';

module.exports = {
    default: {
        files: [
            {
                expand: true,
                src: [
                    '<%= conf.temp %>/**/*.js'
                ]
            }
        ]
    }
};