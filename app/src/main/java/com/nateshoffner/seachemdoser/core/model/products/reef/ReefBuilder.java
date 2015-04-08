package com.nateshoffner.seachemdoser.core.model.products.reef;

import com.nateshoffner.seachemdoser.core.model.SeachemDosage;
import com.nateshoffner.seachemdoser.core.model.SeachemParameter;
import com.nateshoffner.seachemdoser.core.model.SeachemProduct;
import com.nateshoffner.seachemdoser.utils.MathUtils;

public class ReefBuilder implements SeachemProduct {

    private SeachemParameter[] parameters;
    private String comment;
    private String name;

    public ReefBuilder() {
        this.parameters = new SeachemParameter[]{
                new SeachemParameter("Aquarium Volume", "US Gallons"),
                new SeachemParameter("Current Alkalinity", "meq/L"),
                new SeachemParameter("Desired Alkalinity", "meq/L")
        };

        this.name = "Reef Builder";
        this.comment = "Considerations: Reef alkalinity should ideally be maintained at 4-5 meq/L (11-17 dKH). Alkalinity should not be allowed to fall below  2 meq/L. It is advisable to make large adjustments slowly to avoid overshooting intended level or shocking  corals and inverts. Dissolve in at least one cup of freshwater. Do not exceed 12 g/150 L per day.";
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

        double doseB = 0.320000 * (volume * (desired - current));
        double doseA = doseB / 6;
        doseA = MathUtils.round(doseA * 10) / 10;
        doseB = MathUtils.round(doseB * 10) / 10;

        return new SeachemDosage[]{
                new SeachemDosage("Tspns", doseA),
                new SeachemDosage("Grams", doseB)
        };
    }
}
