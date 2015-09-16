package lawa.olapp;

import java.util.ArrayList;
import java.util.UUID;

import android.os.Bundle;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;

import android.support.v4.view.ViewPager;

public class BrygdPagerActivity extends FragmentActivity 
    implements BrygdFragment.OnBrygdsUpdatedListener {
        
    ViewPager mViewPager;
    ArrayList<Brygd> brygds;
//hur få viewpager att uppdatera sig? se SO nedan för acceperad men inte så effektiv lösning
//http://stackoverflow.com/questions/7263291/viewpager-pageradapter-not-updating-the-view
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        mViewPager = new ViewPager(this);
        mViewPager.setId(R.id.viewPager);
        setContentView(mViewPager);

        brygds = BrygdLab.get(this).getBrygds();

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
            @Override
            public int getItemPosition(Object object) {
                return POSITION_NONE;
            }            
        }); 

        String brygdId = (String)getIntent().getSerializableExtra(BrygdFragment.EXTRA_BRYGD_ID);
        for (int i = 0; i < brygds.size(); i++) {
            if (brygds.get(i).getId().equals(brygdId)) {
                mViewPager.setCurrentItem(i);
                break;
            } 
        }//for
    }//onCreate
    
//gör listener, dvs implementering av interface definierat i fragment, där listener anropas av fragment efter hämtad modell.
//listener gör: hämtar brygds på nytt. notifydatasetchanged.
    public void updatePager() {
        brygds = BrygdLab.get(this).getBrygds();
        mViewPager.getAdapter().notifyDataSetChanged();
    }

}//BrygdPagerActivity
