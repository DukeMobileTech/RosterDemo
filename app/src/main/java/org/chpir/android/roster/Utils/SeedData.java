package org.chpir.android.roster.Utils;

import com.activeandroid.ActiveAndroid;

import org.chpir.android.roster.Models.Center;
import org.chpir.android.roster.Models.Participant;
import org.chpir.android.roster.Models.Question;
import org.chpir.android.roster.Models.Response;

import java.util.Random;

public final class SeedData {
    private static final String IPSUM = "Now that there is the Tec-9, a crappy spray gun from " +
            "South Miami. This gun is advertised as the most popular gun in American crime. Do " +
            "you believe that shit? It actually says that in the little book that comes with it: " +
            "the most popular gun in American crime. Like they're actually proud of that shit.";

    public static void seedDatabase() {

        ActiveAndroid.beginTransaction();
        try {
            for (Question.QuestionHeader header : Question.QuestionHeader.values()) {
                Question question = new Question();
                question.setQuestionHeader(header);
                question.setQuestionType(header.getQuestionType());
                question.setText(header.toString().replace("_", " ").toLowerCase());
                question.save();
            }
            for (char alp = 'A'; alp <= 'Z'; alp++) {
                Center center = new Center(Long.toString(Math.round(Math.random() * 1000)) + "-"
                        + alp, "Center " + alp);
                center.save();
                for (int k = 0; k < 100; k++) {
                    Participant participant = new Participant();
                    participant.setCenter(center);
                    participant.save();
                    responseFactory(participant);
                }
            }
            ActiveAndroid.setTransactionSuccessful();
        } finally {
            ActiveAndroid.endTransaction();
        }

    }

    private static String generateResponse(Response response) {
        Question.QuestionType type = response.getQuestion().getQuestionType();
        switch (type) {
            case FREE_RESPONSE:
                if (new Random().nextBoolean()) {
                    return response.getQuestion().getText() + " " + IPSUM.substring(
                            new Random().nextInt(IPSUM.length() - 1));
                } else {
                    return response.getQuestion().getText();
                }
            case DATE:
                return new Random().nextInt(12) + "-" + new Random().nextInt(31) + "-" +
                        (new Random().nextInt(2016 - 1980) + 1980);
            case INTEGER:
                return new Random().nextInt(100) + "";
            case SELECT_ONE:
                return new Random().nextInt(2) + "";
            default:
                return "";
        }
    }

    public static void createDefaultResponses(Participant participant) {
        ActiveAndroid.beginTransaction();
        try {
            responseFactory(participant);
            ActiveAndroid.setTransactionSuccessful();
        } finally {
            ActiveAndroid.endTransaction();
        }
    }

    private static void responseFactory(Participant participant) {
        for (Question question : Question.findAll()) {
            Response response = new Response();
            response.setParticipant(participant);
            response.setQuestion(question);
            response.setText(generateResponse(response));
            response.save();
        }
    }

}