package com.nateshoffner.seachemdoser.core.model.products.reef;

import com.nateshoffner.seachemdoser.DoserApplication;
import com.nateshoffner.seachemdoser.R;
import com.nateshoffner.seachemdoser.core.model.SeachemDosage;
import com.nateshoffner.seachemdoser.core.model.SeachemParameter;
import com.nateshoffner.seachemdoser.core.model.SeachemProduct;
import com.nateshoffner.seachemdoser.core.model.UnitMeasurement;
import com.nateshoffner.seachemdoser.utils.Constants;
import com.nateshoffner.seachemdoser.utils.UnitConversion;

public class ReefIodide extends SeachemProduct {

    public ReefIodide() {
        super(DoserApplication.getContext().getString(R.string.product_reef_iodide));

        addNote(DoserApplication.getContext().getString(R.string.product_comment_reef_iodide));

        addWarning(DoserApplication.getContext().getString(R.string.product_warning_reef_iodide));

        setParameters(UnitMeasurement.ImperialUS, new SeachemParameter[]{
                new SeachemParameter(DoserApplication.getContext().getString(R.string.water_volume),
                        DoserApplication.getContext().getString(R.string.unit_us_gallons)),
                new SeachemParameter(DoserApplication.getContext().getString(R.string.current_iodide),
                        DoserApplication.getContext().getString(R.string.mgL_ppm)),
                new SeachemParameter(DoserApplication.getContext().getString(R.string.desired_iodide),
                        DoserApplication.getContext().getString(R.string.mgL_ppm))
        });

        setParameters(UnitMeasurement.Metric, new SeachemParameter[]{
                new SeachemParameter(DoserApplication.getContext().getString(R.string.water_volume),
                        DoserApplication.getContext().getString(R.string.unit_litres)),
                new SeachemParameter(DoserApplication.getContext().getString(R.string.current_iodide),
                        DoserApplication.getContext().getString(R.string.mgL_ppm)),
                new SeachemParameter(DoserApplication.getContext().getString(R.string.desired_iodide),
                        DoserApplication.getContext().getString(R.string.mgL_ppm))
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

        double ml = (0.5 * volume * (desired - current));
        double caps = ml / Constants.CapmL;

        return new SeachemDosage[]{
                new SeachemDosage(DoserApplication.getContext().getString(R.string.unit_caps), caps),
                new SeachemDosage(DoserApplication.getContext().getString(R.string.unit_ml), ml)
        };
    }
}
