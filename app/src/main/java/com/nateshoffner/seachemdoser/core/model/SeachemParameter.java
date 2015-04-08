package com.nateshoffner.seachemdoser.core.model;

import java.io.Serializable;

public class SeachemParameter implements Serializable {

    private String name;
    private String unit;
    private double value;

    public SeachemParameter(String name, String unit) {
        this.name = name;
        this.unit = unit;
    }

    public String getName() {
        return this.name;
    }

    public String getUnit() {
        return this.unit;
    }

    public double getValue() {
        return this.value;
    }

    public void setValue(double value) {
        this.value = value;
    }
}