package com.appkonst.repete;

import java.util.UUID;
import java.io.IOException;
import java.io.InputStream;
import java.io.ByteArrayOutputStream;
import java.lang.Thread;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.annotation.TargetApi;
import android.os.Bundle;
import android.os.AsyncTask;
import android.support.v4.app.Fragment;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.util.Log;
import android.app.Activity;
import android.app.ProgressDialog;
import android.widget.Toast;
import android.net.Uri;
import android.app.Application;
import java.lang.Thread.UncaughtExceptionHandler;
import android.content.SharedPreferences;
import android.os.Build;

public class RepeteApplication extends Application {
private class CustomExceptionHandler implements UncaughtExceptionHandler {

    private Boolean exceptionOccured = false;
    private RepeteApplication _app;
    private UncaughtExceptionHandler _defaultEH; 

    public CustomExceptionHandler(RepeteApplication ac) {
        _defaultEH = Thread.getDefaultUncaughtExceptionHandler();
        _app = ac;
    }

    @Override
    public void uncaughtException(Thread thread, final Throwable e) {
        if (exceptionOccured) {//logga inte fel i felet
            _defaultEH.uncaughtException(thread, e);
            return;
        }
        exceptionOccured = true;
         
        StackTraceElement[] arr = e.getStackTrace();
        final StringBuffer report = new StringBuffer(e.toString());
        final String lineSeperator = "-------------------------------\n\n";
        report.append("--------- Stack trace ---------\n\n");
        for (int i = 0; i < arr.length; i++) {
            report.append( "    ");
            report.append(arr[i].toString());
            report.append("\n");
        }
        report.append(lineSeperator);
        // If the exception was thrown in a background thread inside
        // AsyncTask, then the actual exception can be found with getCause
        report.append("--------- Cause ---------\n\n");
        Throwable cause = e.getCause();
        if (cause != null) {
            report.append(cause.toString());
            report.append("\n\n");
            arr = cause.getStackTrace();
            for (int i = 0; i < arr.length; i++) {
                report.append("    ");
                report.append(arr[i].toString());
                report.append("\n");
            }
        }
        report.append(lineSeperator);

        Log.e("Report ::", report.toString());

        SharedPreferences.Editor editor = getSharedPreferences(MainActivity.MY_PREFS_NAME, MODE_PRIVATE).edit();
        
        editor.putString("stacktrace", report.toString());
        editor.putBoolean("error", true);
        editor.commit();        

        //MultiPart.sendLogAsync(report.toString());
//        Toast.makeText(_app, "Delivering log...", Toast.LENGTH_LONG).show();
/*        
        StringWriter sw = new StringWriter();
        PrintWriter pw  = new PrintWriter(sw);
        ex.printStackTrace(pw);
        String exStr    = sw.toString();
        ExceptionServer.getInstance().deliverMessageAsync(exStr, _app);
*/        
 //System.exit(0);
        _defaultEH.uncaughtException(thread, e);
    }

}
    @Override
    public void onCreate() {
        super.onCreate();
        Thread.setDefaultUncaughtExceptionHandler(new CustomExceptionHandler(this));
        //MultiPart.sendLogAsync("");
    }
}
