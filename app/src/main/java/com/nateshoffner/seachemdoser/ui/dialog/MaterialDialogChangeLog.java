package com.nateshoffner.seachemdoser.ui.dialog;


import android.content.Context;
import android.content.DialogInterface;
import android.support.annotation.NonNull;
import android.webkit.WebView;

import com.afollestad.materialdialogs.DialogAction;
import com.afollestad.materialdialogs.MaterialDialog;
import com.nateshoffner.seachemdoser.R;

import java.util.List;

import de.cketti.library.changelog.ChangeLog;
import de.cketti.library.changelog.ReleaseItem;


public final class MaterialDialogChangeLog {
    /**
     * Default CSS styles used to format the change log.
     */
    public static final String DEFAULT_CSS = "" +
            "h1 { margin-left: 0px; font-size: 1.2em; }" + "\n" +
            "li { margin-left: 0px; }" + "\n" +
            "ul { padding-left: 2em; }";


    private final Context context;
    private final ChangeLog changeLog;
    private final String css;


    public static MaterialDialogChangeLog newInstance(Context context) {
        return MaterialDialogChangeLog.newInstance(context, DEFAULT_CSS);
    }

    public static MaterialDialogChangeLog newInstance(Context context, String css) {
        ChangeLog changeLog = ChangeLog.newInstance(context);
        return new MaterialDialogChangeLog(context, changeLog, css);
    }

    private MaterialDialogChangeLog(Context context, ChangeLog changeLog, String css) {
        this.context = context;
        this.changeLog = changeLog;
        this.css = css;
    }

    /**
     * Get the "What's New" dialog.
     *
     * @return An AlertDialog displaying the changes since the previous installed version of your
     * app (What's New). But when this is the first run of your app including
     * {@code ChangeLog} then the full log dialog is show.
     */
    public MaterialDialog getLogDialog() {
        return getDialog(changeLog.isFirstRunEver());
    }

    /**
     * Get a dialog with the full change log.
     *
     * @return An AlertDialog with a full change log displayed.
     */
    public MaterialDialog getFullLogDialog() {
        return getDialog(true);
    }

    public boolean isFirstRun() {
        return changeLog.isFirstRun();
    }

    public boolean isFirstRunEver() {
        return changeLog.isFirstRunEver();
    }

    private MaterialDialog getDialog(boolean full) {
        WebView wv = new WebView(context);
        //wv.setBackgroundColor(0); // transparent
        wv.loadDataWithBaseURL(null, getLog(full), "text/html", "UTF-8", null);

        MaterialDialog.Builder builder = new MaterialDialog.Builder(context);
        builder.title(
                context.getResources().getString(
                        full ? R.string.changelog_full_title : R.string.changelog_title))
                .customView(wv, false)
                .cancelable(false)
                .positiveText(R.string.changelog_ok_button)
                .dismissListener(new DialogInterface.OnDismissListener() {
                    @Override
                    public void onDismiss(DialogInterface dialogInterface) {
                        // The user clicked "OK" so save the current version code as
                        // "last version code".
                        changeLog.writeCurrentVersion();
                    }
                });

        if (!full) {
            // Show "More…" button if we're only displaying a partial change log.
            builder.negativeText(R.string.changelog_show_full)
                    .onNegative(new MaterialDialog.SingleButtonCallback() {
                        @Override
                        public void onClick(@NonNull MaterialDialog dialog, @NonNull DialogAction which) {
                            getFullLogDialog().show();
                        }
                    });
        }

        return builder.build();
    }

    private String getLog(boolean full) {
        StringBuilder sb = new StringBuilder();

        sb.append("<html><head><style type=\"text/css\">");
        sb.append(css);
        sb.append("</style></head><body>");

        String versionFormat = context.getResources().getString(R.string.changelog_version_format);

        List<ReleaseItem> changelog = changeLog.getChangeLog(full);

        for (ReleaseItem release : changelog) {
            sb.append("<h1>");
            sb.append(String.format(versionFormat, release.versionName));
            sb.append("</h1><ul>");
            for (String change : release.changes) {
                sb.append("<li>");
                sb.append(change);
                sb.append("</li>");
            }
            sb.append("</ul>");
        }

        sb.append("</body></html>");

        return sb.toString();
    }
}
