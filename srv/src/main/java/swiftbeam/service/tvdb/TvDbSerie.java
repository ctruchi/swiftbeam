package swiftbeam.service.tvdb;

import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlProperty;

public class TvDbSerie {

    @JacksonXmlProperty(localName = "id")
    private String id;
    @JacksonXmlProperty(localName = "language")
    private String language;
    @JacksonXmlProperty(localName = "SeriesName")
    private String name;
    @JacksonXmlProperty(localName = "banner")
    private String banner;
    private String overview;
    private String firstAired;
    private String network;
    @JacksonXmlProperty(localName = "IMDB_ID")
    private String imdbId;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getLanguage() {
        return language;
    }

    public void setLanguage(String language) {
        this.language = language;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getBanner() {
        return banner;
    }

    public void setBanner(String banner) {
        this.banner = banner;
    }

    public String getOverview() {
        return overview;
    }

    public void setOverview(String overview) {
        this.overview = overview;
    }

    public String getFirstAired() {
        return firstAired;
    }

    public void setFirstAired(String firstAired) {
        this.firstAired = firstAired;
    }

    public String getNetwork() {
        return network;
    }

    public void setNetwork(String network) {
        this.network = network;
    }

    public String getImdbId() {
        return imdbId;
    }

    public void setImdbId(String imdbId) {
        this.imdbId = imdbId;
    }

    @Override
    public String toString() {
        return "TvDbSerie{" +
                "id='" + id + '\'' +
                ", language='" + language + '\'' +
                ", name='" + name + '\'' +
                ", banner='" + banner + '\'' +
                ", overview='" + overview + '\'' +
                ", firstAired='" + firstAired + '\'' +
                ", network='" + network + '\'' +
                ", imdbId='" + imdbId + '\'' +
                '}';
    }
}
