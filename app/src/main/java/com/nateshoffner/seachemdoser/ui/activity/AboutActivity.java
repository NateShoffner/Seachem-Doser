package com.nateshoffner.seachemdoser.ui.activity;

import android.os.Bundle;
import android.widget.TextView;

import com.nateshoffner.seachemdoser.BuildConfig;
import com.nateshoffner.seachemdoser.R;

public class AboutActivity extends BaseActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_about);

        TextView tvVersion = (TextView) findViewById(R.id.tvVersion);
        tvVersion.append(BuildConfig.VERSION_NAME);
    }
}
