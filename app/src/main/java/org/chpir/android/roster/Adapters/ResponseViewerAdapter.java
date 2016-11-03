package org.chpir.android.roster.Adapters;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
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

import org.chpir.android.roster.Models.Participant;
import org.chpir.android.roster.Models.Question;
import org.chpir.android.roster.ParticipantEditorActivity;
import org.chpir.android.roster.ParticipantViewerActivity;
import org.chpir.android.roster.R;
import org.chpir.android.roster.ResponseEditorActivity;
import org.chpir.android.roster.RosterActivity;

import java.util.ArrayList;
import java.util.List;

import static android.support.v4.app.ActivityCompat.startActivity;
import static android.support.v4.app.ActivityCompat.startActivityForResult;
import static android.support.v4.content.ContextCompat.startActivities;

/**
 * Created by Harry on 10/20/16.
 */
public class ResponseViewerAdapter extends BaseAdapter implements ListAdapter {
    private List<String> participantIdList;
    private List<String> responseList;
    private String centerId;
    private Question.QuestionHeader questionHeader;
    private Context context;

    public ResponseViewerAdapter(String centerId, List<String> participantIdList, List<String> responseList, Question.QuestionHeader questionHeader, Context context) {
        this.centerId = centerId;
        this.participantIdList = participantIdList;
        this.responseList = responseList;
        this.questionHeader = questionHeader;
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
        participantIdView.setText(Participant.findByIdentifier(participantIdList.get(position)).findResponseByQuestionHeader(Question.QuestionHeader.PARTICIPANT_ID).getLabel());

        TextView responseView = (TextView)view.findViewById(R.id.response);
        responseView.setText(responseList.get(position));

        Button editBtn = (Button)view.findViewById(R.id.edit_response);

        editBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Question question = Question.findByHeader(questionHeader);
                Intent intent = new Intent(context, ResponseEditorActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                intent.putExtra(RosterActivity.EXTRA_CENTER_ID, centerId);
                intent.putExtra(ParticipantEditorActivity.EXTRA_PARTICIPANT_ID, participantIdList.get(position));
                intent.putExtra(ParticipantEditorActivity.EXTRA_QUESTION_ID, question.getIdentifier());
                context.startActivity(intent);
                Log.i("response_viewer", "onClick: Success");
                notifyDataSetChanged();
            }
        });
        return view;
    }

}
