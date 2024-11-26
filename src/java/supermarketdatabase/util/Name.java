package supermarketdatabase.util;

import java.util.Optional;

public record Name(String firstname, Optional<String> lastname) {
    public Name(String firstname, String lastname) {
        this(firstname, Optional.of(lastname));
    }
    public Name(String name) {
        this(name, Optional.empty());
    }
}
