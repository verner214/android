package lawa.olapp;

import java.util.ArrayList;
import java.util.UUID;
import android.app.Activity;
import android.os.Bundle;
import android.view.Menu;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import android.os.Bundle;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.content.SharedPreferences;
import android.support.v4.view.ViewPager;

public class AskPasswordActivity extends Activity  {

    Button btnPassword;
    Button btnDemo;
    EditText etxPassword;
        
    private void setTable(String table) {
        SharedPreferences.Editor editor = getSharedPreferences(BrygdListFragment.MY_PREFS_NAME, MODE_PRIVATE).edit();
        editor.putString("table", table);
        editor.commit();        
    }    
        
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ask_password);

        etxPassword = (EditText) findViewById(R.id.etxPassword);

        btnPassword = (Button) findViewById(R.id.btnPassword);
        btnPassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (etxPassword.getText().toString().equals("n11")) {
                    setTable("ejdemo");
                    setResult(Activity.RESULT_OK);                    
                    getActivity().finish();
                }
            }
        });

        btnDemo = (Button) findViewById(R.id.btnDemo);		
        btnDemo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setTable("demo");
                setResult(Activity.RESULT_OK);                    
                getActivity().finish();
            }
        });
    }
}//AskPasswordActivity
