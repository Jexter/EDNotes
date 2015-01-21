package android.alex.se.dangerousnotes.common;

import android.alex.se.dangerousnotes.model.Availability;
import android.alex.se.dangerousnotes.model.Commodity;
import android.alex.se.dangerousnotes.model.CommodityCategory;
import android.alex.se.dangerousnotes.model.Station;

import java.util.ArrayList;

/**
 * Created by atkinson on 19/12/14.
 */
public class StationFactory {

    public static Station createNewStationWithMarket(String stationName) {

        ArrayList<Commodity> comList1 = new ArrayList<Commodity>();
        ArrayList<Commodity> comList2 = new ArrayList<Commodity>();
        ArrayList<Commodity> comList3 = new ArrayList<Commodity>();

        for(int i = 0; i<10; i++) {
            comList1.add(new Commodity("FOODS " + i, 0, Availability.None));
            comList2.add(new Commodity("MINERAL EXTRACTORS " + i, 0, Availability.None));
            comList3.add(new Commodity("MINERALS " + i, 0, Availability.None));
        }

        CommodityCategory cat1 = new CommodityCategory(comList1, "FOODS");
        CommodityCategory cat2 = new CommodityCategory(comList2, "MACHINERY");
        CommodityCategory cat3 = new CommodityCategory(comList3, "MINERALS");

        ArrayList<CommodityCategory> catList1 = new ArrayList<CommodityCategory>();
        catList1.add(cat1);
        catList1.add(cat2);
        catList1.add(cat3);

        Station newStation = new Station(catList1, stationName);

        return newStation;
    }


}
