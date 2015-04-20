'use strict';

module.exports = {
    options: {
        port: 9000,
        hostname: '0.0.0.0',
        livereload: 35729,
        base: [
            '<%= conf.temp %>',
            '<%= conf.src %>'
        ]
    },
    livereload: {
        options: {
            middleware: function (connect, options) {
                if (!Array.isArray(options.base)) {
                    options.base = [options.base];
                }

                // Setup the proxy
                var middlewares = [require('grunt-connect-proxy/lib/utils').proxyRequest];

                // Serve static files.
                options.base.forEach(function(base) {
                    middlewares.push(connect.static(base));
                });

                // Make directory browse-able.
                var directory = options.directory || options.base[options.base.length - 1];
                middlewares.push(connect.directory(directory));

                return middlewares;
            }
        }
    },
    api: {
        proxies: [{
            host: 'localhost',
            port: 8080,
            context: '/api'
        }]
    }
};
