package lawa.olapp;

import java.util.UUID;
import java.io.IOException;
import java.util.ArrayList;
import android.app.ProgressDialog;

import android.content.Intent;
import android.content.Context;
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
import android.view.inputmethod.InputMethodManager;
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
import android.view.inputmethod.InputMethodManager;
import android.content.Context;

public class GalleryFragment extends Fragment {

    private final static String TAG = "GalleryFragment";
    
    Brygd mBrygd;
    int mPostition;
    OnGalleryKeyboardDisplayedListener mCallback;
    
    EditText mText;
    ScalingImageView mImg;
    Button btnSave;
    CheckBox mChkHide;
    TextView mHideLabel;
    ProgressDialog progress;
    
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
    
    // Container Activity must implement this interface
    public interface OnGalleryKeyboardDisplayedListener {
        public void setActiveTextEdit(EditText e);
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        
        // This makes sure that the container activity has implemented
        // the callback interface. If not, it throws an exception
        try {
            mCallback = (OnGalleryKeyboardDisplayedListener) activity;
        } catch (ClassCastException e) {
            throw new ClassCastException(activity.toString() + " must implement OnGalleryKeyboardDisplayedListener");
        }
    }
        
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup parent, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_gallery, parent, false);
        btnSave = (Button)v.findViewById(R.id.btnSave);
        mChkHide = (CheckBox)v.findViewById(R.id.chkHide);
        mHideLabel = (TextView)v.findViewById(R.id.hideLabel);
        
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
                    InputMethodManager imm = (InputMethodManager) getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
                    if (imm != null) {
                        imm.showSoftInput(mText, InputMethodManager.SHOW_IMPLICIT);
                        mCallback.setActiveTextEdit(mText);
                    }        
                    btnSave.setVisibility(View.VISIBLE);                    
                    mChkHide.setVisibility(View.VISIBLE);                  
                    mHideLabel.setVisibility(View.VISIBLE);  
                }
            });
            
            btnSave.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    saveForm();
                }
            });
        }

        mImg = (ScalingImageView) v.findViewById(R.id.img);
        ImgCacheParam imgP = new ImgCacheParam(getActivity().getExternalCacheDir(), mBrygd.getGalleryItem(mPostition).getImgURL());
        new FetchItemsTask().execute(imgP);
            

        return v; 
    }//onCreateView
    
    private void saveForm() {
        progress = ProgressDialog.show(getActivity(), "Texten sparas", "vänta...", true);
          
        FormGalleryEdit form = new FormGalleryEdit (
            mBrygd.getId(), 
            mBrygd.getGalleryItem(mPostition).getImgURL(), 
            mText.getText().toString(),
            mChkHide.isChecked()
        );        
        new PostFormTask().execute(form);
        
        InputMethodManager imm = (InputMethodManager) getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
        if (imm != null) {
            imm.hideSoftInputFromWindow(mText.getWindowToken(), 0);    
        }        
    }

    private class PostFormTask extends AsyncTask<FormGalleryEdit,Void,String> {
        @Override
        protected String doInBackground(FormGalleryEdit... forms) {
            return MultiPart.PostFormGalleryEdit(forms[0]);
        }

        @Override
        protected void onPostExecute(String result) {
//ta bort busy dialog om den syns
            if (getActivity() != null) {
                progress.dismiss();
                if (result != null) {
                    Toast.makeText(getActivity(), "Något gick fel...\n " + result, Toast.LENGTH_LONG).show();
                    return;
                }
                             
                getActivity().setResult(BrygdFragment.RESULT_BRYGD_SAVED);                    
                getActivity().finish();
            }
        }//onPostExecute        
    }//PostFormTask
    
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
