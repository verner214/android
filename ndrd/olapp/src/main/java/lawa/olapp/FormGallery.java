package lawa.olapp;

public class FormGallery {
	private String mBrygdId;
	private byte[] mImgLarge;
	private byte[] mImgThumbnail;
	private String mText;
	
	public FormGallery(String brygdId, byte[] img, byte[] thumb, String text) {
		mImgLarge = img;
		mImgThumbnail = thumb;
		mBrygdId = brygdId;
		mText = text;
	}
	
	public byte[] getImgLarge() {
		return mImgLarge;
	}

	public byte[] getImgThumbnail() {
		return mImgThumbnail;
	}

	public String getBrygdId() {
		return mBrygdId;
	}

	public String getText() {
		return mText;
	}
}
