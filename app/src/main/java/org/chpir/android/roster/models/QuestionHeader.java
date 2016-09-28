package org.chpir.android.roster.Models;

public enum QuestionHeader {
    PARTICIPANT_ID(QuestionType.FREE_RESPONSE, 0), AGE(QuestionType.INTEGER, 1),
    GENDER(QuestionType.SELECT_ONE, 2), ARRIVAL_DATE(QuestionType.DATE, 3),
    NUM_HOURS_PER_WEEK(QuestionType.INTEGER, 4), NUM_WEEKS_ABSENT_PER_YEAR(QuestionType.INTEGER, 5),
    ASSIGNED_GROUP(QuestionType.FREE_RESPONSE, 6), TIME_IN_GROUP(QuestionType.INTEGER, 7),
    NUM_PREVIOUS_GROUPS(QuestionType.INTEGER, 8), PREVIOUS_CARE(QuestionType.FREE_RESPONSE, 9),
    PRIMARY_CAREGIVER(QuestionType.FREE_RESPONSE, 10), TIME_TOGETHER(QuestionType.INTEGER, 11),
    NUM_SIBLINGS(QuestionType.INTEGER, 12), NUM_SIBLINGS_IN_PROGRAM(QuestionType.INTEGER, 13),
    SIBLINGS_GROUPS(QuestionType.FREE_RESPONSE, 14), VACCINATION(QuestionType.SELECT_ONE, 15),
    SCHOOL(QuestionType.SELECT_ONE, 16), DISABILITY(QuestionType.SELECT_ONE, 17);

    QuestionHeader(QuestionType questionType, int position) {
        this.questionType = questionType;
        this.position = position;
    }

    public enum QuestionType {
        SELECT_ONE, FREE_RESPONSE, DATE, INTEGER;
    }

    private final QuestionType questionType;
    private final int position;

    public QuestionType getQuestionType() {
        return questionType;
    }

    public static QuestionHeader.QuestionType getByIndex(int pos) {
        for (QuestionHeader questionHeader : QuestionHeader.values()) {
            if (questionHeader.position == pos) {
                return questionHeader.questionType;
            }
        }
        return null;
    }
}