package com.dbw.spring_boot.model;

public class ViewMitarbeiterUebersicht {
    private int personalNr;
    private int anzahlVerwalteterBestellungen;
    private int anzahlAngelegterProdukte;

    public void setPersonalNr(int personalNr) {
        this.personalNr = personalNr;
    }

    public int getAnzahlAngelegterProdukte() {
        return anzahlAngelegterProdukte;
    }

    public int getAnzahlVerwalteterBestellungen() {
        return anzahlVerwalteterBestellungen;
    }

    public int getPersonalNr() {
        return personalNr;
    }

    public void setAnzahlAngelegterProdukte(int anzahlAngelegterProdukte) {
        this.anzahlAngelegterProdukte = anzahlAngelegterProdukte;
    }

    public void setAnzahlVerwalteterBestellungen(int anzahlVerwalteterBestellungen) {
        this.anzahlVerwalteterBestellungen = anzahlVerwalteterBestellungen;
    }
}
