package nl.tudelft.cse1110.andy.utils;

import java.io.PrintWriter;
import java.io.StringWriter;

public class ExceptionUtils {

    public static String exceptionMessage(Throwable e) {
        StringWriter sw = new StringWriter();
        PrintWriter pw = new PrintWriter(sw);
        e.printStackTrace(pw);
        String messageToAppend = sw.toString().trim();
        return messageToAppend;
    }

}
