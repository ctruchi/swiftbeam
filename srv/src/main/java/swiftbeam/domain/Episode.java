package swiftbeam.domain;

public class Episode {
    private Integer number;
    private String name;
    private boolean present;

    public void setNumber(Integer number) {
        this.number = number;
    }

    public Integer getNumber() {
        return number;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public boolean isPresent() {
        return present;
    }

    public void setPresent(boolean present) {
        this.present = present;
    }

    @Override
    public String toString() {
        return "Episode{" +
                "number='" + number + '\'' +
                ", name='" + name + '\'' +
                '}';
    }
}
