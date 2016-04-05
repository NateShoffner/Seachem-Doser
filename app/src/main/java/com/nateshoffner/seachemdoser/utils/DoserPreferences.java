package com.nateshoffner.seachemdoser.utils;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.nateshoffner.seachemdoser.R;
import com.nateshoffner.seachemdoser.core.manager.SeachemManager;
import com.nateshoffner.seachemdoser.core.model.SeachemProduct;
import com.nateshoffner.seachemdoser.core.model.UnitMeasurement;

import java.util.ArrayList;
import java.util.List;

public class DoserPreferences {

    private Context mContext;
    private SharedPreferences mSharedPreferences;

    public DoserPreferences(Context context) {
        mContext = context;
        mSharedPreferences = PreferenceManager.getDefaultSharedPreferences(context);
    }

    private SharedPreferences.Editor getEditor() {
        return mSharedPreferences.edit();
    }

    public SharedPreferences getSharedPreferences() {
        return mSharedPreferences;
    }

    private String getString(int resId) {
        return mContext.getString(resId);
    }

    private String getPreferenceKey(int resId) {
        return getString(resId);
    }

    public boolean isUnitMeasurementSet() {
        return getUnitMeasurement() != null;
    }

    public void setUnitMeasurement(UnitMeasurement unitMeasurement) {
        SharedPreferences.Editor editor = getEditor();
        editor.putString(getPreferenceKey(R.string.pref_unit_measurement),
                Integer.toString(unitMeasurement.getId()));
        editor.commit();
    }

    public UnitMeasurement getUnitMeasurement() {
        int id = Integer.parseInt(mSharedPreferences.getString(
                getPreferenceKey(R.string.pref_unit_measurement), "-1"));
        return UnitMeasurement.fromId(id);
    }

    public String getDefaultProductString() {
        return mSharedPreferences.getString(getPreferenceKey(R.string.pref_default_product),
                mContext.getString(R.string.most_recent));
    }

    public void setDefaultProduct(SeachemProduct product) {
        SharedPreferences.Editor editor = getEditor();
        editor.putString(getPreferenceKey(R.string.pref_default_product), product.getName());
        editor.commit();
    }

    public void setLastProduct(SeachemProduct product) {
        SharedPreferences.Editor editor = getEditor();
        editor.putString(getPreferenceKey(R.string.pref_last_product), product.getName());
        editor.commit();
    }

    public SeachemProduct getDefaultProduct() {
        String defaultProductStr = getDefaultProductString();

        Boolean restoreLastProduct = defaultProductStr
                .equals(mContext.getString(R.string.most_recent));

        if (restoreLastProduct) {
            String name = mSharedPreferences.getString(
                    getPreferenceKey(R.string.pref_last_product), null);

            if (name != null)
                return SeachemManager.getProductByName(name);
        } else {
            return SeachemManager.getProductByName(defaultProductStr);
        }

        return null;
    }

    public ThemeHelper.Theme getTheme() {
        int themeId = Integer.parseInt(mSharedPreferences.getString(
                getPreferenceKey(R.string.pref_theme), "0"));
        return ThemeHelper.fromId(themeId);
    }

    public int getTotalCalculations() {
        return mSharedPreferences.getInt(getPreferenceKey(R.string.pref_total_calculations), 0);
    }

    public void incrementTotalCalculations() {
        int total = getTotalCalculations();
        getEditor().putInt(getPreferenceKey(R.string.pref_total_calculations), total + 1);
    }

    public boolean getHasBeenPromptedForRating() {
        return mSharedPreferences.getBoolean(getPreferenceKey(R.string.pref_prompted_for_rating),
                false);
    }

    public void setHasBeenPromptedForRating(boolean prompted) {
        SharedPreferences.Editor editor = getEditor();
        editor.putBoolean(getPreferenceKey(R.string.pref_prompted_for_rating), prompted);
        editor.commit();
    }

    public List<SeachemProduct> getPinnedProducts() {
        String json = mSharedPreferences.getString(getPreferenceKey(R.string.pref_pinned_products), null);

        if (json != null) {
            GsonBuilder builder = new GsonBuilder();
            Gson gson = builder.create();
            String[] names = gson.fromJson(json, String[].class);

            List<SeachemProduct> products = new ArrayList<>();

            for (String name : names) {
                SeachemProduct productMatch = SeachemManager.getProductByName(name);
                if (productMatch != null)
                    products.add(productMatch);
            }

            return products;
        }

        return new ArrayList<>();
    }

    private void savePinnedProducts(List<SeachemProduct> products) {
        String[] names = new String[products.size()];
        for (int i = 0; i < products.size(); i++) {
            SeachemProduct product = products.get(i);
            names[i] = product.getName();
        }

        GsonBuilder builder = new GsonBuilder();
        Gson gson = builder.create();
        String value = gson.toJson(names);
        SharedPreferences.Editor editor = getEditor();
        editor.putString(getPreferenceKey(R.string.pref_pinned_products), value);
        editor.commit();
    }

    public boolean isProductPinned(SeachemProduct product) {
        return getPinnedProducts().contains(product);
    }

    public void addPinnedProduct(SeachemProduct product) {
        List<SeachemProduct> products = getPinnedProducts();
        products.add(product);
        savePinnedProducts(products);
    }

    public void removePinnedProduct(SeachemProduct product) {
        List<SeachemProduct> products = getPinnedProducts();
        products.remove(product);
        savePinnedProducts(products);
    }

    public boolean getUseRecommendedParamValues() {
        return mSharedPreferences.getBoolean(getPreferenceKey(
                R.string.pref_use_recommended_param_values), true);
    }

    public boolean getCopyDosages() {
        return mSharedPreferences.getBoolean(getPreferenceKey(
                R.string.pref_copy_dosages), true);
    }
}
