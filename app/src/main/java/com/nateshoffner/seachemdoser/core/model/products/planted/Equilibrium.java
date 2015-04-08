package com.nateshoffner.seachemdoser.core.model.products.planted;

import com.nateshoffner.seachemdoser.core.model.SeachemDosage;
import com.nateshoffner.seachemdoser.core.model.SeachemParameter;
import com.nateshoffner.seachemdoser.core.model.SeachemProduct;
import com.nateshoffner.seachemdoser.utils.MathUtils;

public class Equilibrium implements SeachemProduct {

    private SeachemParameter[] parameters;
    private String comment;
    private String name;

    public Equilibrium() {
        this.parameters = new SeachemParameter[]{
                new SeachemParameter("Aquarium Volume", "US Gallons"),
                new SeachemParameter("Current GH", "meq/L"),
                new SeachemParameter("Desired GH", "meq/L")
        };

        this.name = "Equilibrium";
        this.comment = "Considerations: Equilibrium™ can be added straight, although for optimum solubility we recommend mixing with ~ 1 L (1 qt.) of water (the resulting mixture will have a white opaque appearance). When this mixture is added to the aquarium it will impart a slight haze that should clear within 15–30 minutes.";
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

        double doseB = volume / 20 * (16 * (desired - current));
        double doseA = doseB / 16;

        doseA = MathUtils.round(doseA * 10) / 10;
        doseB = MathUtils.round(doseB * 10) / 10;

        return new SeachemDosage[]{
                new SeachemDosage("Tbsp", doseA),
                new SeachemDosage("Grams", doseB)
        };
    }
}
