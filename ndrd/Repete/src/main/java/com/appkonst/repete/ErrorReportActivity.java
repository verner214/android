package com.appkonst.repete;

import java.util.ArrayList;
import java.util.UUID;
import android.app.Activity;
import android.os.Bundle;
import android.view.Menu;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.EditText;
import android.content.Context;
import android.os.Bundle;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.content.SharedPreferences;
import android.support.v4.view.ViewPager;

import com.appkonst.repete.MainActivity;

public class ErrorReportActivity extends Activity  {

    TextView txtText;
        
        
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_error_report);

        txtText = (TextView) findViewById(R.id.txtText);
        SharedPreferences prefs = getSharedPreferences(MainActivity.MY_PREFS_NAME, Context.MODE_PRIVATE);
        txtText.setText(prefs.getString("stacktrace", null));

        SharedPreferences.Editor editor = getSharedPreferences(MainActivity.MY_PREFS_NAME, MODE_PRIVATE).edit();
        editor.putBoolean("error", false);
        editor.commit();        
    }
}//AskPasswordActivity
