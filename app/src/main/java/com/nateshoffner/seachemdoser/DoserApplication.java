package com.nateshoffner.seachemdoser;

import android.content.Context;

import uk.co.chrisjenx.calligraphy.CalligraphyConfig;

public class DoserApplication extends android.app.Application {

    private static Context mContext;

    @Override
    public void onCreate() {
        super.onCreate();

        mContext = this;

        CalligraphyConfig.initDefault(new CalligraphyConfig.Builder()
                        .setDefaultFontPath(getResources().getString(R.string.app_font))
                        .setFontAttrId(R.attr.fontPath)
                        .build()
        );
    }

    public static Context getContext() {
        return mContext;
    }
}