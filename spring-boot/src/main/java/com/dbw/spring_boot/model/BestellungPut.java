package com.dbw.spring_boot.model;

import com.fasterxml.jackson.annotation.JsonFormat;

import java.time.OffsetDateTime;

public class BestellungPut {
    private int kunde_id;
    private int personal_nr;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ssX")
    private OffsetDateTime datum;
    private String status;


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

    public int getPersonal_nr() {
        return personal_nr;
    }

    public void setPersonal_nr(int personalNr) {
        this.personal_nr = personalNr;
    }

    public int getKunde_id() {
        return kunde_id;
    }

    public void setKunde_id(int kundeId) {
        this.kunde_id = kundeId;
    }
}
