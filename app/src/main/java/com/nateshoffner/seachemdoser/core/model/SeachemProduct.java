package com.nateshoffner.seachemdoser.core.model;

import java.io.Serializable;

public interface SeachemProduct extends Serializable {
    String getName();

    SeachemParameter[] getParameters();

    String getComment();

    SeachemDosage[] calculateDosage();
}
