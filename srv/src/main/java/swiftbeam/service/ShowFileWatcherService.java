package swiftbeam.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import restx.factory.AutoStartable;
import restx.factory.Component;
import swiftbeam.AppConfig;

import java.io.IOException;
import java.nio.file.FileSystems;
import java.nio.file.FileVisitResult;
import java.nio.file.Files;
import java.nio.file.LinkOption;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.SimpleFileVisitor;
import java.nio.file.WatchEvent;
import java.nio.file.WatchKey;
import java.nio.file.WatchService;
import java.nio.file.attribute.BasicFileAttributes;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import static java.nio.file.StandardWatchEventKinds.ENTRY_CREATE;
import static java.nio.file.StandardWatchEventKinds.ENTRY_DELETE;
import static java.nio.file.StandardWatchEventKinds.ENTRY_MODIFY;

@Component
public class ShowFileWatcherService implements AutoStartable {

    private static final Logger logger = LoggerFactory.getLogger(ShowFileWatcherService.class);
    private AppConfig appConfig;
    private PostProcessService postProcessService;

    public ShowFileWatcherService(AppConfig appConfig, PostProcessService postProcessService) {
        this.appConfig = appConfig;
        this.postProcessService = postProcessService;
    }

    @Override
    public void start() {
        WatchService watchService;
        try {
            watchService = FileSystems.getDefault().newWatchService();
        } catch (IOException e) {
            logger.error("Can't start watchService", e);
            throw new IllegalStateException(e);
        }

        ExecutorService executorService = Executors.newSingleThreadExecutor();

        try {
            Paths.get(appConfig.downloadPath()).register(watchService, ENTRY_CREATE, ENTRY_MODIFY, ENTRY_DELETE);
            Files.walkFileTree(Paths.get(appConfig.downloadPath()), new SimpleFileVisitor<Path>() {
                @Override
                public FileVisitResult preVisitDirectory(Path dir, BasicFileAttributes attrs) throws IOException {
                    dir.register(watchService, ENTRY_CREATE, ENTRY_MODIFY, ENTRY_DELETE);
                    return super.preVisitDirectory(dir, attrs);
                }
            });
        } catch (IOException e) {
            logger.error(String.format("Can't watch folder: %s", appConfig.downloadPath()), e);
            throw new IllegalStateException(e);
        }

        executorService.submit(() -> {

            WatchKey watchKey;
            while (true) {
                try {
                    watchKey = watchService.take();
                } catch (InterruptedException e) {
                    logger.error("Watch service interrupted", e);
                    break;
                }

                List<WatchEvent<?>> watchEvents = watchKey.pollEvents();

                for (WatchEvent<?> watchEvent : watchEvents) {
                    WatchEvent.Kind kind = watchEvent.kind();
                    Path filePath =
                            ((Path) watchKey.watchable()).resolve(((WatchEvent<Path>) watchEvent).context());
                    if (Files.isDirectory(filePath, LinkOption.NOFOLLOW_LINKS)) {
                        if (kind == ENTRY_CREATE) {
                            try {
                                filePath.register(watchService, ENTRY_CREATE, ENTRY_MODIFY, ENTRY_DELETE);
                            } catch (IOException e) {
                                logger.error(String.format("Can't watch folder: %s", filePath), e);
                            }
                        }
                    } else {
                        if (kind == ENTRY_CREATE || kind == ENTRY_MODIFY) {
                            try {
                                postProcessService.process(filePath);
                            } catch (Exception e) {
                                logger.error(String.format("Error processing %s", filePath), e);
                                break;
                            }
                        }
                    }
                }

                watchKey.reset();
            }
        });

        logger.debug("Start watching {}", appConfig.downloadPath());
    }
}
