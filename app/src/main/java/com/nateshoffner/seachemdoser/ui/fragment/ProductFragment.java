package com.nateshoffner.seachemdoser.ui.fragment;

import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.afollestad.materialdialogs.MaterialDialog;
import com.mikepenz.fontawesome_typeface_library.FontAwesome;
import com.mikepenz.google_material_typeface_library.GoogleMaterial;
import com.mikepenz.iconics.IconicsDrawable;
import com.mikepenz.materialdrawer.util.KeyboardUtil;
import com.nateshoffner.seachemdoser.DoserApplication;
import com.nateshoffner.seachemdoser.R;
import com.nateshoffner.seachemdoser.core.model.SeachemDosage;
import com.nateshoffner.seachemdoser.core.model.SeachemParameter;
import com.nateshoffner.seachemdoser.core.model.SeachemProduct;
import com.nateshoffner.seachemdoser.core.model.UnitMeasurement;
import com.nateshoffner.seachemdoser.ui.dialog.DosageDialog;
import com.nateshoffner.seachemdoser.ui.view.DosageInputView;
import java.util.ArrayList;
import java.util.List;

public class ProductFragment extends Fragment
        implements SharedPreferences.OnSharedPreferenceChangeListener {

    public static final String EXTRA_PRODUCT = "PRODUCT";

    private static final String EXTRA_PARAM_VALUES = "PARAMETER_VALUES";

    private static final String TAG = "ProductFragment";

    private List<DosageInputView> dosageInputViews = new ArrayList<>();

    private LinearLayout mParamsContainer;
    private Button btnCalc;

    private MenuItem btnPin;

    private SeachemProduct mProduct;

    private UnitMeasurement mUnitMeasurement;

    public ProductFragment() {
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);

        // back up parameter values
        String[] paramValues = new String[dosageInputViews.size()];
        for (int i = 0; i < dosageInputViews.size(); i++) {
            DosageInputView v = dosageInputViews.get(i);
            paramValues[i] = v.getValue();
        }
        outState.putStringArray(EXTRA_PARAM_VALUES, paramValues);
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        updateInputViewDetails();

        if (savedInstanceState != null) {
            if (savedInstanceState.containsKey(EXTRA_PARAM_VALUES)) {
                String[] values = savedInstanceState.getStringArray(EXTRA_PARAM_VALUES);
                for (int i = 0; i < values.length; i++) {
                    String value = values[i];
                    if (value.length() > 0)
                        dosageInputViews.get(i).setValue(Double.parseDouble(value));
                }
            }
        }

        DosageInputView requiredInput = getRequiredInput();

        if (requiredInput != null) {
            Log.i(TAG, "required input is not null");
            requiredInput.getInputView().requestFocus();
        } else {
            Log.i(TAG, "required input is null");
            btnCalc.requestFocus();
        }
    }

    private DosageInputView getRequiredInput() {
        for (DosageInputView v : dosageInputViews) {
            if (v.getValue().length() == 0)
                return v;
        }

        return null;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        mProduct = (SeachemProduct) getArguments().getSerializable(EXTRA_PRODUCT);
        mUnitMeasurement = DoserApplication.getDoserPreferences().getUnitMeasurement();

        setHasOptionsMenu(true);
    }

    private void updateInputViewDetails() {
        boolean needsInitialized = dosageInputViews.size() == 0;

        SeachemParameter[] get = mProduct.getParameters().get(mUnitMeasurement);
        for (int i = 0; i < get.length; i++) {
            SeachemParameter param = get[i];

            DosageInputView view = needsInitialized ? new
                    DosageInputView(getActivity(), null) : dosageInputViews.get(i);

            view.setLabelText(param.getName() + ":");
            view.setUnitQualifier(param.getUnit());
            view.setUnitText();

            if (DoserApplication.getDoserPreferences().getUseRecommendedParamValues()
                    && param.getValue() == 0 && param.getRecommendedValue() > 0) {
                view.setValue(param.getRecommendedValue());
            }

            if (needsInitialized) {
                dosageInputViews.add(view);
                mParamsContainer.addView(view);
            }
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View mRootView = inflater.inflate(R.layout.fragment_product, container, false);
        mParamsContainer = (LinearLayout) mRootView.findViewById(R.id.params_container);
        btnCalc = (Button) mRootView.findViewById(R.id.btnCalculate);
        ((TextView) mRootView.findViewById(R.id.tvName)).setText(mProduct.getName());

        DoserApplication.getDoserPreferences().getSharedPreferences().
                registerOnSharedPreferenceChangeListener(this);

        btnCalc.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                calculateDosage();
            }
        });

        return mRootView;
    }

    private void calculateDosage() {
        boolean invalidInput = false;

        int inputCount = 0;
        for (DosageInputView view : dosageInputViews) {

            String value = view.getInputView().getText().toString();

            if (value.length() == 0) {
                invalidInput = true;
                break;
            }

            // store input value into working parameter
            mProduct.getParameters().get(mUnitMeasurement)[inputCount].
                    setValue(Double.parseDouble(String.valueOf(view.getInputView().getText().toString())));
            inputCount++;
        }
        if (invalidInput) {
            Toast.makeText(getActivity(), getString(R.string.invalid_parameters),
                    Toast.LENGTH_LONG).show();
            return;
        }

        SeachemDosage[] dosages;

        try {
            dosages = mProduct.calculateDosage(mUnitMeasurement);

            // prevent negative dosages
            for (SeachemDosage dosage : dosages) {
                if (dosage.getAmount() <= 0) {
                    throw new ArithmeticException("Dosage can't be negative.");
                }
            }
        } catch (ArithmeticException ex) {
            Log.e(TAG, ex.toString());
            Toast.makeText(getActivity(), getString(R.string.calculation_error), Toast.LENGTH_LONG).show();
            return;
        }

        // hide keyboard after calculation
        KeyboardUtil.hideKeyboard(getActivity());

        MaterialDialog.Builder builder = new MaterialDialog.Builder(getContext());
        MaterialDialog dosageDialog = new DosageDialog(builder, dosages,
                mProduct.getWarnings(),
                mProduct.getNotes())
                .getDialog(getContext());

        dosageDialog.show();

        DoserApplication.getDoserPreferences().incrementTotalCalculations();
    }

    private void updatePinnedButton() {
        boolean isPinned = DoserApplication.getDoserPreferences().isProductPinned(mProduct);
        btnPin.setTitle(isPinned ? R.string.unpin : R.string.pin);
        btnPin.setIcon(new IconicsDrawable(getContext(),
                FontAwesome.Icon.faw_thumb_tack).actionBar().color(isPinned ?
                Color.WHITE : Color.GRAY));
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_pin:
                boolean isPinned = DoserApplication.getDoserPreferences().isProductPinned(mProduct);

                if (isPinned) {
                    DoserApplication.getDoserPreferences().removePinnedProduct(mProduct);
                    Toast.makeText(getActivity(), getString(R.string.product_has_been_unpinned),
                            Toast.LENGTH_SHORT).show();
                } else {
                    DoserApplication.getDoserPreferences().addPinnedProduct(mProduct);
                    Toast.makeText(getActivity(), getString(R.string.product_has_been_pinned),
                            Toast.LENGTH_SHORT).show();
                }
                updatePinnedButton();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.product_menu, menu);
        btnPin = menu.findItem(R.id.action_pin);
        updatePinnedButton();

        MenuItem btnSettings = menu.findItem(R.id.action_settings);
        btnSettings.setIcon(new IconicsDrawable(getContext(),
                GoogleMaterial.Icon.gmd_settings).actionBar().color(Color.WHITE));

        super.onCreateOptionsMenu(menu, inflater);
    }

    @Override
    public void onSharedPreferenceChanged(SharedPreferences sharedPreferences, String key) {
        if (isAdded()) {
            if (key.equals(getString(R.string.pref_unit_measurement))) {
                mUnitMeasurement =  DoserApplication.getDoserPreferences().
                        getUnitMeasurement();
                updateInputViewDetails();
            }

            if (key.equals(getString(R.string.pref_use_recommended_param_values))) {
                updateInputViewDetails();
            }
        }
    }
}
