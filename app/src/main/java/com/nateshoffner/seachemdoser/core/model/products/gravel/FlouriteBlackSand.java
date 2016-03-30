package com.nateshoffner.seachemdoser.core.model.products.gravel;

import com.nateshoffner.seachemdoser.DoserApplication;
import com.nateshoffner.seachemdoser.R;
import com.nateshoffner.seachemdoser.core.model.SeachemDosage;
import com.nateshoffner.seachemdoser.core.model.UnitMeasurement;

public class FlouriteBlackSand extends Gravel {

    public FlouriteBlackSand() {
        super(DoserApplication.getContext().getString(R.string.product_flourite_black_sand));

        addComment(DoserApplication.getContext().getString(R.string.product_comment_flourite_black_sand));
    }

    @Override
    public SeachemDosage[] calculateDosage(UnitMeasurement unitMeasurement) {
        return CalculateDosage(unitMeasurement, 325, 163);
    }
}
