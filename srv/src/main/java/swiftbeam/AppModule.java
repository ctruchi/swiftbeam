package swiftbeam;

import swiftbeam.domain.User;
import swiftbeam.service.AppUserService;
import com.google.common.base.Charsets;
import com.google.common.collect.Sets;
import com.google.common.io.Resources;
import com.mongodb.MongoClient;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import restx.RestxContext;
import restx.RestxRequest;
import restx.RestxRequestMatch;
import restx.RestxResponse;
import restx.RestxRoute;
import restx.StdRestxRequestMatcher;
import restx.StdRoute;
import restx.admin.AdminModule;
import restx.common.MoreResources;
import restx.config.ConfigLoader;
import restx.config.ConfigSupplier;
import restx.factory.AutoStartable;
import restx.factory.Module;
import restx.factory.Provides;
import restx.mongo.MongoModule;
import restx.security.BCryptCredentialsStrategy;
import restx.security.BasicPrincipalAuthenticator;
import restx.security.CredentialsStrategy;
import restx.security.SecuritySettings;
import restx.security.StdBasicPrincipalAuthenticator;

import javax.inject.Named;
import java.io.IOException;
import java.net.URISyntaxException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Optional;

@Module(priority = -10)
public class AppModule {

    private static final Logger logger = LoggerFactory.getLogger(AppModule.class);

    @Provides
    @Named("app.name")
    public String appName() {
        return "swiftbeam";
    }

    @Provides
    @Named("mongo.db")
    public String jongoDbName() {
        return "swiftbeam";
    }

    @Provides
    @Named("restx.admin.password")
    public String restxAdminPassword() {
        return "admin";
    }

    @Provides
    public ConfigSupplier appConfigSupplier(ConfigLoader configLoader) {
        return configLoader.fromResource("settings");
    }

    @Provides
    public ConfigSupplier secretsConfigSupplier(ConfigLoader configLoader) {
        return configLoader.fromResource("secrets");
    }

    @Provides
    @Named("restx.i18n.labelsJsTemplate")
    public String labelsJsTemplate() throws URISyntaxException, IOException {
        return new String(Files.readAllBytes(
                Paths.get(Optional.ofNullable(
                        Thread.currentThread().getContextClassLoader().getResource("labels_template.js"))
                                  .orElseThrow(IllegalStateException::new).toURI())),
                          Charsets.UTF_8);
    }


    @Provides
    public AutoStartable mongoConnectionLogger(final @Named("restx.server.id") Optional<String> serverId,
                                               final @Named(MongoModule.MONGO_CLIENT_NAME) MongoClient client) {
        return () -> logger.info("{} - connected to Mongo @ {}", serverId.orElse("-"), client.getAllAddress());
    }

    @Provides
    public BasicPrincipalAuthenticator basicPrincipalAuthenticator(AppUserService userService,
                                                                   SecuritySettings securitySettings) {
        return new StdBasicPrincipalAuthenticator(userService, securitySettings);
    }

    @Provides
    public CredentialsStrategy credentialsStrategy() {
        return new BCryptCredentialsStrategy();
    }

    @Provides
    public User defaultAdminUser() {
        User user = new User();
        user.setLogin("admin");
        user.setRoles(Sets.newHashSet(AdminModule.RESTX_ADMIN_ROLE));
        return user;
    }

    @Provides
    @Named("restx.activation::restx.security.RestxSessionCookieFilter::RestxSessionCookieFilter")
    public String disableCookieAuthentication() {
        return "false";
    }

    @Provides
    @Named("restx.activation::restx.security.RestxSessionBareFilter::RestxSessionBareFilter")
    public String enableBareFilter() {
        return "true";
    }

    @Provides
    public RestxRoute loginJs() {
        return new StdRoute("loginJs", new StdRestxRequestMatcher("GET", "/@/ui/js/login.js")) {
            @Override
            public void handle(RestxRequestMatch match, RestxRequest req, RestxResponse resp, RestxContext ctx) throws
                    IOException {
                resp.setContentType("application/javascript");
                Resources.asByteSource(MoreResources.getResource("restx_overrides/login.js", true))
                         .copyTo(resp.getOutputStream());
            }
        };
    }

    @Provides
    public RestxRoute adminJs() {
        return new StdRoute("adminJs", new StdRestxRequestMatcher("GET", "/@/ui/js/admin.js")) {
            @Override
            public void handle(RestxRequestMatch match, RestxRequest req, RestxResponse resp, RestxContext ctx) throws
                    IOException {
                resp.setContentType("application/javascript");
                Resources.asByteSource(MoreResources.getResource("restx_overrides/admin.js", true))
                         .copyTo(resp.getOutputStream());
            }
        };
    }

    @Provides
    @Named("mock.tvdb")
    public String mockTvDb() {
        return "false";
    }
}
