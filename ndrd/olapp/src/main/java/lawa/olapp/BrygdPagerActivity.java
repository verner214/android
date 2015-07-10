package lawa.olapp;

import java.util.ArrayList;
import java.util.UUID;

import android.os.Bundle;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;

import android.support.v4.view.ViewPager;

public class BrygdPagerActivity extends FragmentActivity {
    ViewPager mViewPager;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        mViewPager = new ViewPager(this);
        mViewPager.setId(R.id.viewPager);
        setContentView(mViewPager);

        final ArrayList<Brygd> brygds = BrygdLab.get(this).getBrygds();

        FragmentManager fm = getSupportFragmentManager();
        mViewPager.setAdapter(new FragmentStatePagerAdapter(fm) {
            @Override
            public int getCount() {
                return brygds.size();
            }
            @Override
            public Fragment getItem(int pos) {
                String brygdId =  brygds.get(pos).getId();
                return BrygdFragment.newInstance(brygdId);
            }
        }); 

        String brygdId = (String)getIntent().getSerializableExtra(BrygdFragment.EXTRA_BRYGD_ID);
        for (int i = 0; i < brygds.size(); i++) {
            if (brygds.get(i).getId().equals(brygdId)) {
                mViewPager.setCurrentItem(i);
                break;
            } 
        }
    }
}
