package lawa.olapp;

import java.util.UUID;
import java.io.IOException;
import java.io.InputStream;
import java.io.ByteArrayOutputStream;

import android.content.Intent;
import android.content.pm.PackageManager;
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
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Log;
import android.app.Activity;
import android.app.ProgressDialog;
import android.widget.Toast;
import android.net.Uri;

public class BrygdGalleryNewFragment extends Fragment {
    private final static String TAG = "BrygdGalleryNewFragment";

    Button mBtnSave;
    ScalingImageView mImgLarge;    
    //ImageView mImgLarge;
    EditText mEtxDescription;
    
    Bitmap mBmpLarge;
    
    Uri mImgUri;
    String mBrygdId;
    
    ProgressDialog progress;
    
    public static BrygdGalleryNewFragment newInstance(String brygdId, String imgUri) {
        Bundle args = new Bundle();
        args.putSerializable(BrygdFragment.EXTRA_GALLERY_URI, imgUri);
        args.putSerializable(BrygdFragment.EXTRA_BRYGD_ID, brygdId);
        BrygdGalleryNewFragment fragment = new BrygdGalleryNewFragment();
        fragment.setArguments(args);
        return fragment;
    }
    
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);

        Bundle args = getArguments();
        mImgUri = (Uri) Uri.parse((String) args.getSerializable(BrygdFragment.EXTRA_GALLERY_URI));
        mBrygdId = (String) args.getSerializable(BrygdFragment.EXTRA_BRYGD_ID);
    }
    
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup parent, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_brygd_gallery_new, parent, false);
                
        mBtnSave = (Button)v.findViewById(R.id.btnSave);
        mBtnSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                saveForm();
            }
        });

        mImgLarge = (ScalingImageView) v.findViewById(R.id.img);
        mEtxDescription = (EditText) v.findViewById(R.id.etxDescription);

        mBmpLarge = ImageLibrary.Uri2Bmp(getActivity(), mImgUri, 640, 360, true);
        Toast.makeText(getActivity(), "onActivityResult", Toast.LENGTH_SHORT).show();

        mImgLarge.setImageBitmap(mBmpLarge);

        return v; 
    }
            
    private void saveForm() {
        progress = ProgressDialog.show(getActivity(), "Brygd sparas", "vänta...", true);
        Bitmap bmpThumbnail = ImageLibrary.createThumbnail(mBmpLarge, 100, 100);
          
        FormGallery form = new FormGallery(
            mBrygdId, 
            ImageLibrary.Bmp2Jpg(mBmpLarge, 90), 
            ImageLibrary.Bmp2Jpg(bmpThumbnail, 90), 
            mEtxDescription.getText().toString()
        );
        
        new PostFormTask().execute(form);
    }

    private class PostFormTask extends AsyncTask<FormGallery,Void,String> {
        @Override
        protected String doInBackground(FormGallery... forms) {
            return MultiPart.PostFormGallery(forms[0]);
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

}//class BrygdEditFragment


/*--------------------
här-onCreate:
  hämta bild motsvarande uri

här-sparaklick:
  anropa formmetod som sparar bilder och text. återvänd som i BrygdEditFragment.


--------------------
i BrygdFragment:
  vid klick på ny galleryitem -> starta intent som väljer bild och returnerar uri.
  
onActivityResult
  från bildväljare ovan:  
    om uri inte null -> starta BrygdGalleryNewActivity via intent och sätt Uri som intent parameter
  från BrygdGalleryNewActivity:
    gör som vid ok från BrygdEditActivity.
	
	
------------------	
sen:
1. visa gallery,
2. pager med bildfragments
3. meny för edit och edit.
4. roterande fragment ovan.
*/
