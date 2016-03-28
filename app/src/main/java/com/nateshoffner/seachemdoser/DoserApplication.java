package com.nateshoffner.seachemdoser;

import android.content.Context;

import com.nateshoffner.seachemdoser.utils.DoserPreferences;

import uk.co.chrisjenx.calligraphy.CalligraphyConfig;

public class DoserApplication extends android.app.Application {

    private static Context mContext;
    private static DoserPreferences mDoserPreferences;

    @Override
    public void onCreate() {
        super.onCreate();

        mContext = this;
        mDoserPreferences = new DoserPreferences(this);

        CalligraphyConfig.initDefault(new CalligraphyConfig.Builder()
                        .setDefaultFontPath(getResources().getString(R.string.app_font))
                        .setFontAttrId(R.attr.fontPath)
                        .build()
        );
    }

    public static Context getContext() {
        return mContext;
    }

    public static DoserPreferences getDoserPreferences() {
        return mDoserPreferences;
    }

    public static int getThemeId() {
        return mDoserPreferences.getTheme().resValue;
    }

}