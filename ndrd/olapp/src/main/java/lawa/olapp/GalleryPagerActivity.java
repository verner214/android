package lawa.olapp;

import java.util.ArrayList;
import java.util.UUID;

import android.os.Bundle;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.widget.Toast;
import android.widget.EditText;
import android.view.inputmethod.InputMethodManager;
import android.content.Context;

import android.support.v4.view.ViewPager;

public class GalleryPagerActivity extends FragmentActivity 
    implements GalleryFragment.OnGalleryKeyboardDisplayedListener {
          
    ViewPager mViewPager;
    Brygd mBrygd;
    EditText mText;//en reference till den edittext i en fregment som editeras vid page svep.

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        mViewPager = new ViewPager(this);
        mViewPager.setId(R.id.viewPager);//går den att återanvända?
        setContentView(mViewPager);

//hitta brygd
        ArrayList<Brygd> brygds = BrygdLab.get(this).getBrygds();
        String brygdId = (String)getIntent().getSerializableExtra(BrygdFragment.EXTRA_BRYGD_ID);
        for (int i = 0; i < brygds.size(); i++) {
            if (brygds.get(i).getId().equals(brygdId)) {
                mBrygd = brygds.get(i);
                break;
            } 
        }

        FragmentManager fm = getSupportFragmentManager();
        mViewPager.setAdapter(new FragmentStatePagerAdapter(fm) {
            @Override
            public int getCount() {
                return mBrygd.getNumOfGalleryItems();
            }
            @Override
            public Fragment getItem(int pos) {
                return GalleryFragment.newInstance(mBrygd.getId(), pos);
            }
            @Override
            public int getItemPosition(Object object) {
                return POSITION_NONE;
            }            
        });
        final GalleryPagerActivity a = this;
        mViewPager.setOnPageChangeListener(new OnPageChangeListener() {
            @Override
            public void onPageSelected(int position) {
//                Toast.makeText(a, "onPageSelected", Toast.LENGTH_LONG).show();
                InputMethodManager imm = (InputMethodManager) a.getSystemService(Context.INPUT_METHOD_SERVICE);
                if (mText != null && imm != null) {
                    imm.hideSoftInputFromWindow(mText.getWindowToken(), 0);    
                    mText = null;            
                }

            }
        
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
                    return;
            }
        
            @Override
            public void onPageScrollStateChanged(int state) {
                    return;
            }
        });         
         
//sätt vald bild när pager öppnas
        int position = Integer.parseInt((String) getIntent().getSerializableExtra(BrygdFragment.EXTRA_GALLERY_POSITION));
        mViewPager.setCurrentItem(position);
    }//onCreate
    
    public void setActiveTextEdit(EditText e) {
        mText = e;
    }

}//BrygdPagerActivity
