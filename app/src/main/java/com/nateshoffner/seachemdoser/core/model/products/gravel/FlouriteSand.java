package com.nateshoffner.seachemdoser.core.model.products.gravel;

import com.nateshoffner.seachemdoser.DoserApplication;
import com.nateshoffner.seachemdoser.R;
import com.nateshoffner.seachemdoser.core.model.SeachemDosage;
import com.nateshoffner.seachemdoser.core.model.UnitMeasurement;

public class FlouriteSand extends Gravel {

    public FlouriteSand() {
        super(DoserApplication.getContext().getString(R.string.product_flourite_sand));

        addNote(DoserApplication.getContext().getString(R.string.product_flourite_sand));
    }

    @Override
    public SeachemDosage[] calculateDosage(UnitMeasurement unitMeasurement) {
        return CalculateDosage(unitMeasurement, 260, 130);
    }
}
