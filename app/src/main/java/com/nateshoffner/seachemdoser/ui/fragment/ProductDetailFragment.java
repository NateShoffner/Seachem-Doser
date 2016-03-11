package com.nateshoffner.seachemdoser.ui.fragment;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.nateshoffner.seachemdoser.DoserApplication;
import com.nateshoffner.seachemdoser.R;
import com.nateshoffner.seachemdoser.core.model.SeachemDosage;
import com.nateshoffner.seachemdoser.core.model.SeachemParameter;
import com.nateshoffner.seachemdoser.core.model.SeachemProduct;
import com.nateshoffner.seachemdoser.ui.view.ParameterInputView;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;

public class ProductDetailFragment extends Fragment {

    public static final String EXTRA_PRODUCT = "PRODUCT";

    private static final String TAG = "ProductDetailFragment";

    private final List<EditText> dosageInputs = new ArrayList<>();
    private final List<EditText> dosageOutputs = new ArrayList<>();
    private final DecimalFormat decimalFormat = new DecimalFormat("#.##");

    private SeachemProduct mProduct;

    public ProductDetailFragment() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (getArguments().containsKey(EXTRA_PRODUCT)) {
            mProduct = (SeachemProduct) getArguments().getSerializable(EXTRA_PRODUCT);
        }
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_product_detail, container, false);

        if (mProduct != null) {

            Button btnCalc = (Button) rootView.findViewById(R.id.btnCalculate);
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

                        mProduct.getParameters(DoserApplication.getDoserPreferences().getUnitMeasurement())[inputCount].
                                setValue(Double.parseDouble(String.valueOf(input.getText().toString())));
                        inputCount++;
                    }

                    if (invalidInput) {
                        Toast.makeText(getActivity(),
                                getString(R.string.invalid_parameters),
                                Toast.LENGTH_LONG).show();
                        return;
                    }

                    SeachemDosage[] dosages = null;

                    try {
                        dosages = mProduct.calculateDosage(DoserApplication.getDoserPreferences().getUnitMeasurement());
                    } catch (ArithmeticException ex) {
                        Log.e(TAG, ex.toString());
                        //shouldn't happen, but just incase
                        Toast.makeText(getActivity(),
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

            LinearLayout paramsLayout = (LinearLayout) rootView.findViewById(R.id.paramsLayout);
            for (SeachemParameter param : mProduct.getParameters(DoserApplication.getDoserPreferences().getUnitMeasurement())) {
                ParameterInputView view = new ParameterInputView(getActivity(), null);
                view.setLabelText(param.getName() + ":");
                view.setUnitText(param.getUnit());
                dosageInputs.add(view.getInputView());
                paramsLayout.addView(view);
            }

            int dosageCount = 0;
            LinearLayout dosagesLayout = (LinearLayout) rootView.findViewById(R.id.dosagesLayout);
            for (SeachemDosage dosage : mProduct.calculateDosage(DoserApplication.getDoserPreferences().getUnitMeasurement())) {
                ParameterInputView view = new ParameterInputView(getActivity(), null);

                view.setLabelText(dosageCount == 0 ?
                        getString(R.string.dosage_label) :
                        getString(R.string.dosage_label_or));

                view.setUnitText(dosage.getUnit());
                view.setReadOnly(true);

                dosageOutputs.add(view.getInputView());
                dosagesLayout.addView(view);
                dosageCount++;
            }

            TextView tvProductComment = (TextView) rootView.findViewById(R.id.tvProductComment);
            tvProductComment.setText(mProduct.getComment());
        }

        return rootView;
    }

}
