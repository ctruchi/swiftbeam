package swiftbeam.service;

import com.google.common.base.Strings;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import restx.factory.Component;
import swiftbeam.AppConfig;
import swiftbeam.domain.Episode;
import swiftbeam.domain.Season;
import swiftbeam.domain.Show;
import swiftbeam.persistence.ShowPersistor;
import swiftbeam.service.tvdb.TvDbService;

import java.io.IOException;
import java.nio.file.DirectoryStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Optional;
import java.util.stream.Stream;
import java.util.stream.StreamSupport;

@Component
public class ShowService {

    private static final Logger logger = LoggerFactory.getLogger(ShowService.class);

    private AppConfig appConfig;
    private TvDbService tvDbService;
    private ShowPersistor showPersistor;

    public ShowService(AppConfig appConfig, TvDbService tvDbService, ShowPersistor showPersistor) {
        this.appConfig = appConfig;
        this.tvDbService = tvDbService;
        this.showPersistor = showPersistor;
    }

    public void updateExistingState() {
        showPersistor.persistAll(computeExistingState());
    }

    private Stream<Show> computeExistingState() {
        DirectoryStream<Path> files;
        try {
            files = Files.newDirectoryStream(Paths.get(appConfig.basePath()));
        } catch (IOException e) {
            throw new IllegalStateException(String.format("Can't read %s", appConfig.basePath()), e);
        }

        return StreamSupport.stream(files.spliterator(), true)
                .map(path -> path.getFileName().toString())
                .map(tvDbService::findShow)
                .filter(Optional::isPresent)
                .map(Optional::get)
                .peek(show -> show.getSeasons().forEach(
                        season -> season.getEpisodes().forEach(
                                episode -> computeExistingState(show, season, episode))));
    }

    private void computeExistingState(Show show, Season season, Episode episode) {
        String episodeName = String.format("%s - %sx%s - %s.",
                show.getName(),
                season.getNumber(),
                Strings.padStart(episode.getNumber(), 2, '0'),
                episode.getName());
        Path seasonPath = Paths.get(appConfig.basePath(), show.getName(), "Season " + season.getNumber());
        try {
            episode.setPresent(Files.exists(seasonPath) &&
                    Files.find(seasonPath, 1, (path, basicFileAttributes) ->
                            path.getFileName().toString().startsWith(episodeName))
                            .findFirst()
                            .isPresent());
        } catch (IOException e) {
            logger.error(String.format("Can't search for episode %s", seasonPath), e);
        }
    }
}
