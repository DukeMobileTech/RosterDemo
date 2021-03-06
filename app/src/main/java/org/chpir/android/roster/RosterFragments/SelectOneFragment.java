package org.chpir.android.roster.RosterFragments;

import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RadioGroup.OnCheckedChangeListener;

public class SelectOneFragment extends RosterFragment {
    @Override
    protected void createResponseComponent(ViewGroup responseComponent) {
        RadioGroup mRadioGroup = new RadioGroup(getActivity());

        for (int i = 0; i < getQuestion().getOptions().size(); i++) {
            String option = getQuestion().getOptions().get(i);
            RadioButton radioButton = new RadioButton(getActivity());
            radioButton.setText(option);
            radioButton.setId(i);
            if (getResponse().getText() != null && getResponse().getText().equals(i+"")) {
                radioButton.setChecked(true);
            }
            radioButton.setLayoutParams(new RadioGroup.LayoutParams(
                    LinearLayout.LayoutParams.MATCH_PARENT,
                    LinearLayout.LayoutParams.WRAP_CONTENT));
            mRadioGroup.addView(radioButton, i);
        }

        mRadioGroup.setOnCheckedChangeListener(new OnCheckedChangeListener() {
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                getResponse().setText(checkedId+"");
            }
        });
        responseComponent.addView(mRadioGroup);
    }
}