package lawa.camera;

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
import android.widget.Toast;
import android.net.Uri;

public class CameraFragment extends Fragment {
    private final static String TAG = "CameraFragment";
    static final int REQUEST_IMAGE_GET = 1;

    Button mBtnSelectPhoto;
    ImageView mImgThumbnail;
    
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup parent, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_camera, parent, false);
                
        mBtnSelectPhoto = (Button)v.findViewById(R.id.btnSelectPhoto);
        mBtnSelectPhoto.setText("välj bild");
        mBtnSelectPhoto.setEnabled(true);
        mBtnSelectPhoto.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                selectImage();
            }
        });

        mImgThumbnail = (ImageView) v.findViewById(R.id.imgThumbnail);
        mImgThumbnail.setImageResource(R.drawable.no_photo);
        
        Log.w("Log", "onCreateView lawa");
        
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
                InputStream inputStream = getActivity().getContentResolver().openInputStream(selectedImageURI);
                Bitmap bmp = BitmapFactory.decodeStream(inputStream);
                boolean b = bmp == null;
                Bitmap bmp2 = Bitmap.createScaledBitmap(bmp, 100, 100, true);
                Toast.makeText(getActivity(), "onActivityResult=" + b2 + "," + b, Toast.LENGTH_SHORT).show();
                mImgThumbnail.setImageBitmap(bmp2);
                
//skapa jpg och skicka till azure                
                ByteArrayOutputStream os = new ByteArrayOutputStream();
                bmp2.compress(Bitmap.CompressFormat.JPEG, 70, os);
                new FetchItemsTask().execute(os.toByteArray());

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

    private class FetchItemsTask extends AsyncTask<byte[],Void,String> {
        @Override
        protected String doInBackground(byte[]... streams) {
            return MultiPart.PostImage(streams[0]);
//            return bytes;      
        }

        @Override
        protected void onPostExecute(String result) {
            Toast.makeText(getActivity(), "result från Multipart = " + result, Toast.LENGTH_LONG).show();
        }
        
    }

}//class CameraFragment


