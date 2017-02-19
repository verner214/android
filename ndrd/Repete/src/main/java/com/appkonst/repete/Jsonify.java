package com.appkonst.repete;

import android.util.Log;

import com.google.gson.Gson;

import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

/**
 * Created by lars on 2017-02-18. borde inte ha fått det namnet...

 RowKey
 questionimg
 answerimg
 area1
 area2
 question
 answer
 comments
 showlevel
 hide

 */

public class Jsonify {

        private class PClass {
            private QAItem[] value;

            private PClass() {
            }

            public PClass(QAItem[] value) {
                this.value = value;
            }

            @Override
            public String toString() {
                StringBuilder sb = new StringBuilder();
                for (QAItem i : value) {
                    sb.append(i.toString() + "\n");
                }

                //sb.append("Name: " + name + " ");
                return sb.toString();
            }

            public QAItem[] getItems() {
                return value;
            }
        }

    public static ArrayList<QAItem> String2Json(String jsonstring) {
        Gson gson = new Gson();

        PClass johnDoe = gson.fromJson(jsonstring, PClass.class);

        ArrayList<QAItem> qas = new ArrayList<QAItem>();

        for (QAItem i : johnDoe.getItems()) {
            qas.add(i);
            //Log.d(TAG,  "brygd = " + c);
        }
//sortera på ölnamn
        Collections.sort(qas, new Comparator<QAItem>() {
            public int compare(QAItem v1, QAItem v2) {
                int c= v1.getArea1().compareTo(v2.getArea1());
                if (c == 0) {
                    String v1a2 = v1.getArea2() == null ? "" : v1.getArea2();
                    String v2a2 = v2.getArea2() == null ? "" : v2.getArea2();
                    return v1a2.compareTo(v2a2);
                }
                return c;
            }
        });
        //WriteToFile.writeToFile(TAG, "ArrayList<Brygd> fetchItems() 3");
        return qas;
    }
}
