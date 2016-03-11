package com.nateshoffner.seachemdoser.utils;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

import com.nateshoffner.seachemdoser.R;
import com.nateshoffner.seachemdoser.core.model.UnitMeasurement;

public class DoserPreferences {

    private Context mContext;
    private SharedPreferences mSharedPreferences;

    public DoserPreferences(Context context) {
        mContext = context;
        mSharedPreferences = PreferenceManager.getDefaultSharedPreferences(context);
    }

    public SharedPreferences getSharedPreferences() {
        return mSharedPreferences;
    }

    public UnitMeasurement getUnitMeasurement() {
        String str = mSharedPreferences.getString(
                mContext.getString(R.string.pref_unit_measurement), "Imperial");
        return UnitMeasurement.valueOf(str);
    }
}
