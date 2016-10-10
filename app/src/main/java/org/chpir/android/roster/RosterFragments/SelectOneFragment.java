package org.chpir.android.roster.RosterFragments;

import android.util.Log;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RadioGroup.OnCheckedChangeListener;

public class SelectOneFragment extends RosterFragment {
    @Override
    protected void createResponseComponent(ViewGroup responseComponent) {
        RadioGroup mRadioGroup = new RadioGroup(getActivity());
        Log.i("SELECT ONE", "Type: " + getQuestion().getQuestionType());
        Integer previousOptionId = null;
        if (getQuestion().getOptions() != null) {
             previousOptionId = getQuestion().getOptions().indexOfValue(getQuestion().getResponse());
        }

        for (int i = 0; i < getQuestion().getOptions().size(); i++) {
            int optionId = getQuestion().getOptions().keyAt(i);
            String option = getQuestion().getOptions().get(optionId);
            RadioButton radioButton = new RadioButton(getActivity());
            radioButton.setText(option);
            radioButton.setId(optionId);
            if (previousOptionId != null && previousOptionId == optionId) {
                radioButton.setChecked(true);
            }
            radioButton.setLayoutParams(new RadioGroup.LayoutParams(
                    LinearLayout.LayoutParams.MATCH_PARENT,
                    LinearLayout.LayoutParams.WRAP_CONTENT));
            mRadioGroup.addView(radioButton, optionId);
        }

        mRadioGroup.setOnCheckedChangeListener(new OnCheckedChangeListener() {
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                getQuestion().setResponse(getQuestion().getOptions().get(checkedId));
            }
        });
        responseComponent.addView(mRadioGroup);
    }
}