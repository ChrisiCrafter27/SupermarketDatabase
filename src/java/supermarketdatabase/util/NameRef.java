package supermarketdatabase.util;

public record NameRef(int id, Name name) implements Comparable<NameRef> {
    @Override
    public int compareTo(NameRef o) {
        return Integer.compare(id(), o.id());
    }

    @Override
    public String toString() {
        return name().toString() + " (ID: " + id() + ")";
    }
}
