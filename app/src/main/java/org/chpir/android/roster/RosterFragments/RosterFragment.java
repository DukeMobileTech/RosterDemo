package org.chpir.android.roster.RosterFragments;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import org.chpir.android.roster.Models.Question;
import org.chpir.android.roster.R;
import org.parceler.Parcels;

public abstract class RosterFragment extends Fragment {
    public final int MINIMUM_WIDTH = 250;
    private Question mQuestion;

    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle
            savedInstanceState) {
        View view = inflater.inflate(R.layout.roster_item_fragment, container, false);
        ViewGroup responseComponent = (LinearLayout) view.findViewById(R.id.response_component);
        mQuestion = Parcels.unwrap(getArguments().getParcelable("Question"));

        createResponseComponent(responseComponent);

        return view;
    }

    protected abstract void createResponseComponent(ViewGroup responseComponent);

    @Override
    public void onResume() {
        super.onResume();
        TextView itemText = (TextView) getActivity().findViewById(R.id.roster_item_text);
        itemText.setText(mQuestion.getText());
    }

    Question getQuestion() {
        return mQuestion;
    }
}