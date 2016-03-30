package com.nateshoffner.seachemdoser.utils;

public class StringUtils {

    public static String join(String[] items, String delimeter) {
        StringBuilder builder = new StringBuilder();

        for (int i = 0, itemsSize = items.length; i < itemsSize; i++) {
            String str = items[i];

            builder.append(str);

            if (i < itemsSize - 1)
                builder.append(delimeter);
        }

        return builder.toString();
    }
}
