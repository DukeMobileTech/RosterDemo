package org.chpir.android.roster;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.Window;

import org.chpir.android.roster.Models.Question;
import org.chpir.android.roster.RosterFragments.RosterFragment;
import org.chpir.android.roster.RosterFragments.RosterFragmentGenerator;

public class ResponseEditorActivity extends AppCompatActivity {
    private String mQuestionId;
    private String mCenterId;
    private RosterFragment mRosterFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().requestFeature(Window.FEATURE_ACTION_BAR);
        setContentView(R.layout.activity_participant_editor);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setHomeButtonEnabled(true);
        }
        mCenterId = getIntent().getStringExtra(RosterActivity.EXTRA_CENTER_ID);
        mQuestionId = getIntent().getStringExtra(ParticipantEditorActivity.EXTRA_QUESTION_ID);
        String participantId = getIntent().getStringExtra(ParticipantEditorActivity
                .EXTRA_PARTICIPANT_ID);

        mRosterFragment = RosterFragmentGenerator.createQuestionFragment(
                Question.findByIdentifier(mQuestionId).getQuestionType());
        Bundle bundle = new Bundle();
        bundle.putString(ParticipantEditorActivity.EXTRA_QUESTION_ID, mQuestionId);
        bundle.putString(ParticipantEditorActivity.EXTRA_PARTICIPANT_ID, participantId);
        mRosterFragment.setArguments(bundle);
        switchOutFragment(mRosterFragment);
    }

    private void switchOutFragment(RosterFragment fragment) {
        FragmentManager manager = getSupportFragmentManager();
        if (manager.findFragmentById(R.id.roster_item_container) == null) {
            manager.beginTransaction().add(R.id.roster_item_container, fragment).commit();
        } else {
            manager.beginTransaction().replace(R.id.roster_item_container, fragment).commit();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        super.onCreateOptionsMenu(menu);
        getMenuInflater().inflate(R.menu.response_editor_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.save_new_response:
                saveResponse();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    private void saveResponse() {
        mRosterFragment.getResponse().save();
        Class callingClass = ResponseViewerActivity.class;
        if (getCallingActivity() != null) {
            callingClass = getCallingActivity().getClass();
        }
        Intent intent = new Intent(ResponseEditorActivity.this, callingClass);
        intent.putExtra(RosterActivity.EXTRA_CENTER_ID, mCenterId);
        intent.putExtra(RosterActivity.EXTRA_QUESTION_HEADER, Question.findByIdentifier
                (mQuestionId).getQuestionHeader());
        startActivity(intent);
        finish();
    }
}