package com.nateshoffner.seachemdoser.ui.fragment;

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
import com.nateshoffner.seachemdoser.core.model.SeachemProductType;
import com.nateshoffner.seachemdoser.ui.listener.ProductSelectionListener;

import java.util.ArrayList;
import java.util.List;

public class ProductSelectionFragment extends Fragment {

    private static final String TAG = "ProductSelectionFragment";

    private List<ProductListFragment> mProductListFragments = new ArrayList<>();
    private FragmentTabHost mTabHost;

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

        mTabHost = (FragmentTabHost) rootView.findViewById(android.R.id.tabhost);
        mTabHost.setup(getActivity(), getFragmentManager(), R.id.realtabcontent);

        List<SeachemProductType> productTypes = SeachemManager.GetProductTypes();

        for (int i = 0; i < productTypes.size(); i++) {
            SeachemProductType type = productTypes.get(i);

            TabHost.TabSpec spec = mTabHost.newTabSpec(type.name());

            ProductListFragment fragment = new ProductListFragment();

            mProductListFragments.add(fragment);

            Bundle arguments = new Bundle();
            arguments.putSerializable(ProductListFragment.EXTRA_PRODUCT_TYPE, type);
            mTabHost.addTab(spec.setIndicator(type.name()), fragment.getClass(), arguments);

            int color = 0;
            if (type == SeachemProductType.Gravel)
                color = ContextCompat.getColor(getActivity(), R.color.product_type_gravel);
            else if (type == SeachemProductType.Planted)
                color = ContextCompat.getColor(getActivity(), R.color.product_type_planted);
            else if (type == SeachemProductType.Reef)
                color = ContextCompat.getColor(getActivity(), R.color.product_type_reef);

            TextView tv = (TextView) mTabHost.getTabWidget().getChildAt(i).
                    findViewById(android.R.id.title);
            tv.setTextColor(color);
        }

        mTabHost.setOnTabChangedListener(new TabHost.OnTabChangeListener() {

            @Override
            public void onTabChanged(String tabId) {
                SeachemProductType type = SeachemProductType.valueOf(tabId);
                ((ProductSelectionListener) getActivity()).onProductTypeSelected(type);
            }
        });

        return rootView;
    }

    public void setActivateOnItemClick(boolean activateOnItemClick) {
        for (ProductListFragment fragment : mProductListFragments) {
            fragment.setActivateOnItemClick(activateOnItemClick);
        }
    }

    public void setCurrentProductType(SeachemProductType type) {
        mTabHost.setCurrentTabByTag(type.name());
    }
}
