package org.chpir.android.roster;

import android.app.Activity;
import android.os.Bundle;

import org.chpir.android.roster.Models.Participant;
import org.chpir.android.roster.Models.Question;
import org.chpir.android.roster.RosterFragments.RosterFragment;
import org.chpir.android.roster.RosterFragments.RosterFragmentGenerator;

import java.util.List;

/**
 * Created by Harry on 11/1/16.
 */
public class ResponseEditorActivity extends Activity{
    private Participant mParticipant;
    private Question mQuestion;
    private RosterFragment mRosterFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        String questionId = getIntent().getStringExtra(ParticipantEditorActivity.EXTRA_QUESTION_ID);
        String participantId = getIntent().getStringExtra(ParticipantEditorActivity.EXTRA_PARTICIPANT_ID);
        mRosterFragment = RosterFragmentGenerator.createQuestionFragment(
                Question.findByIdentifier(questionId).getQuestionType());
        Bundle bundle = new Bundle();
        bundle.putString(ParticipantEditorActivity.EXTRA_QUESTION_ID, questionId);
        bundle.putString(ParticipantEditorActivity.EXTRA_PARTICIPANT_ID, participantId);
        mRosterFragment.setArguments(bundle);
    }
}
