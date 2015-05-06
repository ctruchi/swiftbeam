package swiftbeam.service.tvdb;

import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlElementWrapper;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlProperty;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlRootElement;

import java.util.List;

@JacksonXmlRootElement(localName = "Data")
public class TvDbData {

    @JacksonXmlProperty(localName = "Series")
    @JacksonXmlElementWrapper(useWrapping = false)
    private List<TvDbSerie> series;
    @JacksonXmlProperty(localName = "Episode")
    @JacksonXmlElementWrapper(useWrapping = false)
    private List<TvDbEpisode> episodes;

    public List<TvDbSerie> getSeries() {
        return series;
    }

    public void setSeries(List<TvDbSerie> series) {
        this.series = series;
    }

    public List<TvDbEpisode> getEpisodes() {
        return episodes;
    }

    public void setEpisodes(List<TvDbEpisode> episodes) {
        this.episodes = episodes;
    }

    @Override
    public String toString() {
        return "TvDbData{" +
                "series=" + series +
                '}';
    }
}
