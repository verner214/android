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
    private final static String KEY_AREA1_ID = "KEY_AREA1_ID";
    private final static String TAG = "BrygdEditFragment";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.d(TAG, "onCreate -------------------------------------------");
        setContentView(R.layout.activity_main);
        //vid null har get aldrig anropats och singleton ej initierats. då skapas singleton samtidigt som fil hämtas och läses in.
        //när detta är klart anropas callback i denna aktivitet (updataUI)
        if (QALab.get(this) == null) {
            QALab.loadFile(this);//kommer att anropa updateUI async
            Log.d(TAG, "onCreate 2-------------------------------------------");
        }
        //dataExists() returnerar false mellan singleton start och filinläsning klar. skulle aktiviteten startas om under denna tid ska updateUI inte anropas. aktivity hamnar då i dött läge. (man får rotera helt enkelt)
        else if (QALab.get(this).dataExists())
        {
            Log.d(TAG, "onCreate 3------------------------------------------- " + savedInstanceState.getInt("KEY_AREA1_ID", -1));
            updateUI(savedInstanceState.getInt("KEY_AREA1_ID", -1));
        }
    }//onCreate

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        //spara text för ikryssad radiobutton
        rdgrArea1 = (RadioGroup) findViewById(R.id.rdgrArea1);
        int id = rdgrArea1.getCheckedRadioButtonId();
        if (id != -1) {
            outState.putInt("KEY_AREA1_ID", id);
            Log.d(TAG, "saveState 3 -------------------------------------------" + id);
        }
    }
    //hämtar area2 strängar och skapar checkboxar
    private void createArea2(int area1Id) {
        RadioButton rb = (RadioButton) findViewById(area1Id);
        int i = 0;
        llArea2 = (LinearLayout) findViewById(R.id.llArea2);
        llArea2.removeAllViews();
        for (String s : QALab.get(this).getArea2s(rb.getText().toString())) {
            CheckBox chkBox = new CheckBox(this);
            chkBox.setText(s);
            llArea2.addView(chkBox);
            chkBox.setId(i + 100 * (area1Id + 1));
            i++;
        }
    }
    //area1Id == -1 betyder att ingen radioknapp är nedtryckt. dvs
    public void updateUI(int area1Id) {
        RadioGroup group = (RadioGroup) findViewById(R.id.rdgrArea1);
        CheckBox chkBox;
        RadioButton button;
//visa radioknappar
        int i = 0;
        group.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener()
        {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                createArea2(checkedId);
            }
        });
        group.removeAllViews();
        for (String s : QALab.get(this).getArea1s()) {
            button = new RadioButton(this);
            button.setId(i++);
            button.setText(s);
            group.addView(button);
        }
//och checkboxar för area2 om ngn radioknapp är nedtryckt
        if (area1Id != -1) {
            createArea2(area1Id);
        }

// resten av layouten görs här
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
