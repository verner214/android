package com.appkonst.repete;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.Toolbar;

import java.io.File;
import java.io.IOException;

public class QuestionFragment extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    public static final String ARG_PAGERINDEX = "question number";
    public static final int REQUESTCODE_EDIT_QUESTION = 1;//minns inte om 0 är ok

    // TODO: Rename and change types of parameters
    private int mPagerIndex;
    private QAItem mQAItem;
    private final static String TAG = "QuestionFragment";
    private TextView txtQA;
    private boolean mLargeText = true;
    ScalingImageView mImg;
    OnUpdatedListener mCallback;

    public QuestionFragment() {
        // Required empty public constructor
    }

    public static QuestionFragment newInstance(int pos) {
        QuestionFragment fragment = new QuestionFragment();
        Bundle args = new Bundle();
        args.putInt(ARG_PAGERINDEX, pos);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.d(TAG, "inför setHasOptionsMenu");
        setHasOptionsMenu(true);

        if (getArguments() != null) {
            mPagerIndex = getArguments().getInt(ARG_PAGERINDEX);
            mQAItem = QALab.getQAItem(mPagerIndex / 2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        /*
        mToolbar = (Toolbar) v.findViewById(R.id.toolbar);
        setSupportActionBar(mToolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        */

        View v = inflater.inflate(R.layout.fragment_question, container, false);

        TextView txtRubrik = (TextView) v.findViewById(R.id.txtRubrik);
        txtRubrik.setText((mPagerIndex % 2 == 0 ? "F " : "S ") + (mPagerIndex / 2 + 1) + "/" + QALab.Count() + " " + mQAItem.getArea1() + " - " + mQAItem.getArea2());

        txtQA = (TextView) v.findViewById(R.id.txtQA);
        txtQA.setText(mPagerIndex % 2 == 0 ? mQAItem.getQuestion() : mQAItem.getAnswer());
        //txtQA.setTextSize(TypedValue.COMPLEX_UNIT_DIP,35);//gör detta via bar at the top

        TextView txtComments = (TextView) v.findViewById(R.id.txtComments);
        txtComments.setText(mQAItem.getComments());

        mImg = (ScalingImageView) v.findViewById(R.id.imgImg1);
        String imgUrl = mPagerIndex % 2 == 0 ? mQAItem.getQuestionimg() : mQAItem.getAnswerimg();
        if (imgUrl != null) {
            ImgCacheParam imgP = new ImgCacheParam(getActivity().getExternalCacheDir(), imgUrl);
            new FetchItemsTask().executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR, imgP);
        }

        return v;
    }
    // Container Activity must implement this interface
    public interface OnUpdatedListener {
        public void updatePager();
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);

        // This makes sure that the container activity has implemented
        // the callback interface. If not, it throws an exception
        try {
            mCallback = (OnUpdatedListener) activity;
        } catch (ClassCastException e) {
            throw new ClassCastException(activity.toString() + " must implement OnUpdatedListener");
        }
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        Log.d(TAG, "onCreateOptionsMenu1");
        super.onCreateOptionsMenu(menu, inflater);
        inflater.inflate(R.menu.fragment_question, menu);
        Log.d(TAG, "onCreateOptionsMenu2");
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {

            case R.id.menu_item_edit:
                Log.d(TAG, "menu_item_edit, starta QuestionEditActivity");
                Intent i = new Intent(getActivity(), QuestionEditActivity.class);
                i.putExtra(QuestionFragment.ARG_PAGERINDEX, mPagerIndex);
                startActivityForResult(i, REQUESTCODE_EDIT_QUESTION);
                return true;

            case R.id.menu_item_minimize:
                Log.d(TAG, "menu_item_enlarge, gör texten mindre");
                if (mLargeText) {
                    txtQA.setTextSize(TypedValue.COMPLEX_UNIT_DIP, 16);//gör detta via bar at the top
                    mLargeText = false;
                } else {
                    txtQA.setTextSize(TypedValue.COMPLEX_UNIT_DIP, 22);//gör detta via bar at the top
                    mLargeText = true;
                }
                return true;

            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == REQUESTCODE_EDIT_QUESTION)
        {
            getView().setVisibility(View.GONE);//så att man inte ser den ouppdaterade viewn. kommer den hinna ses?
            mCallback.updatePager();
        }
    }

    private class ImgCacheParam {
        private String mUrl;
        private File mCacheDir;

        public ImgCacheParam(File cacheDir, String url) {
            mUrl = url;
            mCacheDir = cacheDir;
        }
        public File getCacheDir() {
            return mCacheDir;
        }
        public String getUrl() {
            return mUrl;
        }
    }

    //hämtar toppbilden. appens cachedir och bildens url skickas med så får
    //det senare avgöras om den ska hämtas från nätet eller om den finns i cachen.
    private class FetchItemsTask extends AsyncTask<ImgCacheParam,Void,byte[]> {
        @Override
        protected byte[] doInBackground(ImgCacheParam... imgCacheParams) {
            byte[] bytes = null;
            try {
                bytes = HTTP.getUrlBytes(imgCacheParams[0].getCacheDir(), imgCacheParams[0].getUrl());
            } catch (IOException ioe) {
                Log.e(TAG, "Error downloading image", ioe);
            }
            return bytes;
        }

        @Override
        protected void onPostExecute(byte[] bitmapBytes) {
            if (bitmapBytes == null) {
                Toast.makeText(getActivity(), "bild kunde inte hämtas, troligen offline", Toast.LENGTH_LONG).show();
                return;
            }
            final Bitmap bitmap = BitmapFactory.decodeByteArray(bitmapBytes, 0, bitmapBytes.length);
            if (isVisible()) {
                mImg.setImageBitmap(bitmap);
            }
        }
    }


}
