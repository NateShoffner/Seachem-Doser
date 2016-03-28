package com.nateshoffner.seachemdoser.ui.fragment;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.design.widget.Snackbar;
import android.support.v7.preference.ListPreference;
import android.support.v7.preference.PreferenceFragmentCompat;

import com.nateshoffner.seachemdoser.DoserApplication;
import com.nateshoffner.seachemdoser.R;
import com.nateshoffner.seachemdoser.core.manager.SeachemManager;
import com.nateshoffner.seachemdoser.core.model.SeachemProduct;
import com.nateshoffner.seachemdoser.core.model.UnitMeasurement;
import com.nateshoffner.seachemdoser.utils.ThemeHelper;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

public class PreferencesFragment extends PreferenceFragmentCompat
        implements SharedPreferences.OnSharedPreferenceChangeListener {

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

        displayUnitMeasurement();
        displayTheme();

        findPreference(getString(R.string.pref_default_product)).setSummary(
                DoserApplication.getDoserPreferences().getDefaultProductString());
        mSharedPreferences.registerOnSharedPreferenceChangeListener(this);
    }

    private void displayTheme() {
        ThemeHelper.Theme theme = DoserApplication.getDoserTheme();
        ListPreference listPreference = (ListPreference) findPreference(getString(R.string.pref_theme));
        listPreference.setSummary(theme.getName());
        listPreference.setValueIndex(theme.getId());
    }

    private void displayUnitMeasurement() {
        UnitMeasurement unit = DoserApplication.getDoserPreferences().getUnitMeasurement();
        ListPreference listPreference = (ListPreference) findPreference(
                getString(R.string.pref_unit_measurement));
        listPreference.setSummary(unit.getName());
        listPreference.setValueIndex(unit.getId());
    }

    @Override
    public void onDetach() {
        mSharedPreferences.unregisterOnSharedPreferenceChangeListener(this);
        super.onDetach();
    }

    private void updatePreferenceSummary(String key, String summary, String defaultValue) {
        if (summary == null) // get summary value based on key
            summary = mSharedPreferences.getString(key, defaultValue);

        findPreference(key).setSummary(summary);
    }

    @Override
    public void onSharedPreferenceChanged(SharedPreferences sharedPreferences, String key) {
        if (isMenuVisible())
            if (key.equals(getString(R.string.pref_unit_measurement))) {
                displayUnitMeasurement();
            }

        if (key.equals(getString(R.string.pref_default_product))) {
            updatePreferenceSummary(getString(R.string.pref_default_product), null, null);
        }

        if (key.equals(getString(R.string.pref_theme))) {
            displayTheme();
            Snackbar.make(getView(), "This change will be made after the next app start",
                    Snackbar.LENGTH_LONG).show();
        }
    }
}
