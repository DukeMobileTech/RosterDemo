package org.chpir.android.roster.Models;

import android.util.SparseArray;

import org.parceler.Parcel;

import java.util.ArrayList;

@Parcel
public class Question {
     String text;
     String response;
     QuestionHeader.QuestionType questionType;

    public Question(){}

    public Question(String t, QuestionHeader.QuestionType questionType) {
        this.text = t;
        this.questionType = questionType;
    }

    public void setResponse(String r) {
        this.response = r;
    }

    public String getResponse() {
        return response;
    }

    public String getText() {
        return text;
    }

    public QuestionHeader.QuestionType getQuestionType() {
        return questionType;
    }

    public static ArrayList<Question> defaultQuestions() {
        ArrayList<Question> questions = new ArrayList<>();
        for (QuestionHeader header : QuestionHeader.values()) {
            questions.add(new Question(header.toString(), header.getQuestionType()));
        }
        return questions;
    }

    public SparseArray<String> getOptions() {
        if (questionType != QuestionHeader.QuestionType.SELECT_ONE) return null;
        SparseArray<String> options = new SparseArray<>();
        if (text.equals(QuestionHeader.GENDER.toString())) {
            options.append(0, "Male");
            options.append(1, "Female");
        } else {
            options.append(0, "Yes");
            options.append(1, "No");
        }
        return options;
    }

}