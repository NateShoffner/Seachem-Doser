package com.nateshoffner.seachemdoser.core.manager;

import com.nateshoffner.seachemdoser.core.model.SeachemProduct;
import com.nateshoffner.seachemdoser.core.model.SeachemProductCategory;
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

    private static HashMap<SeachemProductCategory, List<SeachemProduct>> ProductMap;

    public static List<SeachemProduct> GetProducts() {
        return GetProducts(EnumSet.allOf(SeachemProductCategory.class));
    }

    public static List<SeachemProduct> GetProducts(SeachemProductCategory category) {
        return GetProducts(EnumSet.of(category));
    }

    public static List<SeachemProduct> GetProducts(EnumSet<SeachemProductCategory> categories) {
        InitializeProducts();

        ArrayList<SeachemProduct> products = new ArrayList<>();
        for (SeachemProductCategory category : categories) {
            products.addAll(ProductMap.get(category));
        }
        return products;
    }

    public static List<SeachemProductCategory> GetProductCategories() {
        ArrayList<SeachemProductCategory> categories = new ArrayList<>();
        Collections.addAll(categories, SeachemProductCategory.values());
        return categories;
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

    public static SeachemProductCategory getProductCategory(SeachemProduct product) {
        for (Map.Entry<SeachemProductCategory, List<SeachemProduct>> e : ProductMap.entrySet()) {
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
            ProductMap.put(SeachemProductCategory.Gravel, gravel);

            List<SeachemProduct> planted = new ArrayList<>();
            planted.add(new AlkalineBuffer());
            planted.add(new Equilibrium());
            planted.add(new FlourishIron());
            planted.add(new FlourishNitrogen());
            planted.add(new FlourishPhosphorus());
            planted.add(new FlourishPotassium());
            planted.add(new FlourishTrace());
            planted.add(new LiquidAlkalineBuffer());
            ProductMap.put(SeachemProductCategory.Planted, planted);

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
            ProductMap.put(SeachemProductCategory.Reef, reef);
        }
    }
}
