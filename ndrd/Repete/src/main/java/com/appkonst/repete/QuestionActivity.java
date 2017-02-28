package com.appkonst.repete;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
//import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.ViewPager;

import java.util.ArrayList;

//detta är en pageractivity
public class QuestionActivity extends FragmentActivity {

    ViewPager mViewPager;
    //hur få viewpager att uppdatera sig? se SO nedan för acceperad men inte så effektiv lösning
//http://stackoverflow.com/questions/7263291/viewpager-pageradapter-not-updating-the-view
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        mViewPager = new ViewPager(this);
        mViewPager.setId(R.id.viewPager);
        setContentView(mViewPager);

        FragmentManager fm = getSupportFragmentManager();
        mViewPager.setAdapter(new FragmentStatePagerAdapter(fm) {
            @Override
            public int getCount() {
                return QALab.Count() * 2;
            }
            @Override
            public Fragment getItem(int pos) {
                return QuestionFragment.newInstance(pos);
            }
            @Override
            public int getItemPosition(Object object) {
                return POSITION_NONE;
            }
        });
//bör ha liknande konstruktion så att current item visas och att den inte börjar om från 0 vid varje rotation eller när man kommer tillbaka eftet questionEditactivity
        //intent sätts då i fragment som skapas eller 0 om första gången (kommer från mainactivity)
        /*
        String brygdId = (String)getIntent().getSerializableExtra(QuestionFragment.);
        for (int i = 0; i < brygds.size(); i++) {
            if (brygds.get(i).getId().equals(brygdId)) {
                mViewPager.setCurrentItem(i);
                break;
            }
        }//for
        */
    }//onCreate
}
