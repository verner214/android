package com.appkonst.repete;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
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

import java.util.ArrayList;

public class MainActivity extends FragmentActivity implements QALab.OnModelChanged {
    Button btnSessionStart;
    Button btnCommented;
    LinearLayout llArea2;
    RadioGroup rdgrArea1;
    TextView txtDebug;

    MainActivity that;
    private final static String KEY_AREA1_ID = "KEY_AREA1_ID";
    private final static String TAG = "MainActivity";
    private final static int REQUEST_ERROR_REPORT = Activity.RESULT_FIRST_USER;
    public static final String MY_PREFS_NAME = "MyPrefsFile";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        WriteToFile.writeToFile(TAG, "onCreate --------");
        Log.d(TAG, "onCreate -- savedInstanceState == null ? " + (savedInstanceState == null ? "yes" : "no"));
        setContentView(R.layout.activity_main);

//om fel förra gången, visa det sen avsluta
        SharedPreferences prefs = this.getSharedPreferences(MY_PREFS_NAME, Context.MODE_PRIVATE);
        String table = prefs.getString("table", null);
        WriteToFile.writeToFile(TAG, "table = " + table);
        Log.d(TAG, "table = " + table);
        if (prefs.getBoolean("error", false)) {//fel förra gången?
            WriteToFile.writeToFile(TAG, "error = " + table);
            Log.d(TAG, "error == true ");
            Intent i = new Intent(this, ErrorReportActivity.class);
            startActivityForResult(i, REQUEST_ERROR_REPORT);
            //getActivity().finish();
            WriteToFile.writeToFile(TAG, "efter startErrorReport innan return");
            Log.d(TAG, "efter startErrorReport innan return");
            return;
        }

        //vid null har get aldrig anropats och singleton ej initierats. då skapas singleton samtidigt som fil hämtas och läses in.
        //när detta är klart anropas callback i denna aktivitet (updataUI)
        if (savedInstanceState == null) {//savedInstanceState är null när appen startas om. filen kan ändå vara inläst då QALab inte nödvändigtvis GC-ats och alltså finns i minnet. vid omstart ska docj filen alltid läsas in (på nytt)
            QALab.loadFile(this);//kommer att anropa updateUI async
            Log.d(TAG, "onCreate hämta fil och läs in i array -------------------------------------------");
        }
        //dataExists() returnerar false mellan singleton start och filinläsning klar. skulle aktiviteten startas om under denna tid ska updateUI inte anropas. aktivity hamnar då i dött läge. (man får rotera helt enkelt)
        else //rotation etc.
        {
            Log.d(TAG, "QALab.dataExists() dvs fil redan hämtad dvs inte först onCreate för denna session");
            Log.d(TAG, "onCreate, ------------------------------------------- " + savedInstanceState.getInt("KEY_AREA1_ID", -1));
            updateUI(savedInstanceState.getInt("KEY_AREA1_ID", -1));
        }
    }//onCreate

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        WriteToFile.writeToFile(TAG, "resultCode=" + resultCode);
        Log.d(TAG, "onActivityResult, resultCode=" + resultCode);
        if (requestCode == REQUEST_ERROR_REPORT) {
            finish();
        }
    }

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
        for (String s : QALab.getArea2s(rb.getText().toString())) {
            CheckBox chkBox = new CheckBox(this);
            chkBox.setText(s);
            llArea2.addView(chkBox);
            //chkBox.setId(i + 100 * (area1Id + 1));
            chkBox.setId(getChkId(area1Id, i));
            i++;
        }
    }
    private int getChkId(int area1Id, int cnt) {
        return cnt + 100 * (area1Id + 1);
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
        for (String s : QALab.getArea1s()) {
            button = new RadioButton(this);
            Log.d(TAG, "button id, text = " + i + "," + s);
            button.setId(i++);
            String s1 = new String(s);//annars får alla radioknappar samma text
            button.setText(s1);
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
                rdgrArea1 = (RadioGroup) findViewById(R.id.rdgrArea1);
                RadioButton rb = (RadioButton) findViewById(rdgrArea1.getCheckedRadioButtonId());
                ArrayList<String> area2s = new ArrayList<String>();
                ArrayList<String> area2sAll = new ArrayList<String>();
                for (int i = 0; true; i++) {
                    CheckBox cb = (CheckBox) findViewById(getChkId(rb.getId(), i));
                    if (cb == null) {
                        break;
                    }
                    area2sAll.add(cb.getText().toString());
                    if (cb.isChecked()) {
                        area2s.add(cb.getText().toString());
                    }
                }
                if (area2s.size() == 0) {//om ingen checkad, gör som om alla vore checkade
                    QALab.startSession(rb.getText().toString(), area2sAll);
                } else {
                    QALab.startSession(rb.getText().toString(), area2s);
                }
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
                QALab.startSessionComments();
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
