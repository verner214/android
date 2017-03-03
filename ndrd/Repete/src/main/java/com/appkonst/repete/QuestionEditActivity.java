package com.appkonst.repete;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
//import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

public class QuestionEditActivity extends SingleFragmentActivity {

    @Override
    protected Fragment createFragment() {
        int pos = (int) getIntent().getSerializableExtra(QuestionFragment.ARG_POS);
        return QuestionEditFragment.newInstance(pos);
    }
}
