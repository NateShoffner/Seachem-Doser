package com.nateshoffner.seachemdoser.ui.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.View;

import com.nateshoffner.seachemdoser.R;
import com.nateshoffner.seachemdoser.core.model.SeachemProductType;


public class MainActivity extends BaseActivity {

    private View.OnClickListener onProductClickListener = new View.OnClickListener() {

        @Override
        public void onClick(View v) {

            SeachemProductType productType = null;

            switch (v.getId()) {
                case R.id.btnGravel:
                    productType = SeachemProductType.Gravel;
                    break;
                case R.id.btnPlanted:
                    productType = SeachemProductType.Planted;
                    break;
                case R.id.btnReef:
                    productType = SeachemProductType.Reef;
                    break;
            }

            Intent intent = new Intent(getBaseContext(), ProductTypeActivity.class);
            intent.putExtra(ProductTypeActivity.EXTRA_SEACHEM_PRODUCT_TYPE, productType);
            startActivity(intent);
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        getSupportActionBar().hide();

        findViewById(R.id.btnGravel).setOnClickListener(onProductClickListener);
        findViewById(R.id.btnPlanted).setOnClickListener(onProductClickListener);
        findViewById(R.id.btnReef).setOnClickListener(onProductClickListener);

        findViewById(R.id.btnDownload).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getApplicationContext(), AboutActivity.class));
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.global, menu);
        return true;
    }
}
