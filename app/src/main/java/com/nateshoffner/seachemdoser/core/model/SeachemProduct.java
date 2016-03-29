package com.nateshoffner.seachemdoser.core.model;

import java.io.Serializable;
import java.util.Dictionary;
import java.util.Hashtable;

public abstract class SeachemProduct implements Serializable {

    private String mName;
    private String mComment;
    private Dictionary<UnitMeasurement, SeachemParameter[]> mParameters = new Hashtable<>();

    public SeachemProduct(String name, String comment) {
        mName = name;
        mComment = comment;
    }

    public String getName() {
        return mName;
    }

    public String getComment() {
        return mComment;
    }

    public void setParameters(UnitMeasurement unitMeasurements, SeachemParameter[] params) {
        mParameters.put(unitMeasurements, params);
    }

    public Dictionary<UnitMeasurement, SeachemParameter[]> getParameters() {
        return mParameters;
    }

    public abstract SeachemDosage[] calculateDosage(UnitMeasurement unitMeasurement);
}
