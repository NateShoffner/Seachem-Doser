package com.nateshoffner.seachemdoser.utils;

import com.nateshoffner.seachemdoser.core.model.UnitMeasurement;

import java.util.Locale;

public class UnitLocale {

    public static UnitMeasurement getLocaleMeasurmentUnit() {
        String countryCode = Locale.getDefault().getCountry();
        if ("US".equals(countryCode)) return UnitMeasurement.ImperialUS; // USA
        // todo pure imperial implementation for liberia/burma (will likely never be used)
        if ("LR".equals(countryCode)) return null; // liberia
        if ("MM".equals(countryCode)) return null; // burma
        return UnitMeasurement.Metric;
    }
}