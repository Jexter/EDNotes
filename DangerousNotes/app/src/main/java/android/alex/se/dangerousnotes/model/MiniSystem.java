package android.alex.se.dangerousnotes.model;

import java.io.Serializable;
import java.util.Date;
import java.util.HashMap;

/**
 * Created by atkin_000 on 2014-12-18.
 */
public class MiniSystem implements Serializable {
//    private ArrayList<Station> stations;
    private String name;
    private Date lastVisited;
    private int stationCount;
    private HashMap<String, Object> misc;

    public MiniSystem(String name) {
        this.name = name;
        misc = new HashMap<String, Object>();
    }

    public void setStationCount(int stationCount) {
        this.stationCount = stationCount;
        lastVisited = new Date();
    }

    public int getStationCount() {
        return stationCount;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Date getLastVisited() {
        return lastVisited;
    }

    public HashMap<String, Object> getMisc() {
        return misc;
    }

    public void setMisc(HashMap<String, Object> misc) {
        this.misc = misc;
    }
}
