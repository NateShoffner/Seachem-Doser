package com.nateshoffner.seachemdoser.ui.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListAdapter;
import android.widget.ListView;

import com.nateshoffner.seachemdoser.R;
import com.nateshoffner.seachemdoser.core.manager.SeachemManager;
import com.nateshoffner.seachemdoser.core.model.SeachemProduct;
import com.nateshoffner.seachemdoser.core.model.SeachemProductType;
import com.nateshoffner.seachemdoser.ui.adapter.ProductAdapter;

import java.util.List;


public class ProductTypeActivity extends BaseActivity {

    public static final String EXTRA_SEACHEM_PRODUCT_TYPE = "SEACHEM_PRODUCT_TYPE";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_product_type);

        Bundle extras = getIntent().getExtras();
        SeachemProductType productType = (SeachemProductType) extras.get(EXTRA_SEACHEM_PRODUCT_TYPE);

        final List<SeachemProduct> products = SeachemManager.GetProducts(productType);

        setTitle(productType.name());

        final ListAdapter adapter = new ProductAdapter(this,
                products.toArray(new SeachemProduct[products.size()]));

        ListView lv = (ListView) findViewById(R.id.lvProducts);

        lv.setAdapter(adapter);

        lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                SeachemProduct product = products.get(position);

                Bundle bundle = new Bundle();
                bundle.putSerializable(ProductActivity.EXTRA_SEACHEM_PRODUCT, product);

                Intent intent = new Intent(getBaseContext(), ProductActivity.class);
                intent.putExtras(bundle);
                startActivity(intent);
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.global, menu);
        return true;
    }
}
