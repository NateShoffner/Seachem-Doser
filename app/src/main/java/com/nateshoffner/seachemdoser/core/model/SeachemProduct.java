package com.nateshoffner.seachemdoser.core.model;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Dictionary;
import java.util.Hashtable;
import java.util.List;

public abstract class SeachemProduct implements Serializable {

    private String mName;
    private List<String> mNotes = new ArrayList<>();
    private Dictionary<UnitMeasurement, SeachemParameter[]> mParameters = new Hashtable<>();
    private List<String> mWarnings = new ArrayList<>();
    private boolean mDiscontinued;

    public SeachemProduct(String name) {
        mName = name;
    }

    public String getName() {
        return mName;
    }

    public void setDiscontinued(boolean discontinued) {
        mDiscontinued = discontinued;
    }

    public boolean isDiscontinued() {
        return mDiscontinued;
    }

    public void addNote(String comment) {
        mNotes.add(comment);
    }

    public String[] getNotes() {
        return mNotes.toArray(new String[mNotes.size()]);
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
