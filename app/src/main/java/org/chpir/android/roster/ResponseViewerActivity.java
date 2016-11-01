package org.chpir.android.roster;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.widget.ListView;

import org.chpir.android.roster.Adapters.ResponseViewerAdapter;
import org.chpir.android.roster.Models.Center;
import org.chpir.android.roster.Models.Participant;
import org.chpir.android.roster.Models.Question;
import org.chpir.android.roster.Models.Response;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Harry on 10/18/16.
 */
public class ResponseViewerActivity extends AppCompatActivity {
    private static final String TAG = "ResponseViewerActivity";

    private List<Participant> mParticipants;
    private Center mCenter;
    private List<String> mParticipantIds;
    private List<String> mResponses;
    private Question.QuestionHeader mQuestionHeader;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_response_viewer);
        String centerId = getIntent().getStringExtra(RosterActivity.EXTRA_CENTER_ID);
        mQuestionHeader = (Question.QuestionHeader)getIntent().getExtras().get(RosterActivity.EXTRA_QUESTION_HEADER);
        setTitle(mQuestionHeader.toString());
        if (centerId != null) {
            mCenter = Center.findByIdentifier(centerId);
            mParticipants = mCenter.participants();
        }
        mParticipantIds = new ArrayList<>();
        mResponses = new ArrayList<>();
        for(Participant oneParticipant: mParticipants){
            mParticipantIds.add(oneParticipant.getIdentifier());
            mResponses.add(oneParticipant.findResponseByQuestionHeader(mQuestionHeader).getLabel());
        }
        Log.i(TAG,centerId);
        Log.i(TAG,mQuestionHeader.toString());
        Log.i(TAG,mCenter.toString());
        Log.i(TAG,mParticipants.toString());
        Log.i(TAG,mParticipantIds.toString());
        Log.i(TAG,mResponses.toString());
        ResponseViewerAdapter adapter = new ResponseViewerAdapter(mParticipantIds, mResponses, mQuestionHeader, getApplicationContext());
        Log.i(TAG,""+adapter.getCount());
        ListView lView = (ListView) findViewById(R.id.responseListView);
        lView.setAdapter(adapter);
    }

}
