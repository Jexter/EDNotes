package android.alex.se.dangerousnotes.model;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created by atkin_000 on 2014-12-18.
 */
public class System  implements Serializable {
//    private ArrayList<Station> stations;
    private String name;
    private ArrayList<Station> stations;
    private String notes;
    private HashMap<String, Object> misc;


    public System(ArrayList<Station> stations, String name) {
        this.stations = stations;
        this.name = name;
        misc = new HashMap<String, Object>();
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

    public String getNotes() {
        return notes;
    }

    public HashMap<String, Object> getMisc() {
        return misc;
    }

    public void setMisc(HashMap<String, Object> misc) {
        this.misc = misc;
    }

    public void setNotes(String notes) {
        this.notes = notes;
    }
}
