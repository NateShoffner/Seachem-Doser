package com.nateshoffner.seachemdoser.ui.fragment;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTabHost;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TabHost;

import com.nateshoffner.seachemdoser.R;
import com.nateshoffner.seachemdoser.core.manager.SeachemManager;
import com.nateshoffner.seachemdoser.core.model.SeachemProductType;

import java.util.ArrayList;
import java.util.List;

public class ProductSelectionFragment extends Fragment {

    private static final String TAG = "ProductSelectionFragment";

    private List<ProductListFragment> mProductListFragments = new ArrayList<>();

    public ProductSelectionFragment() {

    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View rootView = inflater.inflate(R.layout.fragment_product_selection, container, false);

        FragmentTabHost tabHost = (FragmentTabHost) rootView.findViewById(android.R.id.tabhost);
        tabHost.setup(getActivity(), getFragmentManager(), R.id.realtabcontent);

        for (SeachemProductType type : SeachemManager.GetProductTypes()) {

            TabHost.TabSpec spec = tabHost.newTabSpec(type.name());

            ProductListFragment fragment = new ProductListFragment();

            mProductListFragments.add(fragment);

            Bundle arguments = new Bundle();
            arguments.putSerializable(ProductListFragment.EXTRA_PRODUCT_TYPE, type);
            tabHost.addTab(spec.setIndicator(type.name()), fragment.getClass(), arguments);
        }

        return rootView;
    }

    public void setActivateOnItemClick(boolean activateOnItemClick) {
        for (ProductListFragment fragment : mProductListFragments) {
            fragment.setActivateOnItemClick(activateOnItemClick);
        }
    }
}
