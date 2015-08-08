package lawa.olapp;

import java.util.UUID;
import java.io.IOException;
import java.util.ArrayList;

import android.content.Intent;
import android.annotation.TargetApi;
import android.os.Bundle;
import android.os.AsyncTask;
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
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Log;
import android.app.Activity;

public class BrygdFragment extends Fragment {
    OnBrygdsUpdatedListener mCallback;

    public static final String EXTRA_BRYGD_ID = "olapp.BRYGD_ID";
    private final static String TAG = "BrygdFragment";
    private final static int EDIT_BEER = 1;

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

    ImageView mImg;
    
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
            Float ffg = Float.parseFloat(mBrygd.getFg());
            Float fog = Float.parseFloat(mBrygd.getOg());
            Float falc = (fog - ffg) * 1000 / 7.5;
            mAlc.setText(falc);//beräknad
        } catch (exception) {
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
                        
        mImg = (ImageView) v.findViewById(R.id.img);
        //mImg.setImageResource(R.drawable.no_photo);
        if (mBrygd.getImgUrl() != null) {        
            ImgCacheParam imgP = new ImgCacheParam(getActivity().getExternalCacheDir(), mBrygd.getImgUrl());
            new FetchItemsTask().execute(imgP);
        }
        
        return v; 
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
        if (resultCode == BrygdEditFragment.RESULT_BRYGD_SAVED) {
            getView().setVisibility(View.GONE);
            new FetchBrygdsTask().execute();
        }
    }

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
