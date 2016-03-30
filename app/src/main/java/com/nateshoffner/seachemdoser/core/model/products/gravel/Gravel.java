package com.nateshoffner.seachemdoser.core.model.products.gravel;

import com.nateshoffner.seachemdoser.DoserApplication;
import com.nateshoffner.seachemdoser.R;
import com.nateshoffner.seachemdoser.core.model.SeachemDosage;
import com.nateshoffner.seachemdoser.core.model.SeachemParameter;
import com.nateshoffner.seachemdoser.core.model.SeachemProduct;
import com.nateshoffner.seachemdoser.core.model.UnitMeasurement;
import com.nateshoffner.seachemdoser.utils.UnitConversion;

public abstract class Gravel extends SeachemProduct {

    protected Gravel(String name) {
        super(name);

        setParameters(UnitMeasurement.ImperialUS, new SeachemParameter[]{
                new SeachemParameter(DoserApplication.getContext().getString(R.string.aquarium_width),
                        DoserApplication.getContext().getString(R.string.unit_inches)),
                new SeachemParameter(DoserApplication.getContext().getString(R.string.aquarium_length),
                        DoserApplication.getContext().getString(R.string.unit_inches)),
                new SeachemParameter(DoserApplication.getContext().getString(R.string.desired_substrate_depth),
                        DoserApplication.getContext().getString(R.string.unit_inches))});

        setParameters(UnitMeasurement.Metric, new SeachemParameter[]{
                new SeachemParameter(DoserApplication.getContext().getString(R.string.aquarium_width),
                        DoserApplication.getContext().getString(R.string.unit_centimeters)),
                new SeachemParameter(DoserApplication.getContext().getString(R.string.aquarium_length),
                        DoserApplication.getContext().getString(R.string.unit_centimeters)),
                new SeachemParameter(DoserApplication.getContext().getString(R.string.desired_substrate_depth),
                        DoserApplication.getContext().getString(R.string.unit_centimeters))});
    }

    protected SeachemDosage[] CalculateDosage(UnitMeasurement unitMeasurement, double divisor,
                                              double divisorSmall) {
        double width = getParameters().get(unitMeasurement)[0].getValue();
        double length = getParameters().get(unitMeasurement)[1].getValue();
        double depth = getParameters().get(unitMeasurement)[2].getValue();

        if (unitMeasurement == UnitMeasurement.Metric) {
            width = UnitConversion.CentimetersToInches(width);
            length = UnitConversion.CentimetersToInches(length);
            depth = UnitConversion.CentimetersToInches(depth);
        }

        double bags = Math.ceil(width * length * depth) / divisor;
        double bagsSmall = Math.ceil(width * length * depth) / divisorSmall;

        // round up all values
        bags = Math.ceil(bags);
        bagsSmall = Math.ceil(bagsSmall);

        return new SeachemDosage[]{
                new SeachemDosage(DoserApplication.getContext().getString(R.string.unit_bags), bags),
                new SeachemDosage(DoserApplication.getContext().getString(R.string.unit_bags_small), bagsSmall)
        };
    }

}
