package com.appkonst.repete;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v4.app.Fragment;
import android.support.v4.content.FileProvider;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.io.File;
import java.io.IOException;
import java.util.List;

public class QuestionEditFragment extends Fragment {

    private int mPagerIndex;
    private QAItem mQAItem;
    private final static String TAG = "QuestionEditFragment";
    static final int REQUESTCODE_IMAGE_GET = Activity.RESULT_FIRST_USER;
    static final int REQUEST_TAKE_PHOTO = Activity.RESULT_FIRST_USER + 1;

    ImageView mImgLarge;
    EditText mTxeComments;


    public QuestionEditFragment() {
        // Required empty public constructor
    }

    public static QuestionEditFragment newInstance(int pagerIndex) {
        QuestionEditFragment fragment = new QuestionEditFragment();
        Bundle args = new Bundle();
        args.putInt(QuestionFragment.ARG_PAGERINDEX, pagerIndex);
        fragment.setArguments(args);
        return fragment;
    }
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mPagerIndex = getArguments().getInt(QuestionFragment.ARG_PAGERINDEX);
            mQAItem = QALab.getQAItem(mPagerIndex / 2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_question_edit, container, false);
        //boolean question = mPos % 2 == 0;

        TextView txtRubrik = (TextView) v.findViewById(R.id.txtRubrik);
        txtRubrik.setText((mPagerIndex % 2 == 0 ? "F " : "S ") + (mPagerIndex / 2 + 1) + "/" + QALab.Count() + " " + mQAItem.getArea1() + " - " + mQAItem.getArea2());

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

        Button btnCapturePhoto = (Button)v.findViewById(R.id.btnCapturePhoto);
        //mBtnSelectPhoto.setText("välj bild");
        //btnSelectPhoto.setEnabled(true);
        btnCapturePhoto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //mBtnSave.setEnabled(true);
                takeAndSavePicture();
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
        new PostFormTask<FormComments>() {
            @Override
            public String doPost(FormComments comments) {
                String errmsg = HTTP.postComments(comments);
                if (errmsg == null) {
                    return QALab.updateQAItem(mPagerIndex / 2);
                }
                return errmsg;
            }
        }.exec(new FormComments(mPagerIndex, mTxeComments.getText().toString()), "kommentar sparas.");
    }
    //startar inbyggd Activity som väljer bild
    private void selectImage() {
        Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
        intent.setType("image/*");
        if (intent.resolveActivity(getActivity().getPackageManager()) != null) {
            startActivityForResult(intent, REQUESTCODE_IMAGE_GET);
        }
    }

//alltid använda samma fil, sparas ju i molnet direkt efter, dessutom kan man inte spara filnamn i membervariabel eftersom fragmentobjectet kan recyclas vid ont om minne
    //https://developer.android.com/training/camera/photobasics.html
    private Uri createImageFile() {
        File f = new File(getActivity().getExternalFilesDir(Environment.DIRECTORY_PICTURES), "myPic.jpg");
        Log.d(TAG, "createImageFile, path=" + f.getPath());
        Uri u = FileProvider.getUriForFile(getActivity(), "com.example.android.fileprovider", f);
        Log.d(TAG, "uri = " + u.toString());
        return u;
    }

    private void takeAndSavePicture() {
        try {
            Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
            // Ensure that there's a camera activity to handle the intent
            if (takePictureIntent.resolveActivity(getActivity().getPackageManager()) == null) {
                Toast.makeText(getActivity(), "kunde inte resolva intent MediaStore.ACTION_IMAGE_CAPTURE", Toast.LENGTH_LONG).show();
                return;
            }
            Uri imageUri = createImageFile();
            takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, imageUri);
            //man måste ge behörighet till mottagande app att läsa / skriva Uri. först en generell metod sen en som funkar med googles kamera.(nexus)
            //http://stackoverflow.com/questions/18249007/how-to-use-support-fileprovider-for-sharing-content-to-other-apps
            List<ResolveInfo> resInfoList = getActivity().getPackageManager().queryIntentActivities(takePictureIntent, PackageManager.MATCH_DEFAULT_ONLY);
            for (ResolveInfo resolveInfo : resInfoList) {
                String packageName = resolveInfo.activityInfo.packageName;
                getActivity().grantUriPermission(packageName, imageUri, Intent.FLAG_GRANT_WRITE_URI_PERMISSION | Intent.FLAG_GRANT_READ_URI_PERMISSION);
            }
            /* funkar bara för google
            getActivity().grantUriPermission(
                    "com.google.android.GoogleCamera",
                    imageUri,
                    Intent.FLAG_GRANT_WRITE_URI_PERMISSION | Intent.FLAG_GRANT_READ_URI_PERMISSION
            );*/
            startActivityForResult(takePictureIntent, REQUEST_TAKE_PHOTO);
        } catch (Exception ex) {
            Toast.makeText(getActivity(), Util.exceptionStacktraceToString(ex), Toast.LENGTH_LONG).show();
            Log.e(TAG, "gick inte ta kort ", ex);
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        try {
//visa vald bild i view, spara i molnet och
            if ((requestCode == REQUESTCODE_IMAGE_GET || requestCode == REQUEST_TAKE_PHOTO) && resultCode == Activity.RESULT_OK) {
                Uri selectedImageURI = requestCode == REQUESTCODE_IMAGE_GET ? data.getData() : createImageFile();
                Bitmap bmpLarge = ImageLibrary.Uri2Bmp(getActivity(), selectedImageURI, 640, 360, true);
                //Toast.makeText(getActivity(), "onActivityResult=" + b2 + "," + b, Toast.LENGTH_SHORT).show();
                mImgLarge.setImageBitmap(bmpLarge);
//spara bild i molnet
                //progress = ProgressDialog.show(getActivity(), "bild sparas", "vänta...", true);
                //FormImage fImg = new FormImage("rowid", ImageLibrary.Bmp2Jpg(bmpLarge, 90), mPos % 2 == 0);
                new PostFormTask<FormImage>() {
                    @Override
                    public String doPost(FormImage fi) {
                        String errmsg = HTTP.postImage(fi);
                        if (errmsg == null) {
                            return QALab.updateQAItem(mPagerIndex / 2);
                        }
                        return errmsg;
                    }
                }.exec(new FormImage(mPagerIndex, ImageLibrary.Bmp2Jpg(bmpLarge, 90)), "bild sparas.");
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
