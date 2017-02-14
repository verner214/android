package com.appkonst.repete;

import android.content.Intent;
import android.os.PersistableBundle;
import android.support.v4.app.FragmentActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

public class MainActivity extends FragmentActivity implements QALab.OnModelChanged {
    Button btnSessionStart;
    Button btnCommented;
    LinearLayout llArea2;
    RadioGroup rdgrArea1;
    TextView txtDebug;

    MainActivity that;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.d("oncreate", "-------------------------------------------");
        setContentView(R.layout.activity_main);
        //vid null, se till att starta filinläsning
        if (QALab.get(this) == null) {
            QALab.loadFile(this);//kommer att anropa updateUI async
        }
        else if (QALab.get(this).dataExists())
        {
            updateUI(savedInstanceState.getString("area1"));
        }
    }//onCreate

    @Override
    public void onSaveInstanceState(Bundle outState, PersistableBundle outPersistentState) {
        super.onSaveInstanceState(outState, outPersistentState);
        //spara text för ikryssad radiobutton
    }

    public void updateUI(String area1) {
        RadioGroup group = (RadioGroup) findViewById(R.id.rdgrArea1);
        CheckBox chkBox;
        RadioButton button;
        llArea2 = (LinearLayout) findViewById(R.id.llArea2);

        for(int i = 0; i < 10; i++) {
            button = new RadioButton(this);
            button.setId(i);
            button.setText("Button " + i);
            group.addView(button);
            chkBox = new CheckBox(this);
            chkBox.setText("chkBox" + i);
            llArea2.addView(chkBox);
            chkBox.setId(i + 256);
        }
        btnSessionStart = (Button) findViewById(R.id.btnSessionStart);
        btnSessionStart.setText("btnSessionStart");
        that = this;
        btnSessionStart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(that, QuestionActivity.class);
                startActivity(i);
            }
        });
        btnCommented = (Button) findViewById(R.id.btnCommented);
        btnCommented.setText("btnCommented");

        that = this;
        btnCommented.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(that, QuestionActivity.class);
                startActivity(i);
            }
        });
    }//updateUI

    public void progressMsg(String message) {
        txtDebug.setText(txtDebug.getText() + "-" + message);
    }
}
        /*
        String logmsg = "bundle = ";
        if (savedInstanceState != null) {
            for (String key : savedInstanceState.keySet()) {
                logmsg += key + ":" + savedInstanceState.get(key) + ";";
                //Log.d ("myApplication", key + " is a key in the bundle");
            }
        }
        Toast.makeText(this, logmsg, Toast.LENGTH_LONG).show();
*/
