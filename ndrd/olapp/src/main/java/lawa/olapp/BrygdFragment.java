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

public class BrygdFragment extends Fragment {
    OnBrygdsUpdatedListener mCallback;
    ArrayList<String> mItems;
    ThumbnailDownloader<ImageView> mThumbnailThread;

    public static final String EXTRA_BRYGD_ID = "olapp.BRYGD_ID";
    public static final String EXTRA_GALLERY_URI = "olapp.IMGURI";
    private final static String TAG = "BrygdFragment";
    private final static int EDIT_BEER = 1;
    private final static int ADD_GALLERY_ITEM = 2;
    static final int RESULT_BRYGD_SAVED = Activity.RESULT_FIRST_USER;
    static final int REQUEST_IMAGE_GET = Activity.RESULT_FIRST_USER + 1;

    Brygd mBrygd;
    TextView mBeerName;
    TextView mBeerStyle;
    TextView mDescription;
    //matrisen, 3 rader
    TextView mOg;
    TextView mFg;
    TextView mAlc;//beräknad
    TextView mBrewingDate;
    TextView mPeople;
    TextView mPlace;

    TextView mRecipe;
    TextView mComments;

    ScalingImageView mImg;
    Button btnAddImage;
    GridView mGridView;
    
    //ta reda på: varför kan man inte sätta mBrygd här?
    public static BrygdFragment newInstance(String brygdId) {
        Bundle args = new Bundle();
        args.putSerializable(EXTRA_BRYGD_ID, brygdId);

        BrygdFragment fragment = new BrygdFragment();
        fragment.setArguments(args);

        return fragment;
    }

    // Container Activity must implement this interface
    public interface OnBrygdsUpdatedListener {
        public void updatePager();
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        
        // This makes sure that the container activity has implemented
        // the callback interface. If not, it throws an exception
        try {
            mCallback = (OnBrygdsUpdatedListener) activity;
        } catch (ClassCastException e) {
            throw new ClassCastException(activity.toString() + " must implement OnBrygdsUpdatedListener");
        }
    }
    
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);

        mItems = new ArrayList<String>();
        mItems.add("https://portalvhdsgfh152bhy290k.blob.core.windows.net/cntolapp/upload/upload_b89d0ce759b6c7ef68d1d3fbd3dd6e5c.jpg");
        mItems.add("https://portalvhdsgfh152bhy290k.blob.core.windows.net/cntolapp/upload/upload_e0a14c9207807c940f510cf34b4d6436.jpg");
        mItems.add("https://portalvhdsgfh152bhy290k.blob.core.windows.net/cntolapp/upload/upload_e0a14c9207807c940f510cf34b4d6436.jpg");
        mItems.add("https://portalvhdsgfh152bhy290k.blob.core.windows.net/cntolapp/upload/upload_b89d0ce759b6c7ef68d1d3fbd3dd6e5c.jpg");
        mItems.add("https://portalvhdsgfh152bhy290k.blob.core.windows.net/cntolapp/upload/upload_e0a14c9207807c940f510cf34b4d6436.jpg");
        mItems.add("https://portalvhdsgfh152bhy290k.blob.core.windows.net/cntolapp/upload/upload_e0a14c9207807c940f510cf34b4d6436.jpg");
        mItems.add("https://portalvhdsgfh152bhy290k.blob.core.windows.net/cntolapp/upload/upload_b89d0ce759b6c7ef68d1d3fbd3dd6e5c.jpg");
        mItems.add("https://portalvhdsgfh152bhy290k.blob.core.windows.net/cntolapp/upload/upload_e0a14c9207807c940f510cf34b4d6436.jpg");
        mItems.add("https://portalvhdsgfh152bhy290k.blob.core.windows.net/cntolapp/upload/upload_e0a14c9207807c940f510cf34b4d6436.jpg");

//initiera handlerthread
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

        mDescription = (TextView)v.findViewById(R.id.description);
        mDescription.setText(mBrygd.getDescription());
//matrisen        
        mOg = (TextView) v.findViewById(R.id.og);
        mOg.setText(mBrygd.getOg());
        mFg = (TextView) v.findViewById(R.id.fg);
        mFg.setText(mBrygd.getFg());
        try { 
            mAlc = (TextView) v.findViewById(R.id.alc);
            float ffg = Float.parseFloat(mBrygd.getFg());
            float fog = Float.parseFloat(mBrygd.getOg());
            float falc = (fog - ffg) * 1000 / (float) 7.5;
            //mAlc.setText(Float.toString(falc));//beräknad
            mAlc.setText(String.format("%.1f", falc));//beräknad
        } catch (NumberFormatException e) {
            Toast.makeText(getActivity(), e.toString(), Toast.LENGTH_LONG).show();
            //do nothing at all
        }
        mBrewingDate = (TextView) v.findViewById(R.id.brewingDate);
        mBrewingDate.setText(mBrygd.getBrewingDate());
        mPeople = (TextView) v.findViewById(R.id.people);
        mPeople.setText(mBrygd.getPeople());
        mPlace = (TextView) v.findViewById(R.id.place);
        mPlace.setText(mBrygd.getPlace());

        mRecipe = (TextView)v.findViewById(R.id.recipe);
        mRecipe.setText(mBrygd.getRecipe());
        mComments = (TextView) v.findViewById(R.id.comments);
        mComments.setText(mBrygd.getComments());
/*        
        mBeerName.setText(
            mBrygd.getBeerName() + "\n" + 
            mBrygd.getBeerStyle() + "\n" + 
            mBrygd.getOg() + "\n" + 
            mBrygd.getFg() + "\n" + 
            mBrygd.getDescription() + "\n" + 
            mBrygd.getRecipe() + "\n" + 
            mBrygd.getComments() + "\n" + 
            mBrygd.getBrewingDate() + "\n" + 
            mBrygd.getPlace() + "\n" + 
            mBrygd.getPeople() + "\n" + 
            mBrygd.getHide() + "\n" + 
            mBrygd.getPictureGallary() + "\n" + 
            "");
*/        
                        
        mImg = (ScalingImageView) v.findViewById(R.id.img);
        //mImg.setImageResource(R.drawable.no_photo);
        if (mBrygd.getImgUrl() != null) {        
            ImgCacheParam imgP = new ImgCacheParam(getActivity().getExternalCacheDir(), mBrygd.getImgUrl());
            new FetchItemsTask().execute(imgP);
        }
        
        btnAddImage = (Button)v.findViewById(R.id.btnAddImage);
        btnAddImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                selectImage();
            }
        });
        mGridView = (GridView)v.findViewById(R.id.gridView);
        mGridView.setAdapter(new GalleryItemAdapter(mItems));

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

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if ((requestCode == ADD_GALLERY_ITEM || requestCode == EDIT_BEER)
             && resultCode == RESULT_BRYGD_SAVED) 
        {
            getView().setVisibility(View.GONE);//så att man inte ser den ouppdaterade viewn
            new FetchBrygdsTask().execute();
        }
        //om bild vald, starta activity som editerar galleryitem
        if (requestCode == REQUEST_IMAGE_GET && resultCode == Activity.RESULT_OK) {
            Uri selectedImageURI = data.getData();
            if (selectedImageURI != null) {
                //starta intent för BrygdGalleryNewActivity, 
                Intent i = new Intent(getActivity(), BrygdGalleryNewActivity.class);
                i.putExtra(BrygdFragment.EXTRA_GALLERY_URI, selectedImageURI.toString());
                i.putExtra(BrygdFragment.EXTRA_BRYGD_ID, mBrygd.getId());
                startActivityForResult(i, ADD_GALLERY_ITEM);                
            }
        }
    }
//hämtar alla brygds på nytt
    private class FetchBrygdsTask extends AsyncTask<Void,Void,ArrayList<Brygd>> {
        @Override
        protected ArrayList<Brygd> doInBackground(Void... params) {
            return new GetGson().fetchItems();
        }

        @Override
        protected void onPostExecute(ArrayList<Brygd> brygds) {
            Log.d(TAG, "onPostExecute, brygds.length = " + brygds.size());
		    BrygdLab.setBrygds(brygds);
            mCallback.updatePager();
            if (getActivity() != null) {
                Toast.makeText(getActivity(), "Brygden uppdaterad.", Toast.LENGTH_LONG).show();
            }
/*            
            if (getActivity() != null) {
                Toast.makeText(getActivity(), "Öllistan uppdaterad.", Toast.LENGTH_LONG).show();
            }
*/            
        }
    }

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

//gallery
    private class GalleryItemAdapter extends ArrayAdapter<String> {
        public GalleryItemAdapter(ArrayList<String> items) {
            super(getActivity(), 0, items);
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            if (convertView == null) {
                convertView = getActivity().getLayoutInflater().inflate(R.layout.gallery_item, parent, false);
            }
            
            String item = getItem(position);
            ImageView imageView = (ImageView) convertView.findViewById(R.id.gallery_item_imageView);
            imageView.setImageResource(R.drawable.no_photo);
            mThumbnailThread.queueThumbnail(imageView, item);//använd title tills vidare, byt sen.
            //mThumbnailThread.queueThumbnail(imageView, item.getUrl());
            
            return convertView;
        }
    }
    
}
