package com.nateshoffner.seachemdoser.core.model.products.gravel;

import com.nateshoffner.seachemdoser.core.model.SeachemDosage;
import com.nateshoffner.seachemdoser.core.model.SeachemParameter;

public abstract class GravelBase {

    protected SeachemParameter[] parameters;
    protected String comment;

    protected GravelBase() {

        this.parameters = new SeachemParameter[]{
                new SeachemParameter("Aquarium Width", "in"),
                new SeachemParameter("Aquarium Length", "in"),
                new SeachemParameter("Desired Substrate Depth", "in")
        };

        this.comment = "If you plan on varying the depth of your substrate, use an average depth as your desired substrate depth.";
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
                new SeachemDosage("Bags", total)
        };
    }

}
