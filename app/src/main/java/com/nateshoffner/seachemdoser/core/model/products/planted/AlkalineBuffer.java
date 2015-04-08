package com.nateshoffner.seachemdoser.core.model.products.planted;

import com.nateshoffner.seachemdoser.core.model.SeachemDosage;
import com.nateshoffner.seachemdoser.core.model.SeachemParameter;
import com.nateshoffner.seachemdoser.core.model.SeachemProduct;
import com.nateshoffner.seachemdoser.utils.MathUtils;

public class AlkalineBuffer implements SeachemProduct {

    private SeachemParameter[] parameters;
    private String comment;
    private String name;

    public AlkalineBuffer() {
        this.parameters = new SeachemParameter[]{
                new SeachemParameter("Aquarium Volume", "US Gallons"),
                new SeachemParameter("Current KH", "meq/L"),
                new SeachemParameter("Desired KH", "meq/L")
        };

        this.name = "Alkaline Buffer";
        this.comment = "Considerations: 2 meq/L is equal to  5.6 dKH. If your test kit measures in dKH, divide your test kit reading by 2.8 to determine your current KH levels in meq/L. Do the same for desired KH.";
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

        double doseB = (desired - current) * 3.500000 * (volume / 10);
        double doseA = doseB / 7;
        doseA = MathUtils.round(doseA * 10) / 10;
        doseB = MathUtils.round(doseB * 10) / 10;

        return new SeachemDosage[]{
                new SeachemDosage("Tspns", doseA),
                new SeachemDosage("Grams", doseB)
        };
    }
}
