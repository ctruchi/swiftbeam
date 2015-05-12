package swiftbeam.service;

import swiftbeam.domain.Episode;
import swiftbeam.domain.Season;
import swiftbeam.domain.Show;

public interface DownloaderService {
    void search(Show show, Season season, Episode episode);
}
