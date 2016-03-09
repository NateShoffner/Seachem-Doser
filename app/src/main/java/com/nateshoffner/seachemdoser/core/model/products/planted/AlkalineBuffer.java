package com.nateshoffner.seachemdoser.core.model.products.planted;

import com.nateshoffner.seachemdoser.DoserApplication;
import com.nateshoffner.seachemdoser.R;
import com.nateshoffner.seachemdoser.core.model.SeachemDosage;
import com.nateshoffner.seachemdoser.core.model.SeachemParameter;
import com.nateshoffner.seachemdoser.core.model.SeachemProduct;
import com.nateshoffner.seachemdoser.utils.MathUtils;

public class AlkalineBuffer implements SeachemProduct {

    private SeachemParameter[] parameters;
    private String comment;
    private String name;

    public AlkalineBuffer() {
        this.parameters = new SeachemParameter[]{
                new SeachemParameter(DoserApplication.getContext().getString(R.string.aquarium_volume),
                        DoserApplication.getContext().getString(R.string.unit_us_gallons)),
                new SeachemParameter(DoserApplication.getContext().getString(R.string.current_kh), DoserApplication.getContext().getString(R.string.meqL)),
                new SeachemParameter(DoserApplication.getContext().getString(R.string.desired_kh), DoserApplication.getContext().getString(R.string.meqL))
        };

        this.name = DoserApplication.getContext().getString(R.string.product_alkaline_buffer);
        this.comment = DoserApplication.getContext().getString(R.string.product_comment_alkaline_buffer);
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
