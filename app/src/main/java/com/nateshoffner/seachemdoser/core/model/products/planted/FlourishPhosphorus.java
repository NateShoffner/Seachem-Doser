package com.nateshoffner.seachemdoser.core.model.products.planted;

import com.nateshoffner.seachemdoser.core.model.SeachemDosage;
import com.nateshoffner.seachemdoser.core.model.SeachemParameter;
import com.nateshoffner.seachemdoser.core.model.SeachemProduct;
import com.nateshoffner.seachemdoser.utils.Constants;
import com.nateshoffner.seachemdoser.utils.MathUtils;

public class FlourishPhosphorus implements SeachemProduct {

    private SeachemParameter[] parameters;
    private String comment;
    private String name;

    public FlourishPhosphorus() {
        this.parameters = new SeachemParameter[]{
                new SeachemParameter("Aquarium Volume", "US Gallons"),
                new SeachemParameter("Current Phosphorus", "mg/L (ppm)"),
                new SeachemParameter("Desired Phosphorus", "mg/L (ppm)")
        };

        this.name = "Flourish Phosphorus";
        this.comment = "Considerations: Use once or twice a week or as needed in response to signs of phosphorus deficiency (e.g. stunted growth, plant dark green). The ideal phosphate level will vary, but generally ranges from 0.15–1.0 mg/L. Use MultiTest: Phosphate™ to monitor phosphate levels.";
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

        double doseB = volume / 20 * ((desired - current) * 16.600000);
        double doseA = doseB / Constants.CapmL;
        doseA = MathUtils.round(doseA * 10) / 10;
        doseB = MathUtils.round(doseB * 10) / 10;

        return new SeachemDosage[]{
                new SeachemDosage("Caps", doseA),
                new SeachemDosage("mL", doseB)
        };
    }
}
