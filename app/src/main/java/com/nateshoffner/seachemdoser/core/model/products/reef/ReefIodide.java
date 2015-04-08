package com.nateshoffner.seachemdoser.core.model.products.reef;

import com.nateshoffner.seachemdoser.core.model.SeachemDosage;
import com.nateshoffner.seachemdoser.core.model.SeachemParameter;
import com.nateshoffner.seachemdoser.core.model.SeachemProduct;
import com.nateshoffner.seachemdoser.utils.Constants;
import com.nateshoffner.seachemdoser.utils.MathUtils;

public class ReefIodide implements SeachemProduct {

    private SeachemParameter[] parameters;
    private String comment;
    private String name;

    public ReefIodide() {
        this.parameters = new SeachemParameter[]{
                new SeachemParameter("Aquarium Volume", "US Gallons"),
                new SeachemParameter("Current Iodide", "mg/L (ppm)"),
                new SeachemParameter("Desired Iodide", "mg/L (ppm)")
        };

        this.name = "Reef Iodide";
        this.comment = "Considerations: If necessary, adjust amount so that iodide reads 0.06–0.08 mg/L 6–12 hours after the last dose. Natural seawater has an iodine concentration of .06 mg/L; however, many corals and crustaceans can benefit from a slightly higher concentration. Make large adjustments slowly to avoid overshooting intended level.";
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

        double doseB = 0.500000 * volume * (desired - current);
        double doseA = doseB / Constants.CapmL;
        doseA = MathUtils.round(doseA * 10) / 10;
        doseB = MathUtils.round(doseB * 10) / 10;

        return new SeachemDosage[]{
                new SeachemDosage("Caps", doseA),
                new SeachemDosage("mL", doseB)
        };
    }
}
