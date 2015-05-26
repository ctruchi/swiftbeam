package swiftbeam.domain;

import java.util.ArrayList;
import java.util.List;

public class Season {
    private Integer number;
    private List<Episode> episodes = new ArrayList<>();

    public Season() {
    }

    public Season(Integer number) {
        this.number = number;
    }

    public Integer getNumber() {
        return number;
    }

    public void setNumber(Integer number) {
        this.number = number;
    }

    public List<Episode> getEpisodes() {
        return episodes;
    }

    public void setEpisodes(List<Episode> episodes) {
        this.episodes = episodes;
    }

    @Override
    public String toString() {
        return "Season{" +
                "number='" + number + '\'' +
                '}';
    }
}
