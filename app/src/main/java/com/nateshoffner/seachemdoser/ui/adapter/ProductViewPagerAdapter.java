package com.nateshoffner.seachemdoser.ui.adapter;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import com.nateshoffner.seachemdoser.core.model.SeachemProductType;
import com.nateshoffner.seachemdoser.ui.fragment.ProductListFragment;

import java.util.List;

public class ProductViewPagerAdapter extends FragmentPagerAdapter {

    private List<SeachemProductType> mProductTypes;

    public ProductViewPagerAdapter(FragmentManager fm, List<SeachemProductType> productTypes) {
        super(fm);
        mProductTypes = productTypes;
    }

    @Override
    public Fragment getItem(int i) {
        ProductListFragment fragment = new ProductListFragment();
        Bundle arguments = new Bundle();
        arguments.putSerializable(ProductListFragment.EXTRA_PRODUCT_TYPE, mProductTypes.get(i));
        fragment.setArguments(arguments);
        return fragment;
    }

    @Override
    public int getCount() {
        return mProductTypes.size();
    }
}
