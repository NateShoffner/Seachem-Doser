package com.nateshoffner.seachemdoser.core.model;

import java.io.Serializable;

public class SeachemDosage implements Serializable {

    private String mUnit;
    private double mValue;

    public SeachemDosage(String unit, double value) {
        mUnit = unit;
        mValue = value;
    }

    public String getUnit() {
        return mUnit;
    }

    public double getAmount() {
        return mValue;
    }
}

