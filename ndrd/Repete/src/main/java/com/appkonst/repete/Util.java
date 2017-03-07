package com.appkonst.repete;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;

/**
 * Created by lars on 2017-02-13.
 */
//http och sånt
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
