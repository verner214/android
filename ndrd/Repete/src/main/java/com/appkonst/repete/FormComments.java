package com.appkonst.repete;

//public static String postImage(String id, byte[] byteArr, boolean question) {
public class FormComments {
    private int mPagerIndex;//pagerIndex är qaIndex * 2
    private String mComments;

    public FormComments(int pagerIndex, String comments) {
        mPagerIndex = pagerIndex;
        mComments = comments;
    }

    public int getPagerIndex() {
        return mPagerIndex;
    }

    public String getComments() {
        return mComments;
    }
}
