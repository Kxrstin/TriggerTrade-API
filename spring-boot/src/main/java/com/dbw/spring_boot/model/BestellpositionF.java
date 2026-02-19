package com.dbw.spring_boot.model;

public class BestellpositionF {
    private int positionsId;
    private int bestellungId;
    private Produkt produkt;
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

    public Produkt getProdukt() {
        return produkt;
    }

    public void setGesamtpreis(Double gesamtpreis) {
        this.gesamtpreis = gesamtpreis;
    }

    public void setProdukt (Produkt produkt) {
        this.produkt = produkt;
    }

}

