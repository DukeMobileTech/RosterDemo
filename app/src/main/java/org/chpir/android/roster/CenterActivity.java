package org.chpir.android.roster;

import android.content.ClipData;
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
import com.activeandroid.ActiveAndroid;
import com.activeandroid.query.Select;

import org.chpir.android.roster.Models.Center;

import java.util.ArrayList;
import java.util.List;

public class CenterActivity extends AppCompatActivity {

    private List<Center> myCenters;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_center);
        setTitle("Centers");
        initializeCenters();
        CentersAdapter adapter = new CentersAdapter(this, myCenters);
        ListView listView = (ListView) findViewById(R.id.center_list);
        listView.setAdapter(adapter);
    }

    private void initializeCenters() {
        //fetch data
        myCenters = new Select()
                .from(Center.class)
                .orderBy("Name ASC")
                .execute();
        if(myCenters.size()==0){
            myCenters = new ArrayList<>();
            //hardcode center data
            ActiveAndroid.beginTransaction();
            try {
                for (char alp = 'A'; alp <= 'Z'; alp++) {
                    Center oneCenter = new Center(Long.toString(Math.round(Math.random() * 1000)) + "-" + alp,
                            "Center " + alp);
                    myCenters.add(oneCenter);
                    oneCenter.save();
                }
                ActiveAndroid.setTransactionSuccessful();
            }
            finally {
                ActiveAndroid.endTransaction();
            }
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
                    intent.putExtra("CenterName", center.getName());
                    startActivity(intent);
                }
            });
            return convertView;
        }
    }

}