package swiftbeam.web.rest;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.PropertyNamingStrategy;
import com.fasterxml.jackson.dataformat.xml.XmlMapper;
import restx.annotations.GET;
import restx.annotations.RestxResource;
import restx.factory.Component;
import restx.security.PermitAll;
import swiftbeam.domain.Show;
import swiftbeam.service.ShowService;
import swiftbeam.service.tvdb.TvDbData;
import swiftbeam.service.tvdb.TvDbSerie;

import java.util.ArrayList;

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

    @PermitAll
    @GET("/test")
    public void test() throws JsonProcessingException {
        TvDbData tvDbData = new TvDbData();
        ArrayList<TvDbSerie> series = new ArrayList<>();
        TvDbSerie serie = new TvDbSerie();
        serie.setId("1");
        series.add(serie);
        tvDbData.setSeries(series);
        XmlMapper xmlMapper = new XmlMapper();
        xmlMapper.setPropertyNamingStrategy(PropertyNamingStrategy.PASCAL_CASE_TO_CAMEL_CASE);
        System.out.println(xmlMapper.writeValueAsString(tvDbData));
    }
}
