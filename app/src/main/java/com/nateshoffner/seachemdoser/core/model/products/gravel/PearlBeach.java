package com.nateshoffner.seachemdoser.core.model.products.gravel;

import com.nateshoffner.seachemdoser.DoserApplication;
import com.nateshoffner.seachemdoser.R;
import com.nateshoffner.seachemdoser.core.model.SeachemDosage;
import com.nateshoffner.seachemdoser.core.model.UnitMeasurement;

public class PearlBeach extends Gravel {

    public PearlBeach() {
        super(DoserApplication.getContext().getString(R.string.product_pearl_beach));

        addNote(DoserApplication.getContext().getString(R.string.product_comment_pearl_beach));
    }

    @Override
    public SeachemDosage[] calculateDosage(UnitMeasurement unitMeasurement) {
        return CalculateDosage(unitMeasurement, 525, 185);
    }
}
