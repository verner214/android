package lawa.olapp;

import android.support.v4.app.Fragment;
import android.net.Uri;

public class BrygdGalleryNewActivity extends SingleFragmentActivity {

    @Override
    protected Fragment createFragment() {
        String imgUri = (String) getIntent().getSerializableExtra(BrygdFragment.EXTRA_GALLERY_URI);            
        String brygdId = (String) getIntent().getSerializableExtra(BrygdFragment.EXTRA_BRYGD_ID);            
        return BrygdGalleryNewFragment.newInstance(brygdId, imgUri);
    }
}
