package com.nateshoffner.seachemdoser.core.model.products.gravel;

import com.nateshoffner.seachemdoser.DoserApplication;
import com.nateshoffner.seachemdoser.R;
import com.nateshoffner.seachemdoser.core.model.SeachemDosage;
import com.nateshoffner.seachemdoser.core.model.UnitMeasurement;

public class GrayCoast extends Gravel {

    public GrayCoast() {
        super(DoserApplication.getContext().getString(R.string.product_gray_coast));

        addNote(DoserApplication.getContext().getString(R.string.product_comment_gray_coast));
    }

    @Override
    public SeachemDosage[] calculateDosage(UnitMeasurement unitMeasurement) {
        return CalculateDosage(unitMeasurement, 366, 130);
    }
}
