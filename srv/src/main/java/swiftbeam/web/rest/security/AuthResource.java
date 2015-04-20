package swiftbeam.web.rest.security;

import restx.RestxRequest;
import restx.WebException;
import restx.annotations.GET;
import restx.annotations.POST;
import restx.annotations.Param;
import restx.annotations.RestxResource;
import restx.factory.Component;
import restx.http.HttpStatus;
import restx.security.PermitAll;
import restx.security.RestxPrincipal;
import restx.security.RestxSession;
import swiftbeam.domain.User;
import swiftbeam.domain.security.Token;
import swiftbeam.service.AppUserService;
import swiftbeam.service.security.AuthService;

import java.util.Map;

@Component
@RestxResource("/auth")
public class AuthResource {

    private AuthService authService;
    private AppUserService userService;

    public AuthResource(AuthService authService, AppUserService userService) {
        this.authService = authService;
        this.userService = userService;
    }

    @PermitAll
    @POST("/login")
    public Token login(Map<String, String> user, @Param(kind = Param.Kind.CONTEXT, value = "request") RestxRequest
            restxRequest) {
        User dbUser = authenticate(user.get("login"), user.get("password"));

        return authService.createToken(restxRequest, dbUser);
    }

    private User authenticate(String login, String password) {
        com.google.common.base.Optional<User> dbUser =
                userService.findAndCheckCredentials(login, password);
        if (!dbUser.isPresent()) {
            throw new WebException(HttpStatus.UNAUTHORIZED, "Invalid login");
        }
        return dbUser.get();
    }

    @GET("/current")
    public RestxPrincipal currentUser() {
        return RestxSession.current().getPrincipal().get();
    }

}
