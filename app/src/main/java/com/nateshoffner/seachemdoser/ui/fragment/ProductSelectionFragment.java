package com.nateshoffner.seachemdoser.ui.fragment;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTabHost;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.HorizontalScrollView;
import android.widget.LinearLayout;
import android.widget.TabHost;
import android.widget.TabWidget;
import android.widget.TextView;

import com.nateshoffner.seachemdoser.R;
import com.nateshoffner.seachemdoser.core.manager.SeachemManager;
import com.nateshoffner.seachemdoser.core.model.SeachemProductType;
import com.nateshoffner.seachemdoser.ui.adapter.ProductViewPagerAdapter;

import java.util.ArrayList;
import java.util.List;

public class ProductSelectionFragment extends Fragment {

    private static final String TAG = "ProductSelectionFragment";

    private List<ProductListFragment> mProductListFragments = new ArrayList<>();
    private FragmentTabHost mTabHost;
    private TabWidget mTabWidget;
    private ProductViewPagerAdapter mPagerAdapter;
    private ViewPager mViewPager;
    private HorizontalScrollView mHorizontalScrollView;

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

        mViewPager = (ViewPager) rootView.findViewById(R.id.pager);
        mTabHost = (FragmentTabHost) rootView.findViewById(android.R.id.tabhost);
        mTabWidget = (TabWidget) rootView.findViewById(android.R.id.tabs);
        mTabHost.setup(getActivity(), getFragmentManager(), R.id.realtabcontent);

        initializeTabs();

        mTabHost.setOnTabChangedListener(new TabHost.OnTabChangeListener() {

            @Override
            public void onTabChanged(String tabId) {
                mViewPager.setCurrentItem(mTabHost.getCurrentTab());
            }
        });

        mPagerAdapter = new ProductViewPagerAdapter(getFragmentManager(), SeachemManager.GetProductTypes());
        mViewPager.setAdapter(mPagerAdapter);
        mViewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageSelected(int position) {
                mTabHost.setCurrentTab(position);
            }

            @Override
            public void onPageScrolled(int arg0, float arg1, int arg2) {
            }

            @Override
            public void onPageScrollStateChanged(int arg0) {
            }
        });

        return rootView;
    }

    private void initializeTabs() {
        LinearLayout layout = (LinearLayout) mTabWidget.getParent();
        mHorizontalScrollView = new HorizontalScrollView(getActivity());
        mHorizontalScrollView.setLayoutParams(new FrameLayout.LayoutParams(
                FrameLayout.LayoutParams.MATCH_PARENT,
                FrameLayout.LayoutParams.WRAP_CONTENT));
        layout.addView(mHorizontalScrollView, 0);
        layout.removeView(mTabWidget);
        mHorizontalScrollView.addView(mTabWidget);
        mHorizontalScrollView.setFillViewport(true);
        mHorizontalScrollView.setHorizontalScrollBarEnabled(false);

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
