package com.nateshoffner.seachemdoser.ui.fragment;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.ListPreference;
import android.preference.PreferenceFragment;
import android.preference.PreferenceManager;

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

public class PreferencesFragment extends PreferenceFragment
        implements SharedPreferences.OnSharedPreferenceChangeListener {

    SharedPreferences mSharedPreferences;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        addPreferencesFromResource(R.xml.preferences);

        mSharedPreferences = PreferenceManager.getDefaultSharedPreferences(getActivity());

        List<CharSequence> entries = new ArrayList<>();
        entries.add(getActivity().getString(R.string.none));
        entries.add(getActivity().getString(R.string.most_recent));
        List<CharSequence> entryValues = new ArrayList<>();
        entryValues.add(getActivity().getString(R.string.none));
        entryValues.add(getActivity().getString(R.string.most_recent));

        List<SeachemProduct> products = SeachemManager.GetProducts();
        Collections.sort(products, new Comparator<SeachemProduct>() {
            @Override
            public int compare(SeachemProduct p1, SeachemProduct p2) {
                return p1.getName().compareTo(p2.getName());
            }
        });

        for (SeachemProduct product : products) {
            if (!DoserApplication.getDoserPreferences().getShowDiscontinuedProducts()
                    && product.isDiscontinued())
                continue;

            entries.add(product.getName());
            entryValues.add(product.getName());
        }

        ListPreference lp = (ListPreference) findPreference(getString(R.string.pref_default_product));
        lp.setEntries(entries.toArray(new CharSequence[entries.size()]));
        lp.setEntryValues(entryValues.toArray(new CharSequence[entryValues.size()]));
        lp.setDefaultValue(getActivity().getString(R.string.none));

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

        if (key.equals(getString(R.string.pref_unit_measurement))) {
            displayUnitMeasurement();
        }

        if (key.equals(getString(R.string.pref_default_product))) {
            updatePreferenceSummary(getString(R.string.pref_default_product), null, null);
        }

        if (key.equals(getString(R.string.pref_theme))) {
            displayTheme();
        }
    }
}
