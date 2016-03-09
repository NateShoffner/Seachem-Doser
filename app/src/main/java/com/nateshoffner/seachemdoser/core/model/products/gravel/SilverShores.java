package com.nateshoffner.seachemdoser.core.model.products.gravel;

import com.nateshoffner.seachemdoser.DoserApplication;
import com.nateshoffner.seachemdoser.R;
import com.nateshoffner.seachemdoser.core.model.SeachemDosage;

public class SilverShores extends GravelBase {

    private String name;

    public SilverShores() {
        this.name = DoserApplication.getContext().getString(R.string.product_silver_shores);
    }

    @Override
    public String getName() {
        return this.name;
    }

    @Override
    public SeachemDosage[] calculateDosage() {
        return CalculateDosage(458);
    }
}
