package com.nateshoffner.seachemdoser.core.model.products.gravel;

import com.nateshoffner.seachemdoser.DoserApplication;
import com.nateshoffner.seachemdoser.R;
import com.nateshoffner.seachemdoser.core.model.SeachemDosage;

public class GrayCoast extends GravelBase {

    private String name;

    public GrayCoast() {
        this.name = DoserApplication.getContext().getString(R.string.product_gray_coast);
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
