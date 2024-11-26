package supermarketdatabase.sql;

import supermarketdatabase.util.LoginMode;
import supermarketdatabase.util.Name;

import java.util.Optional;

public class Statements {
    public static Statement<Optional<Integer>> findId(Name name, LoginMode mode) {
        return switch (mode) {
            case Grosskunde -> new Statement<>(String.format("""
                SELECT "Kunden-ID"
                FROM "Großkunde"
                WHERE "Name" = %s;""", name.firstname()), result -> result.getRows().length == 1 && result.getRows()[0].length == 1 ? Optional.of(Integer.parseInt(result.getRows()[0][0])) : Optional.empty());
            case Mitarbeiter, Admin -> new Statement<>(String.format("""
                SELECT "Mitarbeiter-ID"
                FROM Mitarbeiter
                WHERE Vorname = %s & Nachname = %s;""", name.firstname(), name.lastname().orElseThrow()), result -> result.getRows().length == 1 && result.getRows()[0].length == 1 ? Optional.of(Integer.parseInt(result.getRows()[0][0])) : Optional.empty());
        };
    }

    public static Statement<Optional<Name>> findName(int id, LoginMode mode) {
        return null;
    }

    public static Statement<Boolean> checkPassword(int id, String password, LoginMode mode) {
        return switch (mode) {
            case Grosskunde -> new Statement<>(String.format("""
                SELECT Passwort
                FROM "Großkunde"
                WHERE "Kunden-ID" = %s;""", id), result -> result.getRows().length == 1 && result.getRows()[0].length == 1 && Integer.valueOf(result.getRows()[0][0]).equals(password.hashCode()));
            case Mitarbeiter, Admin -> new Statement<>(String.format("""
                SELECT Passwort
                FROM Mitarbeiter
                WHERE "Mitarbeiter-ID" = %s;""", id), result -> result.getRows().length == 1 && result.getRows()[0].length == 1 && Integer.valueOf(result.getRows()[0][0]).equals(password.hashCode()));
        };
    }
}
