package com.nateshoffner.seachemdoser.ui.view;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.nateshoffner.seachemdoser.R;

public class DosageResultView extends LinearLayout {

    private EditText etValue;
    private TextView tvUnit;
    private TextView tvLabel;
    private TextView tvPrecursor;

    public DosageResultView(Context context, AttributeSet attrs) {
        super(context, attrs);

        LayoutInflater inflater = LayoutInflater.from(context);
        inflater.inflate(R.layout.dosage_result, this, true);

        etValue = (EditText) findViewById(R.id.etValue);
        tvUnit = (TextView) findViewById(R.id.tvUnit);
        tvLabel = (TextView) findViewById(R.id.tvLabel);
        tvPrecursor = (TextView) findViewById(R.id.tvPrecursor);
    }

    public void setLabelText(String text) {
        tvLabel.setText(text);
    }

    public void setValue(String value){
        etValue.setText(value);
    }

    public void setUnitText(String text) {
        tvUnit.setText(text);
    }

    public void togglePrecursor(boolean visible) {
        tvPrecursor.setVisibility(visible ? View.VISIBLE : View.GONE);
    }

    public void toggleLabel(boolean visible) {
        tvLabel.setVisibility(visible ? View.VISIBLE : View.GONE);
    }
}
