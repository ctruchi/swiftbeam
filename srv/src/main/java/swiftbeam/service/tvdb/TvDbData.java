package swiftbeam.service.tvdb;

import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlElementWrapper;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlRootElement;

import java.util.List;

@JacksonXmlRootElement(localName = "Data")
public class TvDbData {

    @JacksonXmlElementWrapper(useWrapping = false)
    public List<TvDbSerie> getSeries() {
        return series;
    }

    public void setSeries(List<TvDbSerie> series) {
        this.series = series;
    }

    private List<TvDbSerie> series;

    @Override
    public String toString() {
        return "TvDbData{" +
                "series=" + series +
                '}';
    }
}
