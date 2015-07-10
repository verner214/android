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
import android.util.Log;


public class BrygdListFragment extends ListFragment {
    private final static String TAG = "BrygdListFragment";
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

        mThumbnailThread = new ThumbnailDownloader<ImageView>(new Handler());
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
        ((BrygdAdapter)getListAdapter()).notifyDataSetChanged();
    }

    private class FetchItemsTask extends AsyncTask<Void,Void,ArrayList<Brygd>> {
        @Override
        protected ArrayList<Brygd> doInBackground(Void... params) {
            return new GetGson().fetchItems();
        }

        @Override
        protected void onPostExecute(ArrayList<Brygd> brygds) {
            //try {Thread.sleep(5000);} catch (InterruptedException e) {}
            Log.d(TAG, "NU ÄR VI I onPostExecute_________________________________________________________________");
            Log.d(TAG, "brygds.length = " + brygds.size());
            mBrygds = brygds;
            BrygdAdapter adapter = new BrygdAdapter(mBrygds);
            setListAdapter(adapter);
            Log.d(TAG, "nu är adaptern satt");
            Log.d(TAG, "onPost... getCount = " + adapter.getCount());
		    BrygdLab.setBrygds(brygds);
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
/*
                Crime crime = new Crime();
                CrimeLab.get(getActivity()).addCrime(crime);
                Intent i = new Intent(getActivity(), CrimeActivity.class);
                i.putExtra(CrimeFragment.EXTRA_CRIME_ID, crime.getId());
                startActivityForResult(i, 0);
*/                
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

