package com.nateshoffner.seachemdoser.core.manager;

import com.nateshoffner.seachemdoser.core.model.SeachemProduct;
import com.nateshoffner.seachemdoser.core.model.SeachemProductType;
import com.nateshoffner.seachemdoser.core.model.products.gravel.Flourite;
import com.nateshoffner.seachemdoser.core.model.products.gravel.FlouriteBlack;
import com.nateshoffner.seachemdoser.core.model.products.gravel.FlouriteBlackSand;
import com.nateshoffner.seachemdoser.core.model.products.gravel.FlouriteDark;
import com.nateshoffner.seachemdoser.core.model.products.gravel.FlouriteRed;
import com.nateshoffner.seachemdoser.core.model.products.gravel.GrayCoast;
import com.nateshoffner.seachemdoser.core.model.products.gravel.KonaCoast;
import com.nateshoffner.seachemdoser.core.model.products.gravel.Merdian;
import com.nateshoffner.seachemdoser.core.model.products.gravel.Onyx;
import com.nateshoffner.seachemdoser.core.model.products.gravel.OnyxSand;
import com.nateshoffner.seachemdoser.core.model.products.gravel.PearlBeach;
import com.nateshoffner.seachemdoser.core.model.products.gravel.SilverShores;
import com.nateshoffner.seachemdoser.core.model.products.planted.AlkalineBuffer;
import com.nateshoffner.seachemdoser.core.model.products.planted.Equilibrium;
import com.nateshoffner.seachemdoser.core.model.products.planted.FlourishIron;
import com.nateshoffner.seachemdoser.core.model.products.planted.FlourishNitrogen;
import com.nateshoffner.seachemdoser.core.model.products.planted.FlourishPhosphorus;
import com.nateshoffner.seachemdoser.core.model.products.planted.FlourishPotassium;
import com.nateshoffner.seachemdoser.core.model.products.planted.LiquidAlkalineBuffer;
import com.nateshoffner.seachemdoser.core.model.products.reef.ReefAdvantageCalcium;
import com.nateshoffner.seachemdoser.core.model.products.reef.ReefAdvantageMagnesium;
import com.nateshoffner.seachemdoser.core.model.products.reef.ReefAdvantageStrontium;
import com.nateshoffner.seachemdoser.core.model.products.reef.ReefBuffer;
import com.nateshoffner.seachemdoser.core.model.products.reef.ReefBuilder;
import com.nateshoffner.seachemdoser.core.model.products.reef.ReefCalcium;
import com.nateshoffner.seachemdoser.core.model.products.reef.ReefCarbonate;
import com.nateshoffner.seachemdoser.core.model.products.reef.ReefComplete;
import com.nateshoffner.seachemdoser.core.model.products.reef.ReefIodide;
import com.nateshoffner.seachemdoser.core.model.products.reef.ReefStrontium;

import java.util.ArrayList;
import java.util.EnumSet;
import java.util.List;

public class SeachemManager {

    public static List<SeachemProduct> GetProducts() {
        return GetProducts(EnumSet.allOf(SeachemProductType.class));
    }

    public static List<SeachemProduct> GetProducts(SeachemProductType type) {
        return GetProducts(EnumSet.of(type));
    }

    public static List<SeachemProduct> GetProducts(EnumSet<SeachemProductType> type) {
        ArrayList<SeachemProduct> products = new ArrayList<>();

        if (type.contains(SeachemProductType.Gravel)) {
            products.add(new Flourite());
            products.add(new FlouriteBlack());
            products.add(new FlouriteBlackSand());
            products.add(new FlouriteDark());
            products.add(new FlouriteRed());
            products.add(new GrayCoast());
            products.add(new KonaCoast());
            products.add(new Merdian());
            products.add(new Onyx());
            products.add(new OnyxSand());
            products.add(new PearlBeach());
            products.add(new SilverShores());
        }

        if (type.contains(SeachemProductType.Planted)) {
            products.add(new AlkalineBuffer());
            products.add(new Equilibrium());
            products.add(new FlourishIron());
            products.add(new FlourishNitrogen());
            products.add(new FlourishPhosphorus());
            products.add(new FlourishPotassium());
            products.add(new LiquidAlkalineBuffer());
        }

        if (type.contains(SeachemProductType.Reef)) {
            products.add(new ReefAdvantageCalcium());
            products.add(new ReefAdvantageMagnesium());
            products.add(new ReefAdvantageStrontium());
            products.add(new ReefBuffer());
            products.add(new ReefBuilder());
            products.add(new ReefCalcium());
            products.add(new ReefCarbonate());
            products.add(new ReefComplete());
            products.add(new ReefIodide());
            products.add(new ReefStrontium());
        }

        return products;
    }

    public static List<SeachemProductType> GetProductTypes() {
        ArrayList<SeachemProductType> types = new ArrayList<SeachemProductType>();

        for (SeachemProductType type : SeachemProductType.values()) {
            types.add(type);
        }
        return types;
    }

}
