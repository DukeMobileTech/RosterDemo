package org.chpir.android.roster;

import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
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

import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.List;

public class RosterActivity extends AppCompatActivity implements ScrollViewListener {
    public final static String EXTRA_CENTER_ID = "org.chpir.android.roster.center_id";
    public final static String EXTRA_PARTICIPANT_ID = "org.chpir.android.roster.participant_id";
    public final static String EXTRA_QUESTION_HEADER = "org.chpir.android.roster.question_header";
    final private String TAG = "RosterActivity";
    final private int HEADER_TEXT_SIZE = 15;
    final private int NON_HEADER_TEXT_SIZE = 15;
    final private int NEW_PARTICIPANT_REQUEST_CODE = 100;
    final private int OLD_PARTICIPANT_REQUEST_CODE = 200;
    final private int DEFAULT_WIDTH = Question.QuestionHeader.PARTICIPANT_ID.toString().length();
    private boolean interceptScroll = true;
    private OHScrollView headerScrollView;
    private OHScrollView contentScrollView;
    private TableLayout dataLayout;
    private TableLayout identifierLayout;
    private Center mCenter;
    private List<Integer> colWidthList;
    private LinkedHashMap<Participant, List<Response>> mParticipantResponses;
    private int numParticipants;
    private int numQuestions;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_roster);
        String centerId = getIntent().getStringExtra(CenterActivity.EXTRA_CENTER_ID);
        if (centerId != null) {
            mCenter = Center.findByIdentifier(centerId);
            setTitle(mCenter.getName());
            numParticipants = mCenter.participantCount();
            numQuestions = Question.questionCount();
            new ParticipantsLoaderTask().execute();
        }

        dataLayout = (TableLayout) findViewById(R.id.content_table);
        identifierLayout = (TableLayout) findViewById(R.id.participant_id);
        headerScrollView = (OHScrollView) findViewById(R.id.header_scroll);
        contentScrollView = (OHScrollView) findViewById(R.id.content_scroll);
        headerScrollView.setScrollViewListener(this);
        contentScrollView.setScrollViewListener(this);
        setHeaders();
        drawTableView();
    }

    private void setHeaders() {
        TextView idHeader = new TextView(this);
        setLinearLayoutHeaderTextViewAttrs(idHeader, DEFAULT_WIDTH);
        idHeader.setText(Question.QuestionHeader.PARTICIPANT_ID.toString());
        LinearLayout participantIDLayout = (LinearLayout) findViewById(R.id.header_1);
        if (participantIDLayout != null) {
            participantIDLayout.addView(idHeader);
        }
        colWidthList = new ArrayList<>();
        TableRow row = new TableRow(this);
        row.setLayoutParams(new TableLayout.LayoutParams(TableLayout.LayoutParams
                .MATCH_PARENT, TableLayout.LayoutParams.WRAP_CONTENT));
        for (Question.QuestionHeader header : Question.QuestionHeader.values()) {
            if (header != Question.QuestionHeader.PARTICIPANT_ID) {
                int colWidth = header.toString().length();
                colWidthList.add(colWidth);
                TextView headerView = new TextView(this);
                headerView.setText(header.toString());
                setTableRowLayoutHeaderTextViewAttrs(headerView, colWidth);
                setFirstRowListener(headerView, header);
                row.addView(headerView);
            }
        }
        TableLayout rosterHeaders = (TableLayout) findViewById(R.id.header_2);
        if (rosterHeaders != null) {
            rosterHeaders.addView(row);
        }
    }

    private void drawTableView() {
        drawFirstColumnViews();
        drawRowViews();
    }

    private void setLinearLayoutHeaderTextViewAttrs(TextView view, int colWidth) {
        setTextViewAttributes(view, ContextCompat.getColor(this, R.color.frozenColumnBackground),
                Color.WHITE, HEADER_TEXT_SIZE, colWidth, Typeface.BOLD);
    }

    private void setTableRowLayoutHeaderTextViewAttrs(TextView view, int colWidth) {
        setTextViewAttributes(view, ContextCompat.getColor(this, R.color.frozenColumnBackground),
                Color.WHITE, HEADER_TEXT_SIZE, colWidth, Typeface.BOLD);
    }

    private void setFirstRowListener(TextView headerView, final Question.QuestionHeader header) {
        headerView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(RosterActivity.this, ResponseViewerActivity.class);
                intent.putExtra(EXTRA_CENTER_ID, mCenter.getIdentifier());
                intent.putExtra(EXTRA_QUESTION_HEADER, header);
                startActivity(intent);
            }
        });
    }

    private void drawFirstColumnViews() {
        for (int j = 0; j < numParticipants; j++) {
            addTextViewToLinearLayout(DEFAULT_WIDTH);
        }
    }

    private void drawRowViews() {
        for (int j = 0; j < numParticipants; j++) {
            addTableRowToTableLayout();
        }
    }

    private void setTextViewAttributes(TextView view, int backgroundColor, int textColor,
                                       int textSize, int colWidth, int typeface) {
        int minimumHeight = 50;
        int margin = 1;
        int padding = 5;
        int maxLinesPerRow = 5;

        view.setMinimumHeight(minimumHeight);
        view.setEms(colWidth);
        view.setTextColor(textColor);
        view.setGravity(Gravity.CENTER_HORIZONTAL);
        view.setPadding(padding, padding, padding, padding);
        view.setTypeface(view.getTypeface(), typeface);
        view.setTextSize(textSize);
        view.setMaxLines(maxLinesPerRow);
        view.setBackgroundColor(backgroundColor);
        view.setEllipsize(TextUtils.TruncateAt.END);
        TableRow.LayoutParams params = new TableRow.LayoutParams(
                TableRow.LayoutParams.WRAP_CONTENT, TableRow.LayoutParams.WRAP_CONTENT);
        params.setMargins(margin, margin, margin, margin);
        view.setLayoutParams(params);
    }

    private void addTextViewToLinearLayout(int colWidth) {
        TableRow row = new TableRow(this);
        TextView idView = new TextView(this);
        setTextViewAttributes(idView, ContextCompat.getColor(this, R.color.frozenColumnBackground),
                Color.WHITE, NON_HEADER_TEXT_SIZE, colWidth, Typeface.NORMAL);
        row.addView(idView);
        identifierLayout.addView(row);
    }

    private void addTableRowToTableLayout() {
        TableRow row = new TableRow(this);
        for (int k = 1; k < numQuestions; k++) {
            TextView view = new TextView(this);
            setTextViewAttributes(view, Color.WHITE, Color.BLACK,
                    NON_HEADER_TEXT_SIZE, colWidthList.get(k - 1), Typeface.NORMAL);
            row.addView(view);
        }
        dataLayout.addView(row);
    }

    private void displayData() {
        int k = 0;
        for (Participant participant : mParticipantResponses.keySet()) {
            setFirstColumnView(participant, k);
            setRowView(participant, k);
            k++;
        }
    }

    private void setFirstColumnView(final Participant participant, int index) {
        TableRow row = (TableRow) identifierLayout.getChildAt(index);
        TextView textView = (TextView) row.getVirtualChildAt(0);
        textView.setText(mParticipantResponses.get(participant).get(0).getLabel());
        textView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(RosterActivity.this, ParticipantViewerActivity.class);
                intent.putExtra(EXTRA_PARTICIPANT_ID, participant.getIdentifier());
                startActivityForResult(intent, OLD_PARTICIPANT_REQUEST_CODE);
            }
        });
    }

    private void setRowView(Participant participant, int index) {
        TableRow row = (TableRow) dataLayout.getChildAt(index);
        List<Response> responses = mParticipantResponses.get(participant);
        List<Integer> lines = new ArrayList<>();
        List<TextView> views = new ArrayList<>();
        TableRow idRow = (TableRow) identifierLayout.getChildAt(index);
        TextView idView = (TextView) idRow.getVirtualChildAt(0);
        lines.add(idView.getLineCount());
        views.add(idView);
        for (int k = 1; k < responses.size(); k++) {
            TextView textView = (TextView) row.getVirtualChildAt(k - 1);
            textView.setText(responses.get(k).getLabel());
            lines.add(textView.getLineCount());
            views.add(textView);
        }
        int max = Collections.max(lines);
        for (TextView view : views) {
            view.setLines(max);
        }
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
        if (data != null) {
            String participantIdentifier = data.getStringExtra(EXTRA_PARTICIPANT_ID);
            Participant participant = Participant.findByIdentifier(participantIdentifier);
            if (requestCode == NEW_PARTICIPANT_REQUEST_CODE) {
                mParticipantResponses.put(participant, participant.responses());
                addNewView(participant, numParticipants);
                numParticipants++;
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

    private void addNewView(Participant participant, int index) {
        addTextViewToLinearLayout(DEFAULT_WIDTH);
        addTableRowToTableLayout();
        setFirstColumnView(participant, index);
        setRowView(participant, index);
    }

    private void updateParticipantRow(int index, Participant participant) {
        TableRow row = (TableRow) dataLayout.getChildAt(index);
        List<Response> responses = participant.responses();
        mParticipantResponses.put(participant, responses);
        for (int k = 1; k < responses.size(); k++) {
            TextView textView = (TextView) row.getVirtualChildAt(k - 1);
            textView.setText(responses.get(k).getLabel());
        }
        TableRow idRow = (TableRow) identifierLayout.getChildAt(index);
        TextView textIdView = (TextView) idRow.getVirtualChildAt(0);
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
            mParticipantResponses = participants;
            displayData();
            if (mDialog.isShowing()) {
                mDialog.dismiss();
            }
        }
    }

}