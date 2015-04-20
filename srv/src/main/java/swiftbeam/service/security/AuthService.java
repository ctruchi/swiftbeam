package swiftbeam.service.security;

import swiftbeam.AppConfig;
import swiftbeam.AppSecrets;
import swiftbeam.domain.User;
import swiftbeam.domain.security.Token;
import swiftbeam.serialization.Views;
import swiftbeam.service.AppUserService;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.nimbusds.jose.JOSEException;
import com.nimbusds.jose.JWSAlgorithm;
import com.nimbusds.jose.JWSHeader;
import com.nimbusds.jose.JWSSigner;
import com.nimbusds.jose.crypto.MACSigner;
import com.nimbusds.jwt.JWTClaimsSet;
import com.nimbusds.jwt.SignedJWT;
import net.minidev.json.JSONAware;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import restx.RestxRequest;
import restx.WebException;
import restx.factory.Component;
import restx.http.HttpStatus;
import restx.jackson.FrontObjectMapperFactory;

import javax.inject.Named;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.Date;
import java.util.Optional;

@Component
public class AuthService {

    private static final Logger logger = LoggerFactory.getLogger(AuthService.class);
    private static final JWSHeader JWT_HEADER = new JWSHeader(JWSAlgorithm.HS256);

    private AppUserService userService;
    private AppSecrets appSecrets;
    private ObjectMapper objectMapper;

    public AuthService(AppUserService userService, AppSecrets appSecrets,
                       @Named(FrontObjectMapperFactory.MAPPER_NAME) ObjectMapper objectMapper) {
        this.userService = userService;
        this.appSecrets = appSecrets;
        this.objectMapper = objectMapper;
    }

    public User findOrCreateUser(User user) {
        com.google.common.base.Optional<User> userOptional = userService.findUserByName(user.getName());

        if (userOptional.isPresent()) {
            return userOptional.get();
        } else {
            user.setCreationDate(new Date());
            user.setModificationDate(new Date());
            return userService.createUser(user, Optional.<String>empty());
        }
    }

    public Token createToken(RestxRequest restxRequest, User user) {
        JWTClaimsSet claim = new JWTClaimsSet();
        claim.setSubject(user.getName());
        claim.setIssuer(restxRequest.getClientAddress());
        claim.setIssueTime(new Date());
        claim.setExpirationTime(Date.from(Instant.now().plus(14, ChronoUnit.DAYS)));
        claim.setCustomClaim("user", new JsonSmartUser(user, objectMapper));

        JWSSigner signer = new MACSigner(appSecrets.oauthSecretsToken());
        SignedJWT jwt = new SignedJWT(JWT_HEADER, claim);


        try {
            jwt.sign(signer);
        } catch (JOSEException e) {
            throw new IllegalStateException();
        }

        return new Token(jwt.serialize());
    }

    private static class JsonSmartUser implements JSONAware {
        private User user;
        private ObjectMapper objectMapper;

        public JsonSmartUser(User user, ObjectMapper objectMapper) {
            this.user = user;
            this.objectMapper = objectMapper;
        }

        @Override
        public String toJSONString() {
            try {
                return objectMapper.writerWithView(Views.Login.class).writeValueAsString(user);
            } catch (JsonProcessingException e) {
                logger.error(String.format("Can't create token for user: %s", user), e);
                throw new WebException(HttpStatus.UNAUTHORIZED, "Can't create token for user");
            }
        }
    }
}
