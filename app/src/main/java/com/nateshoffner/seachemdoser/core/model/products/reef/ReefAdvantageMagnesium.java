package com.nateshoffner.seachemdoser.core.model.products.reef;

import com.nateshoffner.seachemdoser.DoserApplication;
import com.nateshoffner.seachemdoser.R;
import com.nateshoffner.seachemdoser.core.model.SeachemDosage;
import com.nateshoffner.seachemdoser.core.model.SeachemParameter;
import com.nateshoffner.seachemdoser.core.model.SeachemProduct;
import com.nateshoffner.seachemdoser.core.model.UnitMeasurement;
import com.nateshoffner.seachemdoser.utils.UnitConversion;

public class ReefAdvantageMagnesium extends SeachemProduct {

    public ReefAdvantageMagnesium() {
        super(DoserApplication.getContext().getString(R.string.product_reef_advantage_magnesium));

        addNote(DoserApplication.getContext().getString(R.string.product_comment_reef_advantage_magnesium));

        setParameters(UnitMeasurement.ImperialUS, new SeachemParameter[]{
                new SeachemParameter(DoserApplication.getContext().getString(R.string.water_volume),
                        DoserApplication.getContext().getString(R.string.unit_us_gallons)),
                new SeachemParameter(DoserApplication.getContext().getString(R.string.current_magnesium),
                        DoserApplication.getContext().getString(R.string.mgL_ppm)),
                new SeachemParameter(DoserApplication.getContext().getString(R.string.desired_magnesium),
                        DoserApplication.getContext().getString(R.string.mgL_ppm), 1350)
        });

        setParameters(UnitMeasurement.Metric, new SeachemParameter[]{
                new SeachemParameter(DoserApplication.getContext().getString(R.string.water_volume),
                        DoserApplication.getContext().getString(R.string.unit_litres)),
                new SeachemParameter(DoserApplication.getContext().getString(R.string.current_magnesium),
                        DoserApplication.getContext().getString(R.string.mgL_ppm)),
                new SeachemParameter(DoserApplication.getContext().getString(R.string.desired_magnesium),
                        DoserApplication.getContext().getString(R.string.mgL_ppm), 1350)
        });
    }

    @Override
    public SeachemDosage[] calculateDosage(UnitMeasurement unitMeasurement) {
        double volume = getParameters().get(unitMeasurement)[0].getValue();
        double current = getParameters().get(unitMeasurement)[1].getValue();
        double desired = getParameters().get(unitMeasurement)[2].getValue();

        if (unitMeasurement == UnitMeasurement.Metric) {
            volume = UnitConversion.LitresToGallons(volume);
        }

        double tspns = (0.0095 * volume * (desired - current));
        double grams = tspns * 5;

        return new SeachemDosage[]{
                new SeachemDosage(DoserApplication.getContext().getString(R.string.unit_tspns), tspns),
                new SeachemDosage(DoserApplication.getContext().getString(R.string.unit_grams), grams)
        };
    }
}
