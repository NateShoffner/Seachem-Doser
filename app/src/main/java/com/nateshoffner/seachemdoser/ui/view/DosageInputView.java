package com.nateshoffner.seachemdoser.ui.view;

import android.content.Context;
import android.os.Handler;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.nateshoffner.seachemdoser.R;
import com.nateshoffner.seachemdoser.utils.ViewUtils;

import java.text.DecimalFormat;

public class DosageInputView extends LinearLayout {

    private TextView mLabel;
    private EditText mEditText;
    private TextView mUnitLabel;

    private String mUnitQualifier;

    private boolean autoIncrement = false;
    private boolean autoDecrement = false;

    private final long REPEAT_DELAY = 50;

    private final DecimalFormat decimalFormat = new DecimalFormat("#.##");

    private Handler repeatUpdateHandler = new Handler();

    public DosageInputView(Context context, AttributeSet attrs) {
        super(context, attrs);

        LayoutInflater inflater = LayoutInflater.from(context);
        inflater.inflate(R.layout.dosage_input_view, this, true);

        mLabel = (TextView) findViewById(R.id.dosage_input_label);
        mEditText = (EditText) findViewById(R.id.dosage_input_edittext);
        mUnitLabel = (TextView) findViewById(R.id.dosage_unit_label);

        // give EditText unique ID to prevent text duplication with other views
        mEditText.setId(ViewUtils.generateViewId());

        class RepetitiveUpdater implements Runnable {

            @Override
            public void run() {
                if (autoIncrement) {
                    incrementValue();
                    repeatUpdateHandler.postDelayed(new RepetitiveUpdater(), REPEAT_DELAY);
                } else if (autoDecrement) {
                    decrementValue();
                    repeatUpdateHandler.postDelayed(new RepetitiveUpdater(), REPEAT_DELAY);
                }
            }
        }

        Button btnAdd = (Button) findViewById(R.id.btnAdd);
        btnAdd.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                incrementValue();

                if (!mEditText.isFocused())
                    mEditText.requestFocus();
            }
        });
        btnAdd.setOnLongClickListener(new OnLongClickListener() {
            @Override
            public boolean onLongClick(View view) {
                autoIncrement = true;
                repeatUpdateHandler.post(new RepetitiveUpdater());

                if (!mEditText.isFocused())
                    mEditText.requestFocus();

                return false;
            }
        });
        btnAdd.setOnTouchListener(new OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent event) {
                if (event.getAction() == MotionEvent.ACTION_UP && autoIncrement) {
                    autoIncrement = false;

                    if (!mEditText.isFocused())
                        mEditText.requestFocus();
                }
                return false;
            }
        });

        Button btnSubtract = (Button) findViewById(R.id.btnSubtract);
        btnSubtract.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                decrementValue();

                if (!mEditText.isFocused())
                    mEditText.requestFocus();
            }
        });
        btnSubtract.setOnLongClickListener(new OnLongClickListener() {
            @Override
            public boolean onLongClick(View view) {
                autoDecrement = true;
                repeatUpdateHandler.post(new RepetitiveUpdater());

                if (!mEditText.isFocused())
                    mEditText.requestFocus();

                return false;
            }
        });
        btnSubtract.setOnTouchListener(new OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent event) {
                if (event.getAction() == MotionEvent.ACTION_UP && autoDecrement) {
                    autoDecrement = false;

                    if (!mEditText.isFocused())
                        mEditText.requestFocus();
                }
                return false;
            }
        });
    }

    public void setUnitQualifier(String qualifier) {
        mUnitQualifier = qualifier;
    }

    private String resolveUnitQualifier() {
        int firstBracket = mUnitQualifier.indexOf('[');
        String singular = mUnitQualifier.substring(0, firstBracket >= 0 ?
                firstBracket : mUnitQualifier.length());

        String pluralSuffix = "";

        if (firstBracket >= 0) {
            pluralSuffix = mUnitQualifier.substring(firstBracket + 1,
                    mUnitQualifier.indexOf(']', firstBracket));
        }
        return singular + pluralSuffix;
    }

    public String getValue() {
        return mEditText.getText().toString();
    }

    public void setValue(double value) {
        String strValue = value % 1 == 0 ? Integer.toString((int)value) : decimalFormat.format(value);
        mEditText.setText(strValue);
    }

    public EditText getInputView() {
        return mEditText;
    }

    public void setLabelText(String value) {
        mLabel.setText(value);
    }

    public void setUnitText() {
        mUnitLabel.setText(String.format("(%s)", resolveUnitQualifier()));
    }

    private void incrementValue() {
        double value = getValue().length() > 0 ? Double.parseDouble(getValue()) : 0;
        setValue(value + 1);
    }

    private void decrementValue() {
        double value = getValue().length() > 0 ? Double.parseDouble(getValue()) : 0;
        setValue(value - 1);
    }

    public String getLabelText() {
        return mLabel.getText().toString();
    }
}