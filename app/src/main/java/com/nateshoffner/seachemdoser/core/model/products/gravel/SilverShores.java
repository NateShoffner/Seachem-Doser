package com.nateshoffner.seachemdoser.core.model.products.gravel;

import com.nateshoffner.seachemdoser.DoserApplication;
import com.nateshoffner.seachemdoser.R;
import com.nateshoffner.seachemdoser.core.model.SeachemDosage;
import com.nateshoffner.seachemdoser.core.model.UnitMeasurement;

public class SilverShores extends GravelBase {

    private String mName;

    public SilverShores() {
        mName = DoserApplication.getContext().getString(R.string.product_silver_shores);
    }

    @Override
    public String getName() {
        return mName;
    }

    @Override
    public SeachemDosage[] calculateDosage(UnitMeasurement unitMeasurement) {
        return CalculateDosage(unitMeasurement, 458);
    }
}
