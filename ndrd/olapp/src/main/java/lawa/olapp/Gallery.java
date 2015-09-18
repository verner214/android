package lawa.olapp;

public class Gallery {
    
    private String mImgURL;
    private String mThumbURL;
    private String mText;

    public Gallery(String imgURL, String thumbURL, String text) {
        mImgURL = imgURL;
        mThumbURL = thumbURL;
        mText = text;
    }

    public String getImgURL() {
        return mImgURL;
    }

    public String getThumbURL() {
        return mThumbURL;
    }

    public String getText() {
        return mText;
    }    
}
