package com.nateshoffner.seachemdoser.core.model.products.reef;

import com.nateshoffner.seachemdoser.core.model.SeachemDosage;
import com.nateshoffner.seachemdoser.core.model.SeachemParameter;
import com.nateshoffner.seachemdoser.core.model.SeachemProduct;
import com.nateshoffner.seachemdoser.utils.MathUtils;

public class ReefAdvantageCalcium implements SeachemProduct {

    private SeachemParameter[] parameters;
    private String comment;
    private String name;

    public ReefAdvantageCalcium() {
        this.parameters = new SeachemParameter[]{
                new SeachemParameter("Aquarium Volume", "US Gallons"),
                new SeachemParameter("Current Calcium", "mg/L (ppm)"),
                new SeachemParameter("Desired Calcium", "mg/L (ppm)")
        };

        this.name = "Reef Advantage Calcium";
        this.comment = "Considerations:  We recommend a Ca level between 380-420 mg/L with an alkalinity between 4-6 meq/L. It is advisable to make large adjustments slowly to avoid overshooting intended level or shocking  corals and inverts. Each 5 g/150 L will raise calcium by about 12 mg/L. Quantity or frequency can be adjusted, but do not exceed 10 g/150L per day.";
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

        double doseB = 0.025000 * volume * (desired - current);
        double doseA = doseB / 5;
        doseA = MathUtils.round(doseA * 10) / 10;
        doseB = MathUtils.round(doseB * 10) / 10;

        return new SeachemDosage[]{
                new SeachemDosage("Tspns", doseA),
                new SeachemDosage("Grams", doseB)
        };
    }
}
