package lawa.olapp;

import java.util.ArrayList;

import android.content.Intent;

import android.graphics.Bitmap;
import android.os.Bundle;
import android.os.AsyncTask;
import android.os.Handler;

import android.support.v4.app.ListFragment;

import android.view.View;
import android.view.ViewGroup;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.content.Context;

import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.ImageView;
import android.widget.Toast;
import android.util.Log;
import android.app.Activity;
import android.widget.AdapterView;  
import android.content.SharedPreferences;

public class BrygdListFragment extends ListFragment {
    private final static String TAG = "BrygdListFragment";
    public static final String MY_PREFS_NAME = "MyPrefsFile";
    
    private final static int ADD_BEER = 1;
    private final static int ASK_PASSWORD = 2;
    private final static int ERROR_REPORT = 3;
    
    private ArrayList<Brygd> mBrygds;
    ThumbnailDownloader<ImageView> mThumbnailThread;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        WriteToFile.writeToFile(TAG, "onCreate 1");
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
        getActivity().setTitle(R.string.brygds_title);
        
//se om användaren har valt källa an
//gör: onActivityResult-ta emot och fetcha eller avsluta
//AskPasswordActivity-fråga efter table name och spara i prefs samt setResult.
        SharedPreferences prefs = getActivity().getSharedPreferences(MY_PREFS_NAME, Context.MODE_PRIVATE); 
        String table = prefs.getString("table", null);
        if (prefs.getBoolean("error", false)) {//fel förra gången?
            Intent i = new Intent(getActivity(), ErrorReportActivity.class);
            startActivityForResult(i, ERROR_REPORT);
            //getActivity().finish();
            //return;
        }
        else if (table == null) {//fråga efter password
            Intent i = new Intent(getActivity(), AskPasswordActivity.class);
            startActivityForResult(i, ASK_PASSWORD);
        }
        else {//hämta data
        WriteToFile.writeToFile(TAG, "onCreate 2");
            BrygdLab.setSourceIsDemo(table.equals("demo"));
        WriteToFile.writeToFile(TAG, "onCreate 3");
            FetchItemsTask ft = new FetchItemsTask();
        WriteToFile.writeToFile(TAG, "onCreate 4");
            //ft.execute();      
            ft.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);     
        WriteToFile.writeToFile(TAG, "onCreate 5");
        }

        WriteToFile.writeToFile(TAG, "innan BrygdLab.get(getActivity()).getBrygds()");                
        mBrygds = BrygdLab.get(getActivity()).getBrygds();//mBrygds kommer alltid att vara null här men måste anropas innan BrygdLab.set...
        setListAdapter(null);//visa timglas
        WriteToFile.writeToFile(TAG, "efter BrygdLab.get(getActivity()).getBrygds()");                
        
//initiera bakgrundstråden med loopern. i konstruktorn skicka Handler som är associerad till UI-tråden.
//registrera callback för downloaded vertig.        

        mThumbnailThread = new ThumbnailDownloader<ImageView>(getActivity().getExternalCacheDir(), new Handler());
        mThumbnailThread.setListener(new ThumbnailDownloader.Listener<ImageView>() {
            public void onThumbnailDownloaded(ImageView imageView, Bitmap thumbnail) {
                if (isVisible()) {
                    imageView.setImageBitmap(thumbnail);
                }
            }
        });
        mThumbnailThread.start();
        mThumbnailThread.getLooper();    
        WriteToFile.writeToFile(TAG, "efter mThumbnailThread.getLooper(); ");             
    }

    @Override
    public void onListItemClick(ListView l, View v, int position, long id) {
//dessa två rader ovan är ändrade för att felsöka repete
        // get the Crime from the adapter
        Brygd c = ((BrygdAdapter)getListAdapter()).getItem(position);
        // start an instance of CrimePagerActivity
        Intent i = new Intent(getActivity(), BrygdPagerActivity.class);
        i.putExtra(BrygdFragment.EXTRA_BRYGD_ID, c.getId());
        startActivityForResult(i, 0);
    }
    
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        WriteToFile.writeToFile(TAG, "i onActivityResult 1, requestCode = " + requestCode + ", resultCode = " + resultCode);             
        if (requestCode == ERROR_REPORT) {
            getActivity().finish();//döda hela appen.
        } 
        else if (requestCode == ASK_PASSWORD) {
            if (resultCode == Activity.RESULT_CANCELED) {
        WriteToFile.writeToFile(TAG, "i onActivityResult 2, requestCode = " + requestCode + ", resultCode = " + resultCode);             
                getActivity().finish();//döda hela appen.
            } 
            else {
        WriteToFile.writeToFile(TAG, "i onActivityResult 3, requestCode = " + requestCode + ", resultCode = " + resultCode);             
                SharedPreferences prefs = getActivity().getSharedPreferences(MY_PREFS_NAME, Context.MODE_PRIVATE); 
        WriteToFile.writeToFile(TAG, "i onActivityResult 4, requestCode = " + requestCode + ", resultCode = " + resultCode);             
                String table = prefs.getString("table", null);
        WriteToFile.writeToFile(TAG, "i onActivityResult 5, requestCode = " + requestCode + ", resultCode = " + resultCode);             
                BrygdLab.setSourceIsDemo(table.equals("demo"));
        WriteToFile.writeToFile(TAG, "i onActivityResult 6, requestCode = " + requestCode + ", resultCode = " + resultCode);      
                FetchItemsTask ft = new FetchItemsTask();
                //new FetchItemsTask().execute();
        WriteToFile.writeToFile(TAG, "i onActivityResult 7, requestCode = " + requestCode + ", resultCode = " + resultCode);   
        //ft.execute();     
        ft.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);     
        WriteToFile.writeToFile(TAG, "i onActivityResult 8, requestCode = " + requestCode + ", resultCode = " + resultCode);   
            }
            //return;
        }

//            if (requestCode == ADD_BEER && resultCode == Activity.RESULT_OK) {
        //Activity.RESULT_FIRST_USER är om brygd har sparats.
        //Toast.makeText(getActivity(), "onActivityResult" + requestCode + "," + resultCode + "," + data, Toast.LENGTH_LONG).show();
//om ny brygd har sparats. dvs valt ny brygd i action bar och sedan sparat innan back-knappen tryckts.
        else if (resultCode == BrygdFragment.RESULT_BRYGD_SAVED) {
            //new FetchItemsTask().execute();
            new FetchItemsTask().executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR); 
        }
//om en brygd har editerats så har även modellen lästs in på nytt, i så fall uppdatera.
        else {
            ArrayList<Brygd> mBrygds2 = BrygdLab.get(getActivity()).getBrygds();
            if (mBrygds != mBrygds2) {
                Log.d(TAG, "inte samma brygds, call notifyDataSetChanged");
                mBrygds = mBrygds2;
                setListAdapter(new BrygdAdapter(mBrygds2));
//                ((BrygdAdapter)getListAdapter()).notifyDataSetChanged();
            }
        }        
    }

    private class FetchItemsTask extends AsyncTask<Void,Void,ArrayList<Brygd>> {
        @Override
        protected ArrayList<Brygd> doInBackground(Void... params) {
            WriteToFile.writeToFile(TAG, "FetchItemsTask.doInBackground 1");             
            ArrayList<Brygd> a = new GetGson().fetchItems();
            WriteToFile.writeToFile(TAG, "FetchItemsTask.doInBackground 2");             
            return a;
        }

        @Override
        protected void onPostExecute(ArrayList<Brygd> brygds) {
            //try {Thread.sleep(5000);} catch (InterruptedException e) {}
            WriteToFile.writeToFile(TAG, "FetchItemsTask.onPostExecute 1");             
            Log.d(TAG, "onPostExecute, brygds.length = " + brygds.size());
            mBrygds = brygds;
            BrygdAdapter adapter = new BrygdAdapter(mBrygds);
            WriteToFile.writeToFile(TAG, "FetchItemsTask.onPostExecute 2");             
            setListAdapter(adapter);
            WriteToFile.writeToFile(TAG, "FetchItemsTask.onPostExecute 3");             
            Log.d(TAG, "onPostExecute, nu är adaptern satt, getCount = " + adapter.getCount());
		    BrygdLab.setBrygds(brygds);
            WriteToFile.writeToFile(TAG, "FetchItemsTask.onPostExecute 4");             
/*            
            if (getActivity() != null) {
                Toast.makeText(getActivity(), "Öllistan uppdaterad.", Toast.LENGTH_LONG).show();
            }
*/            
        }
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        inflater.inflate(R.menu.fragment_brygd_list, menu);
        Log.d(TAG, "onCreateOptionsMenu");
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            
            case R.id.menu_item_new_brygd:
            Log.d(TAG, "menu_item_new_brygd");

                Intent i = new Intent(getActivity(), BrygdEditActivity.class);
//                i.putExtra(CrimeFragment.EXTRA_CRIME_ID, crime.getId());
                startActivityForResult(i, ADD_BEER);
                
                return true;
                
            default:
                return super.onOptionsItemSelected(item);
        } 
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        mThumbnailThread.quit();
    }
    
    @Override
    public void onDestroyView() {
        super.onDestroyView();
        mThumbnailThread.clearQueue();
    }
    
    private class BrygdAdapter extends ArrayAdapter<Brygd> {
        public BrygdAdapter(ArrayList<Brygd> brygds) {
            super(getActivity(), android.R.layout.simple_list_item_1, brygds);
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            // if we weren't given a view, inflate one
            WriteToFile.writeToFile(TAG, "BrygdAdapter.getView 1");             
            if (null == convertView) {
                convertView = getActivity().getLayoutInflater()
                    .inflate(R.layout.list_item_brygd, null);
            }

            // configure the view for this Crime
            Log.d(TAG, "getView position = " + position);
            Log.d(TAG, "getCount = " + getCount());
            Brygd c = getItem(position);
            Log.d(TAG, "thumbURL = " + c.getThumbUrl());
            
            ImageView imageView = (ImageView)convertView
                    .findViewById(R.id.gallery_item_imageView);
            imageView.setImageResource(R.drawable.no_photo);
            
            WriteToFile.writeToFile(TAG, "BrygdAdapter.getView 2");             
            mThumbnailThread.queueThumbnail(imageView, c.getThumbUrl());//använd title tills vidare, byt sen.

            TextView beerName =
                (TextView)convertView.findViewById(R.id.brygd_list_beername);
            beerName.setText(c.getBeerName());
            
            TextView beerStyle =
                (TextView)convertView.findViewById(R.id.brygd_list_beerstyle);
            beerStyle.setText(c.getBeerStyle());
            
            WriteToFile.writeToFile(TAG, "BrygdAdapter.getView 3");             
            return convertView;
        }
    }
}

