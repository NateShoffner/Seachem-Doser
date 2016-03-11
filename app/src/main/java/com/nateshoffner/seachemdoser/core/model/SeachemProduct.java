package com.nateshoffner.seachemdoser.core.model;

import java.io.Serializable;

public interface SeachemProduct extends Serializable {
    String getName();

    SeachemParameter[] getParameters(UnitMeasurement unitMeasurement);

    String getComment();

    SeachemDosage[] calculateDosage(UnitMeasurement unitMeasurement);
}
