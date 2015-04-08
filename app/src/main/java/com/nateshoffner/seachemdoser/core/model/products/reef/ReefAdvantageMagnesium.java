package com.nateshoffner.seachemdoser.core.model.products.reef;

import com.nateshoffner.seachemdoser.core.model.SeachemDosage;
import com.nateshoffner.seachemdoser.core.model.SeachemParameter;
import com.nateshoffner.seachemdoser.core.model.SeachemProduct;
import com.nateshoffner.seachemdoser.utils.MathUtils;

public class ReefAdvantageMagnesium implements SeachemProduct {

    private SeachemParameter[] parameters;
    private String comment;
    private String name;

    public ReefAdvantageMagnesium() {
        this.parameters = new SeachemParameter[]{
                new SeachemParameter("Aquarium Volume", "US Gallons"),
                new SeachemParameter("Current Magnesium", "mg/L (ppm)"),
                new SeachemParameter("Desired Magnesium", "mg/L (ppm)")
        };

        this.name = "Reef Advantage Magnesium";
        this.comment = "Considerations: We recommend a magnesium  level between 1200-1350 mg/L. Make large adjustments slowly to avoid overshooting intended level. Amount or frequency can be adjusted, but do not exceed 25 g/80 L per day. Dissolve in at least one cup of freshwater. Excess magnesium may enhance the loss of carbonate alkalinity. Do not directly mix with any carbonate supplement.";
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

        double doseA = 0.009500 * volume * (desired - current);
        double doseB = doseA * 5;
        doseA = MathUtils.round(doseA * 10) / 10;
        doseB = MathUtils.round(doseB * 10) / 10;

        return new SeachemDosage[]{
                new SeachemDosage("Tspns", doseA),
                new SeachemDosage("Grams", doseB)
        };
    }
}
