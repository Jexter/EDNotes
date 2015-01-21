package android.alex.se.dangerousnotes.model;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;

/**
 * Created by atkin_000 on 2014-12-18.
 */
public class Station  implements Serializable {
    private Date lastVisited;
    private ArrayList<CommodityCategory> categories;
    private String name;

    public Station(ArrayList<CommodityCategory> categories, String name) {
        this.categories = categories;
        this.name = name;
        lastVisited = new Date();
        lastVisited.setTime(lastVisited.getTime()-(1000*60*60));
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
}
