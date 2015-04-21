package swiftbeam.web.rest;

import restx.annotations.GET;
import restx.annotations.RestxResource;
import restx.factory.Component;
import restx.security.PermitAll;
import swiftbeam.domain.Show;
import swiftbeam.service.ShowService;

@Component
@RestxResource("/show")
public class ShowResource {

    private ShowService showService;

    public ShowResource(ShowService showService) {
        this.showService = showService;
    }

    @PermitAll
    @GET("/state")
    public Iterable<Show> computeExistingState() {
        return () -> showService.computeExistingState().iterator();
    }
}
