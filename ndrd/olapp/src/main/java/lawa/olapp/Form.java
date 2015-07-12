package lawa.olapp;

public class Form {
	private byte[] mImgLarge;
	private byte[] mImgThumbnail;
	private Brygd mBrygd;
	
	public Form(Brygd b, byte[] img, byte[] thumb) {
		mImgLarge = img;
		mImgThumbnail = thumb;
		mBrygd = b;
	}
	
	public byte[] getImgLarge() {
		return mImgLarge;
	}

	public byte[] getImgThumb() {
		return mImgThumbnail;
	}

	public Brygd getBrygd() {
		return mBrygd;
	}
/*
	public String beerName;
	public String beerStyle;
	public String Og;
	public String Fg;
	public String description;
	public String recipe;
	public String comments;
	public String brewingDate;
	public String people;
	public String place;
	public String hide;
*/
}
