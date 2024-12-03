package supermarketdatabase.util;

import java.util.Arrays;

public enum Temperature {
    NORMAL("Normal"),
    COOL("Gekühlt"),
    FROZEN("Gefroren");

    private final String id;

    Temperature(String id) {
        this.id = id;
    }

    public static Temperature of(String id) {
        return Arrays.stream(values()).filter(t -> t.id().equals(id)).findFirst().orElse(null);
    }

    public String id() {
        return id;
    }

    @Override
    public String toString() {
        return id();
    }
}
