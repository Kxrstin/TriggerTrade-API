package com.dbw.spring_boot.model;

import java.util.ArrayList;
import java.util.List;

public class Kunde {
    private Integer kundeId;
    private String email;
    private String vorname;
    private String nachname;
    private String passwort;

    private List<KundeHatAdressen> adressen = new ArrayList<KundeHatAdressen>();

    public Integer getKundeId() {
        return kundeId;
    }

    public void setKundeId(Integer kundeId) {
        this.kundeId = kundeId;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getVorname() {
        return vorname;
    }

    public void setVorname(String vorname) {
        this.vorname = vorname;
    }

    public String getNachname() {
        return nachname;
    }

    public void setNachname(String nachname) {
        this.nachname = nachname;
    }

    public String getPasswort() {
        return passwort;
    }

    public void setPasswort(String passwort) {
        this.passwort = passwort;
    }

    public List<KundeHatAdressen> getAdressen() {
        return adressen;
    }

    public void setAdressen(List<KundeHatAdressen> adressen) {
        this.adressen = adressen;
    }

    public void addAdresse(Adresse adresse, String typ) {
        adressen.add(new KundeHatAdressen(adresse, typ));
    }
}
