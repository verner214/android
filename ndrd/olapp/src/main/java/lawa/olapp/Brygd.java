package lawa.olapp;

import java.util.Date;
import java.util.UUID;

public class Crime {
    private UUID mId;
    private String mBeerName;
    private String mBeerStyle;
    private String mThumbUrl;
    private String mThumbUrl;
    private Date mDate;
    private boolean mSolved;

    public Crime() {
        mId = UUID.randomUUID();
        mDate = new Date();
    }

    @Override
    public String toString() {
        return mTitle;
    }

    public String getTitle() {
        return mTitle;
    }

    public String getMediumURL() {
        return mMediumURL;
    }
    
    public void setMediumURL(String mediumURL) {
        mMediumURL = mediumURL;
    }

    public void setTitle(String title) {
        mTitle = title;
    }

    public UUID getId() {
        return mId;
    }

    public boolean isSolved() {
        return mSolved;
    }

    public void setSolved(boolean solved) {
        mSolved = solved;
    }

    public Date getDate() {
        return mDate;
    }

    public void setDate(Date date) {
        mDate = date;
    }


}
