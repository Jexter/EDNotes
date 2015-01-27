package android.alex.se.dangerousnotes.model;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created by atkin_000 on 2014-12-18.
 */
public class CommodityCategory  implements Serializable {
    private ArrayList<Commodity> commodities;
    private String name;
    private int id;
    private HashMap<String, Object> misc;

    public CommodityCategory(ArrayList<Commodity> commodities, String name, int id) {
        this.commodities = commodities;
        this.name = name;
        this.id = id;
        misc = new HashMap<String, Object>();
    }

    public ArrayList<Commodity> getCommodities() {
        return commodities;
    }

    public void setCommodities(ArrayList<Commodity> commodities) {
        this.commodities = commodities;
    }

    public String getName() {
        return name;
    }

    public int getId() {
        return id;
    }

    public HashMap<String, Object> getMisc() {
        return misc;
    }

    public void setMisc(HashMap<String, Object> misc) {
        this.misc = misc;
    }

    public void setName(String name) {
        this.name = name;
    }
}
