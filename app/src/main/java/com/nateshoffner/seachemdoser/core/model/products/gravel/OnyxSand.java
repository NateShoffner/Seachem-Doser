package com.nateshoffner.seachemdoser.core.model.products.gravel;

import com.nateshoffner.seachemdoser.DoserApplication;
import com.nateshoffner.seachemdoser.R;
import com.nateshoffner.seachemdoser.core.model.SeachemDosage;
import com.nateshoffner.seachemdoser.core.model.UnitMeasurement;

public class OnyxSand extends GravelBase {

    private String mName;

    public OnyxSand() {
        mName = DoserApplication.getContext().getString(R.string.product_onyx_sand);
    }

    @Override
    public String getName() {
        return mName;
    }

    @Override
    public SeachemDosage[] calculateDosage(UnitMeasurement unitMeasurement) {
        return CalculateDosage(unitMeasurement, 260);
    }
}
