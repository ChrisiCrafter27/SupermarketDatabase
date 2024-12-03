package supermarketdatabase.util;

public record GoodRef(int id, String name) implements Comparable<GoodRef> {
    @Override
    public int compareTo(GoodRef o) {
        return Integer.compare(id(), o.id());
    }

    @Override
    public String toString() {
        return name() + " (ID:" + id() + ")";
    }
}
