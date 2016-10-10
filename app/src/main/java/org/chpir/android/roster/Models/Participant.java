package org.chpir.android.roster.Models;

import com.activeandroid.Model;
import com.activeandroid.annotation.Column;
import com.activeandroid.annotation.Table;
import com.activeandroid.query.Select;

import org.chpir.android.roster.Utils.SeedData;

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

    public String identifier() {
        if (questions().size() == 0 || questions().get(0).getResponse() == null || questions()
                .get(0).getResponse().isEmpty()) {
            return mIdentifier;
        } else {
            return questions().get(0).getResponse();
        }
    }

    public List<Question> questions() {
        return new Select().from(Question.class).where("Participant = ?", getId()).execute();
    }

    public void saveParticipant() {
        this.save();
        SeedData.createSeedQuestions(this);
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
}