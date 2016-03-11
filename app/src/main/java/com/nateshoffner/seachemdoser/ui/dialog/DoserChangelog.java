package com.nateshoffner.seachemdoser.ui.dialog;

import android.content.Context;
import android.view.ContextThemeWrapper;

import com.nateshoffner.seachemdoser.R;

import de.cketti.library.changelog.ChangeLog;

public class DoserChangelog extends ChangeLog {
    public static final String DARK_THEME_CSS =
            "body { color: #FFFFFF; background-color: #282828; }" + "\n" + DEFAULT_CSS;

    public DoserChangelog(Context context) {
        super(new ContextThemeWrapper(context, R.style.SeachemDark), DARK_THEME_CSS);
    }
}