package org.chpir.android.roster;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
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
    private List<Center> myCenters;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_center);
        setTitle(getString(R.string.centers));
        initializeCenters();
        CentersAdapter adapter = new CentersAdapter(this, myCenters);
        ListView listView = (ListView) findViewById(R.id.center_list);
        listView.setAdapter(adapter);
    }

    private void initializeCenters() {
        myCenters = Center.findAll();
        if (myCenters.size() == 0) {
            SeedData.createSeedCenters();
            myCenters = Center.findAll();
        }
    }

    private class CentersAdapter extends ArrayAdapter<Center> {

        CentersAdapter(Context context, List<Center> centers) {
            super(context, 0, centers);
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            final Center center = getItem(position);
            if (convertView == null) {
                convertView = LayoutInflater.from(getContext()).inflate(R.layout.item_center,
                        parent, false);
            }
            TextView centerName = (TextView) convertView.findViewById(R.id.center_name);
            TextView centerID = (TextView) convertView.findViewById(R.id.center_identifier);
            Button viewRoster = (Button) convertView.findViewById(R.id.view_center_roster);
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
            return convertView;
        }
    }

}