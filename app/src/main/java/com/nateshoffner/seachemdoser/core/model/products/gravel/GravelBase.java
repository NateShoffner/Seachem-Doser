package com.nateshoffner.seachemdoser.core.model.products.gravel;

import com.nateshoffner.seachemdoser.DoserApplication;
import com.nateshoffner.seachemdoser.R;
import com.nateshoffner.seachemdoser.core.model.SeachemDosage;
import com.nateshoffner.seachemdoser.core.model.SeachemParameter;
import com.nateshoffner.seachemdoser.core.model.SeachemProduct;

public abstract class GravelBase implements SeachemProduct {

    protected SeachemParameter[] parameters;
    protected String comment;

    protected GravelBase() {

        this.parameters = new SeachemParameter[]{
                new SeachemParameter(DoserApplication.getContext().getString(R.string.aquarium_width),
                        DoserApplication.getContext().getString(R.string.unit_inches)),
                new SeachemParameter(DoserApplication.getContext().getString(R.string.aquarium_length),
                        DoserApplication.getContext().getString(R.string.unit_inches)),
                new SeachemParameter(DoserApplication.getContext().getString(R.string.desired_substrate_depth),
                        DoserApplication.getContext().getString(R.string.unit_inches))
        };

        this.comment = DoserApplication.getContext().getString(R.string.product_comment_gravel);
    }

    public SeachemParameter[] getParameters() {
        return this.parameters;
    }

    public String getComment() {
        return this.comment;
    }

    protected SeachemDosage[] CalculateDosage(double size) {
        double width = this.parameters[0].getValue();
        double length = this.parameters[1].getValue();
        double depth = this.parameters[2].getValue();


        double total = Math.ceil(width * length * depth / size);

        return new SeachemDosage[]{
                new SeachemDosage(DoserApplication.getContext().getString(R.string.unit_bags), total)
        };
    }

}
