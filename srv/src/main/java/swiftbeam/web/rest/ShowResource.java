package swiftbeam.web.rest;

import org.bson.types.ObjectId;
import restx.annotations.GET;
import restx.annotations.PUT;
import restx.annotations.RestxResource;
import restx.factory.Component;
import restx.security.PermitAll;
import swiftbeam.domain.Show;
import swiftbeam.service.ShowService;

import java.util.Optional;

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

    @PermitAll
    @GET("/{showId}")
    public Optional<Show> getShow(ObjectId showId) {
        return showService.getShow(showId);
    }

    @PermitAll
    @PUT("/{showId}/{seasonNumber}/{episodeNumber}")
    public void searchEpisode(ObjectId showId, Integer seasonNumber, Integer episodeNumber) {
        showService.searchEpisode(showId, seasonNumber, episodeNumber);
    }
}
