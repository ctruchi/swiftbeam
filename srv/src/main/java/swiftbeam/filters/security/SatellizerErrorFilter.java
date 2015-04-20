package swiftbeam.filters.security;

import com.google.common.base.Optional;
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

import java.io.IOException;

@Component
public class SatellizerErrorFilter implements RestxFilter, RestxHandler {

    @Override
    public Optional<RestxHandlerMatch> match(RestxRequest req) {
        if (req.getRestxPath().startsWith("/auth")) {
            return Optional.of(new RestxHandlerMatch(new StdRestxRequestMatch(req.getRestxPath()), this));
        } else {
            return Optional.absent();
        }
    }

    @Override
    public void handle(RestxRequestMatch match, RestxRequest req, RestxResponse resp, RestxContext ctx) throws
            IOException {
        try {
            ctx.nextHandlerMatch().handle(req, resp, ctx);
        } catch (IllegalArgumentException | IllegalStateException e) {
            throw new WebException(HttpStatus.UNAUTHORIZED, e.getMessage());
        }
    }
}
