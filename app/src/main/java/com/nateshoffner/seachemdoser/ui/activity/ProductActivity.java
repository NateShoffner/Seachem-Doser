package com.nateshoffner.seachemdoser.ui.activity;

import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.nateshoffner.seachemdoser.R;
import com.nateshoffner.seachemdoser.core.model.SeachemDosage;
import com.nateshoffner.seachemdoser.core.model.SeachemParameter;
import com.nateshoffner.seachemdoser.core.model.SeachemProduct;
import com.nateshoffner.seachemdoser.ui.view.ParameterInputView;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;


public class ProductActivity extends BaseActivity {

    public static final String EXTRA_SEACHEM_PRODUCT = "SEACHEM_PRODUCT";
    private static final String TAG = "ProductActivity";
    private final List<EditText> dosageInputs = new ArrayList<>();
    private final List<EditText> dosageOutputs = new ArrayList<>();
    private final DecimalFormat decimalFormat = new DecimalFormat("#.##");
    private SeachemProduct product;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_product);

        product = (SeachemProduct) getIntent().getExtras().getSerializable(EXTRA_SEACHEM_PRODUCT);

        setTitle(product.getName());

        populateProductInterface(product);

        Button btnCalc = (Button) findViewById(R.id.btnCalculate);
        btnCalc.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                boolean invalidInput = false;

                int inputCount = 0;
                for (EditText input : dosageInputs) {

                    String value = input.getText().toString();

                    if (value.length() == 0) {
                        invalidInput = true;
                        break;
                    }

                    product.getParameters()[inputCount].
                            setValue(Double.parseDouble(String.valueOf(input.getText().toString())));
                    inputCount++;
                }

                if (invalidInput) {
                    Toast.makeText(getApplicationContext(),
                            getString(R.string.invalid_parameters),
                            Toast.LENGTH_LONG).show();
                    return;
                }

                SeachemDosage[] dosages = null;

                try {
                    dosages = product.calculateDosage();
                } catch (ArithmeticException ex) {
                    Log.e(TAG, "exception", ex);
                    //shouldn't happen, but just incase
                    Toast.makeText(getApplicationContext(),
                            getString(R.string.calculation_error),
                            Toast.LENGTH_LONG).show();
                    return;
                }

                if (dosages != null) {
                    int dosageCount = 0;
                    for (SeachemDosage dosage : dosages) {
                        String value = decimalFormat.format(dosage.getAmount());
                        dosageOutputs.get(dosageCount).setText(value);
                        dosageCount++;
                    }
                }
            }
        });
    }

    private void populateProductInterface(SeachemProduct product) {
        LinearLayout paramsLayout = (LinearLayout) findViewById(R.id.paramsLayout);
        for (SeachemParameter param : product.getParameters()) {
            ParameterInputView view = new ParameterInputView(this, null);
            view.setLabelText(param.getName() + ":");
            view.setUnitText(param.getUnit());
            dosageInputs.add(view.getInputView());
            paramsLayout.addView(view);
        }

        int dosageCount = 0;
        LinearLayout dosagesLayout = (LinearLayout) findViewById(R.id.dosagesLayout);
        for (SeachemDosage dosage : product.calculateDosage()) {
            ParameterInputView view = new ParameterInputView(this, null);

            view.setLabelText(dosageCount == 0 ?
                    getString(R.string.dosage_label) :
                    getString(R.string.dosage_label_or));

            view.setUnitText(dosage.getUnit());
            view.setReadOnly(true);

            dosageOutputs.add(view.getInputView());
            dosagesLayout.addView(view);
            dosageCount++;
        }

        //product comment
        TextView tvProductComment = (TextView) findViewById(R.id.tvProductComment);
        tvProductComment.setText(product.getComment());
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.global, menu);
        return true;
    }
}
