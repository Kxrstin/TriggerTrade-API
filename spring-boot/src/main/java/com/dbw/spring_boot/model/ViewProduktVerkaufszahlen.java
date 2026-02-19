package com.dbw.spring_boot.model;

public class ViewProduktVerkaufszahlen {
    private String sku;
    private String name;
    private int gesamtVerkaufteMenge;
    private Double umsatz;
    private int anzahlBestellungen;

    public String getSku() {
        return sku;
    }
    public void setSku(String sku) {
        this.sku = sku;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public Double getUmsatz() {
        return umsatz;
    }

    public int getAnzahlBestellungen() {
        return anzahlBestellungen;
    }

    public int getGesamtVerkaufteMenge() {
        return gesamtVerkaufteMenge;
    }

    public void setAnzahlBestellungen(int anzahlBestellungen) {
        this.anzahlBestellungen = anzahlBestellungen;
    }

    public void setGesamtVerkaufteMenge(int gesamtVerkaufteMenge) {
        this.gesamtVerkaufteMenge = gesamtVerkaufteMenge;
    }

    public void setUmsatz(Double umsatz) {
        this.umsatz = umsatz;
    }


}
