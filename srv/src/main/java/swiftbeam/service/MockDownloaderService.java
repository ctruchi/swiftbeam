package swiftbeam.service;

import com.google.common.base.Strings;
import com.google.common.collect.ImmutableSet;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import restx.factory.Alternative;
import restx.factory.When;
import swiftbeam.AppConfig;
import swiftbeam.domain.Episode;
import swiftbeam.domain.Season;
import swiftbeam.domain.Show;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.attribute.PosixFilePermission;
import java.nio.file.attribute.PosixFilePermissions;

@Alternative(to = DownloaderService.class)
@When(name = "mock.download", value = "true")
public class MockDownloaderService implements DownloaderService {

    private static final Logger logger = LoggerFactory.getLogger(MockDownloaderService.class);
    private AppConfig appConfig;

    public MockDownloaderService(AppConfig appConfig) {
        this.appConfig = appConfig;
    }

    @Override
    public void search(Show show, Season season, Episode episode) {

        Path path = Paths.get(appConfig.downloadPath(),
                String.format("%s.s%se%s.hdtv.xvid-yestv.avi",
                        show.getName(),
                        season.getNumber(),
                        Strings.padStart(episode.getNumber().toString(), 2, '0')));
        try {
            Files.createFile(path,
                    PosixFilePermissions.asFileAttribute(ImmutableSet.of(PosixFilePermission.OWNER_READ)));
        } catch (IOException e) {
            logger.error(String.format("Can't create file %s", path), e);
        }

        logger.info("Created file {}", path);
    }
}
