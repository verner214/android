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

import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.ImageView;
import android.widget.Toast;
import android.util.Log;
import android.app.Activity;


public class BrygdListFragment extends ListFragment {
    private final static String TAG = "BrygdListFragment";
    private final static int ADD_BEER = 1;
    private ArrayList<Brygd> mBrygds;
    ThumbnailDownloader<ImageView> mThumbnailThread;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
        getActivity().setTitle(R.string.brygds_title);
        mBrygds = BrygdLab.get(getActivity()).getBrygds();
        setListAdapter(null);//visa timglas
        new FetchItemsTask().execute();
        
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
        
    }

    @Override
    public void onListItemClick(ListView l, View v, int position, long id) {
        // get the Crime from the adapter
        Brygd c = ((BrygdAdapter)getListAdapter()).getItem(position);
        // start an instance of CrimePagerActivity
        Intent i = new Intent(getActivity(), BrygdPagerActivity.class);
        i.putExtra(BrygdFragment.EXTRA_BRYGD_ID, c.getId());
        startActivityForResult(i, 0);
    }
    
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
//            if (requestCode == ADD_BEER && resultCode == Activity.RESULT_OK) {
        //Activity.RESULT_FIRST_USER är om brygd har sparats.
        //Toast.makeText(getActivity(), "onActivityResult" + requestCode + "," + resultCode + "," + data, Toast.LENGTH_LONG).show();
//om ny brygd har sparats. dvs valt ny brygd i action bar och sedan sparat innan back-knappen tryckts.
        if (resultCode == BrygdFragment.RESULT_BRYGD_SAVED) {
            new FetchItemsTask().execute();
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
            return new GetGson().fetchItems();
        }

        @Override
        protected void onPostExecute(ArrayList<Brygd> brygds) {
            //try {Thread.sleep(5000);} catch (InterruptedException e) {}
            Log.d(TAG, "onPostExecute, brygds.length = " + brygds.size());
            mBrygds = brygds;
            BrygdAdapter adapter = new BrygdAdapter(mBrygds);
            setListAdapter(adapter);
            Log.d(TAG, "onPostExecute, nu är adaptern satt, getCount = " + adapter.getCount());
		    BrygdLab.setBrygds(brygds);
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
            
            mThumbnailThread.queueThumbnail(imageView, c.getThumbUrl());//använd title tills vidare, byt sen.

            TextView beerName =
                (TextView)convertView.findViewById(R.id.brygd_list_beername);
            beerName.setText(c.getBeerName());
            
            TextView beerStyle =
                (TextView)convertView.findViewById(R.id.brygd_list_beerstyle);
            beerStyle.setText(c.getBeerStyle());
            
            return convertView;
        }
    }
}

