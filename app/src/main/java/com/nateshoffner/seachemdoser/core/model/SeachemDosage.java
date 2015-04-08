package com.nateshoffner.seachemdoser.core.model;

import java.io.Serializable;

public class SeachemDosage implements Serializable {

    private String unit;
    private double value;

    public SeachemDosage(String unit, double value) {
        this.unit = unit;
        this.value = value;
    }

    public String getUnit() {
        return this.unit;
    }

    public double getAmount() {
        return this.value;
    }
}

