package lawa.olapp;

import android.support.v4.app.Fragment;
import android.net.Uri;

public class BrygdGalleryNewActivity extends SingleFragmentActivity {

    @Override
    protected Fragment createFragment() {
        Uri imgUri = (Uri) getIntent().getSerializableExtra(BrygdFragment.EXTRA_GALLERY_URI);            
        return BrygdGalleryNewFragment.newInstance(imgUri);
    }
}
