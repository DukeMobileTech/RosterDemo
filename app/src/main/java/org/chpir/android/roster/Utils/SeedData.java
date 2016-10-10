package org.chpir.android.roster.Utils;

import com.activeandroid.ActiveAndroid;

import org.chpir.android.roster.Models.Center;
import org.chpir.android.roster.Models.Participant;
import org.chpir.android.roster.Models.Question;

public final class SeedData {

    public static void createSeedCenters() {
        ActiveAndroid.beginTransaction();
        try {
            for (char alp = 'A'; alp <= 'Z'; alp++) {
                Center center = new Center(Long.toString(Math.round(Math.random() * 1000)) + "-"
                        + alp, "Center " + alp);
                center.save();
            }
            ActiveAndroid.setTransactionSuccessful();
        } finally {
            ActiveAndroid.endTransaction();
        }
    }

    public static void createSeedParticipants(Center mCenter) {
        ActiveAndroid.beginTransaction();
        try {
            for (int k = 0; k < 5; k++) {
                Participant participant = new Participant();
                participant.setCenter(mCenter);
                participant.saveParticipant();
            }
            ActiveAndroid.setTransactionSuccessful();
        } finally {
            ActiveAndroid.endTransaction();
        }
        }

    public static void createSeedQuestions(Participant participant) {
        ActiveAndroid.beginTransaction();
        try {
            for (Question.QuestionHeader header : Question.QuestionHeader.values()) {
                Question question = new Question();
                question.setQuestionHeader(header);
                question.setQuestionType(header.getQuestionType());
                question.setText(header.toString().replace("_", " ").toLowerCase());
                question.setResponse("");
                question.setParticipant(participant);
                question.save();
            }
            ActiveAndroid.setTransactionSuccessful();
        } finally {
            ActiveAndroid.endTransaction();
        }
    }

}