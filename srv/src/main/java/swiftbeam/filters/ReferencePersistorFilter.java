package swiftbeam.filters;

import swiftbeam.domain.Reference;
import swiftbeam.persistence.EntityPersistor;
import com.google.common.base.Optional;
import restx.RestxContext;
import restx.RestxFilter;
import restx.RestxHandler;
import restx.RestxHandlerMatch;
import restx.RestxRequest;
import restx.RestxRequestMatch;
import restx.RestxResponse;
import restx.StdRestxRequestMatch;
import restx.factory.Component;

import java.io.IOException;

@Component
public class ReferencePersistorFilter implements RestxFilter, RestxHandler {

    private EntityPersistor persistor;

    public ReferencePersistorFilter(EntityPersistor persistor) {
        this.persistor = persistor;
    }

    @Override
    public Optional<RestxHandlerMatch> match(RestxRequest restxRequest) {
        //Don't filter anything
        return Optional.of(new RestxHandlerMatch(new StdRestxRequestMatch(restxRequest.getRestxPath()), this));
    }

    @Override
    public void handle(RestxRequestMatch match, RestxRequest req, RestxResponse resp, RestxContext ctx) throws IOException {
        Reference.initPersistor(persistor);
        try {
            ctx.nextHandlerMatch().handle(req, resp, ctx);
        } finally {
            Reference.clearPersistor();
        }
    }
}
