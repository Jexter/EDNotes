package android.alex.se.dangerousnotes.model;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;

/**
 * Created by atkin_000 on 2014-12-18.
 */
public class System  implements Serializable {
//    private ArrayList<Station> stations;
    private String name;
    private ArrayList<Station> stations;

    public System(ArrayList<Station> stations, String name) {
        this.stations = stations;
        this.name = name;
    }

    public ArrayList<Station> getStations() {
        return stations;
    }

    public void setStations(ArrayList<Station> stations) {
        this.stations = stations;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

}
