package com.appkonst.repete;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
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
                boolean b2 = selectedImageURI == null;

                Bitmap bmpLarge = ImageLibrary.Uri2Bmp(getActivity(), selectedImageURI, 640, 360, true);
                boolean b = bmpLarge == null;

                Toast.makeText(getActivity(), "onActivityResult=" + b2 + "," + b, Toast.LENGTH_SHORT).show();
                mImgLarge.setImageBitmap(bmpLarge);
//spara bild i molnet

            }
        } catch (Exception e) {
            Toast.makeText(getActivity(), "exception=" + e.toString(), Toast.LENGTH_LONG).show();
            Log.e(TAG, "Error downloading image", e);
        }

    }//onActivityResult

}
