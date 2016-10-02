package com.nateshoffner.seachemdoser.utils;


import android.content.Context;

public class ClipboardUtils {

    public static boolean copyToClipboard(Context context, String text) {
        try {
            android.text.ClipboardManager clipboard = (android.text.ClipboardManager) context
                    .getSystemService(Context.CLIPBOARD_SERVICE);
            clipboard.setText(text);
            return true;
        } catch (Exception e) {
            return false;
        }
    }
}
