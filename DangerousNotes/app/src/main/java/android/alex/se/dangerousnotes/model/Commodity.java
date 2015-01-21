package android.alex.se.dangerousnotes.model;

import java.io.Serializable;

/**
 * Created by atkin_000 on 2014-12-18.
 */
public class Commodity  implements Serializable {
    private String name;
    private int price;
    private Availability supply;

    public Commodity(String name, int price, Availability supply) {
        this.name = name;
        this.price = price;
        this.supply = supply;
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

    public Availability getSupply() {
        return supply;
    }

    public void setSupply(Availability supply) {
        this.supply = supply;
    }

}
