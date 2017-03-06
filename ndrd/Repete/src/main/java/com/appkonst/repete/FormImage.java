package com.appkonst.repete;

//public static String postImage(String id, byte[] byteArr, boolean question) {
public class FormImage {
	private String mId;
	private byte[] mImage;
	private boolean mQuestion;

	public FormImage(String id, byte[] image, boolean question) {
		mId = id;
		mImage = image;
		mQuestion = question;
	}
	
	public byte[] getImage() {
		return mImage;
	}

	public String getId() {
		return mId;
	}

	public boolean getQuestion() {
		return mQuestion;
	}
}
