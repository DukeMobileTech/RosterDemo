package org.chpir.android.roster.Models;

import org.parceler.Parcel;

import java.util.ArrayList;

@Parcel
public class Participant {
    ArrayList<Question> mQuestions;
    String mIdentifier;

    public Participant() {
    }

    public Participant(ArrayList<Question> questions, String id) {
        this.mQuestions = questions;
        this.mIdentifier = id;
    }

    public ArrayList<Question> getQuestions() {
        return mQuestions;
    }

    public String identifier() {
        if (mQuestions.get(0).getResponse() == null || mQuestions.get(0).getResponse().isEmpty()) {
            return mIdentifier;
        } else {
            return mQuestions.get(0).getResponse();
        }
    }

    public String getIdentifier() {
        return mIdentifier;
    }
}