package com.nateshoffner.seachemdoser.ui.fragment;

import android.content.DialogInterface;
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
import android.widget.ScrollView;
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

    private static final String EXTRA_DOSAGES_CALCULATED = "DOSAGES_CALCULATED";

    private static final String TAG = "ProductFragment";

    private final List<DosageInputView> dosageInputViews = new ArrayList<>();

    private LinearLayout mParamsContainer;

    private ScrollView mRootScroll;
    private MenuItem btnPin;
    private Button btnCalc;

    private SeachemProduct mProduct;

    private boolean mDosagesCalculated;

    public ProductFragment() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        mProduct = (SeachemProduct) getArguments().getSerializable(EXTRA_PRODUCT);

        setHasOptionsMenu(true);
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putBoolean(EXTRA_DOSAGES_CALCULATED, mDosagesCalculated);
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        if (savedInstanceState != null) {
            mDosagesCalculated = savedInstanceState.getBoolean(EXTRA_DOSAGES_CALCULATED);
            if (mDosagesCalculated)
                calculateDosage(true);
        }
    }

    private void initializeParameterViews(UnitMeasurement unitMeasurement) {
        if (dosageInputViews.size() > 0) {
            dosageInputViews.clear();

            mParamsContainer.removeAllViews();
        }

        for (SeachemParameter param : mProduct.getParameters().get(unitMeasurement)) {
            DosageInputView view = new DosageInputView(getActivity(), null);
            view.setLabelText(param.getName() + ":");
            view.setUnitQualifier(param.getUnit());
            view.setUnitText();
            dosageInputViews.add(view);


            if (DoserApplication.getDoserPreferences().getUseRecommendedParamValues() &&
                    param.getValue() > 0)
                view.setValue(param.getValue());
            mParamsContainer.addView(view);
        }

        if (DoserApplication.getDoserPreferences().getUseRecommendedParamValues()) {
            populateRecommendedDoses(unitMeasurement);
        }
    }

    private void populateRecommendedDoses(UnitMeasurement unitMeasurement) {
        SeachemParameter[] params = mProduct.getParameters().get(unitMeasurement);
        for (int i = 0; i < params.length; i++) {
            SeachemParameter param = params[i];

            if (param.getRecommendedValue() > 0) {
                DosageInputView view = dosageInputViews.get(i);

                if (view.getInputView().getText().toString().length() == 0)
                    view.setValue(param.getRecommendedValue());
            }
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View mRootView = inflater.inflate(R.layout.fragment_product, container, false);
        mRootScroll = (ScrollView) mRootView.findViewById(R.id.root_scroll);
        mParamsContainer = (LinearLayout) mRootView.findViewById(R.id.params_container);
        btnCalc = (Button) mRootView.findViewById(R.id.btnCalculate);
        ((TextView) mRootView.findViewById(R.id.tvName)).setText(mProduct.getName());

        DoserApplication.getDoserPreferences().getSharedPreferences().
                registerOnSharedPreferenceChangeListener(this);

        btnCalc.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                calculateDosage(false);
            }
        });

        UnitMeasurement unitMeasurement = DoserApplication.getDoserPreferences().getUnitMeasurement();
        initializeParameterViews(unitMeasurement);

        if (!mDosagesCalculated) {
            // manually focus on first param
            dosageInputViews.get(0).getInputView().requestFocus();
        }

        return mRootView;
    }

    private void calculateDosage(boolean suppressErrors) {
        boolean invalidInput = false;

        int inputCount = 0;
        for (DosageInputView view : dosageInputViews) {

            String value = view.getInputView().getText().toString();

            if (value.length() == 0) {
                invalidInput = true;
                break;
            }

            mProduct.getParameters().get(DoserApplication.getDoserPreferences().getUnitMeasurement())[inputCount].
                    setValue(Double.parseDouble(String.valueOf(view.getInputView().getText().toString())));
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
            if (!suppressErrors) {
                Toast.makeText(getActivity(), getString(R.string.calculation_error), Toast.LENGTH_LONG).show();
            }

            return;
        }

        if (!mDosagesCalculated) {
            mDosagesCalculated = true;
        }

        // hide keyboard after calculation
        KeyboardUtil.hideKeyboard(getActivity());

        MaterialDialog.Builder builder = new MaterialDialog.Builder(getContext());
        builder.cancelListener(new DialogInterface.OnCancelListener() {
            @Override
            public void onCancel(DialogInterface dialogInterface) {
                mDosagesCalculated = false;
            }
        });
        builder.dismissListener(new DialogInterface.OnDismissListener() {
            @Override
            public void onDismiss(DialogInterface dialog) {
                mDosagesCalculated = false;
            }
        });

        MaterialDialog dosageDialog = new DosageDialog(builder, dosages,
                mProduct.getWarnings(),
                mProduct.getNotes())
                .getDialog(getContext());

        dosageDialog.show();

        // scroll to top of layout
        mRootScroll.fullScroll(ScrollView.FOCUS_UP);

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
                UnitMeasurement unitMeasurement = DoserApplication.getDoserPreferences().
                        getUnitMeasurement();
                initializeParameterViews(unitMeasurement);
            }

            if (key.equals(getString(R.string.pref_unit_measurement))) {
                UnitMeasurement unitMeasurement = DoserApplication.getDoserPreferences().
                        getUnitMeasurement();

                if (DoserApplication.getDoserPreferences().getUseRecommendedParamValues()) {
                    populateRecommendedDoses(unitMeasurement);
                }
            }
        }
    }
}
