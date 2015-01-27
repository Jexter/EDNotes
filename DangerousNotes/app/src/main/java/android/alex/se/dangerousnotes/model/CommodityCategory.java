package android.alex.se.dangerousnotes.model;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * Created by atkin_000 on 2014-12-18.
 */
public class CommodityCategory  implements Serializable {
    private ArrayList<Commodity> commodities;
    private String name;
    private int id;

    public CommodityCategory(ArrayList<Commodity> commodities, String name, int id) {
        this.commodities = commodities;
        this.name = name;
        this.id = id;
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

    public void setName(String name) {
        this.name = name;
    }
}
