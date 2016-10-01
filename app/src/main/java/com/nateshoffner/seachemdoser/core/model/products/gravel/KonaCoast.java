package com.nateshoffner.seachemdoser.core.model.products.gravel;

import com.nateshoffner.seachemdoser.DoserApplication;
import com.nateshoffner.seachemdoser.R;
import com.nateshoffner.seachemdoser.core.model.SeachemDosage;
import com.nateshoffner.seachemdoser.core.model.UnitMeasurement;

public class KonaCoast extends Gravel {

    public KonaCoast() {
        super(DoserApplication.getContext().getString(R.string.product_kona_coast));

        setDiscontinued(true);

        addNote(DoserApplication.getContext().getString(R.string.product_comment_kona_coast));
    }

    @Override
    public SeachemDosage[] calculateDosage(UnitMeasurement unitMeasurement) {
        return CalculateDosage(unitMeasurement, 366, 183);
    }
}