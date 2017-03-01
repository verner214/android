package com.appkonst.repete;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.io.File;
import java.io.IOException;

public class QuestionFragment extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_POS = "param1";

    // TODO: Rename and change types of parameters
    private int mPos;
    private QAItem mQAItem;
    private final static String TAG = "QuestionFragment";
    ScalingImageView mImg;

    public QuestionFragment() {
        // Required empty public constructor
    }

    public static QuestionFragment newInstance(int pos) {
        QuestionFragment fragment = new QuestionFragment();
        Bundle args = new Bundle();
        args.putInt(ARG_POS, pos);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mPos = getArguments().getInt(ARG_POS);
            mQAItem = QALab.getQAItem(mPos / 2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_question, container, false);
        boolean question = mPos % 2 == 0;

        TextView txtRubrik = (TextView) v.findViewById(R.id.txtRubrik);
        txtRubrik.setText((question ? "F " : "S ") + (mPos / 2 + 1) + "/" + QALab.Count() + " " + mQAItem.getArea1() + " - " + mQAItem.getArea2());

        TextView txtQA = (TextView) v.findViewById(R.id.txtQA);
        txtQA.setText(question ? mQAItem.getQuestion() : mQAItem.getAnswer());

        TextView txtComments = (TextView) v.findViewById(R.id.txtComments);
        txtComments.setText(mQAItem.getComments());

        mImg = (ScalingImageView) v.findViewById(R.id.imgImg1);
        String imgUrl = question ? mQAItem.getQuestionimg() : mQAItem.getAnswerimg();
        if (imgUrl != null) {
            ImgCacheParam imgP = new ImgCacheParam(getActivity().getExternalCacheDir(), imgUrl);
            new FetchItemsTask().executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR, imgP);
        }

        return v;
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
            final Bitmap bitmap = BitmapFactory.decodeByteArray(bitmapBytes, 0, bitmapBytes.length);
            if (isVisible()) {
                mImg.setImageBitmap(bitmap);
            }
        }
    }


}
