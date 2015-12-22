package com.nateshoffner.seachemdoser.ui.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.nateshoffner.seachemdoser.R;
import com.nateshoffner.seachemdoser.core.model.SeachemProduct;
import com.nateshoffner.seachemdoser.ui.fragment.ProductDetailFragment;
import com.nateshoffner.seachemdoser.ui.fragment.ProductSelectionFragment;
import com.nateshoffner.seachemdoser.ui.listener.ProductSelectionListener;

public class ProductListActivity extends BaseActivity implements ProductSelectionListener {

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
    }

    @Override
    public void onProductSelected(SeachemProduct product) {

        if (mTwoPane) {

            setTitle(product.getName());

            TextView tvPlaceholder =(TextView)findViewById(R.id.label_product_placeholder);

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
