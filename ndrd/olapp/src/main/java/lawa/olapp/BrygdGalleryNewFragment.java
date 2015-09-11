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
    ScalingImageView mImg;
    //ImageView mImgLarge;
    EditText mEtxDescription;
    
    Bitmap mBmpLarge;
    Bitmap mBmpThumbnail;
    
    Uri mImgUri;
    ProgressDialog progress;
    
    public static BrygdGalleryNewFragment newInstance(Uri imgUri) {
        Bundle args = new Bundle();
        args.putSerializable(BrygdFragment.EXTRA_GALLERY_URI, imgUri);
        BrygdGalleryNewFragment fragment = new BrygdGalleryNewFragment();
        fragment.setArguments(args);
        return fragment;
    }
    
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);

        Bundle args = getArguments();
        mImgUri = (Uri) args.getSerializable(BrygdFragment.EXTRA_GALLERY_URI);
    }
    
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup parent, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_brygd_gallery_new, parent, false);
                
        mBtnSave = (Button)v.findViewById(R.id.btnSave);
        mBtnSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //saveForm();
            }
        });

        mImgThumbnail = (ImageView) v.findViewById(R.id.imgThumbnail);
        mImgThumbnail.setImageResource(R.drawable.no_photo);
        
        mImgLarge = (ImageView) v.findViewById(R.id.imgLarge);

        mEtxDescription = (EditText) v.findViewById(R.id.etxDescription);

        mBmpLarge = ImageLibrary.Uri2Bmp(getActivity(), selectedImageURI, 640, 360, true);
        Toast.makeText(getActivity(), "onActivityResult", Toast.LENGTH_SHORT).show();

        mImgLarge.setImageBitmap(mBmpLarge);
        mBmpThumbnail = ImageLibrary.createThumbnail(mBmpLarge, 100, 100);
        mImgThumbnail.setImageBitmap(mBmpThumbnail);

        return v; 
    }
            
    private void saveForm() {
// Note: declare ProgressDialog progress as a field in your class.

        progress = ProgressDialog.show(getActivity(), "Brygd sparas", "vänta...", true);
          
        Form form = null;
        if (mImgLarge.getDrawable() == null) {
            form = new Form(mBrygd, null, null);
        } else {
            form = new Form(mBrygd, ImageLibrary.Bmp2Jpg(mBmpLarge, 90), ImageLibrary.Bmp2Jpg(mBmpThumbnail, 90));
        }
        
//        form.imgLarge = ImageLibrary.Bmp2Jpg(mBmpLarge, 90);
//        form.imgThumbnail = ImageLibrary.Bmp2Jpg(mBmpThumbnail, 90);
        new PostFormTask().execute(form);        
    }

    private class PostFormTask extends AsyncTask<Form,Void,String> {
        @Override
        protected String doInBackground(Form... forms) {
            return MultiPart.PostForm(forms[0]);
//            return bytes;      
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
                             
//lägg till villkor att vid edit så ska det inte göras finish
//setResult som visar för anropande aktivitet/fragment att data behöver läsas in på nytt. 
//listan ska vid lyckad save (dvs result=null) hämta ny lista, uppdatera BrygdLab och setAdapter, kanske visa toast som tv.nu

//todo sen
//edit: här: läsa in värden som vid BrygdFragment i onCreate / onCreateView.
//edit: onActivityResult: anropa aktivity, dvs pager, via listener registrerad i onAttach.
//detta anrop gör fetchitems async och setAdapter och currentItem.
//sen: visa endast icke-hide.
//spara alla thumbnails i listan som bmp i minnet.
//gör fin gui.
//--version 0.1
//lru-cahce och disk cache.
//picture gallary 
                getActivity().setResult(BrygdFragment.RESULT_BRYGD_SAVED);                    
                if (!mEdit) {
                    //Intent resultIntent = new Intent();
                    //getActivity().setResult(Activity.RESULT_OK, resultIntent);
                    //getActivity().setResult(BrygdEditFragment.RESULT_BRYGD_SAVED);                    
                    getActivity().finish();
                }
            }//if (getActivity() != null) {
        }//onPostExecute
        
    }//FetchItemsTask

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
