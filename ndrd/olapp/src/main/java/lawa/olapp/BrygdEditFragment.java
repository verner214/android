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

public class BrygdEditFragment extends Fragment {
    private final static String TAG = "BrygdEditFragment";
    static final int REQUEST_IMAGE_GET = 1;
    static final int RESULT_BRYGD_SAVED = Activity.RESULT_FIRST_USER;

//vid edit hämtas mBrygd via Bundle. vid new skapas den här
    Brygd mBrygd; 

    Button mBtnSelectPhoto;
    Button mBtnSave;
    ImageView mImgThumbnail;
    ImageView mImgLarge;
    EditText mBeerName;
    EditText mBeerStyle;
    EditText mEtxOg;
    EditText mEtxFg;
    EditText mEtxDescription;
    EditText mEtxRecipe;
    EditText mEtxComments;
    EditText mEtxBrewingDate;
    EditText mEtxPeople;
    EditText mEtxPlace;
    CheckBox mChkHide;
    
    Bitmap mBmpLarge;
    Bitmap mBmpThumbnail;
    
    boolean mEdit = false;
    ProgressDialog progress;
    
    public static BrygdEditFragment newInstance(String brygdId) {
        Bundle args = new Bundle();
        args.putSerializable(BrygdFragment.EXTRA_BRYGD_ID, brygdId);
        BrygdEditFragment fragment = new BrygdEditFragment();
        fragment.setArguments(args);
        return fragment;
    }
    
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);

        Bundle args = getArguments();
        if (args != null) {
            String brygdId = (String) args.getSerializable(BrygdFragment.EXTRA_BRYGD_ID);
            mBrygd = BrygdLab.get(getActivity()).getBrygd(brygdId);
            mEdit = true;
        }
    }
    
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup parent, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_brygd_edit, parent, false);
                
        mBtnSelectPhoto = (Button)v.findViewById(R.id.btnSelectPhoto);
        mBtnSelectPhoto.setText("välj bild");
        mBtnSelectPhoto.setEnabled(true);
        mBtnSelectPhoto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //mBtnSave.setEnabled(true);
                selectImage();
            }
        });

        mBtnSave = (Button)v.findViewById(R.id.btnSave);
        //mBtnSave.setEnabled(false);
        mBtnSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                saveForm();
            }
        });

        mImgThumbnail = (ImageView) v.findViewById(R.id.imgThumbnail);
        mImgThumbnail.setImageResource(R.drawable.no_photo);
        
        mImgLarge = (ImageView) v.findViewById(R.id.imgLarge);

        mBeerName = (EditText) v.findViewById(R.id.beername);
        mBeerStyle = (EditText) v.findViewById(R.id.beerstyle);
        mEtxOg = (EditText) v.findViewById(R.id.etxOg);
        mEtxFg = (EditText) v.findViewById(R.id.etxFg);
        mEtxDescription = (EditText) v.findViewById(R.id.etxDescription);
        mEtxRecipe = (EditText) v.findViewById(R.id.etxRecipe);
        mEtxComments = (EditText) v.findViewById(R.id.etxComments);
        mEtxBrewingDate = (EditText) v.findViewById(R.id.etxBrewingDate);
        mEtxPeople = (EditText) v.findViewById(R.id.etxPeople);
        mEtxPlace = (EditText) v.findViewById(R.id.etxPlace);
        
        mChkHide = (CheckBox)v.findViewById(R.id.chkHide);
//        mSolvedCheckBox.setChecked(mCrime.isSolved());
        
        if (mBrygd != null) {
            mBeerName.setText(mBrygd.getBeerName());
            mBeerStyle.setText(mBrygd.getBeerStyle());
            mEtxOg.setText(mBrygd.getOg());
            mEtxFg.setText(mBrygd.getFg());
            mEtxDescription.setText(mBrygd.getDescription());
            mEtxRecipe.setText(mBrygd.getRecipe());
            mEtxComments.setText(mBrygd.getComments());
            mEtxBrewingDate.setText(mBrygd.getBrewingDate());
            mEtxPeople.setText(mBrygd.getPeople());
            mEtxPlace.setText(mBrygd.getPlace());
            mChkHide.setChecked(mBrygd.getHide());
        }
        
        Log.d(TAG, "onCreateView lawa");
        
        return v; 
    }
        
    private void selectImage() {
        Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
        intent.setType("image/*");
        if (intent.resolveActivity(getActivity().getPackageManager()) != null) {
            startActivityForResult(intent, REQUEST_IMAGE_GET);
        }
    }
    
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        try {
            if (requestCode == REQUEST_IMAGE_GET && resultCode == Activity.RESULT_OK) {
                //Bitmap thumbnail = data.getParcelableExtra("data");     
                Uri selectedImageURI = data.getData();
                boolean b2 = selectedImageURI == null;
//                InputStream inputStream = getActivity().getContentResolver().openInputStream(selectedImageURI);
//                Bitmap bmp = BitmapFactory.decodeStream(inputStream);
                
                mBmpLarge = ImageLibrary.Uri2Bmp(getActivity(), selectedImageURI, 640, 360, true);
                boolean b = mBmpLarge == null;
                
                //Bitmap bmp2 = Bitmap.createScaledBitmap(bmp, 100, 100, true);
                Toast.makeText(getActivity(), "onActivityResult=" + b2 + "," + b, Toast.LENGTH_SHORT).show();
                mImgLarge.setImageBitmap(mBmpLarge);
                mBmpThumbnail = ImageLibrary.createThumbnail(mBmpLarge, 100, 100);
                mImgThumbnail.setImageBitmap(mBmpThumbnail);
                
//skapa jpg och skicka till azure
/*
                Form form = new Form();
                form.imgLarge = ImageLibrary.Bmp2Jpg(bmp, 90);
                form.imgThumbnail = ImageLibrary.Bmp2Jpg(bmThumb, 90);
                new FetchItemsTask().execute(form);
*/
/*                
                ByteArrayOutputStream os = new ByteArrayOutputStream();
                bmp.compress(Bitmap.CompressFormat.JPEG, 70, os);
                new FetchItemsTask().execute(os.toByteArray());
*/
//dessa tre länkar handlar om hur man får tag i filnamnet om man har en URI. kom på att man inte behöver filnamnet vilket ger en mycket snyggare lösning.
//http://stackoverflow.com/questions/2169649/get-pick-an-image-from-androids-built-in-gallery-app-programmatically?lq=1
//http://stackoverflow.com/questions/20067508/get-real-path-from-uri-android-kitkat-new-storage-access-framework?rq=1
//http://stackoverflow.com/questions/19834842/android-gallery-on-kitkat-returns-different-uri-for-intent-action-get-content

//https://developer.android.com/guide/components/intents-common.html#Storage
//http://stackoverflow.com/questions/5309190/android-pick-images-from-gallery
//man kan alltså använda content resolver istället direkt mot URI.

            }//if
        } catch (Exception e) {
            Toast.makeText(getActivity(), "exception=" + e.toString(), Toast.LENGTH_LONG).show();
            Log.e(TAG, "Error downloading image", e);
        }
//2do: skär bilden och skala med metod som ger bra kvalitet. bra länk från sonymobile som går ut på att man ska skala redan när man decodar inputstream.
//då behöver man inte heller lika mycket minne och algortitmen som används ger bilder av bra kvalitet. man måste dock skala igen när bilden är hämtad, 
//man kan inte skala till godtycklig upplösning, http://developer.sonymobile.com/2011/06/27/how-to-scale-images-for-your-android-application/        
//ungefär samma sak i länken nedan, läs siamii's svar
//http://stackoverflow.com/questions/2507898/how-to-pick-an-image-from-gallery-sd-card-for-my-app?rq=1

//SAMSUNG. mobiler från denna tillverkare stöter tydligen ibland på problem med att data.getData() returnerar null. värt att testa på samsung med andra ord.
//http://stackoverflow.com/questions/14627900/camera-intent-data-null-in-onactivityresultint-requestcode-int-resultcode-int
    
//vid beskärning av bilden, använd inte CROP-intent, http://commonsware.com/blog/2013/01/23/no-android-does-not-have-crop-intent.html    
    
    }//onActivityResult

    private void saveForm() {
// Note: declare ProgressDialog progress as a field in your class.

        progress = ProgressDialog.show(getActivity(), "Brygd sparas", "vänta...", true);
          
//vid new brygd skapa Brygd-instance 
        if (mBrygd == null) {
            mBrygd = new Brygd("");//brygd utan id  betyder new Brygd
        }
//populera mBrygd        
        mBrygd.setBeerName(mBeerName.getText().toString());
        mBrygd.setBeerStyle(mBeerStyle.getText().toString());
        mBrygd.setOg(mEtxOg.getText().toString());
        mBrygd.setFg(mEtxFg.getText().toString());
        mBrygd.setDescription(mEtxDescription.getText().toString());
        mBrygd.setRecipe(mEtxRecipe.getText().toString());
        mBrygd.setComments(mEtxComments.getText().toString());
        mBrygd.setBrewingDate(mEtxBrewingDate.getText().toString());
        mBrygd.setPeople(mEtxPeople.getText().toString());
        mBrygd.setPlace(mEtxPlace.getText().toString());
        mBrygd.setHide(mChkHide.isChecked());
//ide' låt hide visas för inloggade dvs admins

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
                getActivity().setResult(BrygdEditFragment.RESULT_BRYGD_SAVED);                    
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


