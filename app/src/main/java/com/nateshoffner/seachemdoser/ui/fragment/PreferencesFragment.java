package com.nateshoffner.seachemdoser.ui.fragment;

import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v7.preference.PreferenceFragmentCompat;

import com.nateshoffner.seachemdoser.BuildConfig;
import com.nateshoffner.seachemdoser.R;
import com.nateshoffner.seachemdoser.ui.dialog.DoserChangelog;

import de.cketti.library.changelog.dialog.DialogChangeLog;

public class PreferencesFragment extends PreferenceFragmentCompat
        implements SharedPreferences.OnSharedPreferenceChangeListener,
        android.support.v7.preference.Preference.OnPreferenceClickListener {

    SharedPreferences mSharedPreferences;

    @Override
    public void onCreatePreferences(Bundle bundle, String s) {
        addPreferencesFromResource(R.xml.preferences);

        mSharedPreferences = PreferenceManager.getDefaultSharedPreferences(getActivity());

        findPreference("app_version").setSummary("v" + BuildConfig.VERSION_NAME);
        findPreference("about_changelog").setOnPreferenceClickListener(this);
        findPreference("about_rate").setOnPreferenceClickListener(this);
        updatePreferenceSummary(getString(R.string.pref_unit_measurement), null);
        mSharedPreferences.registerOnSharedPreferenceChangeListener(this);
    }

    private void updatePreferenceSummary(String key, String defaultValue) {
        findPreference(key).setSummary(mSharedPreferences.getString(key, defaultValue));
    }

    @Override
    public void onSharedPreferenceChanged(SharedPreferences sharedPreferences, String key) {
        if (key.equals(getString(R.string.pref_unit_measurement))) {
            updatePreferenceSummary(getString(R.string.pref_unit_measurement), null);
        }
    }

    @Override
    public boolean onPreferenceClick(android.support.v7.preference.Preference preference) {
        if (preference.getKey().equals("about_changelog")) {
            DialogChangeLog cl = DoserChangelog.newInstance(getActivity());
            cl.getFullLogDialog().show();
            return true;
        }

        if (preference.getKey().equals("about_rate")) {
            Uri uri = Uri.parse("market://details?id=" + getActivity().getPackageName());
            Intent goToMarket = new Intent(Intent.ACTION_VIEW, uri);
            // To count with Play market backstack, After pressing back button,
            // to taken back to our application, we need to add following flags to intent.
            int flags = Intent.FLAG_ACTIVITY_NO_HISTORY | Intent.FLAG_ACTIVITY_MULTIPLE_TASK;
            flags |= Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP ?
                    Intent.FLAG_ACTIVITY_NEW_DOCUMENT :
                    Intent.FLAG_ACTIVITY_CLEAR_WHEN_TASK_RESET;

            goToMarket.addFlags(flags);
            try {
                startActivity(goToMarket);
            } catch (ActivityNotFoundException e) {
                startActivity(new Intent(Intent.ACTION_VIEW,
                        Uri.parse("http://play.google.com/store/apps/details?id=" +
                                getActivity().getPackageName())));
            }
        }

        return false;
    }
}
