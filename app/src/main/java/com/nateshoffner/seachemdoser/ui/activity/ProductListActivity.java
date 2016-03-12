package com.nateshoffner.seachemdoser.ui.activity;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.View;
import android.widget.TextView;

import com.nateshoffner.seachemdoser.DoserApplication;
import com.nateshoffner.seachemdoser.R;
import com.nateshoffner.seachemdoser.core.model.SeachemProduct;
import com.nateshoffner.seachemdoser.core.model.SeachemProductType;
import com.nateshoffner.seachemdoser.core.model.UnitMeasurement;
import com.nateshoffner.seachemdoser.ui.dialog.DoserChangelog;
import com.nateshoffner.seachemdoser.ui.fragment.ProductDetailFragment;
import com.nateshoffner.seachemdoser.ui.fragment.ProductSelectionFragment;
import com.nateshoffner.seachemdoser.ui.listener.ProductSelectionListener;
import com.nateshoffner.seachemdoser.utils.UnitLocale;

public class ProductListActivity extends BaseActivity implements ProductSelectionListener {

    private static final String TAG = "ProductListActivity";

    private boolean mTwoPane;
    private SharedPreferences mSharedPreferences;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_product_list);

        mSharedPreferences = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());

        ProductSelectionFragment productSelectionFragment = (ProductSelectionFragment)
                getSupportFragmentManager().findFragmentById(R.id.product_selection);

        SeachemProductType selectedType = SeachemProductType.
                valueOf(mSharedPreferences.getString(getString(R.string.pref_last_product_type),
                        getString(R.string.pref_last_product_type_default)));

        productSelectionFragment.setCurrentProductType(selectedType);

        if (findViewById(R.id.product_detail_container) != null) {
            mTwoPane = true;
            productSelectionFragment.setActivateOnItemClick(true);
        }

        DoserChangelog cl = new DoserChangelog(this);
        if (cl.isFirstRun()) {
            final AlertDialog dialog = cl.getLogDialog();
            dialog.show();
            dialog.getButton(DialogInterface.BUTTON_POSITIVE).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    dialog.dismiss();

                    if (!DoserApplication.getDoserPreferences().isUnitMeasurementSet()) {
                        showUnitMeasurementPrompt();
                    }

                    showDefaultProduct();
                }
            });
        }

        else {
            showDefaultProduct();
        }
    }

    private void showUnitMeasurementPrompt() {
        UnitMeasurement detected = UnitLocale.getLocaleMeasurmentUnit();

        final CharSequence[] items = new CharSequence[UnitMeasurement.values().length];
        final UnitMeasurement[] unitMeasurements = UnitMeasurement.values();

        int checkedIndex = -1;
        String[] stringArray = getResources().getStringArray(R.array.unit_measurements);
        for (int i = 0; i < stringArray.length; i++) {
            UnitMeasurement unitMeasurement = unitMeasurements[i];
            items[i] = stringArray[i];

            if (unitMeasurement == detected)
                checkedIndex = i;
        }

        new AlertDialog.Builder(this)
                .setTitle("Unit Measurement")
                .setCancelable(false)
                .setSingleChoiceItems(items, checkedIndex, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int index) {
                        UnitMeasurement unitMeasurement = unitMeasurements[index];
                        DoserApplication.getDoserPreferences().setUnitMeasurment(unitMeasurement);
                    }
                })
                .setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                })
                .show();
    }

    private void showDefaultProduct() {
        SeachemProduct lastProductUsed = DoserApplication.getDoserPreferences().getLastProductUsed();
        if (lastProductUsed != null) {
            onProductSelected(lastProductUsed);
        }
    }

    @Override
    public void onProductSelected(SeachemProduct product) {

        if (mTwoPane) {

            setTitle(product.getName());

            TextView tvPlaceholder = (TextView) findViewById(R.id.label_product_placeholder);

            if (tvPlaceholder.getVisibility() == View.VISIBLE) {
                tvPlaceholder.setVisibility(View.GONE);
            }

            Bundle arguments = new Bundle();
            arguments.putSerializable(ProductDetailFragment.EXTRA_PRODUCT, product);
            ProductDetailFragment fragment = new ProductDetailFragment();
            fragment.setArguments(arguments);
            getSupportFragmentManager().beginTransaction()
                    .replace(R.id.product_detail_container, fragment)
                    .commit();

        } else {
            Intent detailIntent = new Intent(ProductListActivity.this,
                    ProductDetailActivity.class);
            detailIntent.putExtra(ProductDetailFragment.EXTRA_PRODUCT, product);
            startActivity(detailIntent);
        }

        DoserApplication.getDoserPreferences().setLastProductUsed(product);
    }
}
