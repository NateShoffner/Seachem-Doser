package com.nateshoffner.seachemdoser.core.model.products.planted;

import com.nateshoffner.seachemdoser.core.model.SeachemDosage;
import com.nateshoffner.seachemdoser.core.model.SeachemParameter;
import com.nateshoffner.seachemdoser.core.model.SeachemProduct;
import com.nateshoffner.seachemdoser.utils.Constants;
import com.nateshoffner.seachemdoser.utils.MathUtils;

public class FlourishPotassium implements SeachemProduct {

    private SeachemParameter[] parameters;
    private String comment;
    private String name;

    public FlourishPotassium() {
        this.parameters = new SeachemParameter[]{
                new SeachemParameter("Aquarium Volume", "US Gallons"),
                new SeachemParameter("Current Potassium", "mg/L (ppm)"),
                new SeachemParameter("Desired Potassium", "mg/L (ppm)")
        };

        this.name = "Flourish Potassium";
        this.comment = "Considerations: Repeat - 2-3 times per week or as needed (in response to signs of potassium deficiency in older leaves which includes: chlorosis (yellowing), necrossis (death/browning), and weak stems and roots.";
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

        double doseB = volume / 30 * ((desired - current) * 2.500000);
        double doseA = doseB / Constants.CapmL;
        doseA = MathUtils.round(doseA * 10) / 10;
        doseB = MathUtils.round(doseB * 10) / 10;

        return new SeachemDosage[]{
                new SeachemDosage("Caps", doseA),
                new SeachemDosage("mL", doseB)
        };
    }
}
