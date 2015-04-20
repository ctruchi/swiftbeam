'use strict';

module.exports = {
    html: '<%= conf.src %>/index.html',
    options: {
        dest: '<%= conf.dist %>',
        staging: '<%= conf.temp %>',
        root: '{<%= conf.temp %>,<%= conf.src %>}'
    }
};