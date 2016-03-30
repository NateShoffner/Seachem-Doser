package com.nateshoffner.seachemdoser.core.model.products.gravel;

import com.nateshoffner.seachemdoser.DoserApplication;
import com.nateshoffner.seachemdoser.R;
import com.nateshoffner.seachemdoser.core.model.SeachemDosage;
import com.nateshoffner.seachemdoser.core.model.UnitMeasurement;

public class SilverShores extends Gravel {

    public SilverShores() {
        super(DoserApplication.getContext().getString(R.string.product_silver_shores));

        setDiscontinued(true);

        addComment(DoserApplication.getContext().getString(R.string.product_comment_silver_shores));
    }

    @Override
    public SeachemDosage[] calculateDosage(UnitMeasurement unitMeasurement) {
        return CalculateDosage(unitMeasurement, 458, 229);
    }
}