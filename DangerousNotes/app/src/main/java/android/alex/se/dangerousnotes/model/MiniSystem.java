package android.alex.se.dangerousnotes.model;

import java.io.Serializable;
import java.util.Date;

/**
 * Created by atkin_000 on 2014-12-18.
 */
public class MiniSystem implements Serializable {
//    private ArrayList<Station> stations;
    private String name;
    private Date lastVisited;
    private int stationCount;

    public MiniSystem(String name) {
        this.name = name;
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
}
