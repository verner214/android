package lawa.olapp;

public class FormGalleryEdit {
	private String mBrygdId;
	private String mImgURL;
	private String mText;
	private Boolean mHide;
	
	public FormGalleryEdit(String brygdId, String imgURL, String text, Boolean hide) {
		mBrygdId = brygdId;
		mImgURL = imgURL;
		mText = text;
		mHide = hide;
	}
	
	public String getBrygdId() {
		return mBrygdId;
	}

	public String getImgURL() {
		return mImgURL;
	}

	public String getText() {
		return mText;
	}

	public Boolean getHide() {
		return mHide;
	}
}
