package org.chpir.android.roster;

import android.app.Activity;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.util.Log;
import android.view.Gravity;
import android.widget.LinearLayout;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;

public class MainActivity extends Activity {
    private int HEIGHT;
    private int WIDTH;
    private int MARGIN;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        final float scale = getResources().getDisplayMetrics().density;
        HEIGHT = (int) (50 * scale + 0.5f);
        WIDTH = (int) (150 * scale + 0.5f);
        MARGIN = (int) (0.5 * scale + 0.5f);

        for(int k=0; k<100; k++) {
            LinearLayout ly = (LinearLayout) findViewById(R.id.participant_id);
            TableLayout tl = (TableLayout) findViewById(R.id.content_table);

            TextView id = new TextView(this);
            setTextViewLayoutAttributes(id);
            id.setText("ID " + k);
            ly.addView(id);
            Log.i("VIEW", "added view " + k);

            TableRow row = new TableRow(this);
            row.setLayoutParams(new TableLayout.LayoutParams(TableLayout.LayoutParams.MATCH_PARENT, TableLayout.LayoutParams.WRAP_CONTENT));

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

            tl.addView(row);
        }

    }

    private void setTextViewLayoutAttributes(TextView view) {
        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
        params.setMargins(MARGIN,MARGIN,MARGIN,MARGIN);
        view.setHeight(HEIGHT);
        view.setWidth(WIDTH);
        view.setTextColor(Color.BLACK);
        view.setGravity(Gravity.CENTER);
        view.setPadding(2,2,2,2);
        view.setBackgroundColor(ContextCompat.getColor(this, R.color.grayBackground));
        view.setLayoutParams(params);
    }

    private void setRowLayoutAttributes(TextView view) {
        TableRow.LayoutParams params = new TableRow.LayoutParams(TableRow.LayoutParams.MATCH_PARENT, TableRow.LayoutParams.WRAP_CONTENT);
        params.setMargins(MARGIN,MARGIN,MARGIN,MARGIN);
        view.setHeight(HEIGHT);
        view.setWidth(WIDTH);
        view.setTextColor(Color.BLACK);
        view.setGravity(Gravity.CENTER);
        view.setPadding(2,2,2,2);
        view.setBackgroundColor(Color.WHITE);
        view.setLayoutParams(params);
    }

}