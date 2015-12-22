package com.nateshoffner.seachemdoser.ui.fragment;

import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTabHost;
import android.support.v4.content.ContextCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TabHost;
import android.widget.TextView;

import com.nateshoffner.seachemdoser.R;
import com.nateshoffner.seachemdoser.core.manager.SeachemManager;
import com.nateshoffner.seachemdoser.core.model.SeachemProduct;
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

        List<SeachemProductType> productTypes = SeachemManager.GetProductTypes();

        for (int i = 0; i < productTypes.size(); i++) {
            SeachemProductType type = productTypes.get(i);

            TabHost.TabSpec spec = tabHost.newTabSpec(type.name());

            ProductListFragment fragment = new ProductListFragment();

            mProductListFragments.add(fragment);

            Bundle arguments = new Bundle();
            arguments.putSerializable(ProductListFragment.EXTRA_PRODUCT_TYPE, type);
            tabHost.addTab(spec.setIndicator(type.name()), fragment.getClass(), arguments);

            int color = 0;
            if (type == SeachemProductType.Gravel)
                color = ContextCompat.getColor(getActivity(), R.color.product_type_gravel);
            else if (type == SeachemProductType.Planted)
                color = ContextCompat.getColor(getActivity(), R.color.product_type_planted);
            else if (type == SeachemProductType.Reef)
                color = ContextCompat.getColor(getActivity(), R.color.product_type_reef);

            TextView tv = (TextView) tabHost.getTabWidget().getChildAt(i).findViewById(android.R.id.title);
            tv.setTextColor(color);
        }

        return rootView;
    }

    public void setActivateOnItemClick(boolean activateOnItemClick) {
        for (ProductListFragment fragment : mProductListFragments) {
            fragment.setActivateOnItemClick(activateOnItemClick);
        }
    }
}
