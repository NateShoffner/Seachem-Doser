package com.nateshoffner.seachemdoser.core.model.products.gravel;

import com.nateshoffner.seachemdoser.core.model.SeachemDosage;
import com.nateshoffner.seachemdoser.core.model.SeachemProduct;

public class KonaCoast extends GravelBase implements SeachemProduct {

    private String name;

    public KonaCoast() {
        this.name = "Kona Coast";
    }

    @Override
    public String getName() {
        return this.name;
    }

    @Override
    public SeachemDosage[] calculateDosage() {
        return CalculateDosage(366);
    }
}
