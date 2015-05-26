package swiftbeam.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import restx.factory.Component;
import swiftbeam.domain.Episode;
import swiftbeam.domain.Season;
import swiftbeam.domain.Show;
import swiftbeam.persistence.ShowPersistor;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardCopyOption;
import java.util.Optional;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static com.google.common.io.Files.getFileExtension;

@Component
public class PostProcessService {
    private static final Logger logger = LoggerFactory.getLogger(PostProcessService.class);
    public static final String EPISODE_PATTERN = "^(.+)s(\\d+)e(\\d+).+$";
    private ShowService showService;
    private ShowPersistor showPersistor;

    public PostProcessService(ShowService showService, ShowPersistor showPersistor) {
        this.showService = showService;
        this.showPersistor = showPersistor;
    }

    public void process(Path... filenames) {
        for (Path filename : filenames) {
            process(filename);
        }
    }

    private void process(Path filename) {
        logger.debug("Post processing {}", filename.getFileName());
        Optional<EpisodeResult> optionalResult = guessEpisode(filename.getFileName().toString());
        optionalResult.ifPresent(episodeResult -> {
            try {
                Season season = episodeResult.getSeason();
                Episode episode = episodeResult.getEpisode();
                Show show = episodeResult.getShow();

                Path target = showService.computeEpisodePath(show, season, episode,
                        getFileExtension(filename.toString()));

                Files.createDirectories(target.getParent());

                Files.move(filename, target, StandardCopyOption.ATOMIC_MOVE);
                logger.debug("Move {} to {}", filename, target);

                showService.updateEpisodeState(show.getId(), season.getNumber(), episode.getNumber(), true);
            } catch (IOException e) {
                logger.error(String.format("Can't move %s", filename), e);
                throw new RuntimeException(e);
            }

        });
    }


    private Optional<EpisodeResult> guessEpisode(String filename) {
        Pattern pattern = Pattern.compile(EPISODE_PATTERN, Pattern.CASE_INSENSITIVE);
        Matcher matcher = pattern.matcher(filename);
        if (matcher.matches()) {
            Optional<Show> optionalShow = showPersistor.findOne(
                    String.format("{name: {$regex: '^%s', $options: 'i'}}",
                            matcher.group(1).replaceAll("\\W", " ").trim()));

            return optionalShow.map(show -> {
                EpisodeResult episodeResult = new EpisodeResult();

                Season season = show.getSeasons().get(Integer.parseInt(matcher.group(2)));
                Episode episode = season.getEpisodes().get(Integer.parseInt(matcher.group(3)) - 1);

                episodeResult.setShow(show);
                episodeResult.setSeason(season);
                episodeResult.setEpisode(episode);

                logger.debug("Found {} - {}x{}", show.getName(), season.getNumber(), episode.getNumber());

                return episodeResult;
            });
        }

        logger.debug("Found no show matching {}", filename);
        return Optional.empty();
    }

    private static class EpisodeResult {
        private Show show;
        private Season season;
        private Episode episode;

        public Show getShow() {
            return show;
        }

        public void setShow(Show show) {
            this.show = show;
        }

        public Season getSeason() {
            return season;
        }

        public void setSeason(Season season) {
            this.season = season;
        }

        public Episode getEpisode() {
            return episode;
        }

        public void setEpisode(Episode episode) {
            this.episode = episode;
        }
    }
}
