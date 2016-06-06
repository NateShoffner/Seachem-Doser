package com.nateshoffner.seachemdoser.ui.view;

import android.content.Context;
import android.text.InputType;
import android.text.method.NumberKeyListener;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.nateshoffner.seachemdoser.DoserApplication;
import com.nateshoffner.seachemdoser.R;
import com.nateshoffner.seachemdoser.utils.ClipboardUtils;

public class DosageResultView extends LinearLayout {

    private EditText etValue;
    private TextView tvUnit;
    private TextView tvLabel;

    public DosageResultView(Context context, AttributeSet attrs) {
        super(context, attrs);

        LayoutInflater inflater = LayoutInflater.from(context);
        inflater.inflate(R.layout.dosage_result, this, true);

        etValue = (EditText) findViewById(R.id.etValue);
        tvUnit = (TextView) findViewById(R.id.tvUnit);
        tvLabel = (TextView) findViewById(R.id.tvLabel);

        etValue.setKeyListener(new NumberKeyListener() {
            public int getInputType() {
                return InputType.TYPE_NULL;
            }

            protected char[] getAcceptedChars() {
                return new char[]{};
            }
        });
        etValue.setOnFocusChangeListener(new OnFocusChangeListener() {
            @Override
            public void onFocusChange(View view, boolean b) {
                if (DoserApplication.getDoserPreferences().getCopyDosages()) {
                    boolean result = ClipboardUtils.copyToClipboard(getContext(), String.format("%s %s",
                            etValue.getText().toString(), tvUnit.getText().toString()));

                    if (result)
                        Toast.makeText(getContext(), "Dosage copied to clipboard", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    public void setLabelText(String text) {
        tvLabel.setText(text);
    }

    public void setValue(String value) {
        etValue.setText(value);
    }

    public void setUnitText(String text) {
        tvUnit.setText(text);
    }
}
