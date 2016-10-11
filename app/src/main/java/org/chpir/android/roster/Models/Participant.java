package org.chpir.android.roster.Models;

import com.activeandroid.Model;
import com.activeandroid.annotation.Column;
import com.activeandroid.annotation.Table;
import com.activeandroid.query.Select;

import java.util.List;
import java.util.UUID;

@Table(name = "Participants")
public class Participant extends Model {
    @Column(name = "Identifier")
    private String mIdentifier;
    @Column(name = "Center")
    private Center mCenter;

    public Participant() {
        super();
        this.mIdentifier = UUID.randomUUID().toString();
    }

    public static Participant findByIdentifier(String participantId) {
        return new Select().from(Participant.class).where("Identifier = ?", participantId)
                .executeSingle();
    }

    public static List<Participant> findAll() {
        return new Select().from(Participant.class).orderBy("Id ASC").execute();
    }

    public Response identifierResponse() {
        return new Select().from(Response.class).where("Participant = ?", this.getId()).where
                ("Question = ?", new Select().from(Question.class).where("QuestionHeader = ?",
                        Question.QuestionHeader.PARTICIPANT_ID).executeSingle().getId())
                .executeSingle();
    }

    public List<Question> questions() {
        return new Select().from(Question.class).where("Participant = ?", getId()).execute();
    }

    public String getIdentifier() {
        return mIdentifier;
    }

    public Center getCenter() {
        return mCenter;
    }

    public void setCenter(Center center) {
        mCenter = center;
    }

    public List<Response> responses() {
        return new Select().from(Response.class).where("Participant = ?", getId()).execute();
    }
}