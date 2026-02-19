package com.dbw.spring_boot.model;

public class ViewMitarbeiterBestellstatus {
    private int personalNr;
    private String status;
    private int anzahlBestellungen;

    public int getPersonalNr() {
        return personalNr;
    }

    public void setPersonalNr(int personalNr) {
        this.personalNr = personalNr;
    }

    public void setAnzahlBestellungen(int anzahlBestellungen) {
        this.anzahlBestellungen = anzahlBestellungen;
    }

    public int getAnzahlBestellungen() {
        return anzahlBestellungen;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}
