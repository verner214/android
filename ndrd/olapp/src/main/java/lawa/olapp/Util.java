package lawa.olapp;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;

/**
 * Created by lars on 2017-03-19.
 */

public class Util {
    public static String exceptionStacktraceToString(Exception e)
    {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        PrintStream ps = new PrintStream(baos);
        e.printStackTrace(ps);
        ps.close();
        return baos.toString();
    }
}
