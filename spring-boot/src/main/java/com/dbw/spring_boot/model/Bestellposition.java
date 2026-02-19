package com.dbw.spring_boot.model;

public class Bestellposition {
    private int positionsId;
    private int bestellungId;
    private String produktSku;
    private int menge;
    private Double gesamtpreis;

    public int getPositionsId() {
        return positionsId;
    }
    public void setPositionsId(int positionsId) {
        this.positionsId = positionsId;
    }

    public int getMenge() {
        return menge;
    }

    public void setMenge(int menge) {
        this.menge = menge;
    }

    public int getBestellungId() {
        return bestellungId;
    }

    public void setBestellungId(int bestellungId) {
        this.bestellungId = bestellungId;
    }

    public Double getGesamtpreis() {
        return gesamtpreis;
    }

    public String getProduktSku() {
        return produktSku;
    }

    public void setGesamtpreis(Double gesamtpreis) {
        this.gesamtpreis = gesamtpreis;
    }

    public void setProduktSku(String produktSku) {
        this.produktSku = produktSku;
    }

}
