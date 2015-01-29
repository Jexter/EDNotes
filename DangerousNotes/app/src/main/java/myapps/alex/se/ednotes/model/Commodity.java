package myapps.alex.se.ednotes.model;

import java.io.Serializable;
import java.util.HashMap;

/**
 * Created by atkin_000 on 2014-12-18.
 */
public class Commodity  implements Serializable {
    private String name;
    private int price;
    private Availability availability;
    private int id;
    private String notes;
    private HashMap<String, Object> misc;

    public Commodity(String name, int id) {
        this.name = name;
        this.id = id;
        misc = new HashMap<String, Object>();
    }

    public HashMap<String, Object> getMisc() {
        return misc;
    }

    public void setMisc(HashMap<String, Object> misc) {
        this.misc = misc;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getPrice() {
        return price;
    }

    public void setPrice(int price) {
        this.price = price;
    }

    public Availability getAvailability() {
        return availability;
    }

    public void setAvailability(Availability availability) {
        this.availability = availability;
    }

    public int getId() {
        return id;
    }

    public String getNotes() {
        return notes;
    }

    public void setNotes(String notes) {
        this.notes = notes;
    }
}
