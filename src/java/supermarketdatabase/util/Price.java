package supermarketdatabase.util;

public record Price(double value) implements Comparable<Price> {
    @Override
    public int compareTo(Price o) {
        return Double.compare(value(), o.value());
    }

    @Override
    public String toString() {
        return value() + "€";
    }
}
