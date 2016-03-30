package com.nateshoffner.seachemdoser.core.model;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Dictionary;
import java.util.Hashtable;
import java.util.List;

public abstract class SeachemProduct implements Serializable {

    private String mName;
    private List<String> mComments = new ArrayList<>();
    private Dictionary<UnitMeasurement, SeachemParameter[]> mParameters = new Hashtable<>();
    private List<String> mWarnings = new ArrayList<>();

    public SeachemProduct(String name) {
        mName = name;
    }

    public String getName() {
        return mName;
    }

    public void addComment(String comment) {
        mComments.add(comment);
    }

    public String[] getComments() {
        return mComments.toArray(new String[mComments.size()]);
    }

    public void addWarning(String warning) {
        mWarnings.add(warning);
    }

    public String[] getWarnings() {
        return mWarnings.toArray(new String[mWarnings.size()]);
    }

    public void setParameters(UnitMeasurement unitMeasurements, SeachemParameter[] params) {
        mParameters.put(unitMeasurements, params);
    }

    public Dictionary<UnitMeasurement, SeachemParameter[]> getParameters() {
        return mParameters;
    }

    public abstract SeachemDosage[] calculateDosage(UnitMeasurement unitMeasurement);
}
