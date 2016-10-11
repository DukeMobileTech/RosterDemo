package org.chpir.android.roster.Models;

import com.activeandroid.Model;
import com.activeandroid.annotation.Column;
import com.activeandroid.annotation.Table;
import com.activeandroid.query.Select;

import java.util.List;
import java.util.UUID;

@Table(name = "Responses")
public class Response extends Model {
    @Column(name = "Text")
    private String mText;
    @Column(name = "Question")
    private Question mQuestion;
    @Column(name = "Participant")
    private Participant mParticipant;
    @Column(name = "Identifier")
    private String mIdentifier;

    public Response() {
        super();
        mIdentifier = UUID.randomUUID().toString();
    }

    public static Response findByQuestionAndParticipant(Question question, Participant
            participant) {
        return new Select().from(Response.class).where("Question = ?", question.getId()).where
                ("Participant = ?", participant.getId()).executeSingle();
    }

    public static List<Response> findAll() {
        return new Select().from(Response.class).execute();
    }

    public String getLabel() {
        if (getQuestion().getQuestionType() != Question.QuestionType.SELECT_ONE) {
            return getText();
        }
        if (getQuestion().getQuestionHeader() == Question.QuestionHeader.GENDER) {
            if (getText().equals("0")) {
                return "Male";
            } else {
                return "Female";
            }
        } else {
            if (getText().equals("0")) {
                return "Yes";
            } else {
                return "No";
            }
        }
    }

    public Question getQuestion() {
        return mQuestion;
    }

    public String getText() {
        return mText;
    }

    public void setText(String mText) {
        this.mText = mText;
    }

    public void setQuestion(Question mQuestion) {
        this.mQuestion = mQuestion;
    }

    public Participant getParticipant() {
        return mParticipant;
    }

    public void setParticipant(Participant mParticipant) {
        this.mParticipant = mParticipant;
    }

    public String getIdentifier() {
        return mIdentifier;
    }
}