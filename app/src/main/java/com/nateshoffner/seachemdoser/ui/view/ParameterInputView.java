package com.nateshoffner.seachemdoser.ui.view;

import android.content.Context;
import android.content.res.TypedArray;
import android.text.InputFilter;
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

        LayoutInflater inflater = (LayoutInflater) context
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        inflater.inflate(R.layout.parameter_input_view, this, true);

        this.tvLabel = (TextView) findViewById(R.id.tvLabel);
        this.etValue = (EditText) findViewById(R.id.etValue);
        this.tvUnit = (TextView) findViewById(R.id.tvUnit);

        TypedArray a = context.obtainStyledAttributes(attrs, R.styleable.ParameterInputView);

        final int N = a.getIndexCount();
        for (int i = 0; i < N; ++i) {
            int attr = a.getIndex(i);
            switch (attr) {
                case R.styleable.ParameterInputView_label:
                    setLabelText(a.getString(attr));
                    break;
                case R.styleable.ParameterInputView_value:
                    setValue(a.getString(attr));
                    break;
                case R.styleable.ParameterInputView_unit:
                    setUnitText(a.getString(attr));
                    break;
                case R.styleable.ParameterInputView_readonly:
                    setReadOnly(a.getBoolean(attr, false));
                    break;
            }
        }
        a.recycle();
    }

    public String getValue() {
        return ((EditText) findViewById(R.id.etValue)).getText().toString();
    }

    public void setValue(String value) {
        this.etValue.setText(value);
    }

    public EditText getInputView() {
        return this.etValue;
    }

    public void setLabelText(String value) {
        this.tvLabel.setText(value);
    }

    public void setUnitText(String value) {
        this.tvUnit.setText(value);
    }

    public void setReadOnly(boolean readonly) {
        this.etValue.setEnabled(!readonly);
    }

    public void setLimit(int limit) {
        InputFilter[] fa= new InputFilter[1];
        fa[0] = new InputFilter.LengthFilter(limit);
        etValue.setFilters(fa);
    }
}