package com.nateshoffner.seachemdoser.core.model.products.reef;

import com.nateshoffner.seachemdoser.DoserApplication;
import com.nateshoffner.seachemdoser.R;
import com.nateshoffner.seachemdoser.core.model.SeachemDosage;
import com.nateshoffner.seachemdoser.core.model.SeachemParameter;
import com.nateshoffner.seachemdoser.core.model.SeachemProduct;
import com.nateshoffner.seachemdoser.core.model.UnitMeasurement;
import com.nateshoffner.seachemdoser.utils.Constants;
import com.nateshoffner.seachemdoser.utils.UnitConversion;

import java.util.Dictionary;
import java.util.Hashtable;

public class ReefFusion2 implements SeachemProduct {

    private Dictionary<UnitMeasurement, SeachemParameter[]> mParameters = new Hashtable<>();
    private String mComment;
    private String mName;

    public ReefFusion2() {

        mParameters.put(UnitMeasurement.ImperialUS, new SeachemParameter[]{
                new SeachemParameter(DoserApplication.getContext().getString(R.string.water_volume),
                        DoserApplication.getContext().getString(R.string.unit_us_gallons)),
                new SeachemParameter(DoserApplication.getContext().getString(R.string.current_alkalinity),
                        DoserApplication.getContext().getString(R.string.meqL)),
                new SeachemParameter(DoserApplication.getContext().getString(R.string.desired_alkalinity),
                        DoserApplication.getContext().getString(R.string.meqL))
        });

        mParameters.put(UnitMeasurement.Metric, new SeachemParameter[]{
                new SeachemParameter(DoserApplication.getContext().getString(R.string.water_volume),
                        DoserApplication.getContext().getString(R.string.unit_litres)),
                new SeachemParameter(DoserApplication.getContext().getString(R.string.current_alkalinity),
                        DoserApplication.getContext().getString(R.string.meqL)),
                new SeachemParameter(DoserApplication.getContext().getString(R.string.desired_alkalinity),
                        DoserApplication.getContext().getString(R.string.meqL))
        });

        mName = DoserApplication.getContext().getString(R.string.product_reef_fusion_2);
        mComment = DoserApplication.getContext().getString(R.string.product_comment_reef_fusion_2);
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

        double ml = ((volume / 6.5) * ((desired - current) / 4));
        double caps = (ml / Constants.CapmL);

        return new SeachemDosage[]{
                new SeachemDosage(DoserApplication.getContext().getString(R.string.unit_caps), caps),
                new SeachemDosage(DoserApplication.getContext().getString(R.string.unit_ml), ml)
        };
    }
}