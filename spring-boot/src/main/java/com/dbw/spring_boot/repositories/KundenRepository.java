package com.dbw.spring_boot.repositories;

import com.dbw.spring_boot.model.Adresse;
import com.dbw.spring_boot.model.Kunde;
import com.dbw.spring_boot.model.KundeHatAdressen;
import com.dbw.spring_boot.model.ViewKundeBestellungBestellposition;

import java.sql.*;
import java.util.*;

public class KundenRepository {
    public static Connection getConnection() throws SQLException {
        return DriverManager.getConnection("jdbc:postgresql://localhost:5432/abschlussprojekt", "lordoftherows", "rows123");
    }

    public Kunde save(Kunde kunde) {
        try(Connection con = getConnection();
            PreparedStatement ps = con.prepareStatement("INSERT INTO kunde (vorname, nachname, email, passwort) VALUES (?,?,?,?)",
                    Statement.RETURN_GENERATED_KEYS)) {
            ps.setString(1, kunde.getVorname());
            ps.setString(2, kunde.getNachname());
            ps.setString(3, kunde.getEmail());
            ps.setString(4, kunde.getPasswort());
            ps.executeUpdate();

            try (ResultSet rs = ps.getGeneratedKeys()) {
                if (rs.next()) {
                    int generatedId = rs.getInt(1);
                    kunde.setKundeId(generatedId);
                    return kunde;
                }
            }
        } catch(SQLException e) {
            System.out.println("SQLException: " + e.getMessage());
            return null;
        }
        return null;
    }

    public Kunde findByEmail(String email) {
        List<Kunde> kunden = findAllOrByIdWithAdresse(null);
        return kunden.stream().filter(k -> k.getEmail().equals(email)).findFirst().orElse(null);
    }

    public List<Kunde> findAllOrByIdWithAdresse(Integer id) {
        String sql = "SELECT k.kunde_id, k.email, k.vorname, k.nachname, k.passwort, " +
                "a.adresse_id, a.strasse, a.hausnummer, a.plz, a.ort, a.land, a.aktiv, " +
                "kha.typ FROM kunde k LEFT JOIN kunde_hat_adressen kha ON k.kunde_id = kha.kunde_id " +
                "LEFT JOIN adresse a ON kha.adresse_id = a.adresse_id " +
                (id != null ? "WHERE k.kunde_id = ?" : "ORDER BY k.kunde_id");

        Map<Integer, Kunde> kunden = new LinkedHashMap<>();

        try (Connection con = getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            if (id != null) {
                ps.setInt(1, id);
            }
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    int kundeId = rs.getInt("kunde_id");

                    Kunde kunde = kunden.get(kundeId);
                    if (kunde == null) {
                        kunde = new Kunde();
                        kunde.setKundeId(kundeId);
                        kunde.setEmail(rs.getString("email"));
                        kunde.setVorname(rs.getString("vorname"));
                        kunde.setNachname(rs.getString("nachname"));
                        kunde.setPasswort(rs.getString("passwort"));
                        kunden.put(kundeId, kunde);
                    }

                    int adresseId = rs.getInt("adresse_id");
                    if (!rs.wasNull()) {
                        Adresse adresse = new Adresse();
                        adresse.setAdresseId(adresseId);
                        adresse.setAktiv(rs.getBoolean("aktiv"));
                        adresse.setStrasse(rs.getString("strasse"));
                        adresse.setHausnummer(rs.getString("hausnummer"));
                        adresse.setPlz(rs.getString("plz"));
                        adresse.setOrt(rs.getString("ort"));
                        adresse.setLand(rs.getString("land"));

                        KundeHatAdressen kha = new KundeHatAdressen(adresse, rs.getString("typ"));
                        kunde.getAdressen().add(kha);
                    }
                }
            }

        } catch (SQLException e) {
            System.out.println("SQLException: " + e.getMessage());
            return null;
        }

        return new ArrayList<>(kunden.values());
    }

    public int updateKunde(Kunde kunde) {
        try(Connection con = getConnection();
            PreparedStatement ps = con.prepareStatement("UPDATE kunde SET vorname = ?, nachname = ?, email = ?, passwort = ? WHERE kunde_id = ?")) {
            ps.setString(1, kunde.getVorname());
            ps.setString(2, kunde.getNachname());
            ps.setString(3, kunde.getEmail());
            ps.setString(4, kunde.getPasswort());
            ps.setInt(5, kunde.getKundeId());
            return ps.executeUpdate();

        } catch(SQLException e) {
            System.out.println("SQLException: " + e.getMessage());
        }
        return 0;
    }

    public List<ViewKundeBestellungBestellposition> getViewsummeAnzahlBestellungen() {
        List<ViewKundeBestellungBestellposition> ergebnisse = new ArrayList<>();
        try(Connection con = getConnection();
            PreparedStatement ps = con.prepareStatement("SELECT * FROM v_kunde_summe_anzahl_bestellungen");
            ResultSet rs = ps.executeQuery()) {
            while(rs.next()) {
                ViewKundeBestellungBestellposition view = new ViewKundeBestellungBestellposition();
                view.setKundeId(rs.getInt("kunde_id"));
                view.setEmail(rs.getString("email"));
                view.setAnzahlBestellungen(rs.getInt("anzahlBestellungen"));
                view.setGesamtsumme(rs.getDouble("gesamtsumme"));
                ergebnisse.add(view);
            }
        } catch(SQLException e) {
            System.out.println("SQLException: " + e.getMessage());
            return null;
        }
        return ergebnisse;
    }

    /*
    public int deleteByKundeId(int id) {
        int rows = 0;
        try(Connection con = getConnection();
            PreparedStatement ps = con.prepareStatement("DELETE FROM kunde WHERE kunde_id = ?")) {
            ps.setInt(1, id);
            rows = ps.executeUpdate();

        } catch(SQLException e) {
            System.out.println("SQLException: " + e.getMessage());
        }
        return rows;
    }
    */
}

