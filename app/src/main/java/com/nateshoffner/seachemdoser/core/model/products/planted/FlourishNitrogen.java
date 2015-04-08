package com.nateshoffner.seachemdoser.core.model.products.planted;

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
                new SeachemParameter("Aquarium Volume", "US Gallons"),
                new SeachemParameter("Current Nitrogen", "meq/L"),
                new SeachemParameter("Desired Nitrogen", "meq/L")
        };

        this.name = "Flourish Nitrogen";
        this.comment = "Considerations: If using a \"nitrate equivalent\" value for Current and Desired Nitrogen levels, divide the result by 5.";
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
                new SeachemDosage("Caps", doseA),
                new SeachemDosage("mL", doseB)
        };
    }
}
