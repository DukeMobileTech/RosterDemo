package org.chpir.android.roster.Adapters;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListAdapter;
import android.widget.TextView;

import org.chpir.android.roster.R;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Harry on 10/20/16.
 */
public class ResponseViewerAdapter extends BaseAdapter implements ListAdapter {
    private List<String> participantIdList;
    private List<String> responseList;
    private Context context;

    public ResponseViewerAdapter(List<String> participantIdList, List<String> responseList, Context context) {
        this.participantIdList = participantIdList;
        this.responseList = responseList;
        this.context = context;
    }

    @Override
    public int getCount() {
        return participantIdList.size();
    }

    @Override
    public Object getItem(int pos) {
        return participantIdList.get(pos);
    }
    @Override
    public long getItemId(int pos) {
        return 0;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        View view = convertView;
        if (view == null) {
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            view = inflater.inflate(R.layout.response_viewer_list_item, null);
        }

        TextView participantIdView = (TextView)view.findViewById(R.id.participant_id);
        participantIdView.setText(participantIdList.get(position));

        TextView responseView = (TextView)view.findViewById(R.id.response);
        responseView.setText(responseList.get(position));

        Button editBtn = (Button)view.findViewById(R.id.edit_response);

        editBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.i("response_viewer", "onClick: Success");
                notifyDataSetChanged();
            }
        });
        return view;
    }

}
