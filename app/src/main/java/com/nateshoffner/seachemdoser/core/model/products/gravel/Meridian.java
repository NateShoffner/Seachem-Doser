package com.nateshoffner.seachemdoser.core.model.products.gravel;

import com.nateshoffner.seachemdoser.DoserApplication;
import com.nateshoffner.seachemdoser.R;
import com.nateshoffner.seachemdoser.core.model.SeachemDosage;
import com.nateshoffner.seachemdoser.core.model.UnitMeasurement;

public class Meridian extends GravelBase {

    private String mName;

    public Meridian() {
        mName = DoserApplication.getContext().getString(R.string.product_meridian);
    }

    @Override
    public String getName() {
        return mName;
    }

    @Override
    public SeachemDosage[] calculateDosage(UnitMeasurement unitMeasurement) {
        return CalculateDosage(unitMeasurement, 320);
    }
}
