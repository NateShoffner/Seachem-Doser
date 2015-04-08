package com.nateshoffner.seachemdoser.core.model.products.reef;

import com.nateshoffner.seachemdoser.core.model.SeachemDosage;
import com.nateshoffner.seachemdoser.core.model.SeachemParameter;
import com.nateshoffner.seachemdoser.core.model.SeachemProduct;
import com.nateshoffner.seachemdoser.utils.Constants;
import com.nateshoffner.seachemdoser.utils.MathUtils;

public class ReefStrontium implements SeachemProduct {

    private SeachemParameter[] parameters;
    private String comment;
    private String name;

    public ReefStrontium() {
        this.parameters = new SeachemParameter[]{
                new SeachemParameter("Aquarium Volume", "US Gallons"),
                new SeachemParameter("Current Strontium", "mg/L (ppm)"),
                new SeachemParameter("Desired Strontium", "mg/L (ppm)")
        };

        this.name = "Reef Strontium";
        this.comment = "Considerations: We recommend a strontium level between 8-12 mg/L. It is advisable to make large adjustments slowly to avoid overshooting intended level. Excess strontium  may enhance the loss of carbonate alkalinity. Do not directly mix with any carbonate supplement.";
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

        double doseB = 0.400000 * volume * (desired - current);
        double doseA = doseB / Constants.CapmL;
        doseA = MathUtils.round(doseA * 10) / 10;
        doseB = MathUtils.round(doseB * 10) / 10;

        return new SeachemDosage[]{
                new SeachemDosage("Caps", doseA),
                new SeachemDosage("mL", doseB)
        };
    }
}
