package com.dbw.spring_boot.model;

public class BestellpositionPut {
    private int bestellung_id;
    private String sku;
    private int menge;

    public BestellpositionPut(int bestellung_id, String sku, int menge) {
        this.bestellung_id = bestellung_id;
        this.sku = sku;
        this.menge = menge;
    }

    public int getBestellung_id() {
        return bestellung_id;
    }

    public void setBestellung_id(int bestellung_id) {
        this.bestellung_id = bestellung_id;
    }

    public String getSku() {
        return sku;
    }

    public void setSku(String sku) {
        this.sku = sku;
    }

    public int getMenge() {
        return menge;
    }

    public void setMenge(int menge) {
        this.menge = menge;
    }
}
