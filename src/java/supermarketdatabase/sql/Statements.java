package supermarketdatabase.sql;

import supermarketdatabase.util.StaffData;
import supermarketdatabase.util.*;

import java.time.LocalDate;
import java.util.*;

public class Statements {
    public static Statement<Boolean> enableForeignKeys() {
        return new Statement<>("""
                PRAGMA foreign_keys = ON;""", MyQueryResult::succeeded);
    }

    public static Statement<Optional<Integer>> findId(Name name, LoginMode mode) {
        return switch (mode) {
            case Grosskunde -> new Statement<>(String.format("""
                SELECT "Kunden-ID"
                FROM "Großkunde"
                WHERE "Name" = "%s";""", name.firstname()), result -> result.succeeded() && result.getRows().length == 1 && result.getRows()[0].length == 1 ? Optional.of(Integer.parseInt(result.getRows()[0][0])) : Optional.empty());
            case Mitarbeiter -> new Statement<>(String.format("""
                SELECT "Mitarbeiter-ID"
                FROM Mitarbeiter
                WHERE Vorname = "%s" AND Nachname = "%s";""", name.firstname(), name.lastname().orElseThrow()), result -> result.succeeded() && result.getRows().length == 1 && result.getRows()[0].length == 1 ? Optional.of(Integer.parseInt(result.getRows()[0][0])) : Optional.empty());
        };
    }

    public static Statement<Optional<Name>> findName(int id, LoginMode mode) {
        return switch (mode) {
            case Grosskunde -> new Statement<>(String.format("""
                SELECT Name
                FROM "Großkunde"
                WHERE "Kunden-ID" = "%s";""", id), result -> result.succeeded() && result.getRows().length == 1 && result.getRows()[0].length == 1 ? Optional.of(new Name(result.getRows()[0][0])) : Optional.empty());
            case Mitarbeiter -> new Statement<>(String.format("""
                SELECT Vorname, Nachname
                FROM Mitarbeiter
                WHERE "Mitarbeiter-ID" = "%s";""", id), result -> result.succeeded() && result.getRows().length == 1 && result.getRows()[0].length == 2 ? Optional.of(new Name(result.getRows()[0][0], result.getRows()[0][1])) : Optional.empty());
        };
    }

    public static Statement<Boolean> checkPassword(int id, String password, LoginMode mode) {
        return switch (mode) {
            case Grosskunde -> new Statement<>(String.format("""
                SELECT Passwort
                FROM "Großkunde"
                WHERE "Kunden-ID" = "%s";""", id), result -> result.succeeded() && result.getRows().length == 1 && result.getRows()[0].length == 1 && Integer.valueOf(result.getRows()[0][0]).equals(password.hashCode()));
            case Mitarbeiter -> new Statement<>(String.format("""
                SELECT Passwort
                FROM Mitarbeiter
                WHERE "Mitarbeiter-ID" = "%s";""", id), result -> result.succeeded() && result.getRows().length == 1 && result.getRows()[0].length == 1 && Integer.valueOf(result.getRows()[0][0]).equals(password.hashCode()));
        };
    }

    public static Statement<Optional<CustomerData>> customerData(int id) {
        return new Statement<>(String.format("""
                SELECT Name, Ort, "Straße", Registrierungsdatum, Gesamtausgaben
                FROM "Großkunde"
                WHERE "Kunden-ID" = "%s";""", id), result -> result.succeeded() && result.getRows().length == 1 && result.getRows()[0].length == 5 ? Optional.of(new CustomerData(result.getRows()[0][0], result.getRows()[0][1], result.getRows()[0][2], LocalDate.parse(result.getRows()[0][3]).format(FormattedDate.FORMATTER), Double.parseDouble(result.getRows()[0][4]))) : Optional.empty());
    }

    public static Statement<List<Order>> orders(int id) {
        return new Statement<>(String.format("""
                SELECT b."Bestell-ID", k."Kunden-ID", k.Name, w."Waren-ID", w.Name, b.Menge, b.Bestelldatum, b.Abholdatum, b.Gesamtpreis
                FROM bestellt AS b
                INNER JOIN Ware AS w
                ON w."Waren-ID" = b."Waren-ID"
                INNER JOIN "Großkunde" AS k
                ON k."Kunden-ID" = b."Kunden-ID"
                WHERE b."Kunden-ID" = "%s";""", id), result -> result.succeeded() ? Arrays.stream(result.getRows()).map(array -> new Order(Integer.parseInt(array[0]), new NameRef(Integer.parseInt(array[1]), new Name(array[2])), new GoodRef(Integer.parseInt(array[3]), array[4]), Integer.parseInt(array[5]), new FormattedDate(array[6]), new FormattedDate(array[7]), new Price(Double.parseDouble(array[8])))).toList() : List.of());
    }

    public static Statement<List<Order>> orders() {
        return new Statement<>("""
                SELECT b."Bestell-ID", k."Kunden-ID", k.Name, w."Waren-ID", w.Name, b.Menge, b.Bestelldatum, b.Abholdatum, b.Gesamtpreis
                FROM bestellt AS b
                INNER JOIN Ware AS w
                ON w."Waren-ID" = b."Waren-ID"
                INNER JOIN "Großkunde" AS k
                ON k."Kunden-ID" = b."Kunden-ID";""", result -> result.succeeded() ? Arrays.stream(result.getRows()).map(array -> new Order(Integer.parseInt(array[0]), new NameRef(Integer.parseInt(array[1]), new Name(array[2])), new GoodRef(Integer.parseInt(array[3]), array[4]), Integer.parseInt(array[5]), new FormattedDate(array[6]), new FormattedDate(array[7]), new Price(Double.parseDouble(array[8])))).toList() : List.of());
    }

    public static Statement<List<Good>> goods() {
        return new Statement<>("""
                SELECT "Waren-ID", Name, Preis, Bestand, Temperatur, Platzbedarf
                FROM Ware;""", result -> result.succeeded() ? Arrays.stream(result.getRows()).map(array -> new Good(Integer.parseInt(array[0]), array[1], new Price(Double.parseDouble(array[2])), Integer.parseInt(array[3]), Temperature.of(array[4]), Double.parseDouble(array[5]))).toList() : List.of());
    }

    public static Statement<Boolean> order(int customer, int good, int amount, LocalDate orderDate, LocalDate pickupDate) {
        return new Statement<>(String.format("""
                INSERT INTO bestellt ("Kunden-ID", "Waren-ID", Menge, Bestelldatum, Abholdatum)
                VALUES ("%s", "%s", "%s", "%s", "%s")""", customer, good, amount, orderDate.toString(), pickupDate.toString()), MyQueryResult::succeeded);
    }

    public static Statement<Boolean> deleteCustomer(int id) {
        return new Statement<>(String.format("""
                DELETE FROM "Großkunde"
                WHERE "Kunden-ID" = %s;""", id), MyQueryResult::succeeded);
    }

    public static Statement<Boolean> updateCustomerData(int id, SimpleCustomerData simpleCustomerData) {
        return new Statement<>(String.format("""
                UPDATE "Großkunde"
                SET Name = "%s", Ort = "%s", "Straße" = "%s", Passwort = "%s"
                WHERE "Kunden-ID" = "%s";""", simpleCustomerData.name(), simpleCustomerData.city(), simpleCustomerData.street(), simpleCustomerData.password().hashCode(), id), MyQueryResult::succeeded);
    }

    public static Statement<Boolean> registerCustomer(SimpleCustomerData simpleCustomerData) {
        return new Statement<>(String.format("""
                INSERT INTO "Großkunde" (Name, Ort, "Straße", Passwort, Registrierungsdatum)
                VALUES ("%s", "%s", "%s", "%s", "%s");""", simpleCustomerData.name(), simpleCustomerData.city(), simpleCustomerData.street(), simpleCustomerData.password().hashCode(), LocalDate.now()), MyQueryResult::succeeded);
    }

    public static Statement<Boolean> deleteGood(int id) {
        return new Statement<>(String.format("""
                DELETE FROM Ware
                WHERE "Waren-ID" = %s;""", id), MyQueryResult::succeeded);
    }

    public static Statement<Boolean> updateGoodNameAndPrice(int id, EditableGoodData editableGoodData) {
        return new Statement<>(String.format("""
                UPDATE Ware
                SET Name = "%s", Preis = "%s", Bestand = "%s"
                WHERE "Waren-ID" = "%s";""", editableGoodData.name(), editableGoodData.price().value(), editableGoodData.stock(), id), MyQueryResult::succeeded);
    }

    public static Statement<Boolean> createGood(SimpleGoodData simpleGoodData) {
        return new Statement<>(String.format("""
                INSERT INTO Ware (Name, Preis, Temperatur, Platzbedarf)
                VALUES ("%s", "%s", "%s", "%s");""", simpleGoodData.name(), simpleGoodData.price().value(), simpleGoodData.temperature().id(), simpleGoodData.requiredSpace()), MyQueryResult::succeeded);
    }

    public static Statement<Optional<StaffData>> staffData(int id) {
        return new Statement<>(String.format("""
                SELECT this."Mitarbeiter-ID", this.Vorname, this.Nachname, this.Geburtsdatum, this.Ort, this."Straße", this.Wochenstunden, this.Gehalt, this.Aufgabenbereich, this.Vorgesetzter, other.Vorname, other.Nachname
                FROM Mitarbeiter AS this
                LEFT OUTER JOIN Mitarbeiter AS other
                ON this.Vorgesetzter = other."Mitarbeiter-ID"
                WHERE this."Mitarbeiter-ID" = %s;""", id), result -> result.succeeded() && result.getRows().length == 1 && result.getRows()[0].length == 12 ? Optional.of(new StaffData(Integer.parseInt(result.getRows()[0][0]), new Name(result.getRows()[0][1], result.getRows()[0][2]), new FormattedDate(result.getRows()[0][3]), result.getRows()[0][4], result.getRows()[0][5], Integer.parseInt(result.getRows()[0][6]), Integer.parseInt(result.getRows()[0][7]), result.getRows()[0][8], result.getRows()[0][9] == null ? 0 : Integer.parseInt(result.getRows()[0][9]), result.getRows()[0][9] == null ? null : new Name(result.getRows()[0][10], result.getRows()[0][11]))) : Optional.empty());
    }

    public static Statement<List<StaffData>> staffs() {
        return new Statement<>("""
                SELECT this."Mitarbeiter-ID", this.Vorname, this.Nachname, this.Geburtsdatum, this.Ort, this."Straße", this.Wochenstunden, this.Gehalt, this.Aufgabenbereich, this.Vorgesetzter, other.Vorname, other.Nachname
                FROM Mitarbeiter AS this
                LEFT OUTER JOIN Mitarbeiter AS other
                ON this.Vorgesetzter = other."Mitarbeiter-ID";""", result -> result.succeeded() ? Arrays.stream(result.getRows()).map(array -> new StaffData(Integer.parseInt(array[0]), new Name(array[1], array[2]), new FormattedDate(array[3]), array[4], array[5], Integer.parseInt(array[6]), Integer.parseInt(array[7]), array[8], array[9] == null ? 0 : Integer.parseInt(array[9]), array[9] == null ? null : new Name(array[10], array[11]))).toList() : List.of());
    }

    public static Statement<Boolean> deleteStaff(int id) {
        return new Statement<>(String.format("""
                DELETE FROM Mitarbeiter
                WHERE "Mitarbeiter-ID" = %s;""", id), MyQueryResult::succeeded);
    }

    public static Statement<Boolean> updateStaff(int id, StaffData staffData) {
        return new Statement<>(String.format("""
                UPDATE Mitarbeiter
                SET Vorname = "%s", Nachname = "%s", Geburtsdatum = "%s", Ort = "%s", Straße = "%s", Wochenstunden = "%s", Gehalt = "%s", Aufgabenbereich = "%s", Vorgesetzter = %s
                WHERE "Mitarbeiter-ID" = "%s";""", staffData.name().firstname(), staffData.name().lastname().orElseThrow(), staffData.birthday().unformatted(), staffData.city(), staffData.street(), staffData.weeklyHours(), staffData.salary(), staffData.taskType(), staffData.supervisorId() == 0 ? "NULL" : staffData.supervisorId(), id), MyQueryResult::succeeded);
    }

    public static Statement<Boolean> updateStaffPasswort(int id, String password) {
        return new Statement<>(String.format("""
                UPDATE Mitarbeiter
                SET Passwort = "%s"
                WHERE "Mitarbeiter-ID" = "%s";""", password.hashCode(), id), MyQueryResult::succeeded);
    }

    public static Statement<Boolean> registerStaff(StaffData staffData, String password) {
        return new Statement<>(String.format("""
                INSERT INTO Mitarbeiter (Vorname, Nachname, Geburtsdatum, Ort, Straße, Wochenstunden, Gehalt, Aufgabenbereich, Vorgesetzter, Passwort)
                VALUES ("%s", "%s", "%s", "%s", "%s", "%s", "%s", "%s", %s, "%s");""", staffData.name().firstname(), staffData.name().lastname().orElseThrow(), staffData.birthday().unformatted(), staffData.city(), staffData.street(), staffData.weeklyHours(), staffData.salary(), staffData.taskType(), staffData.supervisorId() == 0 ? "NULL" : staffData.supervisorId(), password.hashCode()), MyQueryResult::succeeded);
    }


    public static Statement<Optional<Integer>> rackId(int x, int y) {
        return new Statement<>(String.format("""
                SELECT "Regal-ID"
                FROM Regal
                WHERE Spalte = "%s" AND Reihe = "%s";""", x, y), result -> result.succeeded() && result.getRows().length == 1 && result.getRows()[0].length == 1 ? Optional.of(Integer.parseInt(result.getRows()[0][0])) : Optional.empty());
    }

    public static Statement<List<GoodInRack>> goodsInRack(int id) {
        return new Statement<>(String.format("""
                SELECT "Enthält-ID", "enthält"."Waren-ID", Name, Menge, (Menge * Platzbedarf)
                FROM "enthält"
                INNER JOIN Ware
                ON Ware."Waren-ID" = "enthält"."Waren-ID"
                WHERE "enthält"."Regal-ID" = "%s";""", id), result -> result.succeeded() ? Arrays.stream(result.getRows()).map(array -> new GoodInRack(Integer.parseInt(array[0]), new GoodRef(Integer.parseInt(array[1]), array[2]), Integer.parseInt(array[3]), Double.parseDouble(array[4]))).toList() : List.of());
    }

    public static Statement<Optional<Rack>> rack(int id) {
        return new Statement<>(String.format("""
                SELECT Spalte, Reihe, "Kapazität", Temperatur
                FROM Regal
                WHERE "Regal-ID" = "%s";""", id), result -> result.succeeded() && result.getRows().length == 1 && result.getRows()[0].length == 4 ? Optional.of(new Rack(Integer.parseInt(result.getRows()[0][0]), Integer.parseInt(result.getRows()[0][1]), Integer.parseInt(result.getRows()[0][2]), Temperature.of(result.getRows()[0][3]))) : Optional.empty());
    }

    public static Statement<Boolean> deleteGoodInRack(int id) {
        return new Statement<>(String.format("""
                DELETE FROM "enthält"
                WHERE "Enthält-ID" = "%s";""", id), MyQueryResult::succeeded);
    }

    public static Statement<Boolean> putGoodInRack(int rackId, int goodId, int amount) {
        return new Statement<>(String.format("""
                INSERT INTO "enthält" ("Regal-ID", "Waren-ID", Menge)
                VALUES ("%s", "%s", "%s");""", rackId, goodId, amount), MyQueryResult::succeeded);
    }

    public static Statement<Boolean> deleteRack(int id) {
        return new Statement<>(String.format("""
                DELETE FROM Regal
                WHERE "Regal-ID" = "%s";""", id), MyQueryResult::succeeded);
    }

    public static Statement<Boolean> createRack(Rack rack) {
        return new Statement<>(String.format("""
                INSERT INTO Regal (Spalte, Reihe, "Kapazität", Temperatur)
                VALUES ("%s", "%s", "%s", "%s");""", rack.x(), rack.y(), rack.capacity(), rack.temperature().id()), MyQueryResult::succeeded);
    }

    public static Statement<Boolean> moveRack(int rackId, int x, int y) {
        return new Statement<>(String.format("""
                UPDATE Regal
                SET Reihe = "%s", Spalte = "%s"
                WHERE "Regal-ID" = "%s";""", x, y, rackId), MyQueryResult::succeeded);
    }

    public static Statement<Boolean> reduceGoodInRack(int id, int amount) {
        return new Statement<>(String.format("""
                UPDATE "enthält"
                SET Menge = Menge - "%s"
                WHERE "Enthält-ID" = "%s";""", amount, id), MyQueryResult::succeeded);
    }
}
