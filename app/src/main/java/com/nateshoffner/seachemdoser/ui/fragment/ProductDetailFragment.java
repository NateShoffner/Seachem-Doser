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
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.Toast;

import com.afollestad.materialdialogs.MaterialDialog;
import com.mikepenz.fontawesome_typeface_library.FontAwesome;
import com.mikepenz.iconics.IconicsDrawable;
import com.mikepenz.materialdrawer.util.KeyboardUtil;
import com.nateshoffner.seachemdoser.DoserApplication;
import com.nateshoffner.seachemdoser.R;
import com.nateshoffner.seachemdoser.core.model.SeachemDosage;
import com.nateshoffner.seachemdoser.core.model.SeachemParameter;
import com.nateshoffner.seachemdoser.core.model.SeachemProduct;
import com.nateshoffner.seachemdoser.core.model.UnitMeasurement;
import com.nateshoffner.seachemdoser.ui.view.DosageResultView;
import com.nateshoffner.seachemdoser.ui.view.ParameterInputView;
import com.nateshoffner.seachemdoser.utils.StringUtils;

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

    private LinearLayout mDosagesContainer;
    private LinearLayout mParamsContainer;
    private LinearLayout mInfoContainer;

    private ScrollView mRootScroll;
    private MenuItem btnPin;

    private SeachemProduct mProduct;

    private boolean mDosagesVisible;

    public SeachemProduct getProduct() {
        return mProduct;
    }

    public ProductDetailFragment() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (getArguments().containsKey(EXTRA_PRODUCT)) {
            mProduct = (SeachemProduct) getArguments().getSerializable(EXTRA_PRODUCT);
        }

        setHasOptionsMenu(true);
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putBoolean("dosagesVisible", mDosagesVisible);
    }

    private void initializeParameterViews(UnitMeasurement unitMeasurement) {
        if (dosageInputs.size() > 0) {
            dosageInputs.clear();

            mParamsContainer.removeAllViews();
        }

        for (SeachemParameter param : mProduct.getParameters().get(unitMeasurement)) {
            ParameterInputView view = new ParameterInputView(getActivity(), null);
            view.setLabelText(param.getName() + ":");
            view.setUnitText(param.getUnit());
            dosageInputs.add(view.getInputView());


            if (DoserApplication.getDoserPreferences().getUseRecommendedParamValues() &&
                    param.getValue() > 0)
                view.setValue(decimalFormat.format(param.getValue()));
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
                EditText input = dosageInputs.get(i);

                if (input.getText().toString().length() == 0)
                    input.setText(decimalFormat.format(param.getRecommendedValue()));
            }
        }
    }

    private void initializeDosageViews(UnitMeasurement unitMeasurement) {
        if (dosageResultViews.size() > 0) {
            dosageResultViews.clear();

            mDosagesContainer.removeAllViews();
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
            mDosagesContainer.addView(view);
        }
    }

    private void initializeAdditionalInfoViews() {
        if (mInfoContainer == null)
            return;

        /*
        TextView tvWarnings = (TextView) mRootView.findViewById(R.id.product_warnings);
        tvWarnings.setText(StringUtils.join(mProduct.getWarnings(), "\n\n"));
        tvWarnings.setVisibility(mProduct.getWarnings().length > 0 ? View.VISIBLE : View.GONE);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR1) {
            tvWarnings.setCompoundDrawablesRelativeWithIntrinsicBounds(new IconicsDrawable(getContext(),
                    FontAwesome.Icon.faw_exclamation_circle).actionBar().color(Color.LTGRAY), null, null, null);
        }

        TextView tvInfo = (TextView) mRootView.findViewById(R.id.product_info);
        tvInfo.setText(StringUtils.join(mProduct.getComments(), "\n\n"));
        tvInfo.setVisibility(mProduct.getComments().length > 0 ? View.VISIBLE : View.GONE);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR1) {
            tvInfo.setCompoundDrawablesRelativeWithIntrinsicBounds(new IconicsDrawable(getContext(),
                    FontAwesome.Icon.faw_info_circle).actionBar().color(Color.LTGRAY), null, null, null);
        }*/
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View mRootView = inflater.inflate(R.layout.fragment_product_detail, container, false);
        mRootScroll = (ScrollView) mRootView.findViewById(R.id.root_scroll);
        mDosagesContainer = (LinearLayout) mRootView.findViewById(R.id.dosages_container);
        mParamsContainer = (LinearLayout) mRootView.findViewById(R.id.params_container);
        mInfoContainer = (LinearLayout) mRootView.findViewById(R.id.info_container);
        mDosagesContainer.setVisibility(View.INVISIBLE);

        if (savedInstanceState != null) {
            if (savedInstanceState.containsKey("dosagesVisible")) {
                mDosagesVisible = savedInstanceState.getBoolean("dosagesVisible");
                if (!mDosagesVisible) {
                    mDosagesContainer.setVisibility(View.INVISIBLE);
                }
            }
        }

        DoserApplication.getDoserPreferences().getSharedPreferences().
                registerOnSharedPreferenceChangeListener(this);

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

                    mProduct.getParameters().get(DoserApplication.getDoserPreferences().getUnitMeasurement())[inputCount].
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

                if (!mDosagesVisible) {
                    mDosagesContainer.setVisibility(View.VISIBLE);
                    mDosagesVisible = true;
                }

                // hide keyboard after calculation
                KeyboardUtil.hideKeyboard(getActivity());

                // scroll to top of layout
                mRootScroll.fullScroll(ScrollView.FOCUS_UP);

                DoserApplication.getDoserPreferences().incrementTotalCalculations();
            }
        });

        UnitMeasurement unitMeasurement = DoserApplication.getDoserPreferences().getUnitMeasurement();
        initializeParameterViews(unitMeasurement);
        initializeDosageViews(unitMeasurement);
        initializeAdditionalInfoViews();

        // manually focus on first param
        dosageInputs.get(0).requestFocus();

        return mRootView;
    }

    private void showWarningDialog() {
        new MaterialDialog.Builder(getActivity())
                .content(StringUtils.join(mProduct.getWarnings(), "\n\n"))
                .show();
    }


    private void showCommentDialog() {
        new MaterialDialog.Builder(getActivity())
                .content(StringUtils.join(mProduct.getComments(), "\n\n"))
                .show();
    }

    private void updatePinnedButton() {
        boolean isPinned = DoserApplication.getDoserPreferences().isProductPinned(mProduct);
        IconicsDrawable icon = new IconicsDrawable(getContext(),
                FontAwesome.Icon.faw_thumb_tack).actionBar();

        icon.color(isPinned ? Color.WHITE : Color.GRAY);

        btnPin.setTitle(isPinned ? R.string.unpin : R.string.pin);
        btnPin.setIcon(icon);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_pin:
                boolean isPinned = DoserApplication.getDoserPreferences().isProductPinned(mProduct);

                if (isPinned) {
                    DoserApplication.getDoserPreferences().removePinnedProduct(mProduct);
                    Toast.makeText(getActivity(), String.format("%s %s", mProduct.getName(),
                            getString(R.string.has_been_unpinned)), Toast.LENGTH_SHORT).show();
                } else {
                    DoserApplication.getDoserPreferences().addPinnedProduct(mProduct);
                    Toast.makeText(getActivity(), String.format("%s %s", mProduct.getName(),
                            getString(R.string.has_been_pinned)), Toast.LENGTH_SHORT).show();
                }
                updatePinnedButton();
                return true;
            case R.id.action_warning:
                showWarningDialog();
                return true;
            case R.id.action_info:
                showCommentDialog();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.detail_fragment_menu, menu);
        btnPin = menu.findItem(R.id.action_pin);

        MenuItem warningItem = menu.findItem(R.id.action_warning);
        if (warningItem != null) {
            warningItem.setIcon(new IconicsDrawable(getContext(),
                    FontAwesome.Icon.faw_exclamation_circle).actionBar().color(Color.LTGRAY))
                    .setVisible(mProduct.getWarnings().length > 0);
        }

        MenuItem infoItem = menu.findItem(R.id.action_info);
        if (infoItem != null) {
            infoItem.setIcon(new IconicsDrawable(getContext(),
                    FontAwesome.Icon.faw_info_circle).actionBar().color(Color.LTGRAY))
                    .setVisible(mProduct.getComments().length > 0);
        }

        updatePinnedButton();
        super.onCreateOptionsMenu(menu, inflater);
    }

    @Override
    public void onSharedPreferenceChanged(SharedPreferences sharedPreferences, String key) {
        if (isAdded()) {
            if (key.equals(getString(R.string.pref_unit_measurement))) {
                UnitMeasurement unitMeasurement = DoserApplication.getDoserPreferences().
                        getUnitMeasurement();
                initializeParameterViews(unitMeasurement);
                initializeDosageViews(unitMeasurement);
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
