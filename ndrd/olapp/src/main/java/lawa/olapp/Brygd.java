package lawa.olapp;

import java.util.Date;
//import java.util.UUID;

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
    private String mPictureGallary;//json-struct med array av {url, bildtext}

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

    public String getPictureGallary() {
        return mPictureGallary;
    }
    
    public void setPictureGallary(String pictureGallary) {
        mPictureGallary = pictureGallary;
    }

}
