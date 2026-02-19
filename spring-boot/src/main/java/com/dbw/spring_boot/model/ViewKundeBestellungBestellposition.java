package com.dbw.spring_boot.model;

public class ViewKundeBestellungBestellposition {
    private Integer kundeId;
    private String email;
    private int anzahlBestellungen;
    private Double gesamtsumme;

    public void setEmail(String email) {
        this.email = email;
    }

    public String getEmail() {
        return email;
    }

    public void setAnzahlBestellungen(int anzahlBestellungen) {
        this.anzahlBestellungen = anzahlBestellungen;
    }

    public int getAnzahlBestellungen() {
        return anzahlBestellungen;
    }

    public void setGesamtsumme(Double gesamtsumme) {
        this.gesamtsumme = gesamtsumme;
    }

    public Double getGesamtsumme() {
        return gesamtsumme;
    }

    public void setKundeId(Integer kundeId) {
        this.kundeId = kundeId;
    }

    public Integer getKundeId() {
        return kundeId;
    }
}
