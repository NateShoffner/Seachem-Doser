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
import java.util.Dictionary;
import java.util.EnumSet;
import java.util.List;

public class SeachemManager {

    private static Dictionary<SeachemProductType, List<SeachemProduct>> Products;

    public static List<SeachemProduct> GetProducts() {
        return GetProducts(EnumSet.allOf(SeachemProductType.class));
    }

    public static List<SeachemProduct> GetProducts(SeachemProductType type) {
        return GetProducts(EnumSet.of(type));
    }

    public static List<SeachemProduct> GetProducts(EnumSet<SeachemProductType> type) {
        InitializeProducts();

        ArrayList<SeachemProduct> products = new ArrayList<>();
        for (SeachemProductType t : type) {
            products.addAll(Products.get(t));
        }
        return products;
    }

    public static List<SeachemProductType> GetProductTypes() {
        ArrayList<SeachemProductType> types = new ArrayList<>();

        for (SeachemProductType type : SeachemProductType.values()) {
            types.add(type);
        }
        return types;
    }

    private static void InitializeProducts() {
        if (Products == null) {
            List<SeachemProduct> gravel = new ArrayList<>();
            gravel.add(new Flourite());
            gravel.add(new FlouriteBlack());
            gravel.add(new FlouriteBlackSand());
            gravel.add(new FlouriteDark());
            gravel.add(new FlouriteRed());
            gravel.add(new GrayCoast());
            gravel.add(new KonaCoast());
            gravel.add(new Merdian());
            gravel.add(new Onyx());
            gravel.add(new OnyxSand());
            gravel.add(new PearlBeach());
            gravel.add(new SilverShores());
            Products.put(SeachemProductType.Gravel, gravel);

            List<SeachemProduct> planted = new ArrayList<>();
            planted.add(new AlkalineBuffer());
            planted.add(new Equilibrium());
            planted.add(new FlourishIron());
            planted.add(new FlourishNitrogen());
            planted.add(new FlourishPhosphorus());
            planted.add(new FlourishPotassium());
            planted.add(new LiquidAlkalineBuffer());
            Products.put(SeachemProductType.Planted, planted);

            List<SeachemProduct> reef = new ArrayList<>();
            reef.add(new ReefAdvantageCalcium());
            reef.add(new ReefAdvantageMagnesium());
            reef.add(new ReefAdvantageStrontium());
            reef.add(new ReefBuffer());
            reef.add(new ReefBuilder());
            reef.add(new ReefCalcium());
            reef.add(new ReefCarbonate());
            reef.add(new ReefComplete());
            reef.add(new ReefIodide());
            reef.add(new ReefStrontium());
            Products.put(SeachemProductType.Reef, gravel);
        }
    }
}
