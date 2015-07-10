package lawa.olapp;

import java.util.UUID;
import java.io.IOException;

import android.annotation.TargetApi;
import android.os.Bundle;
import android.os.AsyncTask;
import android.support.v4.app.Fragment;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Log;

public class BrygdFragment extends Fragment {
    public static final String EXTRA_BRYGD_ID = "olapp.BRYGD_ID";
    private final static String TAG = "BrygdFragment";

    Brygd mBrygd;
    TextView mBeerName;
    TextView mBeerStyle;
    ImageView mImg;
    
    //ta reda på: varför kan man inte sätta mBrygd här?
    public static BrygdFragment newInstance(String brygdId) {
        Bundle args = new Bundle();
        args.putSerializable(EXTRA_BRYGD_ID, brygdId);

        BrygdFragment fragment = new BrygdFragment();
        fragment.setArguments(args);

        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        String brygdId = (String) getArguments().getSerializable(EXTRA_BRYGD_ID);
        mBrygd = BrygdLab.get(getActivity()).getBrygd(brygdId);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup parent, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_brygd, parent, false);
        
        mBeerName = (TextView)v.findViewById(R.id.beername);
        mBeerName.setText(mBrygd.getBeerName());
        
        mBeerStyle = (TextView)v.findViewById(R.id.beerstyle);
        mBeerStyle.setText(mBrygd.getBeerStyle());
                        
        mImg = (ImageView) v.findViewById(R.id.img);
        mImg.setImageResource(R.drawable.no_photo);
        if (mBrygd.getImgUrl() != null) {        
            new FetchItemsTask().execute(mBrygd.getImgUrl());
        }
        
        return v; 
    }
    
    private class FetchItemsTask extends AsyncTask<String,Void,byte[]> {
        @Override
        protected byte[] doInBackground(String... urls) {
            byte[] bytes = null; 
            try {
                bytes = GetGson.getUrlBytes(urls[0]);
            } catch (IOException ioe) {
                Log.e(TAG, "Error downloading image", ioe);
            }          
            return bytes;      
        }

        @Override
        protected void onPostExecute(byte[] bitmapBytes) {
            final Bitmap bitmap = BitmapFactory.decodeByteArray(bitmapBytes, 0, bitmapBytes.length);
            if (isVisible()) {
                mImg.setImageBitmap(bitmap);
            }
        }
    }

    
}
