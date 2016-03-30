package com.nateshoffner.seachemdoser.core.model.products.gravel;

import com.nateshoffner.seachemdoser.DoserApplication;
import com.nateshoffner.seachemdoser.R;
import com.nateshoffner.seachemdoser.core.model.SeachemDosage;
import com.nateshoffner.seachemdoser.core.model.UnitMeasurement;

public class Meridian extends Gravel {

    public Meridian() {
        super(DoserApplication.getContext().getString(R.string.product_meridian));

        addComment(DoserApplication.getContext().getString(R.string.product_comment_meridian));
    }

    @Override
    public SeachemDosage[] calculateDosage(UnitMeasurement unitMeasurement) {
        return CalculateDosage(unitMeasurement, 320, 125);
    }
}
