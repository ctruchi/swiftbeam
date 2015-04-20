package swiftbeam.web.rest;

import swiftbeam.domain.User;
import swiftbeam.domain.security.Token;
import swiftbeam.service.AppUserService;
import swiftbeam.service.security.AuthService;
import restx.RestxRequest;
import restx.annotations.POST;
import restx.annotations.Param;
import restx.annotations.RestxResource;
import restx.factory.Component;
import restx.security.PermitAll;

import java.util.Optional;

@Component
@RestxResource("/user")
public class UserResource {

    private AppUserService userService;
    private AuthService authService;

    public UserResource(AppUserService userService, AuthService authService) {
        this.userService = userService;
        this.authService = authService;
    }

    @PermitAll
    @POST("")
    public Token createUser(User user, @Param(kind = Param.Kind.CONTEXT, value = "request") RestxRequest restxRequest) {
        User dbUser = userService.createUser(user, Optional.ofNullable(user.getPassword()));
        return authService.createToken(restxRequest, dbUser);
    }
}
