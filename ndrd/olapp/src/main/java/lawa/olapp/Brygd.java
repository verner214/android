package lawa.olapp;

import java.util.Date;
import com.google.gson.Gson;
import android.util.Log;
//import java.util.UUID;
/*
private class GalleryArray {
    private Gallery[] value;
    
    private PClass() {
    }
    
    public PClass(Item[] value) {
        this.value = value;
    }
    
    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        for (Item i : value) {
            sb.append(i.toString() + "\n");
        }
                    
        //sb.append("Name: " + name + " ");
        return sb.toString();
    }
    
    public Item[] getItems() {
        return value;
    }
}
*/
public class Brygd {
    
    private String mId;
    private String mBeerName;
    private String mBeerStyle;
    private String mThumbUrl;
    private String mImgUrl;
    private String mOg;
    private String mFg;//alkohol räknas ut!
    private String mDescription;
    private String mRecipe;
    private String mComments;
    private String mBrewingDate;
    private String mPeople;
    private String mPlace;
    private Gallery[] mPictureGallary;//json-struct med array av {url, bildtext}
    private boolean mHide;//true, false
    private final static String TAG = "Brygd";
    
    public Brygd(String id) {
        mId = id;
    }

    public String getId() {
        return mId;
    }

    @Override
    public String toString() {
        return mBeerName;
    }

    public String getBeerName() {
        return mBeerName;
    }

    public void setBeerName(String beerName) {
        mBeerName = beerName;
    }

    public String getBeerStyle() {
        return mBeerStyle;
    }
    
    public void setBeerStyle(String beerStyle) {
        mBeerStyle = beerStyle;
    }

    public String getThumbUrl() {
        return mThumbUrl;
    }
    
    public void setThumbUrl(String thumbUrl) {
        mThumbUrl = thumbUrl;
    }

    public String getImgUrl() {
        return mImgUrl;
    }
    
    public void setImgUrl(String imgUrl) {
        mImgUrl = imgUrl;
    }

    public String getOg() {
        return mOg;
    }
    
    public void setOg(String og) {
        mOg = og;
    }

    public String getFg() {
        return mFg;
    }
    
    public void setFg(String fg) {
        mFg = fg;
    }

    public String getDescription() {
        return mDescription;
    }
    
    public void setDescription(String description) {
        mDescription = description;
    }

    public String getRecipe() {
        return mRecipe;
    }
    
    public void setRecipe(String recipe) {
        mRecipe = recipe;
    }

    public String getComments() {
        return mComments;
    }
    
    public void setComments(String comments) {
        mComments = comments;
    }

    public String getBrewingDate() {
        return mBrewingDate;
    }
    
    public void setBrewingDate(String brewingDate) {
        mBrewingDate = brewingDate;
    }

    public String getPeople() {
        return mPeople;
    }
    
    public void setPeople(String people) {
        mPeople = people;
    }

    public String getPlace() {
        return mPlace;
    }
    
    public void setPlace(String place) {
        mPlace = place;
    }
/*
    public String getPictureGallaryString() {
        return mPictureGallary;
    }
*/  
/*  
    public Gallery[] getPictureGallery() {
        return mPictureGallary;
    }
  */  
    public Gallery getGalleryItem(int pos) {
        int numOf = 0;
        for (Gallery g : mPictureGallary) {
            if (!g.getHide()) {
                if (numOf == pos) {
                    return g;
                }
                numOf++;
            }
        }
        return null;
    } 
    
    public int getNumOfGalleryItems() {
        if (mPictureGallary == null) {
            return 0;
        }
        int numOf = 0;
        for (Gallery g : mPictureGallary) {
            if (!g.getHide()) {
                numOf++;
            }
        }
        return numOf;
    } 
    
    public void setPictureGallary(String pictureGallary) {
        Log.d(TAG, "pictureGallary=" + pictureGallary);        
        Gson gson = new Gson();
        mPictureGallary = gson.fromJson(pictureGallary, Gallery[].class);
    }
    
    public boolean getHide() {
        return mHide;
    }
    
    public void setHide(boolean hide) {
        mHide = hide;
    }
}
