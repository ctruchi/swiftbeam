package swiftbeam.web.rest;

import restx.annotations.GET;
import restx.annotations.PUT;
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
    @PUT("/state")
    public void updateExistingState() {
        showService.updateExistingState();
    }

    @PermitAll
    @GET("")
    public Iterable<Show> findAll() {
        return showService.findAll();
    }
}
