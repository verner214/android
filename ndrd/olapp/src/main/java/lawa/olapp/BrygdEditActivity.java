package lawa.olapp;

import android.support.v4.app.Fragment;

public class BrygdEditActivity extends SingleFragmentActivity {

    @Override
    protected Fragment createFragment() {
        return new BrygdEditFragment();
    }
}
