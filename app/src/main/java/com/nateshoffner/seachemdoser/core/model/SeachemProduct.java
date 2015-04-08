package com.nateshoffner.seachemdoser.core.model;

import java.io.Serializable;

public interface SeachemProduct extends Serializable {
    public String getName();

    public SeachemParameter[] getParameters();

    public String getComment();

    public SeachemDosage[] calculateDosage();
}
