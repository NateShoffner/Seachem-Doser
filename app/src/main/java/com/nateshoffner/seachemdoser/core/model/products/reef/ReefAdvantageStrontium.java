package com.nateshoffner.seachemdoser.core.model.products.reef;

import com.nateshoffner.seachemdoser.DoserApplication;
import com.nateshoffner.seachemdoser.R;
import com.nateshoffner.seachemdoser.core.model.SeachemDosage;
import com.nateshoffner.seachemdoser.core.model.SeachemParameter;
import com.nateshoffner.seachemdoser.core.model.SeachemProduct;
import com.nateshoffner.seachemdoser.core.model.UnitMeasurement;
import com.nateshoffner.seachemdoser.utils.UnitConversion;

public class ReefAdvantageStrontium extends SeachemProduct {

    public ReefAdvantageStrontium() {
        super(DoserApplication.getContext().getString(R.string.product_reef_advantage_strontium));

        addNote(DoserApplication.getContext().getString(R.string.product_comment_reef_advantage_strontium));

        setParameters(UnitMeasurement.ImperialUS, new SeachemParameter[]{
                new SeachemParameter(DoserApplication.getContext().getString(R.string.water_volume),
                        DoserApplication.getContext().getString(R.string.unit_us_gallons)),
                new SeachemParameter(DoserApplication.getContext().getString(R.string.current_strontium),
                        DoserApplication.getContext().getString(R.string.mgL_ppm)),
                new SeachemParameter(DoserApplication.getContext().getString(R.string.desired_strontium),
                        DoserApplication.getContext().getString(R.string.mgL_ppm), 420)
        });

        setParameters(UnitMeasurement.Metric, new SeachemParameter[]{
                new SeachemParameter(DoserApplication.getContext().getString(R.string.water_volume),
                        DoserApplication.getContext().getString(R.string.unit_litres)),
                new SeachemParameter(DoserApplication.getContext().getString(R.string.current_strontium),
                        DoserApplication.getContext().getString(R.string.mgL_ppm)),
                new SeachemParameter(DoserApplication.getContext().getString(R.string.desired_strontium),
                        DoserApplication.getContext().getString(R.string.mgL_ppm), 420)
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

        double grams = ((volume * (desired - current)) / 7.5);
        double tspns = ((grams / 8));

        return new SeachemDosage[]{
                new SeachemDosage(DoserApplication.getContext().getString(R.string.unit_tspns), tspns),
                new SeachemDosage(DoserApplication.getContext().getString(R.string.unit_grams), grams)
        };
    }
}
