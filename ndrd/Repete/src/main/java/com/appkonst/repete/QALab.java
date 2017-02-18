package com.appkonst.repete;

import android.app.Activity;
import android.content.Context;
import android.os.AsyncTask;
import android.support.annotation.NonNull;
import android.util.Log;
import android.widget.EditText;

import com.google.gson.Gson;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;

/**
 * Created by lars on 2017-02-13.
 */
//tillstånd 1. aldrig refererad (app precis startad) -- 1,2 metod get
    //tillstång 2: singleton skapad
    //tillstånd 3: fil inläst -- metod loadFile
    //tillstånd 4: session skapad -- startSession
public class QALab {

    private static ArrayList<QAItem> mQAItems;
    private final static String TAG = "BrygdEditFragment";
    private static boolean fileIsRequested = false;
    private static OnModelChanged mCallback;
    private static Context mAppContext;


    // Container Activity must implement this interface
    public interface OnModelChanged {
        public void updateUI(int area1Id);
        public void progressMsg(String msg);
    }

    public static boolean FileIsRequested() {
        Log.d(TAG, "FileIsRequested -------------------------------------------");
        return fileIsRequested;
    }

    private static String getStringFromFile(Context ctx, String filename) throws Exception {
        Log.d(TAG, "getStringFromFile -------------------------------------------");
        FileInputStream in = ctx.openFileInput(filename);
        BufferedReader reader = new BufferedReader(new InputStreamReader(in));
        StringBuilder sb = new StringBuilder();
        String line = null;
        while ((line = reader.readLine()) != null) {
            sb.append(line).append("\n");
        }
        reader.close();
        in.close();
        return sb.toString();
    }
    private static class FetchItemsTask extends AsyncTask<Void,Void,Void> {
        @Override
        protected Void doInBackground(Void... params) {
            Log.d(TAG, "doInBackground -------------------------------------------");
            try {
                String string = new HTTP().GET("https://portalvhdsgfh152bhy290k.table.core.windows.net/tblrepete?st=2017-02-08T20%3A34%3A21Z&se=2036-02-14T08%3A54%3A21Z&sp=r&sv=2014-02-14&tn=tblrepete&sig=HMFUBRLCbQbegxPB3X%2FC5O2%2FbbKe2P%2Fp9GNShPvIRvw%3D");

                FileOutputStream outputStream = mAppContext.openFileOutput("jsonFile", Context.MODE_PRIVATE);
                outputStream.write(string.getBytes());
                outputStream.close();
            } catch (Exception e) {
                e.printStackTrace();//logga detta på något sätt, kanske särbehandla no connection.
            }
            //parsa fil till objekt som sparas i mQAItems
            String jsonstring = null;
            try {
                jsonstring = getStringFromFile(mAppContext, "jsonFile");
            } catch (Exception e) {
                e.printStackTrace();//alla fel måste fårngas i java
            }
            mQAItems = Jsonify.String2Json(jsonstring);
            return null;
        }

        @Override
        protected void onPostExecute(Void param) {
            Log.d(TAG, "onPostExecute -------------------------------------------");
            mCallback.updateUI(-1);
        }
    }

    //här hämtar vi fil (om det går) sen parsar json från fil (alltid). det skapar mQAItems
    public static void loadFile(Activity activity) {
        Log.d(TAG, "loadFile -------------------------------------------");
        fileIsRequested = true;
        mAppContext = activity.getApplicationContext();
        try {
            mCallback = (OnModelChanged) activity;

        } catch (ClassCastException e) {
            throw new ClassCastException(activity.toString() + " must implement OnModelChanged");
        }
        new FetchItemsTask().executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
        //spara filen - om ej uppkopplad fånga felet och fortsätt med parsning av befintlig fil
    }

//returnerar alla distinkta area1 som finns
    public static List<String> getArea1s() {
        //List<String> s = Arrays.asList("csharp", "js");
        List<String> ls = new ArrayList<String>();
        for (QAItem qa : mQAItems) {
            Log.d(TAG, "getArea1s -------------------------------------------" + qa.getArea1());
            ls.add(qa.getArea1());
        }
        return ls;
        //return null;
    }
    //returnerar alla distinkta area2 som finns för ett givet area1
    public static List<String> getArea2s(String area1) {
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
    public static boolean dataExists() {
        Log.d(TAG, "dataExists");
        //return mQAItems != null;
        return true;
    }

    public static void startSession(String area1, List<String> area2s) {
    }
    //dessa nedan är endast giltiga om startSession har anropats
    public static int Count() {
        return 0;
    }
    public static QAItem getQAItem(int pos) {
        return null;
    }

}
