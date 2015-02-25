package myapps.alex.se.ednotes.model;

import java.io.Serializable;

/**
 * Created by atkin_000 on 2015-01-31.
 */
public class CommodityTradeRoute implements Serializable {
    private System fromSystem;
    private System toSystem;
    private Station fromStation;
    private Station toStation;
    private Commodity fromStationCommodity;
    private Commodity toStationCommodity;
    private Commodity backCommodity;

    private int profit;


    public System getFromSystem() {
        return fromSystem;
    }

    public void setFromSystem(System fromSystem) {
        this.fromSystem = fromSystem;
    }

    public System getToSystem() {
        return toSystem;
    }

    public void setToSystem(System toSystem) {
        this.toSystem = toSystem;
    }

    public Station getFromStation() {
        return fromStation;
    }

    public void setFromStation(Station fromStation) {
        this.fromStation = fromStation;
    }

    public Station getToStation() {
        return toStation;
    }

    public void setToStation(Station toStation) {
        this.toStation = toStation;
    }

    public Commodity getFromStationCommodity() {
        return fromStationCommodity;
    }

    public void setFromStationCommodity(Commodity fromStationCommodity) {
        this.fromStationCommodity = fromStationCommodity;
    }

    public Commodity getToStationCommodity() {
        return toStationCommodity;
    }

    public void setToStationCommodity(Commodity toStationCommodity) {
        this.toStationCommodity = toStationCommodity;
    }

    public int getProfit() {
        return profit;
    }

    public void setProfit(int profit) {
        this.profit = profit;
    }
}
