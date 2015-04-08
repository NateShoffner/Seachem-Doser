package com.nateshoffner.seachemdoser.core.model.products.gravel;

import com.nateshoffner.seachemdoser.core.model.SeachemDosage;
import com.nateshoffner.seachemdoser.core.model.SeachemProduct;

public class PearlBeach extends GravelBase implements SeachemProduct {

    private String name;

    public PearlBeach() {
        this.name = "Pearl Beach";
    }

    @Override
    public String getName() {
        return this.name;
    }

    @Override
    public SeachemDosage[] calculateDosage() {
        return CalculateDosage(525);
    }
}
