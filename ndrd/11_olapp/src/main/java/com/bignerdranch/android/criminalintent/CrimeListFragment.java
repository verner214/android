package com.bignerdranch.android.criminalintent;

import java.util.ArrayList;

import android.content.Intent;

import android.graphics.Bitmap;
import android.os.Bundle;
import android.os.AsyncTask;
import android.os.Handler;

import android.support.v4.app.ListFragment;

import android.view.View;
import android.view.ViewGroup;

import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.ImageView;

public class CrimeListFragment extends ListFragment {
    private ArrayList<Crime> mCrimes;
    ThumbnailDownloader<ImageView> mThumbnailThread;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getActivity().setTitle(R.string.crimes_title);
        mCrimes = CrimeLab.get(getActivity()).getCrimes();
//        CrimeAdapter adapter = new CrimeAdapter(mCrimes);
//        setListAdapter(adapter);
//        setListAdapter(mCrimes);//först sätter vi med ordinarie crimes
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
        Crime c = ((CrimeAdapter)getListAdapter()).getItem(position);
        // start an instance of CrimePagerActivity
        Intent i = new Intent(getActivity(), CrimePagerActivity.class);
        i.putExtra(CrimeFragment.EXTRA_CRIME_ID, c.getId());
        startActivityForResult(i, 0);
    }
    
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        ((CrimeAdapter)getListAdapter()).notifyDataSetChanged();
    }

    private class FetchItemsTask extends AsyncTask<Void,Void,ArrayList<Crime>> {
        @Override
        protected ArrayList<Crime> doInBackground(Void... params) {
            return new GetGson().fetchItems();
        }

        @Override
        protected void onPostExecute(ArrayList<Crime> crimes) {
            //try {Thread.sleep(5000);} catch (InterruptedException e) {}
            mCrimes = crimes;
            CrimeAdapter adapter = new CrimeAdapter(mCrimes);
            setListAdapter(adapter);
		    CrimeLab.setCrimes(crimes);
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
    
    private class CrimeAdapter extends ArrayAdapter<Crime> {
        public CrimeAdapter(ArrayList<Crime> crimes) {
            super(getActivity(), android.R.layout.simple_list_item_1, crimes);
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            // if we weren't given a view, inflate one
            if (null == convertView) {
                convertView = getActivity().getLayoutInflater()
                    .inflate(R.layout.list_item_crime, null);
            }

            // configure the view for this Crime
            Crime c = getItem(position);

            ImageView imageView = (ImageView)convertView
                    .findViewById(R.id.gallery_item_imageView);
            imageView.setImageResource(R.drawable.no_photo);
            
            mThumbnailThread.queueThumbnail(imageView, c.getTitle());//använd title tills vidare, byt sen.

            TextView titleTextView =
                (TextView)convertView.findViewById(R.id.crime_list_item_titleTextView);
            titleTextView.setText(c.getTitle());
            TextView dateTextView =
                (TextView)convertView.findViewById(R.id.crime_list_item_dateTextView);
            dateTextView.setText(c.getDate().toString());
            CheckBox solvedCheckBox =
                (CheckBox)convertView.findViewById(R.id.crime_list_item_solvedCheckBox);
            solvedCheckBox.setChecked(c.isSolved());

            return convertView;
        }
    }
}

