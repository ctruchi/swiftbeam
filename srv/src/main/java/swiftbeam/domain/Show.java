package swiftbeam.domain;

import java.util.List;

public class Show {

    private String name;
    private TvDbMetadata tvDbMetadata;
    private String imdbId;
    private List<Season> seasons;

    public Show() {
    }

    public Show(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public TvDbMetadata getTvDbMetadata() {
        return tvDbMetadata;
    }

    public void setTvDbMetadata(TvDbMetadata tvDbMetadata) {
        this.tvDbMetadata = tvDbMetadata;
    }

    public String getImdbId() {
        return imdbId;
    }

    public void setImdbId(String imdbId) {
        this.imdbId = imdbId;
    }

    public List<Season> getSeasons() {
        return seasons;
    }

    public void setSeasons(List<Season> seasons) {
        this.seasons = seasons;
    }

    @Override
    public String toString() {
        return "Show{" +
                "name='" + name + '\'' +
                '}';
    }
}
