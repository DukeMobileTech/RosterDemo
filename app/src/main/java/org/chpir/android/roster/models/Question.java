package org.chpir.android.roster.Models;

import android.util.SparseArray;

import com.activeandroid.Model;
import com.activeandroid.annotation.Column;
import com.activeandroid.annotation.Table;

import org.parceler.Parcel;

import java.util.ArrayList;

@Table(name = "Questions")
public class Question extends Model {
    @Column(name = "Text")
    private String text;
    @Column(name = "Response")
    private String response;
    @Column(name = "QuestionType")
    private QuestionType questionType;
    @Column(name = "QuestionHeader")
    private QuestionHeader questionHeader;


    public Question(){}

    public Question(String t, QuestionType questionType) {
        this.text = t;
        this.questionType = questionType;
    }

    public enum QuestionType {
        SELECT_ONE, FREE_RESPONSE, DATE, INTEGER;
    }

    public enum QuestionHeader {
        PARTICIPANT_ID(QuestionType.FREE_RESPONSE), AGE(QuestionType.INTEGER),
        GENDER(QuestionType.SELECT_ONE), ARRIVAL_DATE(QuestionType.DATE),
        NUM_HOURS_PER_WEEK(QuestionType.INTEGER), NUM_WEEKS_ABSENT_PER_YEAR(QuestionType.INTEGER),
        ASSIGNED_GROUP(QuestionType.FREE_RESPONSE), TIME_IN_GROUP(QuestionType.INTEGER),
        NUM_PREVIOUS_GROUPS(QuestionType.INTEGER), PREVIOUS_CARE(QuestionType.FREE_RESPONSE),
        PRIMARY_CAREGIVER(QuestionType.FREE_RESPONSE), TIME_TOGETHER(QuestionType.INTEGER),
        NUM_SIBLINGS(QuestionType.INTEGER), NUM_SIBLINGS_IN_PROGRAM(QuestionType.INTEGER),
        SIBLINGS_GROUPS(QuestionType.FREE_RESPONSE), VACCINATION(QuestionType.SELECT_ONE),
        SCHOOL(QuestionType.SELECT_ONE), DISABILITY(QuestionType.SELECT_ONE);


        private final QuestionType questionType;

        QuestionHeader(QuestionType questionType) {
            this.questionType = questionType;
        }

        public QuestionType getQuestionType() {
            return questionType;
        }
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

    public QuestionHeader getQuestionHeader(){return questionHeader;}

    public QuestionType getQuestionType() {
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
        if (questionType != QuestionType.SELECT_ONE) return null;
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