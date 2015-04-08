package com.nateshoffner.seachemdoser.core.model.products.reef;

import com.nateshoffner.seachemdoser.core.model.SeachemDosage;
import com.nateshoffner.seachemdoser.core.model.SeachemParameter;
import com.nateshoffner.seachemdoser.core.model.SeachemProduct;
import com.nateshoffner.seachemdoser.utils.MathUtils;

public class ReefBuffer implements SeachemProduct {

    private SeachemParameter[] parameters;
    private String comment;
    private String name;

    public ReefBuffer() {
        this.parameters = new SeachemParameter[]{
                new SeachemParameter("Aquarium Volume", "US Gallons"),
                new SeachemParameter("Current Alkalinity", "meq/L"),
                new SeachemParameter("Desired Alkalinity", "meq/L")
        };

        this.name = "Reef Buffer";
        this.comment = "Considerations: Reef alkalinity should ideally be maintained at 4-5 meq/L (11-14 dKH).  Reef Buffer™ will raise carbonate alkalinity; however, it is intended primarily for use as a buffer in a reef system where the maintenance of a pH of 8.3 is often difficult. When pH is not an issue, try  Reef Builder™ or Reef Carbonate™";
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

        double doseA = (desired - current) / 0.500000 * (volume / 40);
        double doseB = doseA * 5;
        doseA = MathUtils.round(doseA * 10) / 10;
        doseB = MathUtils.round(doseB * 10) / 10;

        return new SeachemDosage[]{
                new SeachemDosage("Tspns", doseA),
                new SeachemDosage("Grams", doseB)
        };
    }
}
