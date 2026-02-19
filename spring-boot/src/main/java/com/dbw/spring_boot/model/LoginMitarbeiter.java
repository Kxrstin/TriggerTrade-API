package com.dbw.spring_boot.model;

public class LoginMitarbeiter {
    private Integer personalNr;
    private String passwort;

    public void setPersonalNr(Integer personalNr) {
        this.personalNr = personalNr;
    }

    public void setPasswort(String passwort) {
        this.passwort = passwort;
    }

    public Integer getPersonalNr() {
        return personalNr;
    }

    public String getPasswort() {
        return passwort;
    }
}
