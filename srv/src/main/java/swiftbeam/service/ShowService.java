package swiftbeam.service;

import restx.factory.Component;
import swiftbeam.AppConfig;
import swiftbeam.domain.Show;
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

    private AppConfig appConfig;
    private TvDbService tvDbService;

    public ShowService(AppConfig appConfig, TvDbService tvDbService) {
        this.appConfig = appConfig;
        this.tvDbService = tvDbService;
    }

    public Stream<Show> computeExistingState() {
        DirectoryStream<Path> files;
        try {
            files = Files.newDirectoryStream(Paths.get(appConfig.basePath()));
        } catch (IOException e) {
            throw new IllegalStateException(String.format("Can't read %s", appConfig.basePath()), e);
        }

        Stream<Optional<Show>> shows = StreamSupport.stream(files.spliterator(), true)
                                                      .map(path -> path.getFileName().toString())
                                                      .map(tvDbService::findShow);

        return shows.filter(Optional::isPresent).map(Optional::get);
    }
}
