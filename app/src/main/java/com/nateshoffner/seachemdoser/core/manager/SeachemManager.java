package com.nateshoffner.seachemdoser.core.manager;

import com.nateshoffner.seachemdoser.core.model.SeachemProduct;
import com.nateshoffner.seachemdoser.core.model.SeachemProductType;
import com.nateshoffner.seachemdoser.core.model.products.gravel.*;
import com.nateshoffner.seachemdoser.core.model.products.planted.*;
import com.nateshoffner.seachemdoser.core.model.products.reef.*;

import java.util.ArrayList;
import java.util.Collections;
import java.util.EnumSet;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class SeachemManager {

    private static HashMap<SeachemProductType, List<SeachemProduct>> ProductMap;

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
            products.addAll(ProductMap.get(t));
        }
        return products;
    }

    public static List<SeachemProductType> GetProductTypes() {
        ArrayList<SeachemProductType> types = new ArrayList<>();
        Collections.addAll(types, SeachemProductType.values());
        return types;
    }

    public static SeachemProduct getProductByName(String name) {
        InitializeProducts();
        for (List<SeachemProduct> products : ProductMap.values()) {
            for (SeachemProduct product : products) {
                if (product.getName().equals(name)) {
                    return product;
                }
            }
        }

        return null;
    }

    public static SeachemProductType getProductType(SeachemProduct product) {
        for (Map.Entry<SeachemProductType, List<SeachemProduct>> e : ProductMap.entrySet()) {
            if (e.getValue().contains(product))
                return e.getKey();
        }

        return null;
    }

    private static void InitializeProducts() {
        if (ProductMap == null) {
            ProductMap = new HashMap<>();

            List<SeachemProduct> gravel = new ArrayList<>();
            gravel.add(new Flourite());
            gravel.add(new FlouriteBlack());
            gravel.add(new FlouriteBlackSand());
            gravel.add(new FlouriteDark());
            gravel.add(new FlouriteRed());
            gravel.add(new FlouriteSand());
            gravel.add(new GrayCoast());
            gravel.add(new KonaCoast());
            gravel.add(new Meridian());
            gravel.add(new Onyx());
            gravel.add(new OnyxSand());
            gravel.add(new PearlBeach());
            gravel.add(new SilverShores());
            ProductMap.put(SeachemProductType.Gravel, gravel);

            List<SeachemProduct> planted = new ArrayList<>();
            planted.add(new AlkalineBuffer());
            planted.add(new Equilibrium());
            planted.add(new FlourishIron());
            planted.add(new FlourishNitrogen());
            planted.add(new FlourishPhosphorus());
            planted.add(new FlourishPotassium());
            planted.add(new FlourishTrace());
            planted.add(new LiquidAlkalineBuffer());
            ProductMap.put(SeachemProductType.Planted, planted);

            List<SeachemProduct> reef = new ArrayList<>();
            reef.add(new ReefAdvantageCalcium());
            reef.add(new ReefAdvantageMagnesium());
            reef.add(new ReefAdvantageStrontium());
            reef.add(new ReefBuffer());
            reef.add(new ReefBuilder());
            reef.add(new ReefCalcium());
            reef.add(new ReefCarbonate());
            reef.add(new ReefComplete());
            reef.add(new ReefFusion1());
            reef.add(new ReefFusion2());
            reef.add(new ReefIodide());
            reef.add(new ReefStrontium());
            ProductMap.put(SeachemProductType.Reef, reef);
        }
    }
}
