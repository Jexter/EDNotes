package android.alex.se.dangerousnotes.model;

import java.io.Serializable;

/**
 * Created by atkin_000 on 2014-12-18.
 */
public class Commodity  implements Serializable {
    private String name;
    private int price;
    private Availability availability;
    private int id;

    public Commodity(String name, int id) {
        this.name = name;
        this.id = id;
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
}
