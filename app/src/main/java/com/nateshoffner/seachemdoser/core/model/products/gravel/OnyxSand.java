package com.nateshoffner.seachemdoser.core.model.products.gravel;

import com.nateshoffner.seachemdoser.DoserApplication;
import com.nateshoffner.seachemdoser.R;
import com.nateshoffner.seachemdoser.core.model.SeachemDosage;
import com.nateshoffner.seachemdoser.core.model.UnitMeasurement;

public class OnyxSand extends Gravel {

    public OnyxSand() {
        super(DoserApplication.getContext().getString(R.string.product_onyx_sand));

        addComment(DoserApplication.getContext().getString(R.string.product_comment_onyx_sand));
    }

    @Override
    public SeachemDosage[] calculateDosage(UnitMeasurement unitMeasurement) {
        return CalculateDosage(unitMeasurement, 260, 130);
    }
}
