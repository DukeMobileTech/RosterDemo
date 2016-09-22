package org.chpir.android.roster.models;

public class Question {
    private String label;
    private String text;
    private String response;

    public Question(String l, String t) {
        this.label = l;
        this.text = t;
    }

    public void setResponse(String r) {
        this.response = r;
    }

}