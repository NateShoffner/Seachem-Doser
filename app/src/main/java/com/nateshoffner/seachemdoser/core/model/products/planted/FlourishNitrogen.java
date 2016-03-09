package com.nateshoffner.seachemdoser.core.model.products.planted;

import com.nateshoffner.seachemdoser.DoserApplication;
import com.nateshoffner.seachemdoser.R;
import com.nateshoffner.seachemdoser.core.model.SeachemDosage;
import com.nateshoffner.seachemdoser.core.model.SeachemParameter;
import com.nateshoffner.seachemdoser.core.model.SeachemProduct;
import com.nateshoffner.seachemdoser.utils.Constants;
import com.nateshoffner.seachemdoser.utils.MathUtils;

public class FlourishNitrogen implements SeachemProduct {

    private SeachemParameter[] parameters;
    private String comment;
    private String name;

    public FlourishNitrogen() {
        this.parameters = new SeachemParameter[]{
                new SeachemParameter(DoserApplication.getContext().getString(R.string.aquarium_volume),
                        DoserApplication.getContext().getString(R.string.unit_us_gallons)),
                new SeachemParameter(DoserApplication.getContext().getString(R.string.current_nitrogen), DoserApplication.getContext().getString(R.string.meqL)),
                new SeachemParameter(DoserApplication.getContext().getString(R.string.desired_nitrogen), DoserApplication.getContext().getString(R.string.meqL))
        };

        this.name = DoserApplication.getContext().getString(R.string.product_flourish_nitrogen);
        this.comment = DoserApplication.getContext().getString(R.string.product_comment_flourish_nitrogen);
    }


    @Override
    public String getName() {
        return this.name;
    }

    @Override
    public SeachemParameter[] getParameters() {
        return this.parameters;
    }

    @Override
    public String getComment() {
        return this.comment;
    }

    @Override
    public SeachemDosage[] calculateDosage() {
        double volume = this.parameters[0].getValue();
        double current = this.parameters[1].getValue();
        double desired = this.parameters[2].getValue();

        double doseB = (desired - current) * (volume * 0.250000);
        double doseA = doseB / Constants.CapmL;
        doseA = MathUtils.round(doseA * 10) / 10;
        doseB = MathUtils.round(doseB * 10) / 10;

        return new SeachemDosage[]{
                new SeachemDosage(DoserApplication.getContext().getString(R.string.unit_caps), doseA),
                new SeachemDosage(DoserApplication.getContext().getString(R.string.unit_ml), doseB)
        };
    }
}
