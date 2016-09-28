package org.chpir.android.roster;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.util.Log;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;

import org.chpir.android.roster.CustomViews.OHScrollView;
import org.chpir.android.roster.Listeners.ScrollViewListener;
import org.chpir.android.roster.Models.Participant;
import org.chpir.android.roster.Models.Question;
import org.chpir.android.roster.Models.QuestionHeader;
import org.parceler.Parcels;

import java.util.ArrayList;
import java.util.UUID;

public class RosterActivity extends AppCompatActivity implements ScrollViewListener {
    final String TAG = "RosterActivity";
    final private int PADDING = 2;
    final private int MAX_LINES_PER_ROW = 1;
    final private int HEADER_TEXT_SIZE = 18;
    final private int NON_HEADER_TEXT_SIZE = 15;
    final private int NEW_PARTICIPANT_REQUEST_CODE = 100;
    private int HEIGHT;
    private int WIDTH;
    private int MARGIN;
    private OHScrollView headerScrollView;
    private OHScrollView contentScrollView;
    private boolean interceptScroll = true;
    private ArrayList<Participant> mParticipants;
    private TableLayout mTableLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_roster);
        setTitle(getIntent().getStringExtra("CenterName"));
        initializeParticipants();
        setDimensions();
        setHeaders();

        headerScrollView = (OHScrollView) findViewById(R.id.header_scroll);
        contentScrollView = (OHScrollView) findViewById(R.id.content_scroll);
        headerScrollView.setScrollViewListener(this);
        contentScrollView.setScrollViewListener(this);

        for (Participant participant : mParticipants) {
            mTableLayout = (TableLayout) findViewById(R.id.content_table);
            setParticipantIdView(participant);
            mTableLayout.addView(getParticipantRow(participant));
        }

    }

    private void initializeParticipants() {
        mParticipants = new ArrayList<>();
        for (int k = 0; k < 5; k++) {
            mParticipants.add(new Participant(Question.defaultQuestions(),
                    UUID.randomUUID().toString()));
        }
    }

    private void setDimensions() {
        final float scale = getResources().getDisplayMetrics().density;
        HEIGHT = (int) (50 * scale + 0.5f);
        WIDTH = (int) (150 * scale + 0.5f);
        MARGIN = (int) (0.5 * scale + 0.5f);
    }

    private void setHeaders() {
        LinearLayout participantIDLayout = (LinearLayout) findViewById(R.id.header_1);
        TextView idHeader = new TextView(this);
        setLinearLayoutHeaderTextViewAttrs(idHeader);
        idHeader.setText(QuestionHeader.PARTICIPANT_ID.toString());
        participantIDLayout.addView(idHeader);

        TableLayout rosterHeaders = (TableLayout) findViewById(R.id.header_2);
        TableRow row = new TableRow(this);
        row.setLayoutParams(new TableLayout.LayoutParams(TableLayout.LayoutParams
                .MATCH_PARENT, TableLayout.LayoutParams.WRAP_CONTENT));
        for (QuestionHeader header : QuestionHeader.values()) {
            if (header != QuestionHeader.PARTICIPANT_ID) {
                TextView headerView = new TextView(this);
                headerView.setText(header.toString());
                setTableRowLayoutHeaderTextViewAttrs(headerView);
                row.addView(headerView);
                Log.i(TAG, "Added header: " + header);
            }
        }
        rosterHeaders.addView(row);
    }

    private void setParticipantIdView(final Participant participant) {
        LinearLayout linearLayout = (LinearLayout) findViewById(R.id.participant_id);
        LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(LinearLayout
                .LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
        TextView idView = new TextView(this);
        setTextViewAttributes(idView, layoutParams, ContextCompat.getColor(this,
                R.color.frozenColumnBackground), Color.WHITE, NON_HEADER_TEXT_SIZE,
                MAX_LINES_PER_ROW, Typeface.NORMAL);
        idView.setText(participant.getIdentifier().substring(0, 15));
        idView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(RosterActivity.this, ParticipantActivity.class);
                intent.putExtra("Participant", Parcels.wrap(participant));
                startActivity(intent);
            }
        });
        linearLayout.addView(idView);
    }

    @NonNull
    private TableRow getParticipantRow(Participant participant) {
        TableRow row = new TableRow(this);
        TableRow.LayoutParams params = new TableRow.LayoutParams(TableRow.LayoutParams
                .MATCH_PARENT, TableRow.LayoutParams.WRAP_CONTENT);

        for (int k = 1; k < participant.getQuestions().size(); k++) {
            TextView view = new TextView(this);
            setTextViewAttributes(view, params, Color.WHITE, Color.BLACK,
                    NON_HEADER_TEXT_SIZE, MAX_LINES_PER_ROW, Typeface.NORMAL);
            view.setText(participant.getQuestions().get(k).getResponse());
            row.addView(view);
        }
        return row;
    }

    private void setLinearLayoutHeaderTextViewAttrs(TextView view) {
        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(LinearLayout
                .LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
        setTextViewAttributes(view, params, ContextCompat.getColor(this,
                R.color.frozenColumnBackground), Color.WHITE, HEADER_TEXT_SIZE,
                MAX_LINES_PER_ROW, Typeface.BOLD);
    }

    private void setTableRowLayoutHeaderTextViewAttrs(TextView view) {
        TableRow.LayoutParams params = new TableRow.LayoutParams(TableRow.LayoutParams
                .MATCH_PARENT, TableRow.LayoutParams.WRAP_CONTENT);
        setTextViewAttributes(view, params, ContextCompat.getColor(this,
                R.color.frozenColumnBackground), Color.WHITE, HEADER_TEXT_SIZE, MAX_LINES_PER_ROW,
                Typeface.BOLD);
    }

    private void setTextViewAttributes(TextView view, ViewGroup.MarginLayoutParams params, int
            backgroundColor, int textColor, int textSize, int numLines, int typeface) {
        params.setMargins(MARGIN, MARGIN, MARGIN, MARGIN);
        view.setHeight(HEIGHT);
        view.setWidth(WIDTH);
        view.setTextColor(textColor);
        view.setGravity(Gravity.CENTER);
        view.setPadding(PADDING, PADDING, PADDING, PADDING);
        view.setTypeface(view.getTypeface(), typeface);
        view.setTextSize(textSize);
        view.setMaxLines(numLines);
        view.setBackgroundColor(backgroundColor);
        view.setEllipsize(TextUtils.TruncateAt.END);
        view.setLayoutParams(params);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.roster_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.add_participant:
                startActivityForResult(new Intent(this, ParticipantDetailsActivity.class),
                        NEW_PARTICIPANT_REQUEST_CODE);
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    public void onScrollChanged(OHScrollView scrollView, int x, int y, int oldx, int oldy) {
        if (interceptScroll) {
            interceptScroll = false;
            if (scrollView == headerScrollView) {
                contentScrollView.onOverScrolled(x, y, true, true);
            } else if (scrollView == contentScrollView) {
                headerScrollView.onOverScrolled(x, y, true, true);
            }
            interceptScroll = true;
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == NEW_PARTICIPANT_REQUEST_CODE) {
            Participant participant = Parcels.unwrap(data.getParcelableExtra("newParticipant"));
            mParticipants.add(participant);
            setParticipantIdView(participant);
            mTableLayout.addView(getParticipantRow(participant));
        }
    }

}