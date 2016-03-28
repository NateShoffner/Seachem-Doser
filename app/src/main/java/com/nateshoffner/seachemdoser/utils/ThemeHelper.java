package com.nateshoffner.seachemdoser.utils;


import com.nateshoffner.seachemdoser.R;

public class ThemeHelper {

    public enum Theme {
        Dark("Dark", 0, R.style.AppTheme_Dark),
        Light("Light", 1, R.style.AppTheme);

        private String mName;
        private int mId;
        private int mResId;


        Theme(String name, int id, int resId) {
            mName = name;
            mId = id;
            mResId = resId;
        }

        public String getName() {
            return mName;
        }

        public int getId() {
            return mId;
        }

        public int getResourceId() {
            return mResId;
        }

    }

    public static Theme fromId(int id) {
        switch(id) {
            case 0:
                return Theme.Dark;
            case 1:
                return Theme.Light;
        }

        return null;
    }

}
