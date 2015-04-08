package com.nateshoffner.seachemdoser;

import uk.co.chrisjenx.calligraphy.CalligraphyConfig;

public class DoserApplication extends android.app.Application {
    @Override
    public void onCreate() {
        super.onCreate();
        CalligraphyConfig.initDefault(new CalligraphyConfig.Builder()
                        .setDefaultFontPath(getResources().getString(R.string.app_font))
                        .setFontAttrId(R.attr.fontPath)
                        .build()
        );
    }
}