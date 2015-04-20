package swiftbeam.filters.security;

import swiftbeam.AppConfig;
import com.google.common.base.Optional;
import com.nimbusds.jose.JOSEException;
import com.nimbusds.jose.crypto.MACVerifier;
import com.nimbusds.jwt.JWTClaimsSet;
import com.nimbusds.jwt.SignedJWT;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import restx.RestxContext;
import restx.RestxFilter;
import restx.RestxHandler;
import restx.RestxHandlerMatch;
import restx.RestxRequest;
import restx.RestxRequestMatch;
import restx.RestxResponse;
import restx.StdRestxRequestMatch;
import restx.WebException;
import restx.factory.Component;
import restx.http.HttpStatus;
import restx.security.BasicPrincipalAuthenticator;
import restx.security.RestxPrincipal;
import restx.security.RestxSession;
import swiftbeam.AppSecrets;

import java.io.IOException;
import java.text.ParseException;
import java.util.Date;
import java.util.Locale;

@Component(priority = -195)
public class JWTAuthenticationFilter implements RestxFilter {

    private static final Logger logger = LoggerFactory.getLogger(JWTAuthenticationFilter.class);

    private final String secretsToken;
    private BasicPrincipalAuthenticator authenticator;

    private RestxHandler bearerHandler = new RestxHandler() {
        @Override
        public void handle(RestxRequestMatch match, RestxRequest req, RestxResponse resp, RestxContext ctx) throws
                IOException {
            JWTClaimsSet claimsSet;
            try {
                claimsSet = decodeToken(req.getHeader("Authorization").get());
            } catch (ParseException | JOSEException e) {
                throw new WebException(HttpStatus.BAD_REQUEST, "Invalid JWT Token - " + e.getMessage());
            }

            if (claimsSet.getExpirationTime().before(new Date())) {
                throw new WebException(HttpStatus.UNAUTHORIZED, "JWT Token expired");
            } else {
                Optional<? extends RestxPrincipal> principal = authenticator.findByName(claimsSet.getSubject());
                if (principal.isPresent()) {
                    logger.debug("JWT authenticated '{}'", principal.get().getName());

                    RestxSession.current().authenticateAs(principal.get());

                    ctx.nextHandlerMatch().handle(req, resp, ctx);
                } else {
                    throw new WebException(HttpStatus.UNAUTHORIZED, "Principal unknown");
                }
            }
        }

        private JWTClaimsSet decodeToken(String authHeader) throws ParseException, JOSEException {
            SignedJWT signedJWT = SignedJWT.parse(getSerializationToken(authHeader));
            if (!signedJWT.verify(new MACVerifier(secretsToken))) {
                throw new JOSEException("Signature verification failed");
            }
            return (JWTClaimsSet) signedJWT.getJWTClaimsSet();
        }

        private String getSerializationToken(String authHeader) {
            return authHeader.split(" ")[1];
        }
    };

    public JWTAuthenticationFilter(AppSecrets appSecrets, BasicPrincipalAuthenticator authenticator) {
        this.authenticator = authenticator;
        secretsToken = appSecrets.oauthSecretsToken();
    }

    @Override
    public Optional<RestxHandlerMatch> match(RestxRequest req) {
        Optional<String> authorization = req.getHeader("Authorization");
        if (authorization.isPresent()) {
            if (authorization.get().toLowerCase(Locale.ENGLISH).startsWith("bearer ")) {
                return Optional.of(new RestxHandlerMatch(
                        new StdRestxRequestMatch("*", req.getRestxPath()), bearerHandler));
            }
        }
        return Optional.absent();
    }
}
