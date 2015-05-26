package swiftbeam.service.tvdb;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.PropertyNamingStrategy;
import com.fasterxml.jackson.dataformat.xml.XmlMapper;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlProperty;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlRootElement;
import com.github.kevinsawicki.http.HttpRequest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import restx.factory.Alternative;
import restx.factory.When;
import swiftbeam.AppSecrets;
import swiftbeam.domain.Episode;
import swiftbeam.domain.Season;
import swiftbeam.domain.Show;
import swiftbeam.domain.TvDbMetadata;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

@Alternative(to = TvDbService.class)
@When(name = "mock.tvdb", value = "false")
public class TvDbServiceImpl implements TvDbService {

    private static final Logger logger = LoggerFactory.getLogger(TvDbServiceImpl.class);

    private AppSecrets secrets;
    private ObjectMapper mapper;

    public TvDbServiceImpl(AppSecrets secrets) {
        this.secrets = secrets;
        mapper = new XmlMapper().setPropertyNamingStrategy(PropertyNamingStrategy.PASCAL_CASE_TO_CAMEL_CASE)
                .disable(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES);
    }

    @Override
    public Optional<Show> findShow(String showName) {
        Optional<String> serverTime = getServerTime();
        if (!serverTime.isPresent()) {
            logger.error("Can't get tvdb servertime");
            return Optional.empty();
        }

        Show show = new Show();
        TvDbMetadata tvDbMetadata = new TvDbMetadata();
        tvDbMetadata.setLastUpdate(serverTime.get());
        show.setTvDbMetadata(tvDbMetadata);

        return getShow(showName, serverTime.get());
    }

    private Optional<Show> getShow(String showName, String lastUpdate) {
        Optional<Show> showOptional = getShowInfos(showName, lastUpdate);
        showOptional.ifPresent(this::getShowDetails);
        return showOptional;
    }

    private void getShowDetails(Show show) {
        Map<String, Season> seasons = new HashMap<>();
        httpGet(HttpRequest.get(String.format("http://thetvdb.com/api/%s/series/%s/all/%s.xml",
                        secrets.tvDbApiKey(), show.getTvDbMetadata().getId(), show.getTvDbMetadata().getLanguage())),
                TvDbData.class)
                .ifPresent(tvDbData -> Optional.ofNullable(tvDbData.getEpisodes()).ifPresent(
                        episodes -> episodes.forEach(tvDbEpisode -> {
                            Season season = seasons.computeIfAbsent(tvDbEpisode.getSeason(),
                                    s -> new Season(Integer.parseInt(s)));
                            Episode episode = new Episode();
                            episode.setNumber(Integer.parseInt(tvDbEpisode.getNumber()));
                            episode.setName(tvDbEpisode.getName());
                            season.getEpisodes().add(episode);
                        })));
        show.setSeasons(seasons.values().stream().collect(Collectors.toList()));
    }

    private Optional<Show> getShowInfos(String showName, String lastUpdate) {
        return httpGet(HttpRequest.get("http://thetvdb.com/api/GetSeries.php?seriesname=" + showName), TvDbData.class)
                .map(tvDbData -> {
                    List<TvDbSerie> series = tvDbData.getSeries();
                    if (series.isEmpty()) {
                        return null;
                    }

                    TvDbSerie tvDbSerie = series.get(0);
                    Show show = new Show(tvDbSerie.getName());
                    TvDbMetadata tvDbMetadata = new TvDbMetadata();
                    tvDbMetadata.setLastUpdate(lastUpdate);
                    tvDbMetadata.setId(tvDbSerie.getId());
                    tvDbMetadata.setLanguage(tvDbSerie.getLanguage());
                    show.setTvDbMetadata(tvDbMetadata);

                    show.setImdbId(tvDbSerie.getImdbId());
                    return show;
                });
    }

    private Optional<String> getServerTime() {
        Optional<ServerTime> serverTime =
                httpGet(HttpRequest.get("http://thetvdb.com/api/Updates.php?type=none"), ServerTime.class);

        return serverTime.map(ServerTime::getTime);
    }

    private <T> Optional<T> httpGet(HttpRequest request, Class<T> type) {
        String response = request.body();
        if (!request.ok()) {
            logger.warn("Can't contact tvdb ({}): {}\n {}", request.url(), request.code(), response);
            return Optional.empty();
        }

        T result;
        try {
            result = mapper.reader(type).readValue(response);
        } catch (IOException e) {
            logger.warn(String.format("Can't process result of tvdb: %s", response), e);
            return Optional.empty();
        }

        return Optional.of(result);
    }

    @JacksonXmlRootElement(localName = "Items")
    public static class ServerTime {

        @JacksonXmlProperty(localName = "Time")
        private String time;

        public String getTime() {
            return time;
        }

        public void setTime(String time) {
            this.time = time;
        }
    }
}
