package com.nateshoffner.seachemdoser.core.model;

import java.io.Serializable;

public class SeachemParameter implements Serializable {

    private String mName;
    private String mUnit;
    private double mValue;
    private double mRecommendedValue;

    public SeachemParameter(String name, String unit) {
        mName = name;
        mUnit = unit;
    }

    public SeachemParameter(String name, String unit, double recommendedValue) {
        mName = name;
        mUnit = unit;
        mRecommendedValue = recommendedValue;
    }

    public String getName() {
        return mName;
    }

    public String getUnit() {
        return mUnit;
    }

    public double getRecommendedValue() {
        return mRecommendedValue;
    }

    public double getValue() {
        return mValue;
    }

    public void setValue(double value) {
        mValue = value;
    }
}