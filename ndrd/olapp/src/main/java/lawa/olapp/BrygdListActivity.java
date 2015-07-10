package lawa.olapp;

import android.support.v4.app.Fragment;

public class BrygdListActivity extends SingleFragmentActivity {

    @Override
    protected Fragment createFragment() {
        return new BrygdListFragment();
    }
}
