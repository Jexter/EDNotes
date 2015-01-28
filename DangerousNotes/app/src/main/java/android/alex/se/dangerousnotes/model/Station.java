package android.alex.se.dangerousnotes.model;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;

/**
 * Created by atkin_000 on 2014-12-18.
 */
public class Station  implements Serializable {
    private Date lastVisited;
    private ArrayList<CommodityCategory> categories;
    private String name;
    private String notes;
    private HashMap<String, Object> misc;

    public Station(ArrayList<CommodityCategory> categories, String name) {
        this.categories = categories;
        this.name = name;
        lastVisited = new Date();

        misc = new HashMap<String, Object>();
    }

    public HashMap<String, Object> getMisc() {
        return misc;
    }

    public void setMisc(HashMap<String, Object> misc) {
        this.misc = misc;
    }

    public ArrayList<CommodityCategory> getCategories() {
        return categories;
    }

    public void setCategories(ArrayList<CommodityCategory> categories) {
        this.categories = categories;
        lastVisited = new Date();
    }

    public Date getLastUpdated() {
        return lastVisited;
    }

    public void setLastUpdated(Date lastUpdated) {
        this.lastVisited = lastUpdated;
    }

    public Date getLastVisited() {
        return lastVisited;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getNotes() {
        return notes;
    }

    public void setNotes(String notes) {
        this.notes = notes;
    }
}
