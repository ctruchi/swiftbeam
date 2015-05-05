package swiftbeam.domain;

public class Show {

    private String name;
    private TvDbMetadata tvDbMetadata;
    private String imdbId;

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
}
