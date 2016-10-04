package org.chpir.android.roster.Models;

import com.activeandroid.Model;
import com.activeandroid.annotation.Column;
import com.activeandroid.annotation.Table;

import org.parceler.Parcel;

import java.util.ArrayList;

@Table(name="Participants")
public class Participant extends Model{
    @Column(name="Questions")
    private ArrayList<Question> mQuestions;
    @Column(name="Identifier")
    private String mIdentifier;

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