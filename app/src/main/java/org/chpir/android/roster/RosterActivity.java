package org.chpir.android.roster;

import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.AsyncTask;
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
import org.chpir.android.roster.Models.Center;
import org.chpir.android.roster.Models.Participant;
import org.chpir.android.roster.Models.Question;
import org.chpir.android.roster.Models.Response;

import java.util.LinkedHashMap;
import java.util.List;

public class RosterActivity extends AppCompatActivity implements ScrollViewListener {
    public final static String EXTRA_CENTER_ID = "org.chpir.android.roster.center_id";
    public final static String EXTRA_PARTICIPANT_ID = "org.chpir.android.roster.participant_id";
    final String TAG = "RosterActivity";
    final private int PADDING = 2;
    final private int MAX_LINES_PER_ROW = 1;
    final private int HEADER_TEXT_SIZE = 18;
    final private int NON_HEADER_TEXT_SIZE = 15;
    final private int NEW_PARTICIPANT_REQUEST_CODE = 100;
    final private int OLD_PARTICIPANT_REQUEST_CODE = 200;
    private int HEIGHT;
    private int WIDTH;
    private int MARGIN;
    private OHScrollView headerScrollView;
    private OHScrollView contentScrollView;
    private boolean interceptScroll = true;
    private TableLayout mTableLayout;
    private LinearLayout mLinearLayout;
    private Center mCenter;
    private LinkedHashMap<Participant, List<Response>> mParticipantResponses;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_roster);
        String centerId = getIntent().getStringExtra(CenterActivity.EXTRA_CENTER_ID);
        if (centerId != null) {
            mCenter = Center.findByIdentifier(centerId);
            setTitle(mCenter.getName());
            new ParticipantsLoaderTask().execute();
        }

        mTableLayout = (TableLayout) findViewById(R.id.content_table);
        mLinearLayout = (LinearLayout) findViewById(R.id.participant_id);
        headerScrollView = (OHScrollView) findViewById(R.id.header_scroll);
        contentScrollView = (OHScrollView) findViewById(R.id.content_scroll);
        headerScrollView.setScrollViewListener(this);
        contentScrollView.setScrollViewListener(this);
        setDimensions();
        setHeaders();
    }

    private void setDimensions() {
        final float scale = getResources().getDisplayMetrics().density;
        HEIGHT = (int) (50 * scale + 0.5f);
        WIDTH = (int) (150 * scale + 0.5f);
        MARGIN = (int) (0.5 * scale + 0.5f);
    }

    private void setHeaders() {
        TextView idHeader = new TextView(this);
        setLinearLayoutHeaderTextViewAttrs(idHeader);
        idHeader.setText(Question.QuestionHeader.PARTICIPANT_ID.toString());
        LinearLayout participantIDLayout = (LinearLayout) findViewById(R.id.header_1);
        if (participantIDLayout != null) {
            participantIDLayout.addView(idHeader);
        }

        TableRow row = new TableRow(this);
        row.setLayoutParams(new TableLayout.LayoutParams(TableLayout.LayoutParams
                .MATCH_PARENT, TableLayout.LayoutParams.WRAP_CONTENT));
        for (Question.QuestionHeader header : Question.QuestionHeader.values()) {
            if (header != Question.QuestionHeader.PARTICIPANT_ID) {
                TextView headerView = new TextView(this);
                headerView.setText(header.toString());
                setTableRowLayoutHeaderTextViewAttrs(headerView);
                row.addView(headerView);
            }
        }
        TableLayout rosterHeaders = (TableLayout) findViewById(R.id.header_2);
        if (rosterHeaders != null) {
            rosterHeaders.addView(row);
        }
    }

    private void setLinearLayoutHeaderTextViewAttrs(TextView view) {
        setTextViewAttributes(view, getLinearLayoutParams(), ContextCompat.getColor(this,
                R.color.frozenColumnBackground), Color.WHITE, HEADER_TEXT_SIZE,
                MAX_LINES_PER_ROW, Typeface.BOLD);
    }

    private void setTableRowLayoutHeaderTextViewAttrs(TextView view) {
        setTextViewAttributes(view, getTableRowLayoutParams(), ContextCompat.getColor(this,
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

    @NonNull
    private LinearLayout.LayoutParams getLinearLayoutParams() {
        return new LinearLayout.LayoutParams(LinearLayout
                .LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
    }

    @NonNull
    private TableRow.LayoutParams getTableRowLayoutParams() {
        return new TableRow.LayoutParams(TableRow.LayoutParams.MATCH_PARENT,
                TableRow.LayoutParams.WRAP_CONTENT);
    }

    private void displayParticipants() {
        mTableLayout.removeAllViews();
        mLinearLayout.removeAllViews();
        for (Participant participant : mParticipantResponses.keySet()) {
            setParticipantIdView(participant);
            mTableLayout.addView(getParticipantRow(participant));
        }
    }

    private void setParticipantIdView(final Participant participant) {
        TextView idView = new TextView(this);
        setTextViewAttributes(idView, getLinearLayoutParams(), ContextCompat.getColor(this,
                R.color.frozenColumnBackground), Color.WHITE, NON_HEADER_TEXT_SIZE,
                MAX_LINES_PER_ROW, Typeface.NORMAL);
        idView.setText(participant.identifierResponse().getText());
        idView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(RosterActivity.this, ParticipantViewerActivity.class);
                intent.putExtra(EXTRA_PARTICIPANT_ID, participant.getIdentifier());
                startActivityForResult(intent, OLD_PARTICIPANT_REQUEST_CODE);
            }
        });
        mLinearLayout.addView(idView);
    }

    @NonNull
    private TableRow getParticipantRow(Participant participant) {
        TableRow row = new TableRow(this);
        List<Response> responses = mParticipantResponses.get(participant);
        for (int k = 1; k < Question.findAll().size(); k++) {
            TextView view = new TextView(this);
            setTextViewAttributes(view, getTableRowLayoutParams(), Color.WHITE, Color.BLACK,
                    NON_HEADER_TEXT_SIZE, MAX_LINES_PER_ROW, Typeface.NORMAL);
            view.setText(responses.get(k).getLabel());
            row.addView(view);
        }
        return row;
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
                Intent intent = new Intent(this, ParticipantEditorActivity.class);
                intent.putExtra(EXTRA_CENTER_ID, mCenter.getIdentifier());
                startActivityForResult(intent, NEW_PARTICIPANT_REQUEST_CODE);
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
        Log.i(TAG, "requestCode: " + requestCode);
        if (data != null) {
            String participantIdentifier = data.getStringExtra(EXTRA_PARTICIPANT_ID);
            Participant participant = Participant.findByIdentifier(participantIdentifier);
            if (requestCode == NEW_PARTICIPANT_REQUEST_CODE) {
                mParticipantResponses.put(participant, participant.responses());
                setParticipantIdView(participant);
                mTableLayout.addView(getParticipantRow(participant));
            } else if (requestCode == OLD_PARTICIPANT_REQUEST_CODE) {
                int k = 0;
                for (Participant part : mParticipantResponses.keySet()) {
                    if (part.getIdentifier().equals(participant.getIdentifier())) {
                        updateParticipantRow(k, participant);
                        break;
                    }
                    k++;
                }
            }
        }
    }

    private void updateParticipantRow(int index, Participant participant) {
        TableRow row = (TableRow) mTableLayout.getChildAt(index);
        List<Response> responses = mParticipantResponses.get(participant);
        for (int k = 1; k < responses.size(); k++) {
            TextView textView = (TextView) row.getVirtualChildAt(k - 1);
            textView.setText(responses.get(k).getLabel());
        }
        TextView textIdView = (TextView) mLinearLayout.getChildAt(index);
        textIdView.setText(responses.get(0).getLabel());
    }

    private class ParticipantsLoaderTask extends AsyncTask<Void, Void, LinkedHashMap<Participant,
            List<Response>>> {
        private ProgressDialog mDialog = new ProgressDialog(RosterActivity.this);

        @Override
        protected LinkedHashMap<Participant, List<Response>> doInBackground(Void... params) {
            LinkedHashMap<Participant, List<Response>> map = new LinkedHashMap<>();
            for (Participant participant : mCenter.participants()) {
                map.put(participant, participant.responses());
            }
            return map;
        }

        @Override
        protected void onPreExecute() {
            this.mDialog.setTitle(getString(R.string.loading_participants_title));
            this.mDialog.setMessage(getString(R.string.please_wait_dialog_message));
            this.mDialog.show();
        }

        @Override
        protected void onPostExecute(LinkedHashMap<Participant, List<Response>> participants) {
            if (mDialog.isShowing()) {
                mDialog.dismiss();
            }
            mParticipantResponses = participants;
            displayParticipants();
        }
    }

}