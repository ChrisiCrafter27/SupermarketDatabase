package supermarketdatabase.util;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

public class FormattedDate implements Comparable<FormattedDate> {
    public static final DateTimeFormatter FORMATTER = DateTimeFormatter.ofPattern("dd.MM.yyyy");
    
    private final LocalDate parent;
    private final DateTimeFormatter formatter;

    public FormattedDate(String date) {
        this(LocalDate.parse(date), FORMATTER);
    }

    public FormattedDate(LocalDate parent, DateTimeFormatter formatter) {
        this.parent = parent;
        this.formatter = formatter;
    }

    @Override
    public int compareTo(FormattedDate o) {
        return parent.compareTo(o.parent);
    }

    @Override
    public String toString() {
        return parent.format(formatter);
    }

    public String unformatted() {
        return parent.toString();
    }

    public static FormattedDate parseFormatted(String date) {
        return new FormattedDate(LocalDate.parse(date, FORMATTER), FORMATTER);
    }
}
