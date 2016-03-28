package com.nateshoffner.seachemdoser.ui.fragment;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.nateshoffner.seachemdoser.DoserApplication;
import com.nateshoffner.seachemdoser.R;
import com.nateshoffner.seachemdoser.core.model.SeachemDosage;
import com.nateshoffner.seachemdoser.core.model.SeachemParameter;
import com.nateshoffner.seachemdoser.core.model.SeachemProduct;
import com.nateshoffner.seachemdoser.core.model.UnitMeasurement;
import com.nateshoffner.seachemdoser.ui.view.DosageResultView;
import com.nateshoffner.seachemdoser.ui.view.ParameterInputView;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;

public class ProductDetailFragment extends Fragment
        implements SharedPreferences.OnSharedPreferenceChangeListener {

    public static final String EXTRA_PRODUCT = "PRODUCT";

    private static final String TAG = "ProductDetailFragment";

    private final List<EditText> dosageInputs = new ArrayList<>();
    private final List<DosageResultView> dosageResultViews = new ArrayList<>();
    private final DecimalFormat decimalFormat = new DecimalFormat("#.##");

    private View mRootView;

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

    private void initializeParameterViews(UnitMeasurement unitMeasurement, View rootView) {
        LinearLayout paramsLayout = (LinearLayout) rootView.findViewById(R.id.params_container);

        if (dosageInputs.size() > 0) {
            dosageInputs.clear();

            paramsLayout.removeAllViews();
        }

        for (SeachemParameter param : mProduct.getParameters(unitMeasurement)) {
            ParameterInputView view = new ParameterInputView(getActivity(), null);
            view.setLabelText(param.getName() + ":");
            view.setUnitText(param.getUnit());
            dosageInputs.add(view.getInputView());
            paramsLayout.addView(view);
        }
    }

    private void initializeDosageViews(UnitMeasurement unitMeasurement, View rootView) {
        LinearLayout dosagesLayout = (LinearLayout) rootView.findViewById(R.id.dosages_container);

        if (dosageResultViews.size() > 0) {
            dosageResultViews.clear();

            dosagesLayout.removeAllViews();
        }

        SeachemDosage[] dosages = mProduct.calculateDosage(unitMeasurement);
        for (int i = 0; i < dosages.length; i++) {
            SeachemDosage dosage = dosages[i];
            DosageResultView view = new DosageResultView(getActivity(), null);
            view.setUnitText(dosage.getUnit());

            if (i == 0) {
                view.toggleLabel(true);
                view.setLabelText(getString(R.string.label_dosage_youll_need));
            }

            if (i < dosages.length - 1) {
                view.togglePrecursor(true);
            }

            dosageResultViews.add(view);
            dosagesLayout.addView(view);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        mRootView = inflater.inflate(R.layout.fragment_product_detail, container, false);

        DoserApplication.getDoserPreferences().getSharedPreferences().
                registerOnSharedPreferenceChangeListener(this);

        if (mProduct != null) {

            Button btnCalc = (Button) mRootView.findViewById(R.id.btnCalculate);
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

                    SeachemDosage[] dosages;

                    try {
                        dosages = mProduct.calculateDosage(DoserApplication.getDoserPreferences().getUnitMeasurement());

                        // prevent negative dosages
                        for (SeachemDosage dosage : dosages) {
                            if (dosage.getAmount() <= 0) {
                                throw new ArithmeticException("Dosage can't be negative.");
                            }
                        }
                    } catch (ArithmeticException ex) {
                        Log.e(TAG, ex.toString());
                        //shouldn't happen, but just incase
                        Toast.makeText(getActivity(),
                                getString(R.string.calculation_error),
                                Toast.LENGTH_LONG).show();
                        return;
                    }

                    for (int i = 0, dosagesLength = dosages.length; i < dosagesLength; i++) {
                        SeachemDosage dosage = dosages[i];
                        String value = decimalFormat.format(dosage.getAmount());
                        dosageResultViews.get(i).setValue(value);
                    }

                    DoserApplication.getDoserPreferences().incrementTotalCalculations();
                }
            });

            UnitMeasurement unitMeasurement = DoserApplication.getDoserPreferences().getUnitMeasurement();
            initializeParameterViews(unitMeasurement, mRootView);
            initializeDosageViews(unitMeasurement, mRootView);
        }

        return mRootView;
    }

    @Override
    public void onSharedPreferenceChanged(SharedPreferences sharedPreferences, String key) {
        if (isAdded()) {
            if (key.equals(getString(R.string.pref_unit_measurement))) {
                UnitMeasurement unitMeasurement = DoserApplication.getDoserPreferences().
                        getUnitMeasurement();
                initializeParameterViews(unitMeasurement, mRootView);
                initializeDosageViews(unitMeasurement, mRootView);
            }
        }
    }
}
