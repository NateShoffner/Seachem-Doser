package com.nateshoffner.seachemdoser.ui.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import com.nateshoffner.seachemdoser.R;

public class BaseActivity extends AppCompatActivity {

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        overridePendingTransition(0, 0);
        overridePendingTransition(R.anim.pull_in_right, R.anim.push_out_left);

    }

    @Override
    public void startActivity(Intent intent) {
        super.startActivity(intent);
        overridePendingTransition(0, 0);
        overridePendingTransition(R.anim.pull_in_left, R.anim.push_out_right);
    }
}