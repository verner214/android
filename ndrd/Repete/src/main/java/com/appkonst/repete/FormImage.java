package com.appkonst.repete;

//public static String postImage(String id, byte[] byteArr, boolean question) {
public class FormImage {
	private int mPagerIndex;
	private byte[] mImage;

	public FormImage(int pagerIndex, byte[] image) {
		mPagerIndex = pagerIndex;
		mImage = image;
	}
	
	public byte[] getImage() {
		return mImage;
	}

	public int getPagerIndex() {
		return mPagerIndex;
	}
}
