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


    public UnitMeasurement getUnitMeasurement() {
        String str = mSharedPreferences.getString(
                getPreferenceKey(R.string.pref_unit_measurement), "Imperial (US)");
        return UnitMeasurement.fromString(str);
    }

    public void setLastProductUsed(SeachemProduct product) {
        SharedPreferences.Editor editor = getEditor();
        editor.putString(getPreferenceKey(R.string.pref_last_product), product.getName());
        editor.commit();
    }

    public SeachemProduct getLastProductUsed() {

        if (mSharedPreferences.getBoolean(getPreferenceKey(R.string.pref_remember_last_product),
                false)) {

            String name = mSharedPreferences.getString(
                    getPreferenceKey(R.string.pref_last_product), null);

            if (name == null)
                return null;

            SeachemProduct lastProduct = null;

            for (SeachemProduct product : SeachemManager.GetProducts()) {
                if (product.getName().equalsIgnoreCase(name)) {
                    lastProduct = product;
                    break;
                }
            }

            return lastProduct;
        }

        return null;
    }
}
