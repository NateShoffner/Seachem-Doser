package com.nateshoffner.seachemdoser.utils;

import java.math.BigDecimal;
import java.math.RoundingMode;

public class MathUtils {
    public static double round(double value) {
        BigDecimal bd = new BigDecimal(value);
        bd = bd.setScale(1, RoundingMode.HALF_EVEN);
        return bd.doubleValue();
    }
}
