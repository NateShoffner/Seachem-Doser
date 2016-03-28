package com.nateshoffner.seachemdoser.ui.fragment;

import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.design.widget.Snackbar;
import android.support.v7.preference.ListPreference;
import android.support.v7.preference.PreferenceFragmentCompat;

import com.nateshoffner.seachemdoser.BuildConfig;
import com.nateshoffner.seachemdoser.DoserApplication;
import com.nateshoffner.seachemdoser.R;
import com.nateshoffner.seachemdoser.core.manager.SeachemManager;
import com.nateshoffner.seachemdoser.core.model.SeachemProduct;
import com.nateshoffner.seachemdoser.ui.dialog.DoserChangelog;
import com.nateshoffner.seachemdoser.utils.DoserPreferences;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

public class PreferencesFragment extends PreferenceFragmentCompat
        implements SharedPreferences.OnSharedPreferenceChangeListener,
        android.support.v7.preference.Preference.OnPreferenceClickListener {

    SharedPreferences mSharedPreferences;

    @Override
    public void onCreatePreferences(Bundle bundle, String s) {
        addPreferencesFromResource(R.xml.preferences);

        mSharedPreferences = PreferenceManager.getDefaultSharedPreferences(getActivity());

        List<CharSequence> entries = new ArrayList<>();
        entries.add(getActivity().getString(R.string.most_recent));
        List<CharSequence> entryValues = new ArrayList<>();
        entryValues.add(getActivity().getString(R.string.most_recent));

        List<SeachemProduct> products = SeachemManager.GetProducts();
        Collections.sort(products, new Comparator<SeachemProduct>() {
            @Override
            public int compare(SeachemProduct p1, SeachemProduct p2) {
                return p1.getName().compareTo(p2.getName());
            }
        });

        for (SeachemProduct product : products) {
            entries.add(product.getName());
            entryValues.add(product.getName());
        }

        ListPreference lp = (ListPreference) findPreference(getString(R.string.pref_default_product));
        lp.setEntries(entries.toArray(new CharSequence[entries.size()]));
        lp.setEntryValues(entryValues.toArray(new CharSequence[entryValues.size()]));
        lp.setDefaultValue(getActivity().getString(R.string.most_recent));

        String revision = getString(R.string.build_revision);
        findPreference("app_version").setSummary(String.format("%s (rev. %s)",
                BuildConfig.VERSION_NAME, revision));
        findPreference(getString(R.string.pref_default_product)).setSummary(
                DoserApplication.getDoserPreferences().getDefaultProductString());
        findPreference("about_changelog").setOnPreferenceClickListener(this);
        findPreference("about_rate").setOnPreferenceClickListener(this);
        updatePreferenceSummary(getString(R.string.pref_unit_measurement), null);
        updatePreferenceSummary(getString(R.string.pref_theme), null);
        mSharedPreferences.registerOnSharedPreferenceChangeListener(this);
    }

    @Override
    public void onDetach() {
        mSharedPreferences.unregisterOnSharedPreferenceChangeListener(this);
        super.onDetach();
    }

    private void updatePreferenceSummary(String key, String defaultValue) {
        findPreference(key).setSummary(mSharedPreferences.getString(key, defaultValue));
    }

    @Override
    public void onSharedPreferenceChanged(SharedPreferences sharedPreferences, String key) {
        if (isMenuVisible())
            if (key.equals(getString(R.string.pref_unit_measurement))) {
                updatePreferenceSummary(getString(R.string.pref_unit_measurement), null);
            }

        if (key.equals(getString(R.string.pref_default_product))) {
            updatePreferenceSummary(getString(R.string.pref_default_product), null);
        }

        if (key.equals(getString(R.string.pref_theme))) {
            updatePreferenceSummary(getString(R.string.pref_theme), null);

            Snackbar.make(getView(), "This change will be made after the next app start", Snackbar.LENGTH_LONG).show();
        }
    }

    @Override
    public boolean onPreferenceClick(android.support.v7.preference.Preference preference) {
        if (preference.getKey().equals("about_changelog")) {
            DoserChangelog cl = new DoserChangelog(getActivity());
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
