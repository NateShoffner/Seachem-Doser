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

import java.text.DecimalFormat;

public class DosageInputView extends LinearLayout {

    private TextView tvLabel;
    private EditText etValue;
    private TextView tvUnit;

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

        tvLabel = (TextView) findViewById(R.id.tvLabel);
        etValue = (EditText) findViewById(R.id.etValue);
        tvUnit = (TextView) findViewById(R.id.tvUnit);

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

                if (!etValue.isFocused())
                    etValue.requestFocus();
            }
        });
        btnAdd.setOnLongClickListener(new OnLongClickListener() {
            @Override
            public boolean onLongClick(View view) {
                autoIncrement = true;
                repeatUpdateHandler.post(new RepetitiveUpdater());

                if (!etValue.isFocused())
                    etValue.requestFocus();

                return false;
            }
        });
        btnAdd.setOnTouchListener(new OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent event) {
                if (event.getAction() == MotionEvent.ACTION_UP && autoIncrement) {
                    autoIncrement = false;

                    if (!etValue.isFocused())
                        etValue.requestFocus();
                }
                return false;
            }
        });

        Button btnSubtract = (Button) findViewById(R.id.btnSubtract);
        btnSubtract.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                decrementValue();

                if (!etValue.isFocused())
                    etValue.requestFocus();
            }
        });
        btnSubtract.setOnLongClickListener(new OnLongClickListener() {
            @Override
            public boolean onLongClick(View view) {
                autoDecrement = true;
                repeatUpdateHandler.post(new RepetitiveUpdater());

                if (!etValue.isFocused())
                    etValue.requestFocus();

                return false;
            }
        });
        btnSubtract.setOnTouchListener(new OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent event) {
                if (event.getAction() == MotionEvent.ACTION_UP && autoDecrement) {
                    autoDecrement = false;

                    if (!etValue.isFocused())
                        etValue.requestFocus();
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
        return ((EditText) findViewById(R.id.etValue)).getText().toString();
    }

    public void setValue(double value) {
        String strValue = value % 1 == 0 ? Integer.toString((int)value) : decimalFormat.format(value);
        etValue.setText(strValue);
    }

    public EditText getInputView() {
        return etValue;
    }

    public void setLabelText(String value) {
        tvLabel.setText(value);
    }

    public void setUnitText() {
        tvUnit.setText(String.format("(%s)", resolveUnitQualifier()));
    }

    private void incrementValue() {
        double value = getValue().length() > 0 ? Double.parseDouble(getValue()) : 0;
        setValue(value + 1);
    }

    private void decrementValue() {
        double value = getValue().length() > 0 ? Double.parseDouble(getValue()) : 0;
        setValue(value - 1);
    }
}