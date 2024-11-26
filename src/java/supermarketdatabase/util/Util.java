package supermarketdatabase.util;

import java.io.PrintWriter;
import java.io.StringWriter;

public class Util {
    public static String exceptionString(Exception e) {
        StringWriter stringWriter = new StringWriter();
        PrintWriter printWriter = new PrintWriter(stringWriter);
        e.printStackTrace(printWriter);
        return stringWriter.toString();
    }
}
