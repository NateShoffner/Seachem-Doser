package com.nateshoffner.seachemdoser.ui.dialog;

import android.content.Context;
import android.view.ContextThemeWrapper;

import com.nateshoffner.seachemdoser.DoserApplication;
import com.nateshoffner.seachemdoser.R;
import com.nateshoffner.seachemdoser.utils.DoserPreferences;

public class DoserChangelog extends ChangeLog {
    public static final String DARK_THEME_CSS =
            "body { color: #FFFFFF; background-color: #282828; }" + "\n" + DEFAULT_CSS;

    public DoserChangelog(Context context) {
        super(new ContextThemeWrapper(context, DoserApplication.getDoserTheme().getResourceId()),
                DARK_THEME_CSS);
    }

    public void updateVersionInPreferences() {
        super.updateVersionInPreferences();
    }
}