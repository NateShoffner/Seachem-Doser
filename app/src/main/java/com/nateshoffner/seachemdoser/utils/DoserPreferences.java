package com.nateshoffner.seachemdoser.utils;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

import com.nateshoffner.seachemdoser.R;
import com.nateshoffner.seachemdoser.core.manager.SeachemManager;
import com.nateshoffner.seachemdoser.core.model.SeachemProduct;
import com.nateshoffner.seachemdoser.core.model.UnitMeasurement;

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
        }

        else {
            return SeachemManager.getProductByName(defaultProductStr);
        }

        return null;
    }

    public ThemeHelper.Theme getTheme() {
        int themeId = Integer.parseInt(mSharedPreferences.getString(
                getPreferenceKey(R.string.pref_theme), "0"));
        return ThemeHelper.fromId(themeId);
    }
}
