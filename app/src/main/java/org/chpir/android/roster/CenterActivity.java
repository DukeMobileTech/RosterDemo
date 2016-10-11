package org.chpir.android.roster;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

import org.chpir.android.roster.Models.Center;
import org.chpir.android.roster.Utils.SeedData;

import java.util.List;

public class CenterActivity extends AppCompatActivity {
    public final static String EXTRA_CENTER_ID = "org.chpir.android.roster.center_id";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_center);
        setTitle(getString(R.string.centers));
        initializeCenters();
    }

    private void initializeCenters() {
        List<Center> centers = Center.findAll();
        if (centers.size() == 0) {
            new SeedDatabaseTask().execute();
        } else {
            CentersAdapter adapter = new CentersAdapter(this, centers);
            ListView listView = (ListView) findViewById(R.id.center_list);
            if (listView != null) {
                listView.setAdapter(adapter);
            }
        }
    }

    private class CentersAdapter extends ArrayAdapter<Center> {

        CentersAdapter(Context context, List<Center> centers) {
            super(context, 0, centers);
        }

        @NonNull
        @Override
        public View getView(int position, View convertView, @NonNull ViewGroup parent) {
            if (convertView == null) {
                convertView = LayoutInflater.from(getContext()).inflate(R.layout.item_center,
                        parent, false);
            }
            TextView centerName = (TextView) convertView.findViewById(R.id.center_name);
            TextView centerID = (TextView) convertView.findViewById(R.id.center_identifier);
            Button viewRoster = (Button) convertView.findViewById(R.id.view_center_roster);
            final Center center = getItem(position);
            if (center != null) {
                centerName.setText(center.getName());
                centerID.setText(center.getIdentifier());
                viewRoster.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent intent = new Intent(CenterActivity.this, RosterActivity.class);
                        intent.putExtra(EXTRA_CENTER_ID, center.getIdentifier());
                        startActivity(intent);
                    }
                });
            }
            return convertView;
        }
    }

    private class SeedDatabaseTask extends AsyncTask<Void, Void, List<Center>> {
        private ProgressDialog mDialog = new ProgressDialog(CenterActivity.this);

        @Override
        protected List<Center> doInBackground(Void... params) {
            SeedData.seedDatabase();
            return Center.findAll();
        }

        @Override
        protected void onPreExecute() {
            this.mDialog.setTitle(getString(R.string.seeding_dialog_title));
            this.mDialog.setMessage(getString(R.string.seeding_dialog_message));
            this.mDialog.show();
        }

        @Override
        protected void onPostExecute(List<Center> centers) {
            if (mDialog.isShowing()) {
                mDialog.dismiss();
            }
            CentersAdapter adapter = new CentersAdapter(CenterActivity.this, centers);
            ListView listView = (ListView) findViewById(R.id.center_list);
            if (listView != null) {
                listView.setAdapter(adapter);
            }
        }
    }

}