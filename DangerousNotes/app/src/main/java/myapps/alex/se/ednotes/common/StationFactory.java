package myapps.alex.se.ednotes.common;

import java.util.ArrayList;

import myapps.alex.se.ednotes.model.Commodity;
import myapps.alex.se.ednotes.model.CommodityCategory;
import myapps.alex.se.ednotes.model.Station;

/**
 * Created by atkinson on 19/12/14.
 */
public class StationFactory {

    public static Station createNewStationWithMarket(String stationName) {

        ArrayList<Commodity> chemicals = new ArrayList<Commodity>();
        chemicals.add(new Commodity("EXPLOSIVES", 1));
        chemicals.add(new Commodity("HYDROGEN FUEL", 2));
        chemicals.add(new Commodity("MINERAL OIL", 3));
        chemicals.add(new Commodity("PESTICIDES", 4));

        ArrayList<Commodity> consumerItems = new ArrayList<Commodity>();
        consumerItems.add(new Commodity("CLOTHING", 5));
        consumerItems.add(new Commodity("CONSUMER TECHNOLOGY", 6));
        consumerItems.add(new Commodity("DOMESTIC APPLIANCES", 7));

        ArrayList<Commodity> foods = new ArrayList<Commodity>();
        foods.add(new Commodity("ALGAE", 8));
        foods.add(new Commodity("ANIMAL MEAT", 9));
        foods.add(new Commodity("COFFEE", 10));
        foods.add(new Commodity("FISH", 11));
        foods.add(new Commodity("FOOD CARTRIDGES", 12));
        foods.add(new Commodity("FRUIT AND VEGETABLES", 13));
        foods.add(new Commodity("GRAIN", 14));
        foods.add(new Commodity("SYNTHETIC MEAT", 15));
        foods.add(new Commodity("TEA", 16));

        ArrayList<Commodity> industrialMaterials = new ArrayList<Commodity>();
        industrialMaterials.add(new Commodity("POLYMERS", 17));
        industrialMaterials.add(new Commodity("SEMICONDUCTORS", 19));
        industrialMaterials.add(new Commodity("SUPERCONDUCTORS", 18));

        ArrayList<Commodity> legalDrugs = new ArrayList<Commodity>();
        legalDrugs.add(new Commodity("BEER", 20));
        legalDrugs.add(new Commodity("LIQUOR", 21));
        legalDrugs.add(new Commodity("NARCOTICS", 22));
        legalDrugs.add(new Commodity("TOBACCO", 23));
        legalDrugs.add(new Commodity("WINE", 24));

        ArrayList<Commodity> machinery = new ArrayList<Commodity>();
        machinery.add(new Commodity("ATMOSPHERIC PROCESSORS", 25));
        machinery.add(new Commodity("CROP HARVESTERS", 26));
        machinery.add(new Commodity("MARINE EQUIPMENT", 27));
        machinery.add(new Commodity("MICROBIAL FURNACES", 28));
        machinery.add(new Commodity("MINERAL EXTRACTORS", 29));
        machinery.add(new Commodity("POWER GENERATORS", 30));
        machinery.add(new Commodity("WATER PURIFIERS", 31));

        ArrayList<Commodity> medicines = new ArrayList<Commodity>();
        medicines.add(new Commodity("AGRI-MEDICINES", 32));
        medicines.add(new Commodity("BASIC MEDICINES", 33));
        medicines.add(new Commodity("COMBAT STABILISERS", 34));
        medicines.add(new Commodity("PERFORMANCE ENHANCERS", 35));
        medicines.add(new Commodity("PROGENITOR CELLS", 36));

        ArrayList<Commodity> metals = new ArrayList<Commodity>();
        metals.add(new Commodity("ALUMINIUM", 37));
        metals.add(new Commodity("BERYLLIUM", 38));
        metals.add(new Commodity("COBALT", 39));
        metals.add(new Commodity("COPPER", 40));
        metals.add(new Commodity("GALLIUM", 41));
        metals.add(new Commodity("GOLD", 42));
        metals.add(new Commodity("INDIUM", 43));
        metals.add(new Commodity("LITHIUM", 44));
        metals.add(new Commodity("PALLADIUM", 45));
        metals.add(new Commodity("PLATINUM", 46));
        metals.add(new Commodity("SILVER", 47));
        metals.add(new Commodity("TANTALUM", 48));
        metals.add(new Commodity("TITANIUM", 49));
        metals.add(new Commodity("URANIUM", 50));

        ArrayList<Commodity> minerals = new ArrayList<Commodity>();
        minerals.add(new Commodity("BAUXITE", 51));
        minerals.add(new Commodity("BERTRANDITE", 52));
        minerals.add(new Commodity("COLTAN", 53));
        minerals.add(new Commodity("GALLITE", 54));
        minerals.add(new Commodity("INDITE", 55));
        minerals.add(new Commodity("LEPIDOLITE", 56));
        minerals.add(new Commodity("OSMIUM", 92));
        minerals.add(new Commodity("PAINITE", 88));
        minerals.add(new Commodity("RUTILE", 57));
        minerals.add(new Commodity("URANITE", 58));

        ArrayList<Commodity> slavery = new ArrayList<Commodity>();
        slavery.add(new Commodity("IMPERIAL SLAVES", 59));
        slavery.add(new Commodity("SLAVES", 60));

        ArrayList<Commodity> technology = new ArrayList<Commodity>();
        technology.add(new Commodity("ADVANCED CATALYSERS", 61));
        technology.add(new Commodity("ANIMAL MONITORS", 62));
        technology.add(new Commodity("AQUAPONIC SYSTEMS", 63));
        technology.add(new Commodity("AUTO FABRICATORS", 64));
        technology.add(new Commodity("BIO REDUCING LICHEN", 65));
        technology.add(new Commodity("COMPUTER COMPONENTS", 66));
        technology.add(new Commodity("H.E. SUITS", 67));
        technology.add(new Commodity("LAND ENRICHMENT SYSTEMS", 68));
        technology.add(new Commodity("RESONATING SEPARATORS", 69));
        technology.add(new Commodity("ROBOTICS", 70));

        ArrayList<Commodity> textiles = new ArrayList<Commodity>();
        textiles.add(new Commodity("LEATHER", 71));
        textiles.add(new Commodity("NATURAL FABRICS", 72));
        textiles.add(new Commodity("SYNTHETIC FABRICS", 73));

        ArrayList<Commodity> waste = new ArrayList<Commodity>();
        waste.add(new Commodity("BIOWASTE", 74));
        waste.add(new Commodity("CHEMICAL WASTE", 75));
        waste.add(new Commodity("SCRAP", 76));

        ArrayList<Commodity> weapons = new ArrayList<Commodity>();
        weapons.add(new Commodity("BATTLE WEAPONS", 77));
        weapons.add(new Commodity("NON-LETHAL WEAPONS", 78));
        weapons.add(new Commodity("PERSONAL WEAPONS", 79));
        weapons.add(new Commodity("REACTIVE ARMOUR", 80));

        ArrayList<Commodity> salvage = new ArrayList<Commodity>();
        salvage.add(new Commodity("AI RELICS", 93));
        salvage.add(new Commodity("ANCIENT ARTIFACTS", 81));
        salvage.add(new Commodity("ANTIQUITIES", 95));
        salvage.add(new Commodity("BLACK BOX", 82));
        salvage.add(new Commodity("EXPERIMENTAL CHEMICALS", 83));
        salvage.add(new Commodity("MILITARY PLANS", 84));
        salvage.add(new Commodity("LIMPET", 89));
        salvage.add(new Commodity("PROTOTYPE TECH", 85));
        salvage.add(new Commodity("RARE ARTWORK", 86));
        salvage.add(new Commodity("REBEL TRANSMISSIONS", 90));
        salvage.add(new Commodity("SAP 8 CORE CONTAINER", 94));
        salvage.add(new Commodity("TECHNICAL BLUEPRINTS", 91));
        salvage.add(new Commodity("TRADE DATA", 87));

        CommodityCategory chemicalsCat = new CommodityCategory(chemicals, "CHEMICALS", 1);
        CommodityCategory consumerItemsCat = new CommodityCategory(consumerItems, "CONSUMER ITEMS", 2);
        CommodityCategory foodsCat = new CommodityCategory(foods, "FOODS", 3);
        CommodityCategory industrialMaterialsCat = new CommodityCategory(industrialMaterials, "INDUSTRIAL MATERIALS", 4);
        CommodityCategory legalDrugsCat = new CommodityCategory(legalDrugs, "LEGAL DRUGS", 5);
        CommodityCategory machineryCat = new CommodityCategory(machinery, "MACHINERY", 6);
        CommodityCategory medicinesCat = new CommodityCategory(medicines, "MEDICINE", 7);
        CommodityCategory metalsCat = new CommodityCategory(metals, "METALS", 8);
        CommodityCategory mineralsCat = new CommodityCategory(minerals, "MINERALS", 9);
        CommodityCategory slaveryCat = new CommodityCategory(slavery, "SLAVERY", 10);
        CommodityCategory technologyCat = new CommodityCategory(technology, "TECHNOLOGY", 11);
        CommodityCategory textilesCat = new CommodityCategory(textiles, "TEXTILES", 12);
        CommodityCategory wasteCat = new CommodityCategory(waste, "WASTE", 13);
        CommodityCategory weaponsCat = new CommodityCategory(weapons, "WEAPONS", 14);
        CommodityCategory salvageCat = new CommodityCategory(salvage, "SALVAGE", 15);

        ArrayList<CommodityCategory> categories = new ArrayList<CommodityCategory>();
        categories.add(chemicalsCat);
        categories.add(consumerItemsCat);
        categories.add(foodsCat);
        categories.add(industrialMaterialsCat);
        categories.add(legalDrugsCat);
        categories.add(machineryCat);
        categories.add(medicinesCat);
        categories.add(metalsCat);
        categories.add(mineralsCat);
        categories.add(slaveryCat);
        categories.add(technologyCat);
        categories.add(textilesCat);
        categories.add(wasteCat);
        categories.add(weaponsCat);
        categories.add(salvageCat);


        Station newStation = new Station(categories, stationName);

        return newStation;
    }


}
