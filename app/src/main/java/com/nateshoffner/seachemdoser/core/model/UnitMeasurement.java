package com.nateshoffner.seachemdoser.core.model;

public enum UnitMeasurement {
    Metric("Metric", 0),
    ImperialUS("Imperial (US)", 1);

    private String mName;
    private int mId;

    UnitMeasurement(String name, int id) {
        mName = name;
        mId = id;
    }

    public String getName() {
        return mName;
    }

    public int getId() {
        return mId;
    }

    public static UnitMeasurement fromId(int id) {
        switch (id) {
            case 0:
                return UnitMeasurement.Metric;
            case 1:
                return UnitMeasurement.ImperialUS;
        }

        return null;
    }
}