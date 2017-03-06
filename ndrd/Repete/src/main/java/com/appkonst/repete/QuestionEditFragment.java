package com.appkonst.repete;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

public class QuestionEditFragment extends Fragment {

    private int mPos;
    private QAItem mQAItem;
    private final static String TAG = "QuestionEditFragment";
    static final int REQUESTCODE_IMAGE_GET = Activity.RESULT_FIRST_USER;
    ImageView mImgLarge;
    EditText mTxeComments;


    public QuestionEditFragment() {
        // Required empty public constructor
    }

    public static QuestionEditFragment newInstance(int pos) {
        QuestionEditFragment fragment = new QuestionEditFragment();
        Bundle args = new Bundle();
        args.putInt(QuestionFragment.ARG_POS, pos);
        fragment.setArguments(args);
        return fragment;
    }
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mPos = getArguments().getInt(QuestionFragment.ARG_POS);
            mQAItem = QALab.getQAItem(mPos / 2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_question_edit, container, false);
        boolean question = mPos % 2 == 0;

        TextView txtRubrik = (TextView) v.findViewById(R.id.txtRubrik);
        txtRubrik.setText((question ? "F " : "S ") + (mPos / 2 + 1) + "/" + QALab.Count() + " " + mQAItem.getArea1() + " - " + mQAItem.getArea2());

        mTxeComments = (EditText) v.findViewById(R.id.txeComments);
        mTxeComments.setText(mQAItem.getComments());

        mImgLarge = (ImageView) v.findViewById(R.id.imgLarge);

        Button btnSelectPhoto = (Button)v.findViewById(R.id.btnSelectPhoto);
        //mBtnSelectPhoto.setText("välj bild");
        //btnSelectPhoto.setEnabled(true);
        btnSelectPhoto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //mBtnSave.setEnabled(true);
                selectImage();
            }
        });

        Button btnSaveComments = (Button)v.findViewById(R.id.btnSaveComments);
        btnSaveComments.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                saveComments();
            }
        });

        return v;
    }//onCreateView

    private void saveComments() {
        new PostFormTask<String>() {
            @Override
            public String doPost(String comments) {
//    public static String postComments(String id, String comments) {

                return HTTP.postComments(comments);
            }
        }.exec(mTxeComments.getText().toString(), "kommentar sparas.");

    }
    //startar inbyggd Activity som väljer bild
    private void selectImage() {
        Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
        intent.setType("image/*");
        if (intent.resolveActivity(getActivity().getPackageManager()) != null) {
            startActivityForResult(intent, REQUESTCODE_IMAGE_GET);
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        try {
//visa vald bild i view, spara i molnet och
            if (requestCode == REQUESTCODE_IMAGE_GET && resultCode == Activity.RESULT_OK) {
                Uri selectedImageURI = data.getData();
                Bitmap bmpLarge = ImageLibrary.Uri2Bmp(getActivity(), selectedImageURI, 640, 360, true);
                //Toast.makeText(getActivity(), "onActivityResult=" + b2 + "," + b, Toast.LENGTH_SHORT).show();
                mImgLarge.setImageBitmap(bmpLarge);
//spara bild i molnet
                //progress = ProgressDialog.show(getActivity(), "bild sparas", "vänta...", true);
                new PostFormTask<byte[]>() {
                    @Override
                    public String doPost(byte[] bytaArr) {
                        //public static String postImage(String id, byte[] byteArr, boolean question) {

                        return HTTP.postImage(bytaArr, mPos % 2 == 0/*question*/);
                    }
                }.exec(ImageLibrary.Bmp2Jpg(bmpLarge, 90), "bild sparas.");
            }

        } catch (Exception e) {
            Toast.makeText(getActivity(), "exception=" + e.toString(), Toast.LENGTH_LONG).show();
            Log.e(TAG, "Error downloading image", e);
        }

    }//onActivityResult

    private abstract class PostFormTask<FormItem> extends AsyncTask<FormItem,Void,String> {
        protected abstract String doPost(FormItem f);
        ProgressDialog mProgress;

        public void exec(FormItem fi, String msg) {
            mProgress = ProgressDialog.show(getActivity(), "Vänta...", msg, true);
            executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR, fi);
        }

        @Override
        protected String doInBackground(FormItem... forms) {
            return doPost(forms[0]);
            //return MultiPart.PostForm(forms[0]);
//            return bytes;
        }

        @Override
        protected void onPostExecute(String result) {
//ta bort busy dialog om den syns
            if (getActivity() != null) {
                if (mProgress != null) {//måste ju inte ha använt exec.
                    mProgress.dismiss();
                }
                if (result != null) {
                    Toast.makeText(getActivity(), "Något gick fel...\n " + result, Toast.LENGTH_LONG).show();
                    return;
                }
                getActivity().setResult(QuestionFragment.REQUESTCODE_EDIT_QUESTION);
                getActivity().finish();
            }//if (getActivity() != null) {
        }//onPostExecute

    }//FetchItemsTask

}
