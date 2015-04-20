'use strict';

module.exports = {
    options: {
        browsers: [
            '> 1%',
            'Android >= 2.1',
            'Chrome >= 21',
            'Explorer >= 9',
            'Firefox >= 17',
            'Safari >= 6.0',
            'ExplorerMobile >= 9'
        ]
    },
    default: {
        src: '<%= conf.temp %>/<%= conf.css.dir %>/**/*.css'
    }
};