package com.appkonst.repete;

import android.app.Activity;
import android.content.Context;
import android.widget.EditText;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Created by lars on 2017-02-13.
 */
//tillstånd 1. aldrig refererad (app precis startad) -- 1,2 metod get
    //tillstång 2: singleton skapad
    //tillstånd 3: fil inläst -- metod loadFile
    //tillstånd 4: session skapad -- startSession
public class QALab {

    private ArrayList<QAItem> mQAItems;

    private static QALab sQALab;
    private Context mAppContext;

    // Container Activity must implement this interface
    public interface OnModelChanged {
        public void updateUI(int area1Id);
        public void progressMsg(String msg);
    }

    private QALab(Context appContext) {
        mAppContext = appContext;
    }

    //returnerar null vid första anropet så att man vet det är dags att anropa updateUI.
    public static QALab get(Context c) {
        if (sQALab == null) {
            sQALab = new QALab(c.getApplicationContext());
            return null;
        }
        return sQALab;
    }

    //här hämtar vi fil (om det går) sen parsar json från fil (alltid). det skapar mQAItems
    public static void loadFile(Activity activity) {
        OnModelChanged mCallback;
        try {
            mCallback = (OnModelChanged) activity;
        } catch (ClassCastException e) {
            throw new ClassCastException(activity.toString() + " must implement OnModelChanged");
        }
    }

//returnerar alla distinkta area1 som finns
    public List<String> getArea1s() {
        List<String> s = Arrays.asList("csharp", "js");
        return s;
        //return null;
    }
    //returnerar alla distinkta area2 som finns för ett givet area1
    public List<String> getArea2s(String area1) {
        if (area1.equals("csharp")) {
            //List<String> s = Arrays.asList("area2_1", "area2_2", "area2_3");
            return Arrays.asList("csharp_1", "csharp_2", "csharp_3");
        }
        else if (area1.equals("js")) {
            //List<String> s = Arrays.asList("area2_1", "area2_2", "area2_3");
            return Arrays.asList("js_1", "js_2", "js_3");
        }
        return Arrays.asList("null_1", "null_2", "null_3");
    }
    //sann om filen har lästs in. används innan anrop till updateUI i MainActivity. kan vara falsk om MainActivity startats om efter anrop till get men innan filen har lästs in.
    public boolean dataExists() {
        return mQAItems != null;
    }

    public void startSession(String area1, List<String> area2s) {
    }
    //dessa nedan är endast giltiga om startSession har anropats
    public int Count() {
        return 0;
    }
    public QAItem getQAItem(int pos) {
        return null;
    }

}
