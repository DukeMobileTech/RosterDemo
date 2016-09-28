package org.chpir.android.roster;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.v4.app.FragmentManager;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.widget.Button;

import org.chpir.android.roster.Models.Participant;
import org.chpir.android.roster.Models.Question;
import org.chpir.android.roster.Models.QuestionHeader;
import org.chpir.android.roster.RosterFragments.RosterFragment;
import org.chpir.android.roster.RosterFragments.RosterFragmentGenerator;
import org.parceler.Parcels;

import java.util.UUID;

public class ParticipantDetailsActivity extends AppCompatActivity {
    private static final String TAG = "ParticipantDetailsActivity";
    private DrawerLayout mDrawer;
    private NavigationView navigationView;
    private int currentMenuItem = 0;
    private Participant mParticipant;
    private Button mPrevious;
    private Button mNext;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().requestFeature(Window.FEATURE_ACTION_BAR);
        setContentView(R.layout.activity_participant_details);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeButtonEnabled(true);

        mParticipant = new Participant(Question.defaultQuestions(),
                UUID.randomUUID().toString());
        mDrawer = (DrawerLayout) findViewById(R.id.roster_drawer_layout);
        navigationView = (NavigationView) findViewById(R.id.roster_drawer_view);

        setNavigationViewMenu();
        setNavigationViewListener(navigationView);

        mPrevious = (Button) findViewById(R.id.previous_question);
        mNext = (Button) findViewById(R.id.next_question);
        mPrevious.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                updateFragment(currentMenuItem - 1);
            }
        });
        mNext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                updateFragment(currentMenuItem + 1);
            }
        });
        Button saveButton = (Button) findViewById(R.id.save_participant);
        saveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(ParticipantDetailsActivity.this, RosterActivity.class);
                intent.putExtra("newParticipant", Parcels.wrap(mParticipant));
                setResult(100, intent);
                finish();
            }
        });
    }

    @Override
    protected void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
    }

    private void setNavigationViewMenu() {
        Menu menu = navigationView.getMenu();
        int index = 0;
        for (QuestionHeader header : QuestionHeader.values()) {
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

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        super.onCreateOptionsMenu(menu);
        getMenuInflater().inflate(R.menu.drawer_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                mDrawer.openDrawer(GravityCompat.START);
                return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onResume() {
        super.onResume();
        updateFragment(currentMenuItem);
    }

    private void updateFragment(int position) {
        currentMenuItem = position;
        RosterFragment fragment = RosterFragmentGenerator.createQuestionFragment(
                QuestionHeader.getByIndex(position));
        Bundle bundle = new Bundle();
        bundle.putParcelable("Question", Parcels.wrap(mParticipant.getQuestions().get(position)));
        fragment.setArguments(bundle);
        switchOutFragment(fragment);
        hideNavigationButtons();
        checkMenuItem(navigationView.getMenu().getItem(currentMenuItem));
    }

    private void switchOutFragment(RosterFragment fragment) {
        FragmentManager manager = getSupportFragmentManager();
        if (manager.findFragmentById(R.id.roster_item_container) == null) {
            manager.beginTransaction().add(R.id.roster_item_container, fragment).commit();
        } else {
            manager.beginTransaction().replace(R.id.roster_item_container, fragment).commit();
        }
    }

    private void hideNavigationButtons() {
        Log.i(TAG, "currentMenuItem: " + currentMenuItem);
        Log.i(TAG, "num of items: " + mParticipant.getQuestions().size());
        if (currentMenuItem == 0) {
            if (mPrevious != null) mPrevious.setVisibility(View.INVISIBLE);
        } else if (currentMenuItem == mParticipant.getQuestions().size() - 1) {
            if (mNext != null) mNext.setVisibility(View.INVISIBLE);
        } else {
            if (mNext != null) mNext.setVisibility(View.VISIBLE);
            if (mPrevious != null) mPrevious.setVisibility(View.VISIBLE);
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

}