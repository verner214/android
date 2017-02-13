package com.appkonst.repete;

import android.content.Context;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by lars on 2017-02-13.
 */

public class QALab {

    private ArrayList<QAItem> mQAItems;

    private static QALab sQALab;
    private Context mAppContext;

    private QALab(Context appContext) {
        mAppContext = appContext;
    }

    public static QALab get(Context c) {
        if (sQALab == null) {
            sQALab = new QALab(c.getApplicationContext());
            //här hämtar vi fil (om det går) sen parsar json från fil (alltid). det skapar mQAItems
        }
        return sQALab;
    }


    public List<String> getArea1s() {
        return null;
    }
    public List<String> getArea2s(String area1) {
        return null;
    }
    //de tre sista har med en session att göra
    public void startSession(String area1, List<String> area2s) {
    }
    public int Count() {
        return 0;
    }
    public QAItem getQAItem(int pos) {
        return null;
    }

}
