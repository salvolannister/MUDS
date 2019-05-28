import java.util.Objects;

public class Distance {
    private Polo posizione;
    private Integer rssi;

    public Distance(Polo posizione, Integer rssi) {
        this.posizione = posizione;
        this.rssi = rssi;
    }

    public Polo getPosizione() {
        return posizione;
    }

    public void setPosizione(Polo posizione) {
        this.posizione = posizione;
    }

    public Integer getRssi() {
        return rssi;
    }

    public void setRssi(Integer rssi) {
        this.rssi = rssi;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Distance distance = (Distance) o;
        return posizione.equals(distance.posizione) &&
                rssi.equals(distance.rssi);
    }

    @Override
    public int hashCode() {
        return Objects.hash(posizione, rssi);
    }

    @Override
    public String toString() {
        return "Distance{" +
                "posizione=" + posizione +
                ", rssi=" + rssi +
                '}';
    }
}
