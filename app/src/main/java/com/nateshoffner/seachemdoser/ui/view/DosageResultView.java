package com.nateshoffner.seachemdoser.ui.view;

import android.content.Context;
import android.text.InputType;
import android.text.method.NumberKeyListener;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.nateshoffner.seachemdoser.R;
import com.nateshoffner.seachemdoser.utils.ClipboardUtils;
import com.nateshoffner.seachemdoser.utils.ViewUtils;

import java.text.DecimalFormat;

public class DosageResultView extends LinearLayout {

    private EditText mEditText;
    private TextView mLabel;
    private TextView mUnitLabel;

    private String mUnitQualifier;

    private final DecimalFormat decimalFormat = new DecimalFormat("#.##");
    public DosageResultView(Context context, AttributeSet attrs) {
        super(context, attrs);

        LayoutInflater inflater = LayoutInflater.from(context);
        inflater.inflate(R.layout.dosage_result_view, this, true);

        mEditText = (EditText) findViewById(R.id.dosage_result_edittext);
        mLabel = (TextView) findViewById(R.id.dosage_result_unit_label);
        mUnitLabel = (TextView) findViewById(R.id.dosage_result_label);

        // give EditText unique ID to prevent text duplication with other views
        mEditText.setId(ViewUtils.generateViewId());

        Button btnCopy = (Button) findViewById(R.id.btnCopy);
        btnCopy.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                boolean result = ClipboardUtils.copyToClipboard(getContext(), String.format("%s %s",
                        mEditText.getText().toString(), mLabel.getText().toString()));

                if (result)
                    Toast.makeText(getContext(), "Dosage copied to clipboard", Toast.LENGTH_SHORT).show();
            }
        });

        mEditText.setKeyListener(new NumberKeyListener() {
            public int getInputType() {
                return InputType.TYPE_NULL;
            }

            protected char[] getAcceptedChars() {
                return new char[]{};
            }
        });
    }

    public void setUnitQualifier(String qualifier) {
        mUnitQualifier = qualifier;
    }

    private String resolveUnitQualifier(boolean plural) {
        int firstBracket = mUnitQualifier.indexOf('[');
        String singular = mUnitQualifier.substring(0, firstBracket >= 0 ?
                firstBracket : mUnitQualifier.length());

        if (!plural)
            return singular;
        else {
            String pluralSuffix = "";

            if (firstBracket >= 0) {
                pluralSuffix = mUnitQualifier.substring(firstBracket + 1,
                        mUnitQualifier.indexOf(']', firstBracket));
            }
            return singular + pluralSuffix;
        }
    }

    public void setLabelText(String text) {
        mUnitLabel.setText(text);
    }

    public void setValue(double value) {
        String strValue = value % 1 == 0 ? Double.toString(value) : decimalFormat.format(value);
        mEditText.setText(strValue);
        setUnitText(resolveUnitQualifier(value != 1));
    }

    public void setUnitText(String text) {
        mLabel.setText(text);
    }
}
