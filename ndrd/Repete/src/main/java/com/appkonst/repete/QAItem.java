package com.appkonst.repete;

/**
 * Created by lars on 2017-02-13.
 */

public class QAItem {
    private String RowKey;
    private String questionimg;
    private String answerimg;
    private String area1;
    private String area2;
    private String question;
    private String answer;
    private String comments;
    private String showlevel;
    private String hide;

    private QAItem() {
    }//försök ta bort den här?

    public QAItem(String RowKey,
                String questionimg,
                String answerimg,
                String area1,
                String area2,
                String question,
                String answer,
                String comments,
                String showlevel,
                String hide) {
        this.RowKey = RowKey;
        this.questionimg = questionimg;
        this.answerimg = answerimg;
        this.area1 = area1;
        this.area2 = area2;
        this.question = question;
        this.answer = answer;
        this.comments = comments;
        this.showlevel = showlevel;
        this.comments = comments;
        this.hide = hide;
    }

    public String getRowKey() {
        return RowKey;
    }

    public String getQuestionimg() {
        return questionimg;
    }

    public String getAnswerimg() {
        return answerimg;
    }

    public String getArea1() {
        return area1;
    }

    public String getArea2() {
        return area2;
    }

    public String getQuestion() {
        return question;
    }

    public String getAnswer() {
        return answer;
    }

    public String getComments() {
        return comments;
    }

    public String getShowlevel() {
        return showlevel;
    }

    public String getHide() { return hide; }
}//class
