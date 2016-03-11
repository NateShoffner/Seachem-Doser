package com.nateshoffner.seachemdoser.ui.activity;

import android.os.Bundle;

import com.nateshoffner.seachemdoser.R;
import com.nateshoffner.seachemdoser.core.model.SeachemProduct;
import com.nateshoffner.seachemdoser.ui.fragment.ProductDetailFragment;

public class ProductDetailActivity extends BaseActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_product_detail);

        // Show the Up button in the action bar.
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        if (savedInstanceState == null) {

            SeachemProduct product = (SeachemProduct) getIntent().
                    getSerializableExtra(ProductDetailFragment.EXTRA_PRODUCT);
            setTitle(product.getName());

            Bundle arguments = new Bundle();
            arguments.putSerializable(ProductDetailFragment.EXTRA_PRODUCT, product);
            ProductDetailFragment fragment = new ProductDetailFragment();
            fragment.setArguments(arguments);
            getSupportFragmentManager().beginTransaction()
                    .add(R.id.product_detail_container, fragment)
                    .commit();
        }
    }
}
