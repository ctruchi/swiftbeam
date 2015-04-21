package swiftbeam.service.tvdb;

import restx.factory.Alternative;
import restx.factory.When;
import swiftbeam.domain.Show;

import java.util.Optional;

@Alternative(to = TvDbService.class)
@When(name = "mock.tvdb", value = "true")
public class MockTvDbService implements TvDbService {

    @Override
    public Optional<Show> findShow(String showName) {
        return Optional.of(new Show(showName));
    }
}
