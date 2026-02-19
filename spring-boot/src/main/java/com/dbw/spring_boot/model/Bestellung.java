package com.dbw.spring_boot.model;

import com.fasterxml.jackson.annotation.JsonFormat;

import java.time.OffsetDateTime;
import java.util.LinkedList;
import java.util.List;

public class Bestellung {
    private Integer bestellungId;
    private int kundeId;
    private int personalnr;
    private OffsetDateTime datum;
    private String status;
    private List<BestellpositionF> positionen = new LinkedList<>();

    public Integer getBestellungId() {
        return bestellungId;
    }

    public void setBestellungId(Integer bestellungId) {
        this.bestellungId = bestellungId;
    }

    public OffsetDateTime getDatum() {
        return datum;
    }

    public void setDatum(OffsetDateTime datum) {
        this.datum = datum;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public int getPersonalnr() {
        return personalnr;
    }

    public void setPersonalnr(int personalnr) {
        this.personalnr = personalnr;
    }

    public int getKundeId() {
        return kundeId;
    }

    public void setKundeId(int kundeId) {
        this.kundeId = kundeId;
    }

    public List<BestellpositionF> getPositionen() {
        return positionen;
    }

    public void setPositionen(List<BestellpositionF> positionen) {
        this.positionen = positionen;
    }

    public void addPositionen(BestellpositionF bp) {
        positionen.add(bp);
    }
}
