package swiftbeam.domain;

import java.util.ArrayList;
import java.util.List;

public class Season {
    private String number;
    private List<Episode> episodes = new ArrayList<>();

    public Season(String number) {
        this.number = number;
    }

    public String getNumber() {
        return number;
    }

    public void setNumber(String number) {
        this.number = number;
    }

    public List<Episode> getEpisodes() {
        return episodes;
    }

    public void setEpisodes(List<Episode> episodes) {
        this.episodes = episodes;
    }
}
