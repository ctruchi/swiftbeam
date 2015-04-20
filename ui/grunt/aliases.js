'use strict';

module.exports = function (grunt) {

    grunt.registerTask('style', [
        'sass',
        'autoprefixer',
    ]);

    grunt.registerTask('preRev', [
        'copy',
        'style',
        'useminPrepare',
        'concat',
        'ngAnnotate',
        'uglify',
        'cssmin',
    ]);

    grunt.registerTask('build', [
        'clean',
        'concurrent',
        'rev',
        'usemin',
        'htmlmin'
    ]);

    grunt.registerTask('serve', [
        'style',
        'configureProxies:api',
        'connect:livereload',
        'watch'
    ]);
};
