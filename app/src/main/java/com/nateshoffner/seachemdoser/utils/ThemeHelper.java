package com.nateshoffner.seachemdoser.utils;


import com.nateshoffner.seachemdoser.R;

public class ThemeHelper {

    public enum Theme {
        Dark("Dark", R.style.AppTheme_Dark),
        Light("Light", R.style.AppTheme);

        public String name;
        public int resValue;

        Theme(String name, int resValue) {
            this.name = name;
            this.resValue = resValue;
        }
    }

}
