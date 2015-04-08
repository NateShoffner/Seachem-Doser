package com.nateshoffner.seachemdoser.core.model.products.planted;

import com.nateshoffner.seachemdoser.core.model.SeachemDosage;
import com.nateshoffner.seachemdoser.core.model.SeachemParameter;
import com.nateshoffner.seachemdoser.core.model.SeachemProduct;
import com.nateshoffner.seachemdoser.utils.Constants;
import com.nateshoffner.seachemdoser.utils.MathUtils;

public class FlourishIron implements SeachemProduct {

    private SeachemParameter[] parameters;
    private String comment;
    private String name;

    public FlourishIron() {
        this.parameters = new SeachemParameter[]{
                new SeachemParameter("Aquarium Volume", "US Gallons"),
                new SeachemParameter("Current Iron", "mg/L (ppm)"),
                new SeachemParameter("Desired Iron", "mg/L (ppm)")
        };

        this.name = "Flourish Iron";
        this.comment = "Considerations: We recommend maintaining an Fe level of about 0.10 mg/L. Iron is used quickly, so you will want to initially exceed 0.10 mg/L in order to prevent iron levels from falling below that level. For smaller doses, please note that each cap thread is about 1 mL";
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

        double doseB = volume / 50 * ((desired - current) * 20);
        double doseA = doseB / Constants.CapmL;
        doseA = MathUtils.round(doseA * 10) / 10;
        doseB = MathUtils.round(doseB * 10) / 10;

        return new SeachemDosage[]{
                new SeachemDosage("Caps", doseA),
                new SeachemDosage("mL", doseB)
        };
    }
}
