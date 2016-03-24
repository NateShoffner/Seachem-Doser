package com.nateshoffner.seachemdoser.ui.activity;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.nateshoffner.seachemdoser.DoserApplication;
import com.nateshoffner.seachemdoser.R;
import com.nateshoffner.seachemdoser.core.model.SeachemProduct;
import com.nateshoffner.seachemdoser.core.model.UnitMeasurement;
import com.nateshoffner.seachemdoser.ui.dialog.DoserChangelog;
import com.nateshoffner.seachemdoser.ui.fragment.ProductDetailFragment;
import com.nateshoffner.seachemdoser.ui.fragment.ProductSelectionFragment;
import com.nateshoffner.seachemdoser.ui.listener.ProductSelectionListener;
import com.nateshoffner.seachemdoser.utils.UnitLocale;


public class ProductListActivity extends BaseActivity implements ProductSelectionListener {

    private static final String TAG = "ProductListActivity";

    private boolean mTwoPane;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_product_list);

        ProductSelectionFragment productSelectionFragment = (ProductSelectionFragment)
                getSupportFragmentManager().findFragmentById(R.id.product_selection);

        if (findViewById(R.id.product_detail_container) != null) {
            mTwoPane = true;
            productSelectionFragment.setActivateOnItemClick(true);
        }

        final DoserChangelog cl = new DoserChangelog(this);
        if (cl.isFirstRun()) {
            final AlertDialog dialog = cl.getLogDialog();
            dialog.show();
            dialog.getButton(DialogInterface.BUTTON_POSITIVE).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    dialog.dismiss();

                    cl.updateVersionInPreferences();

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
                        DoserApplication.getDoserPreferences().setUnitMeasurement(unitMeasurement);
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
        SeachemProduct defaultProduct = DoserApplication.getDoserPreferences().getDefaultProduct();
        if (defaultProduct != null) {
            onProductSelected(defaultProduct);
        }
    }

    @Override
    public void onProductSelected(SeachemProduct product) {
        DoserApplication.getDoserPreferences().setLastProduct(product);

        if (mTwoPane) {
            getSupportActionBar().setSubtitle(product.getName());

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
    }
}
