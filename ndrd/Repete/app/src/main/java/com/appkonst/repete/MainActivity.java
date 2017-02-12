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

public class MainActivity extends FragmentActivity {
    Button btnMain;
    MainActivity that;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.d("oncreate", "-------------------------------------------");
        setContentView(R.layout.activity_main);
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


    }
}
