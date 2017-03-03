package com.appkonst.repete;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;


/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link QuestionEditFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link QuestionEditFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class QuestionEditFragment extends Fragment {

    private int mPos;
    private QAItem mQAItem;
    private final static String TAG = "QuestionEditFragment";

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

        TextView txtComments = (TextView) v.findViewById(R.id.txtComments);
        txtComments.setText(mQAItem.getComments());

        return v;
    }

}
