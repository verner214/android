package lawa.olapp;

import java.util.UUID;
import java.io.IOException;
import java.util.ArrayList;

import android.content.Intent;
import android.annotation.TargetApi;
import android.os.Bundle;
import android.os.AsyncTask;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ArrayAdapter;
import android.widget.GridView;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Log;
import android.app.Activity;
import android.net.Uri;

public class GalleryFragment extends Fragment {

    private final static String TAG = "GalleryFragment";

    Brygd mBrygd;
    int mPostition;
    
    EditText mText;
    ScalingImageView mImg;
    Button btnSave;
    
    public static GalleryFragment newInstance(String brygdId, int position) {
        Bundle args = new Bundle();
        args.putSerializable(BrygdFragment.EXTRA_BRYGD_ID, brygdId);
        args.putSerializable(BrygdFragment.EXTRA_GALLERY_POSITION, position);

        GalleryFragment fragment = new GalleryFragment();
        fragment.setArguments(args);

        return fragment;
    }
    
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//        setHasOptionsMenu(true);
        String brygdId = (String) getArguments().getSerializable(BrygdFragment.EXTRA_BRYGD_ID);
        mBrygd = BrygdLab.get(getActivity()).getBrygd(brygdId);
        mPostition = (int) getArguments().getSerializable(BrygdFragment.EXTRA_GALLERY_POSITION);
    }
    
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup parent, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_gallery, parent, false);
        
        mText = (EditText)v.findViewById(R.id.text);
        if (mText != null) {
            mText.setText(mBrygd.getGalleryItem(mPostition).getText());
            mText.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    //mBtnSave.setEnabled(true);
                    //mText.setEnabled(true);
                    mText.setFocusableInTouchMode(true);
                    mText.requestFocus();                    
                }
            });
            btnSave = (Button)v.findViewById(R.id.btnSave);
            btnSave.setVisibility(View.VISIBLE);
            /*
            btnSave.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    selectImage();
                }
            });
            */
        }

        mImg = (ScalingImageView) v.findViewById(R.id.img);
        ImgCacheParam imgP = new ImgCacheParam(getActivity().getExternalCacheDir(), mBrygd.getGalleryItem(mPostition).getImgURL());
        new FetchItemsTask().execute(imgP);
            
        return v; 
    }
    
/*    
    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        inflater.inflate(R.menu.fragment_brygd, menu);
        Log.d(TAG, "onCreateOptionsMenu");
    }
    
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            
            case R.id.menu_item_edit_brygd:
                Log.d(TAG, "menu_item_edit_brygd");
    
                Intent i = new Intent(getActivity(), BrygdEditActivity.class);
                i.putExtra(BrygdFragment.EXTRA_BRYGD_ID, mBrygd.getId());
                startActivityForResult(i, EDIT_BEER);                
                return true;
                
            default:
                return super.onOptionsItemSelected(item);
        } 
    }
*/
    //hämtar toppbilden. appens cachedir och bildens url skickas med så får 
    //det senare avgöras om den ska hämtas från nätet eller om den finns i cachen.
    private class FetchItemsTask extends AsyncTask<ImgCacheParam,Void,byte[]> {
        @Override
        protected byte[] doInBackground(ImgCacheParam... imgCacheParams) {
            byte[] bytes = null; 
            try {
                bytes = GetGson.getUrlBytes(imgCacheParams[0].getCacheDir(), imgCacheParams[0].getUrl());
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
