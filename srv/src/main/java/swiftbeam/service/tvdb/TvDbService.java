package swiftbeam.service.tvdb;

import swiftbeam.domain.Show;

import java.util.Optional;

public interface TvDbService {
    Optional<Show> findShow(String showName);
}
