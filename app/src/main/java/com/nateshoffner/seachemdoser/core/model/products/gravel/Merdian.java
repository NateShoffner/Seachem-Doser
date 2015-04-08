package com.nateshoffner.seachemdoser.core.model.products.gravel;

import com.nateshoffner.seachemdoser.core.model.SeachemDosage;
import com.nateshoffner.seachemdoser.core.model.SeachemProduct;

public class Merdian extends GravelBase implements SeachemProduct {

    private String name;

    public Merdian() {
        this.name = "Merdian";
    }

    @Override
    public String getName() {
        return this.name;
    }

    @Override
    public SeachemDosage[] calculateDosage() {
        return CalculateDosage(320);
    }
}
