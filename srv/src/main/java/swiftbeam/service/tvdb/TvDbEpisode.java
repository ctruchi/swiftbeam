package swiftbeam.service.tvdb;

import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlProperty;

public class TvDbEpisode {

    @JacksonXmlProperty(localName = "EpisodeNumber")
    private String number;
    @JacksonXmlProperty(localName = "EpisodeName")
    private String name;
    @JacksonXmlProperty(localName = "SeasonNumber")
    private String season;

    public String getNumber() {
        return number;
    }

    public void setNumber(String number) {
        this.number = number;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getSeason() {
        return season;
    }

    public void setSeason(String season) {
        this.season = season;
    }
}
