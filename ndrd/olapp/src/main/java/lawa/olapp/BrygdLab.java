package lawa.olapp;

import java.util.ArrayList;
//import java.util.UUID;

import android.content.Context;

public class BrygdLab {
    private ArrayList<Brygd> mBrygds;
    private static Boolean mTable;

    private static BrygdLab sBrygdLab;
    private Context mAppContext;

    private BrygdLab(Context appContext) {
        mAppContext = appContext;
    }

    public static BrygdLab get(Context c) {
        if (sBrygdLab == null) {
            sBrygdLab = new BrygdLab(c.getApplicationContext());
            OlAppApplication appState = ((OlAppApplication) c.getApplicationContext());
            appState.setSingleton(sBrygdLab);
        }
        return sBrygdLab;
    }

    public Brygd getBrygd(String id) {
        for (Brygd c : mBrygds) {
            if (c.getId().equals(id))
                return c;
        }
        return null;
    }
    
    public ArrayList<Brygd> getBrygds() {
        return mBrygds;
    }
    
    private void setBrygdsInstance(ArrayList<Brygd> brygds) {
        mBrygds = brygds;
    }
    //exception om den anropas innan get.
    public static void setBrygds(ArrayList<Brygd> brygds) {
        sBrygdLab.setBrygdsInstance(brygds);
    }
    
    public static void setSourceIsDemo(Boolean demo) {
        mTable = demo;
    }
    
    public static Boolean getSourceIsDemo() {
        return mTable;
    }
    
}

