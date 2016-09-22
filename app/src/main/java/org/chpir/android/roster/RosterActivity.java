package org.chpir.android.roster;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
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

import org.chpir.android.roster.custom_views.OHScrollView;
import org.chpir.android.roster.listeners.ScrollViewListener;

public class RosterActivity extends Activity implements ScrollViewListener {
    final String TAG = "RosterActivity";
    final private int PADDING = 2;
    final private int MAX_LINES_PER_ROW = 1;
    final private int HEADER_TEXT_SIZE = 18;
    final private int NON_HEADER_TEXT_SIZE = 15;
    private int HEIGHT;
    private int WIDTH;
    private int MARGIN;
    private OHScrollView headerScrollView;
    private OHScrollView contentScrollView;
    private boolean interceptScroll = true;
    private String[] mHeaders = {"Participant ID", "Age", "Gender", "Arrival Date", "# Hours/Week",
            "# Weeks Absent/Year", "Assigned Group", "Time in Group", "# Previous Groups",
            "Previous Care", "Primary Caregiver", "Time Together", "# Siblings",
            "# of Siblings in Program", "Siblings Groups", "Vaccination", "School",
            "Disability"};

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_roster);
        setTitle(getIntent().getStringExtra("CenterName"));
        setDimensions();
        setHeaders();

        headerScrollView = (OHScrollView) findViewById(R.id.header_scroll);
        contentScrollView = (OHScrollView) findViewById(R.id.content_scroll);
        headerScrollView.setScrollViewListener(this);
        contentScrollView.setScrollViewListener(this);

        for (int k = 0; k < 100; k++) {
            LinearLayout linearLayout = (LinearLayout) findViewById(R.id.participant_id);
            TableLayout tableLayout = (TableLayout) findViewById(R.id.content_table);
            LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(LinearLayout
                    .LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
            TextView idView = new TextView(this);
            setTextViewAttributes(idView, layoutParams, ContextCompat.getColor(this,
                    R.color.grayBackground), NON_HEADER_TEXT_SIZE, MAX_LINES_PER_ROW,
                    Typeface.NORMAL);
            idView.setText(mHeaders[0] + " " + k);
            idView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    startActivity(new Intent(RosterActivity.this, ParticipantDetailsActivity
                            .class));
                }
            });
            linearLayout.addView(idView);

            TableRow row = new TableRow(this);
            TableRow.LayoutParams params = new TableRow.LayoutParams(TableRow.LayoutParams
                    .MATCH_PARENT, TableRow.LayoutParams.WRAP_CONTENT);

            for (int j = 1; j < mHeaders.length; j++) {
                TextView view = new TextView(this);
                setTextViewAttributes(view, params, Color.WHITE, NON_HEADER_TEXT_SIZE,
                        MAX_LINES_PER_ROW, Typeface.NORMAL);
                view.setText(mHeaders[j] + " " + k);
                row.addView(view);
            }

            tableLayout.addView(row);
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
        idHeader.setText(mHeaders[0]);
        participantIDLayout.addView(idHeader);

        TableLayout rosterHeaders = (TableLayout) findViewById(R.id.header_2);
        TableRow row = new TableRow(this);
        row.setLayoutParams(new TableLayout.LayoutParams(TableLayout.LayoutParams
                .MATCH_PARENT, TableLayout.LayoutParams.WRAP_CONTENT));
        for (int i = 1; i < mHeaders.length; i++) {
            TextView header = new TextView(this);
            header.setText(mHeaders[i]);
            setTableRowLayoutHeaderTextViewAttrs(header);
            row.addView(header);
            Log.i(TAG, "Added header: " + mHeaders[i]);
        }
        rosterHeaders.addView(row);
    }

    private void setTextViewAttributes(TextView view, ViewGroup.MarginLayoutParams params, int
            color, int textSize, int numLines, int typeface) {
        params.setMargins(MARGIN, MARGIN, MARGIN, MARGIN);
        view.setHeight(HEIGHT);
        view.setWidth(WIDTH);
        view.setTextColor(Color.BLACK);
        view.setGravity(Gravity.CENTER);
        view.setPadding(PADDING, PADDING, PADDING, PADDING);
        view.setTypeface(view.getTypeface(), typeface);
        view.setTextSize(textSize);
        view.setMaxLines(numLines);
        view.setBackgroundColor(color);
        view.setEllipsize(TextUtils.TruncateAt.END);
        view.setLayoutParams(params);
    }

    private void setLinearLayoutHeaderTextViewAttrs(TextView view) {
        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(LinearLayout
                .LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
        setTextViewAttributes(view, params, ContextCompat.getColor(this, R.color.grayBackground),
                HEADER_TEXT_SIZE, MAX_LINES_PER_ROW, Typeface.BOLD);
    }

    private void setTableRowLayoutHeaderTextViewAttrs(TextView view) {
        TableRow.LayoutParams params = new TableRow.LayoutParams(TableRow.LayoutParams
                .MATCH_PARENT, TableRow.LayoutParams.WRAP_CONTENT);
        setTextViewAttributes(view, params, Color.WHITE, HEADER_TEXT_SIZE, MAX_LINES_PER_ROW,
                Typeface.BOLD);
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
                startActivity(new Intent(this, ParticipantDetailsActivity.class));
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

}