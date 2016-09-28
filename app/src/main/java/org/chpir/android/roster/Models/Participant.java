package org.chpir.android.roster.Models;

import org.parceler.Parcel;

import java.util.ArrayList;

@Parcel
public class Participant {
    ArrayList<Question> mQuestions;
    String mIdentifier;

    public Participant() {}

    public Participant(ArrayList<Question> questions, String id) {
        this.mQuestions = questions;
        this.mIdentifier = id;
    }

    public ArrayList<Question> getQuestions() {
        return mQuestions;
    }

    public Question getIdQuestion() {
        return mQuestions.get(0);
    }

    public String getIdentifier() {
        return mIdentifier;
    }
}