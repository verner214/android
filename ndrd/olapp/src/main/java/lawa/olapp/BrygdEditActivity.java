package lawa.olapp;

import android.support.v4.app.Fragment;

public class BrygdEditActivity extends SingleFragmentActivity {

    @Override
    protected Fragment createFragment() {
        String brygdId = (String)getIntent()
            .getSerializableExtra(BrygdFragment.EXTRA_BRYGD_ID);
        if (brygdId == null) {
            return new BrygdEditFragment();
        }
        return BrygdEditFragment.newInstance(brygdId);

//        return new BrygdEditFragment();
    }
}
