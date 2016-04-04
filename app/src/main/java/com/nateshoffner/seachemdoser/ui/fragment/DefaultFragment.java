package com.nateshoffner.seachemdoser.ui.fragment;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.nateshoffner.seachemdoser.R;
import com.nateshoffner.seachemdoser.ui.activity.DrawerActivity;

public class DefaultFragment extends Fragment {

    private View mRootView;

    public DefaultFragment() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        mRootView = inflater.inflate(R.layout.fragment_default, container, false);

        TextView tvVersion = (TextView) mRootView.findViewById(R.id.version);
        tvVersion.append(getString(R.string.version_name));

        Button btnStart = (Button)mRootView.findViewById(R.id.btnGetStarted);
        btnStart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ((DrawerActivity)getActivity()).openDrawer();
            }
        });

        return mRootView;
    }
}
