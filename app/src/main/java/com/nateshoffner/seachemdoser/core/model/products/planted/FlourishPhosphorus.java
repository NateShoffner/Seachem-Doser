package com.nateshoffner.seachemdoser.core.model.products.planted;

import com.nateshoffner.seachemdoser.DoserApplication;
import com.nateshoffner.seachemdoser.R;
import com.nateshoffner.seachemdoser.core.model.SeachemDosage;
import com.nateshoffner.seachemdoser.core.model.SeachemParameter;
import com.nateshoffner.seachemdoser.core.model.SeachemProduct;
import com.nateshoffner.seachemdoser.core.model.UnitMeasurement;
import com.nateshoffner.seachemdoser.utils.Constants;
import com.nateshoffner.seachemdoser.utils.MathUtils;
import com.nateshoffner.seachemdoser.utils.UnitConversion;

import java.util.Dictionary;
import java.util.Hashtable;

public class FlourishPhosphorus implements SeachemProduct {

    private Dictionary<UnitMeasurement, SeachemParameter[]> mParameters = new Hashtable<>();
    private String mComment;
    private String mName;

    public FlourishPhosphorus() {

        mParameters.put(UnitMeasurement.ImperialUS, new SeachemParameter[]{
                new SeachemParameter(DoserApplication.getContext().getString(R.string.water_volume),
                        DoserApplication.getContext().getString(R.string.unit_us_gallons)),
                new SeachemParameter(DoserApplication.getContext().getString(R.string.current_phosphorus),
                        DoserApplication.getContext().getString(R.string.mgL_ppm)),
                new SeachemParameter(DoserApplication.getContext().getString(R.string.desired_phosphorus),
                        DoserApplication.getContext().getString(R.string.mgL_ppm))
        });

        mParameters.put(UnitMeasurement.Metric, new SeachemParameter[]{
                new SeachemParameter(DoserApplication.getContext().getString(R.string.water_volume),
                        DoserApplication.getContext().getString(R.string.unit_litres)),
                new SeachemParameter(DoserApplication.getContext().getString(R.string.current_phosphorus),
                        DoserApplication.getContext().getString(R.string.mgL_ppm)),
                new SeachemParameter(DoserApplication.getContext().getString(R.string.desired_phosphorus),
                        DoserApplication.getContext().getString(R.string.mgL_ppm))
        });

        mName = DoserApplication.getContext().getString(R.string.product_flourish_phosphorus);
        mComment = DoserApplication.getContext().getString(R.string.product_comment_flourish_phosphorus);
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

        double doseB = volume / 20 * ((desired - current) * 16.600000);
        double doseA = doseB / Constants.CapmL;
        doseA = MathUtils.round(doseA * 10) / 10;
        doseB = MathUtils.round(doseB * 10) / 10;

        return new SeachemDosage[]{
                new SeachemDosage(DoserApplication.getContext().getString(R.string.unit_caps), doseA),
                new SeachemDosage(DoserApplication.getContext().getString(R.string.unit_ml), doseB)
        };
    }
}
