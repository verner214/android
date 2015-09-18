package lawa.olapp;

public class Gallery {
    
    private String imgURL;
    private String thumbURL;
    private String text;
    private boolean hide;

    public Gallery(String imgURL, String thumbURL, String text, boolean hide) {
        this.imgURL = imgURL;
        this.thumbURL = thumbURL;
        this.text = text;
        this.hide = hide;
    }

    public String getImgURL() {
        return imgURL;
    }

    public String getThumbURL() {
        return thumbURL;
    }

    public String getText() {
        return text;
    }    

    public boolean getHide() {
        return hide;
    }    
}
