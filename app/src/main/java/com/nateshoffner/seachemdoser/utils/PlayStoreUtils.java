package com.nateshoffner.seachemdoser.utils;

import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;

public class PlayStoreUtils {

    public static void GoToPlayStore(Context context) {
        Uri uri = Uri.parse("market://details?id=" + context.getPackageName());
        Intent goToMarket = new Intent(Intent.ACTION_VIEW, uri);
        // To count with Play market backstack, After pressing back button,
        // to taken back to our application, we need to add following flags to intent.
        int flags = Intent.FLAG_ACTIVITY_NO_HISTORY | Intent.FLAG_ACTIVITY_MULTIPLE_TASK;
        flags |= Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP ?
                Intent.FLAG_ACTIVITY_NEW_DOCUMENT :
                Intent.FLAG_ACTIVITY_CLEAR_WHEN_TASK_RESET;

        goToMarket.addFlags(flags);
        try {
            context.startActivity(goToMarket);
        } catch (ActivityNotFoundException e) {
            context.startActivity(new Intent(Intent.ACTION_VIEW,
                    Uri.parse("http://play.google.com/store/apps/details?id=" +
                            context.getPackageName())));
        }
    }
}
