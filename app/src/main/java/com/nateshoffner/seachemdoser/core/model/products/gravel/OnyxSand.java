package com.nateshoffner.seachemdoser.core.model.products.gravel;

import com.nateshoffner.seachemdoser.core.model.SeachemDosage;
import com.nateshoffner.seachemdoser.core.model.SeachemProduct;

public class OnyxSand extends GravelBase implements SeachemProduct {

    private String name;

    public OnyxSand() {
        this.name = "Onyx Sand";
    }

    @Override
    public String getName() {
        return this.name;
    }

    @Override
    public SeachemDosage[] calculateDosage() {
        return CalculateDosage(260);
    }
}
