package com.dbw.spring_boot.repositories;

import com.dbw.spring_boot.model.Mitarbeiter;
import com.dbw.spring_boot.model.ViewMitarbeiterBestellstatus;
import com.dbw.spring_boot.model.ViewMitarbeiterUebersicht;
import com.dbw.spring_boot.model.ViewProduktVerkaufszahlen;

import java.sql.*;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

public class MitarbeiterRepository {
    public static Connection getConnection() throws SQLException {
        return DriverManager.getConnection("jdbc:postgresql://localhost:5432/abschlussprojekt", "lordoftherows", "rows123");
    }

    public List<Mitarbeiter> findAll() {
        LinkedList<Mitarbeiter> mitarbeiter = new LinkedList<>();
        try (Connection con = getConnection();
             Statement statement = con.createStatement()) {
            try(ResultSet result = statement.executeQuery("SELECT * FROM mitarbeiter")) {
                while (result.next()) {
                    Mitarbeiter m = new Mitarbeiter();
                    m.setPersonalNr(result.getInt("personal_nr"));
                    m.setEmail(result.getString("email"));
                    m.setPasswort(result.getString("passwort"));
                    m.setVorname(result.getString("vorname"));
                    m.setNachname(result.getString("nachname"));
                    mitarbeiter.add(m);
                }
            }
        } catch(SQLException e) {
            System.out.println("SQLException: " + e.getMessage());
            return null;
        }
        return mitarbeiter;
    }

    public Mitarbeiter findByPersonalNr(int id) {
        try(Connection con = getConnection();
            PreparedStatement statement = con.prepareStatement("SELECT * FROM mitarbeiter WHERE personal_nr = ?")) {
            statement.setInt(1, id);
            try(ResultSet result = statement.executeQuery()) {
                result.next();
                Mitarbeiter mitarbeiter = new Mitarbeiter();
                mitarbeiter.setPersonalNr(result.getInt("personal_nr"));
                mitarbeiter.setEmail(result.getString("email"));
                mitarbeiter.setPasswort(result.getString("passwort"));
                mitarbeiter.setVorname(result.getString("vorname"));
                mitarbeiter.setNachname(result.getString("nachname"));
                return mitarbeiter;
            }
        } catch(SQLException e) {
            System.out.println("SQLException: " + e.getMessage());
        }
        return null;
    }

    public Mitarbeiter save(Mitarbeiter mitarbeiter) {
        try (Connection con = getConnection();
             PreparedStatement ps = con.prepareStatement("INSERT INTO mitarbeiter (vorname, nachname, email, passwort) VALUES (?,?,?,?)",
                     Statement.RETURN_GENERATED_KEYS)){
            ps.setString(1, mitarbeiter.getVorname());
            ps.setString(2, mitarbeiter.getNachname());
            ps.setString(3, mitarbeiter.getEmail());
            ps.setString(4, mitarbeiter.getPasswort());
            ps.executeUpdate();

            try (ResultSet rs = ps.getGeneratedKeys()) {
                if (rs.next()) {
                    int generatedId = rs.getInt(1);
                    mitarbeiter.setPersonalNr(generatedId);
                }
            }

        } catch(SQLException e) {
            System.out.println("SQLException: " + e.getMessage());
        }
        return mitarbeiter;
    }

    public int deleteByPersonalNr(int id) {
        try(Connection con = getConnection();
            PreparedStatement ps = con.prepareStatement("DELETE FROM mitarbeiter WHERE personal_nr = ?")) {
            ps.setInt(1, id);
            return ps.executeUpdate();

        } catch(SQLException e) {
            System.out.println("SQLException: " + e.getMessage());
        }
        return 0;
    }

    public List<ViewMitarbeiterUebersicht> getViewMitarbeiterUebersicht() {
        List<ViewMitarbeiterUebersicht> ergebnisse = new ArrayList<>();
        try(Connection con = getConnection();
            PreparedStatement ps = con.prepareStatement("SELECT * FROM v_mitarbeiter_uebersicht");
            ResultSet rs = ps.executeQuery()) {
            while(rs.next()) {
                ViewMitarbeiterUebersicht view = new ViewMitarbeiterUebersicht();
                view.setPersonalNr(rs.getInt("personal_nr"));
                view.setAnzahlVerwalteterBestellungen(rs.getInt("anzahl_verwalteter_bestellungen"));
                view.setAnzahlAngelegterProdukte(rs.getInt("anzahl_angelegter_produkte"));
                ergebnisse.add(view);
            }
        } catch(SQLException e) {
            System.out.println("SQLException: " + e.getMessage());
            return null;
        }
        return ergebnisse;
    }

    public List<ViewMitarbeiterBestellstatus> getViewMitarbeiterBestellstatus() {
        List<ViewMitarbeiterBestellstatus> ergebnisse = new ArrayList<>();
        try(Connection con = getConnection();
            PreparedStatement ps = con.prepareStatement("SELECT * FROM v_mitarbeiter_bestellstatus_uebersicht");
            ResultSet rs = ps.executeQuery()) {
            while(rs.next()) {
                ViewMitarbeiterBestellstatus view = new ViewMitarbeiterBestellstatus();
                view.setPersonalNr(rs.getInt("personal_nr"));
                view.setStatus(rs.getString("status"));
                view.setAnzahlBestellungen(rs.getInt("anzahl_bestellungen"));
                ergebnisse.add(view);
            }
        } catch(SQLException e) {
            System.out.println("SQLException: " + e.getMessage());
            return null;
        }
        return ergebnisse;
    }
}
