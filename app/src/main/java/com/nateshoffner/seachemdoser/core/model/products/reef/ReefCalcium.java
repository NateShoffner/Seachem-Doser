package com.nateshoffner.seachemdoser.core.model.products.reef;

import com.nateshoffner.seachemdoser.DoserApplication;
import com.nateshoffner.seachemdoser.R;
import com.nateshoffner.seachemdoser.core.model.SeachemDosage;
import com.nateshoffner.seachemdoser.core.model.SeachemParameter;
import com.nateshoffner.seachemdoser.core.model.SeachemProduct;
import com.nateshoffner.seachemdoser.core.model.UnitMeasurement;
import com.nateshoffner.seachemdoser.utils.Constants;
import com.nateshoffner.seachemdoser.utils.UnitConversion;

public class ReefCalcium extends SeachemProduct {

    public ReefCalcium() {
        super(DoserApplication.getContext().getString(R.string.product_reef_calcium));

        addComment(DoserApplication.getContext().getString(R.string.product_comment_reef_calcium));

        addWarning(DoserApplication.getContext().getString(R.string.product_warning_reef_calcium));


        setParameters(UnitMeasurement.ImperialUS, new SeachemParameter[]{
                new SeachemParameter(DoserApplication.getContext().getString(R.string.water_volume),
                        DoserApplication.getContext().getString(R.string.unit_us_gallons)),
                new SeachemParameter(DoserApplication.getContext().getString(R.string.current_calcium),
                        DoserApplication.getContext().getString(R.string.mgL_ppm)),
                new SeachemParameter(DoserApplication.getContext().getString(R.string.desired_calcium),
                        DoserApplication.getContext().getString(R.string.mgL_ppm), 420)});

        setParameters(UnitMeasurement.Metric, new SeachemParameter[]{
                new SeachemParameter(DoserApplication.getContext().getString(R.string.water_volume),
                        DoserApplication.getContext().getString(R.string.unit_litres)),
                new SeachemParameter(DoserApplication.getContext().getString(R.string.current_calcium),
                        DoserApplication.getContext().getString(R.string.mgL_ppm)),
                new SeachemParameter(DoserApplication.getContext().getString(R.string.desired_calcium),
                        DoserApplication.getContext().getString(R.string.mgL_ppm), 420)});
    }

    @Override
    public SeachemDosage[] calculateDosage(UnitMeasurement unitMeasurement) {
        double volume = getParameters().get(unitMeasurement)[0].getValue();
        double current = getParameters().get(unitMeasurement)[1].getValue();
        double desired = getParameters().get(unitMeasurement)[2].getValue();

        if (unitMeasurement == UnitMeasurement.Metric) {
            volume = UnitConversion.LitresToGallons(volume);
        }

        double ml = ((((desired - current) / 3) * (volume / 20)) * 5);
        double caps = ml / Constants.CapmL;

        return new SeachemDosage[]{
                new SeachemDosage(DoserApplication.getContext().getString(R.string.unit_caps), caps),
                new SeachemDosage(DoserApplication.getContext().getString(R.string.unit_ml), ml)
        };
    }
}
