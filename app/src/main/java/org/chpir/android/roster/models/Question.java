package org.chpir.android.roster.Models;

import android.util.SparseArray;

import com.activeandroid.Model;
import com.activeandroid.annotation.Column;
import com.activeandroid.annotation.Table;
import com.activeandroid.query.Select;

import java.util.UUID;

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
    @Column(name = "Participant")
    private Participant mParticipant;
    @Column(name = "Identifier")
    private String mIdentifier;

    public Question() {
        super();
        this.mIdentifier = UUID.randomUUID().toString();
    }

    public static Question findByIdentifier(String questionId) {
        return new Select().from(Question.class).where("Identifier = ?", questionId)
                .executeSingle();
    }

    public void setParticipant(Participant participant) {
        this.mParticipant = participant;
    }

    public String getIdentifier() {
        return mIdentifier;
    }

    public String getResponse() {
        return response;
    }

    public void setResponse(String r) {
        this.response = r;
        save(); //TODO change to save response when save button pressed
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public QuestionHeader getQuestionHeader() {
        return questionHeader;
    }

    public void setQuestionHeader(QuestionHeader questionHeader) {
        this.questionHeader = questionHeader;
    }

    public QuestionType getQuestionType() {
        return questionType;
    }

    public void setQuestionType(QuestionType questionType) {
        this.questionType = questionType;
    }

    public SparseArray<String> getOptions() {
        if (questionType != QuestionType.SELECT_ONE) return null;
        final SparseArray<String> options = new SparseArray<>();
        if (questionHeader == QuestionHeader.GENDER) {
            options.append(0, "Male");
            options.append(1, "Female");
        } else {
            options.append(0, "Yes");
            options.append(1, "No");
        }
        return options;
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

        public static QuestionHeader getByIndex(int pos) {
            return QuestionHeader.values()[pos];
        }

        public QuestionType getQuestionType() {
            return questionType;
        }
    }

}