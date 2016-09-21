package org.chpir.android.roster;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;

public class RosterActivity extends Activity implements ScrollViewListener {
    private int HEIGHT;
    private int WIDTH;
    private int MARGIN;
    final private int PADDING = 2;
    private OHScrollView headerScrollView;
    private OHScrollView contentScrollView;
    private boolean interceptScroll = true;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_roster);
        setTitle(getIntent().getStringExtra("CenterName"));
        setDimensions();

        headerScrollView = (OHScrollView) findViewById(R.id.header_scroll);
        contentScrollView = (OHScrollView) findViewById(R.id.content_scroll);
        headerScrollView.setScrollViewListener(this);
        contentScrollView.setScrollViewListener(this);

        for (int k = 0; k < 100; k++) {
            LinearLayout ly = (LinearLayout) findViewById(R.id.participant_id);
            TableLayout tl = (TableLayout) findViewById(R.id.content_table);

            TextView id = new TextView(this);
            setTextViewLayoutAttributes(id);
            id.setText("ID " + k);
            id.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    startActivity(new Intent(RosterActivity.this, ParticipantDetailsActivity.class));
                }
            });
            ly.addView(id);

            TableRow row = new TableRow(this);
            row.setLayoutParams(new TableLayout.LayoutParams(TableLayout.LayoutParams
                    .MATCH_PARENT, TableLayout.LayoutParams.WRAP_CONTENT));

            TextView gender = new TextView(this);
            setRowLayoutAttributes(gender);
            gender.setText("Male " + k);
            row.addView(gender);

            TextView age = new TextView(this);
            setRowLayoutAttributes(age);
            age.setText("Months: " + k);
            row.addView(age);

            TextView arrival = new TextView(this);
            setRowLayoutAttributes(arrival);
            arrival.setText("Date: " + k);
            row.addView(arrival);

            TextView hours = new TextView(this);
            setRowLayoutAttributes(hours);
            hours.setText("Hours " + k);
            row.addView(hours);

            TextView weeks = new TextView(this);
            setRowLayoutAttributes(weeks);
            weeks.setText("Weeks absent " + k);
            row.addView(weeks);

            TextView assigned = new TextView(this);
            setRowLayoutAttributes(assigned);
            assigned.setText("Assigned " + k);
            row.addView(assigned);

            TextView time = new TextView(this);
            setRowLayoutAttributes(time);
            time.setText("Time " + k);
            row.addView(time);

            TextView previous = new TextView(this);
            setRowLayoutAttributes(previous);
            previous.setText("Previous " + k);
            row.addView(previous);

            TextView care = new TextView(this);
            setRowLayoutAttributes(care);
            care.setText("Prev Care " + k);
            row.addView(care);

            TextView primary = new TextView(this);
            setRowLayoutAttributes(primary);
            primary.setText("Primary CG " + k);
            row.addView(primary);

            tl.addView(row);
        }

    }

    private void setDimensions() {
        final float scale = getResources().getDisplayMetrics().density;
        HEIGHT = (int) (50 * scale + 0.5f);
        WIDTH = (int) (150 * scale + 0.5f);
        MARGIN = (int) (0.5 * scale + 0.5f);
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

    private void setTextViewLayoutAttributes(TextView view) {
        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(LinearLayout
                .LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
        params.setMargins(MARGIN, MARGIN, MARGIN, MARGIN);
        view.setHeight(HEIGHT);
        view.setWidth(WIDTH);
        view.setTextColor(Color.BLACK);
        view.setGravity(Gravity.CENTER);
        view.setPadding(PADDING, PADDING, PADDING, PADDING);
        view.setBackgroundColor(ContextCompat.getColor(this, R.color.grayBackground));
        view.setLayoutParams(params);
    }

    private void setRowLayoutAttributes(TextView view) {
        TableRow.LayoutParams params = new TableRow.LayoutParams(TableRow.LayoutParams
                .MATCH_PARENT, TableRow.LayoutParams.WRAP_CONTENT);
        params.setMargins(MARGIN, MARGIN, MARGIN, MARGIN);
        view.setHeight(HEIGHT);
        view.setWidth(WIDTH);
        view.setTextColor(Color.BLACK);
        view.setGravity(Gravity.CENTER);
        view.setPadding(PADDING, PADDING, PADDING, PADDING);
        view.setBackgroundColor(Color.WHITE);
        view.setLayoutParams(params);
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