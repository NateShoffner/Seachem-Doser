package com.nateshoffner.seachemdoser.core.model;

public enum UnitMeasurement {
    ImperialUS, Metric;

    public static UnitMeasurement fromString(String str) {
        if (str.equalsIgnoreCase("Imperial (US)"))
            return ImperialUS;
        if (str.equalsIgnoreCase("Metric"))
            return Metric;
        return null;
    }
}
