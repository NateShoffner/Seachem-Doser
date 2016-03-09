package com.nateshoffner.seachemdoser.core.model.products.gravel;

import com.nateshoffner.seachemdoser.DoserApplication;
import com.nateshoffner.seachemdoser.R;
import com.nateshoffner.seachemdoser.core.model.SeachemDosage;

public class PearlBeach extends GravelBase {

    private String name;

    public PearlBeach() {
        this.name = DoserApplication.getContext().getString(R.string.product_pearl_beach);
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
