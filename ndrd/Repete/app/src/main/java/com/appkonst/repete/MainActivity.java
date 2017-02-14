package com.appkonst.repete;

import android.app.Activity;
import android.content.Intent;
import android.support.v4.app.FragmentActivity;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

public class MainActivity extends FragmentActivity implements QALab.OnModelChanged {
    Button btnMain;
    MainActivity that;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.d("oncreate", "-------------------------------------------");
        setContentView(R.layout.activity_main);
        //vid null, se till att starta filinläsning
        if (QALab.get(this) == null) {
            QALab.loadFile(this);
        } else {
            
        }
        RadioGroup group = (RadioGroup) findViewById(R.id.myRadioGroup);
        RadioButton button;
        for(int i = 0; i < 3; i++) {
            button = new RadioButton(this);
            button.setId(i);
            button.setText("Button " + i);
            group.addView(button);
        }
        btnMain = (Button) findViewById(R.id.btnMain);
        that = this;
        btnMain.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(that, QuestionActivity.class);
                startActivity(i);
            }
        });


    }//onCreate

    public void updateUI(String area1) {
        ;//uppdatera gränssnitt
    }
    public void progressMsg(String message) {
        ;//addera message till view
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
