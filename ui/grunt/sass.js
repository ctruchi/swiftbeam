'use strict';

module.exports = {
    options: {
        outputStyle: 'nested',
        imagePath: '/images'
    },
    default: {
        files: [
            {
                expand: true,
                dot: true,
                cwd: '<%= conf.src %>/<%= conf.css.dir %>',
                src: '<%= conf.css.src %>',
                dest: '<%= conf.temp %>/<%= conf.css.dir %>',
                rename: function (dest, src) {
                    return dest + "/" + src.replace(/\.scss$/, '.css');
                }
            }
        ]
    }
};