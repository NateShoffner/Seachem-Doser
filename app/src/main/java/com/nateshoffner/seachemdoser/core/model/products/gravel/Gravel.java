package com.nateshoffner.seachemdoser.core.model.products.gravel;

import com.nateshoffner.seachemdoser.DoserApplication;
import com.nateshoffner.seachemdoser.R;
import com.nateshoffner.seachemdoser.core.model.SeachemDosage;
import com.nateshoffner.seachemdoser.core.model.SeachemParameter;
import com.nateshoffner.seachemdoser.core.model.SeachemProduct;
import com.nateshoffner.seachemdoser.core.model.UnitMeasurement;
import com.nateshoffner.seachemdoser.utils.UnitConversion;

import java.util.Dictionary;
import java.util.Hashtable;

public abstract class Gravel implements SeachemProduct {

    protected String mName;
    protected Dictionary<UnitMeasurement, SeachemParameter[]> mParameters = new Hashtable<>();
    protected String mComment;

    protected Gravel(String name) {
        mName = name;

        mParameters.put(UnitMeasurement.ImperialUS, new SeachemParameter[]{
                new SeachemParameter(DoserApplication.getContext().getString(R.string.aquarium_width),
                        DoserApplication.getContext().getString(R.string.unit_inches)),
                new SeachemParameter(DoserApplication.getContext().getString(R.string.aquarium_length),
                        DoserApplication.getContext().getString(R.string.unit_inches)),
                new SeachemParameter(DoserApplication.getContext().getString(R.string.desired_substrate_depth),
                        DoserApplication.getContext().getString(R.string.unit_inches))});

        mParameters.put(UnitMeasurement.Metric, new SeachemParameter[]{
                new SeachemParameter(DoserApplication.getContext().getString(R.string.aquarium_width),
                        DoserApplication.getContext().getString(R.string.unit_centimeters)),
                new SeachemParameter(DoserApplication.getContext().getString(R.string.aquarium_length),
                        DoserApplication.getContext().getString(R.string.unit_centimeters)),
                new SeachemParameter(DoserApplication.getContext().getString(R.string.desired_substrate_depth),
                        DoserApplication.getContext().getString(R.string.unit_centimeters))});

        mComment = DoserApplication.getContext().getString(R.string.product_comment_gravel);
    }

    @Override
    public String getName() {
        return mName;
    }

    public SeachemParameter[] getParameters(UnitMeasurement unitMeasurement) {
        return mParameters.get(unitMeasurement);
    }

    public String getComment() {
        return mComment;
    }

    protected SeachemDosage[] CalculateDosage(UnitMeasurement unitMeasurement, double divisor,
                                              double divisorSmall) {
        double width = mParameters.get(unitMeasurement)[0].getValue();
        double length = mParameters.get(unitMeasurement)[1].getValue();
        double depth = mParameters.get(unitMeasurement)[2].getValue();

        if (unitMeasurement == UnitMeasurement.Metric) {
            width = UnitConversion.CentimetersToInches(width);
            length = UnitConversion.CentimetersToInches(length);
            depth = UnitConversion.CentimetersToInches(depth);
        }

        double bags = Math.ceil(width * length * depth) / divisor;
        double bagsSmall = Math.ceil(width * length * depth) / divisorSmall;

        return new SeachemDosage[]{
                new SeachemDosage(DoserApplication.getContext().getString(R.string.unit_bags), bags),
                new SeachemDosage(DoserApplication.getContext().getString(R.string.unit_bags_small), bagsSmall)
        };
    }

}