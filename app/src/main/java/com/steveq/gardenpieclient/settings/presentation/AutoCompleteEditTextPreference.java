package com.steveq.gardenpieclient.settings.presentation;

import android.content.Context;
import android.preference.EditTextPreference;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.AttributeSet;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.EditText;

import com.steveq.gardenpieclient.weather.callout.GeoCodeAutocompleteController;
import com.steveq.gardenpieclient.weather.callout.adapters.CustomAdapter;
import com.steveq.gardenpieclient.weather.callout.model.GeoAutocompleteDescriptionModel;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Created by Adam on 2017-08-14.
 */

public class AutoCompleteEditTextPreference extends EditTextPreference {
    private static final String TAG = AutoCompleteEditTextPreference.class.getSimpleName();
    private static AutoCompleteTextView mACTV;
    private static ArrayAdapter<String> adapter;
    public static List<String> propositions = new ArrayList<String>();
    private GeoCodeAutocompleteController controller = new GeoCodeAutocompleteController(getContext());
    public AutoCompleteEditTextPreference(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
    }

    public AutoCompleteEditTextPreference(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    public AutoCompleteEditTextPreference(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public AutoCompleteEditTextPreference(Context context) {
        super(context);
    }

    @Override
    protected void onBindDialogView(View view) {
        super.onBindDialogView(view);

        // find the current EditText object
        final EditText editText = (EditText)view.findViewById(android.R.id.edit);
        // copy its layout params
        ViewGroup.LayoutParams params = editText.getLayoutParams();
        ViewGroup vg = (ViewGroup)editText.getParent();
        String curVal = editText.getText().toString();
        // remove it from the existing layout hierarchy
        vg.removeView(editText);
        // construct a new editable autocomplete object with the appropriate params
        // and id that the TextEditPreference is expecting
        mACTV = new AutoCompleteTextView(getContext());
        mACTV.setLayoutParams(params);
        mACTV.setId(android.R.id.edit);
        mACTV.setText(curVal);
        mACTV.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                Log.d(TAG, "INPUT STRING : " + s.toString());
                controller.getAutocompleteProposition(s.toString());
            }

            @Override
            public void afterTextChanged(Editable s) {
            }
        });
        adapter = new CustomAdapter(getContext(),
                android.R.layout.simple_dropdown_item_1line);
        adapter.setNotifyOnChange(true);
        mACTV.setAdapter(adapter);
        // add the new view to the layout
        vg.addView(mACTV);
    }

    public static void updatePropositions(){
        Log.d(TAG, "FIRST ITEM : " + propositions);
        adapter.clear();
        adapter.addAll(propositions);
        adapter.notifyDataSetChanged();
    }

    protected void onDialogClosed(boolean positiveResult)
    {
        super.onDialogClosed(positiveResult);

        if (positiveResult && mACTV != null)
        {
            Log.d(TAG, "DIALOG CLOSED POSITIVELY");
            String value = mACTV.getText().toString();
            Log.d(TAG, "VALUE FROM DIALOG : " + value);
            if (callChangeListener(value)) {
                setText(value);
            }
        }
    }

    /**
     * again we need to override methods from the base class
     */
    public EditText getEditText()
    {
        return mACTV;
    }
}
