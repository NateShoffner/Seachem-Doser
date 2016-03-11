package com.nateshoffner.seachemdoser.core.model.products.planted;

import com.nateshoffner.seachemdoser.DoserApplication;
import com.nateshoffner.seachemdoser.R;
import com.nateshoffner.seachemdoser.core.model.SeachemDosage;
import com.nateshoffner.seachemdoser.core.model.SeachemParameter;
import com.nateshoffner.seachemdoser.core.model.SeachemProduct;
import com.nateshoffner.seachemdoser.core.model.UnitMeasurement;
import com.nateshoffner.seachemdoser.utils.MathUtils;
import com.nateshoffner.seachemdoser.utils.UnitConversion;

import java.util.Dictionary;
import java.util.Hashtable;

public class AlkalineBuffer implements SeachemProduct {

    private Dictionary<UnitMeasurement, SeachemParameter[]> mParameters = new Hashtable<>();
    private String mComment;
    private String mName;

    public AlkalineBuffer() {

        mParameters.put(UnitMeasurement.Imperial, new SeachemParameter[]{
                new SeachemParameter(DoserApplication.getContext().getString(R.string.aquarium_volume),
                        DoserApplication.getContext().getString(R.string.unit_us_gallons)),
                new SeachemParameter(DoserApplication.getContext().getString(R.string.current_kh),
                        DoserApplication.getContext().getString(R.string.meqL)),
                new SeachemParameter(DoserApplication.getContext().getString(R.string.desired_kh),
                        DoserApplication.getContext().getString(R.string.meqL))});

        mParameters.put(UnitMeasurement.Metric, new SeachemParameter[]{
                new SeachemParameter(DoserApplication.getContext().getString(R.string.aquarium_volume),
                        DoserApplication.getContext().getString(R.string.unit_litres)),
                new SeachemParameter(DoserApplication.getContext().getString(R.string.current_kh),
                        DoserApplication.getContext().getString(R.string.meqL)),
                new SeachemParameter(DoserApplication.getContext().getString(R.string.desired_kh),
                        DoserApplication.getContext().getString(R.string.meqL))});

        mName = DoserApplication.getContext().getString(R.string.product_alkaline_buffer);
        mComment = DoserApplication.getContext().getString(R.string.product_comment_alkaline_buffer);
    }

    @Override
    public String getName() {
        return mName;
    }

    @Override
    public SeachemParameter[] getParameters(UnitMeasurement unitMeasurement) {
        return mParameters.get(unitMeasurement);
    }

    @Override
    public String getComment() {
        return mComment;
    }

    @Override
    public SeachemDosage[] calculateDosage(UnitMeasurement unitMeasurement) {
        double volume = mParameters.get(unitMeasurement)[0].getValue();
        double current = mParameters.get(unitMeasurement)[1].getValue();
        double desired = mParameters.get(unitMeasurement)[2].getValue();

        if (unitMeasurement == UnitMeasurement.Metric) {
            volume = UnitConversion.LitresToGallons(volume);
        }

        double doseB = (desired - current) * 3.500000 * (volume / 10);
        double doseA = doseB / 7;
        doseA = MathUtils.round(doseA * 10) / 10;
        doseB = MathUtils.round(doseB * 10) / 10;

        return new SeachemDosage[]{
                new SeachemDosage(DoserApplication.getContext().getString(R.string.unit_tspns), doseA),
                new SeachemDosage(DoserApplication.getContext().getString(R.string.unit_grams), doseB)
        };
    }
}
