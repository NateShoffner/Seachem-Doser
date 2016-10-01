package com.nateshoffner.seachemdoser.core.model.products.gravel;

import com.nateshoffner.seachemdoser.DoserApplication;
import com.nateshoffner.seachemdoser.R;
import com.nateshoffner.seachemdoser.core.model.SeachemDosage;
import com.nateshoffner.seachemdoser.core.model.UnitMeasurement;

public class Onyx extends Gravel {

    public Onyx() {
        super(DoserApplication.getContext().getString(R.string.product_onyx));

        addNote(DoserApplication.getContext().getString(R.string.product_comment_onyx));
    }

    @Override
    public SeachemDosage[] calculateDosage(UnitMeasurement unitMeasurement) {
        return CalculateDosage(unitMeasurement, 425, 212);
    }
}
