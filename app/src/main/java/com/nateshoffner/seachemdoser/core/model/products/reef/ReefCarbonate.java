package com.nateshoffner.seachemdoser.core.model.products.reef;

import com.nateshoffner.seachemdoser.core.model.SeachemDosage;
import com.nateshoffner.seachemdoser.core.model.SeachemParameter;
import com.nateshoffner.seachemdoser.core.model.SeachemProduct;
import com.nateshoffner.seachemdoser.utils.Constants;
import com.nateshoffner.seachemdoser.utils.MathUtils;

public class ReefCarbonate implements SeachemProduct {

    private SeachemParameter[] parameters;
    private String comment;
    private String name;

    public ReefCarbonate() {
        this.parameters = new SeachemParameter[]{
                new SeachemParameter("Aquarium Volume", "US Gallons"),
                new SeachemParameter("Current Alkalinity", "meq/L"),
                new SeachemParameter("Desired Alkalinity", "meq/L")
        };

        this.name = "Reef Carbonate";
        this.comment = "Considerations: Reef alkalinity should ideally be maintained at 4-5 meq/L (11-17 dKH). It is advisable to make large adjustments slowly to avoid overshooting intended level or shocking  corals and inverts. Each dose will raise alkalinity by about 0.25 meq/L. Size or frequency of dose can be adjusted, but do not exceed 1 meq/L per day.";
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

        double doseA = (desired - current) / 0.250000 * (volume / 20);
        double doseB = doseA * Constants.CapmL;
        doseA = MathUtils.round(doseA * 10) / 10;
        doseB = MathUtils.round(doseB * 10) / 10;

        return new SeachemDosage[]{
                new SeachemDosage("Caps", doseA),
                new SeachemDosage("mL", doseB)
        };
    }
}
