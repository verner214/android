package lawa.olapp;

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

public class OlAppApplication extends Application {
private class CustomExceptionHandler implements UncaughtExceptionHandler {

    private OlAppApplication                _app;
    private UncaughtExceptionHandler _defaultEH; 

    public CustomExceptionHandler(OlAppApplication ac){

        _defaultEH = Thread.getDefaultUncaughtExceptionHandler();
        _app = ac;
    }

    @Override
    public void uncaughtException(Thread thread, final Throwable ex) {
        SharedPreferences.Editor editor = getSharedPreferences(BrygdListFragment.MY_PREFS_NAME, MODE_PRIVATE).edit();
        editor.putString("stacktrace", ex.toString());
        editor.putBoolean("error", true);
        editor.commit();        

//        Toast.makeText(_app, "Delivering log...", Toast.LENGTH_LONG).show();
/*        
        StringWriter sw = new StringWriter();
        PrintWriter pw  = new PrintWriter(sw);
        ex.printStackTrace(pw);
        String exStr    = sw.toString();
        ExceptionServer.getInstance().deliverMessageAsync(exStr, _app);
*/        
 //System.exit(0);
        _defaultEH.uncaughtException(thread, ex);
    }

}
    @Override
    public void onCreate() {
        super.onCreate();
        Thread.setDefaultUncaughtExceptionHandler(new CustomExceptionHandler(this));
    }
}
