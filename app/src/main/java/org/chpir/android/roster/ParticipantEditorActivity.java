package org.chpir.android.roster;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.v4.app.FragmentManager;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.Window;

import org.chpir.android.roster.Models.Center;
import org.chpir.android.roster.Models.Participant;
import org.chpir.android.roster.Models.Question;
import org.chpir.android.roster.RosterFragments.RosterFragment;
import org.chpir.android.roster.RosterFragments.RosterFragmentGenerator;
import org.chpir.android.roster.Utils.SeedData;

import java.util.List;

public class ParticipantEditorActivity extends AppCompatActivity {
    public final static String EXTRA_QUESTION_ID = "org.chpir.android.roster.question_id";
    public final static String EXTRA_PARTICIPANT_ID = "org.chpir.android.roster.participant_id";
    private static final String TAG = "ParticipantEditorActivity";
    private DrawerLayout mDrawer;
    private NavigationView navigationView;
    private int currentMenuItem = 0;
    private Participant mParticipant;
    private List<Question> mQuestions;
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
        setTitle(getString(R.string.new_participant));

        String centerId = getIntent().getStringExtra(RosterActivity.EXTRA_CENTER_ID);
        String participantId = getIntent().getStringExtra(ParticipantViewerActivity
                .EXTRA_PARTICIPANT_ID);
        if (participantId == null && centerId != null) {
            Log.i(TAG, "Number of questions: " + Question.findAll().size());
            new NewParticipantTask().execute(centerId);
        } else {
            mParticipant = Participant.findByIdentifier(participantId);
        }
        mQuestions = Question.findAll();
        mDrawer = (DrawerLayout) findViewById(R.id.roster_drawer_layout);
        navigationView = (NavigationView) findViewById(R.id.roster_drawer_view);
        setNavigationViewMenu();
        setNavigationViewListener(navigationView);
    }

    @Override
    protected void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
    }

    private void setNavigationViewMenu() {
        Menu menu = navigationView.getMenu();
        int index = 0;
        for (Question.QuestionHeader header : Question.QuestionHeader.values()) {
            menu.add(R.id.drawer_menu_group, index, Menu.NONE, header.toString())
                    .setShowAsAction(MenuItem.SHOW_AS_ACTION_ALWAYS);
            index++;
        }
        menu.getItem(currentMenuItem).setChecked(true);
    }

    private void setNavigationViewListener(final NavigationView navigationView) {
        navigationView.setNavigationItemSelectedListener(
                new NavigationView.OnNavigationItemSelectedListener() {

                    @Override
                    public boolean onNavigationItemSelected(MenuItem menuItem) {
                        mDrawer.closeDrawers();
                        updateFragment(menuItem.getItemId());
                        return true;
                    }
                });
    }

    private void updateFragment(int position) {
        if (mParticipant != null) {
            currentMenuItem = position;
            mRosterFragment = RosterFragmentGenerator.createQuestionFragment(
                    Question.QuestionHeader.getByIndex(position).getQuestionType());
            Bundle bundle = new Bundle();
            bundle.putString(EXTRA_QUESTION_ID, mQuestions.get(position).getIdentifier());
            bundle.putString(EXTRA_PARTICIPANT_ID, mParticipant.getIdentifier());
            mRosterFragment.setArguments(bundle);
            switchOutFragment(mRosterFragment);
            invalidateOptionsMenu();
            checkMenuItem(navigationView.getMenu().getItem(currentMenuItem));
        }
    }

    private void switchOutFragment(RosterFragment fragment) {
        FragmentManager manager = getSupportFragmentManager();
        if (manager.findFragmentById(R.id.roster_item_container) == null) {
            manager.beginTransaction().add(R.id.roster_item_container, fragment).commit();
        } else {
            manager.beginTransaction().replace(R.id.roster_item_container, fragment).commit();
        }
    }

    private void checkMenuItem(MenuItem current) {
        unCheckAllMenuItems(navigationView);
        current.setChecked(true);
    }

    private void unCheckAllMenuItems(NavigationView navigationView) {
        final Menu menu = navigationView.getMenu();
        for (int i = 0; i < menu.size(); i++) {
            MenuItem item = menu.getItem(i);
            if (item.isChecked()) {
                item.setChecked(false);
            }
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        super.onCreateOptionsMenu(menu);
        getMenuInflater().inflate(R.menu.participant_editor_menu, menu);
        return true;
    }

    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {
        if (currentMenuItem == 0) {
            menu.findItem(R.id.menu_item_previous).setVisible(false);
        } else if (currentMenuItem == mQuestions.size() - 1) {
            menu.findItem(R.id.menu_item_next).setVisible(false);
        }
        return super.onPrepareOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                mDrawer.openDrawer(GravityCompat.START);
                return true;
            case R.id.save_new_participant:
                saveParticipant();
                return true;
            case R.id.menu_item_next:
                mRosterFragment.getResponse().save();
                updateFragment(currentMenuItem + 1);
                return true;
            case R.id.menu_item_previous:
                mRosterFragment.getResponse().save();
                updateFragment(currentMenuItem - 1);
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    private void saveParticipant() {
        mRosterFragment.getResponse().save();
        Class callingClass = RosterActivity.class;
        if (getCallingActivity() != null) {
            callingClass = getCallingActivity().getClass();
        }
        Intent intent = new Intent(ParticipantEditorActivity.this, callingClass);
        intent.putExtra(EXTRA_PARTICIPANT_ID, mParticipant.getIdentifier());
        setResult(100, intent);
        finish();
    }

    @Override
    protected void onResume() {
        super.onResume();
        updateFragment(currentMenuItem);
    }

    private class NewParticipantTask extends AsyncTask<String, Void, Participant> {
        private ProgressDialog mDialog = new ProgressDialog(ParticipantEditorActivity.this);

        @Override
        protected Participant doInBackground(String... params) {
            Participant participant = new Participant();
            participant.setCenter(Center.findByIdentifier(params[0]));
            participant.save();
            SeedData.createDefaultResponses(participant);
            return participant;
        }

        @Override
        protected void onPreExecute() {
            this.mDialog.setTitle(getString(R.string.participant_responses_title));
            this.mDialog.setMessage(getString(R.string.please_wait_dialog_message));
            this.mDialog.show();
        }

        @Override
        protected void onPostExecute(Participant result) {
            if (mDialog.isShowing()) {
                mDialog.dismiss();
            }
            mParticipant = result;
            updateFragment(0);
        }
    }

}