package org.chpir.android.roster.RosterFragments;

import org.chpir.android.roster.Models.Question;

public class RosterFragmentGenerator {

    public static RosterFragment createQuestionFragment(Question.QuestionType type) {
        switch (type) {
            case FREE_RESPONSE:
                return new FreeResponseFragment();
            case INTEGER:
                return new IntegerFragment();
            case DATE:
                return new DateFragment();
            case SELECT_ONE:
                return new SelectOneFragment();
            default:
                return new FreeResponseFragment();
        }
    }
}