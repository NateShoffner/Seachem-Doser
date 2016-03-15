package com.nateshoffner.seachemdoser.ui.view;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.nateshoffner.seachemdoser.R;

public class ParameterInputView extends LinearLayout {

    private TextView tvLabel;
    private EditText etValue;
    private TextView tvUnit;

    public ParameterInputView(Context context, AttributeSet attrs) {
        super(context, attrs);

        LayoutInflater inflater = LayoutInflater.from(context);
        inflater.inflate(R.layout.parameter_input_view, this, true);

        tvLabel = (TextView) findViewById(R.id.tvLabel);
        etValue = (EditText) findViewById(R.id.etValue);
        tvUnit = (TextView) findViewById(R.id.tvUnit);
    }

    public String getValue() {
        return ((EditText) findViewById(R.id.etValue)).getText().toString();
    }

    public void setValue(String value) {
        etValue.setText(value);
    }

    public EditText getInputView() {
        return etValue;
    }

    public void setLabelText(String value) {
        tvLabel.setText(value);
    }

    public void setUnitText(String value) {
        tvUnit.setText(value);
    }
}