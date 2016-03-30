package com.nateshoffner.seachemdoser.core.model.products.gravel;

import com.nateshoffner.seachemdoser.DoserApplication;
import com.nateshoffner.seachemdoser.R;
import com.nateshoffner.seachemdoser.core.model.SeachemDosage;
import com.nateshoffner.seachemdoser.core.model.UnitMeasurement;

public class FlouriteRed extends Gravel {

    public FlouriteRed() {
        super(DoserApplication.getContext().getString(R.string.product_flourite_red));

        addComment(DoserApplication.getContext().getString(R.string.product_comment_flourite_red));
    }

    @Override
    public SeachemDosage[] calculateDosage(UnitMeasurement unitMeasurement) {
        return CalculateDosage(unitMeasurement, 425, 212);
    }
}
