package swiftbeam;

import restx.server.JettyWebServer;

import java.util.Optional;

public class AppServer {

    public static final String WEB_INF_LOCATION = "src/main/webapp/WEB-INF/web.xml";

    public static final String WEB_APP_LOCATION = "../ui/app";

    public static void main(String[] args) throws Exception {
        String mode = System.getProperty("restx.mode", "dev");
        System.setProperty("restx.mode", mode);
        System.setProperty("restx.app.package", "swiftbeam");

        //Disable hot reload due to issue with AutoStartables
        System.setProperty("restx.router.autocompile", "false");
        System.setProperty("restx.router.hotcompile", "false");
        System.setProperty("restx.router.hotreload", "false");

        int port = Integer.valueOf(Optional.ofNullable(System.getenv("PORT")).orElse("8080"));
        JettyWebServer server = new JettyWebServer(WEB_INF_LOCATION, WEB_APP_LOCATION, port, "0.0.0.0");

        System.setProperty("restx.server.id", server.getServerId());
        System.setProperty("restx.server.baseUrl", System.getProperty("rest.server.baseurl", server.baseUrl()));

        server.startAndAwait();
    }
}
