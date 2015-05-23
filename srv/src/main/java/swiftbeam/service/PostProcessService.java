package swiftbeam.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import restx.factory.Component;

import java.nio.file.Path;

@Component
public class PostProcessService {
    private static final Logger logger = LoggerFactory.getLogger(PostProcessService.class);

    public void process(Path... filename) {
        logger.debug("Post processing {}", filename);
    }
}
