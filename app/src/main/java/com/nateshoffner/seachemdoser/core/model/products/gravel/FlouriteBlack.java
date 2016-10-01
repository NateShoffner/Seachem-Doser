package com.nateshoffner.seachemdoser.core.model.products.gravel;

import com.nateshoffner.seachemdoser.DoserApplication;
import com.nateshoffner.seachemdoser.R;
import com.nateshoffner.seachemdoser.core.model.SeachemDosage;
import com.nateshoffner.seachemdoser.core.model.UnitMeasurement;

public class FlouriteBlack extends Gravel {

    public FlouriteBlack() {
        super(DoserApplication.getContext().getString(R.string.product_flourite_black));

        addNote(DoserApplication.getContext().getString(R.string.product_comment_flourite_black));
    }

    @Override
    public SeachemDosage[] calculateDosage(UnitMeasurement unitMeasurement) {
        return CalculateDosage(unitMeasurement, 325, 163);
    }
}
