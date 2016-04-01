package com.nateshoffner.seachemdoser.ui.dialog;

import android.content.Context;
import android.support.v4.content.ContextCompat;
import android.view.ContextThemeWrapper;

import com.nateshoffner.seachemdoser.DoserApplication;
import com.nateshoffner.seachemdoser.R;
import com.nateshoffner.seachemdoser.utils.ThemeHelper;

import de.cketti.library.changelog.*;

public final class DoserChangelog {

    public static MaterialDialogChangeLog getInstance(Context context) {
        return MaterialDialogChangeLog.newInstance(
                new ContextThemeWrapper(context, DoserApplication.getDoserTheme().getResourceId()),
                getCss(context));
    }

    private static String getCss(Context context) {
        ThemeHelper.Theme theme = DoserApplication.getDoserTheme();

        int backgroundColor = 0;
        int textColor = 0;

        if (theme == ThemeHelper.Theme.Dark) {
            backgroundColor = ContextCompat.getColor(context, R.color.background_inverse);
            textColor = ContextCompat.getColor(context, R.color.text_color_inverse);
        } else if (theme == ThemeHelper.Theme.Light) {
            backgroundColor = ContextCompat.getColor(context, R.color.background);
            textColor = ContextCompat.getColor(context, R.color.text_color);
        }

        return String.format("body { color: #%s; background-color: #%s }\n%s",
                String.format("%06X", (0xFFFFFF & textColor)),
                String.format("%06X", (0xFFFFFF & backgroundColor)),
                MaterialDialogChangeLog.DEFAULT_CSS);
    }
}