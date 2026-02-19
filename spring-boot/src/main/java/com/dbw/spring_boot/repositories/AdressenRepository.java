package com.dbw.spring_boot.repositories;

import com.dbw.spring_boot.model.Adresse;

import java.sql.*;
import java.util.LinkedList;
import java.util.List;

public class AdressenRepository {
    public static Connection getConnection() throws SQLException {
        return DriverManager.getConnection("jdbc:postgresql://localhost:5432/abschlussprojekt", "lordoftherows", "rows123");
    }

    public List<Adresse> findAll() {
        LinkedList<Adresse> adressen = new LinkedList<>();
        try (Connection con = getConnection();
            Statement statement = con.createStatement()) {
            try(ResultSet result = statement.executeQuery("SELECT * FROM adresse ORDER BY adresse_id")) {
                while (result.next()) {
                    Adresse adresse = new Adresse();
                    adresse.setAdresseId(result.getInt("adresse_id"));
                    adresse.setAktiv(result.getBoolean("aktiv"));
                    adresse.setStrasse(result.getString("strasse"));
                    adresse.setHausnummer(result.getString("hausnummer"));
                    adresse.setPlz(result.getString("plz"));
                    adresse.setOrt(result.getString("ort"));
                    adresse.setLand(result.getString("land"));
                    adressen.add(adresse);
                }
            }
        } catch(SQLException e) {
            System.out.println("SQLException: " + e.getMessage());
            return null;
        }
        return adressen;
    }

    public Adresse findById(int id) {
        try(Connection con = getConnection();
            PreparedStatement statement = con.prepareStatement("SELECT * FROM adresse WHERE adresse_id = ?")) {
            statement.setInt(1, id);
            try(ResultSet result = statement.executeQuery()) {
                result.next();
                Adresse adresse = new Adresse();
                adresse.setAdresseId(result.getInt("adresse_id"));
                adresse.setAktiv(result.getBoolean("aktiv"));
                adresse.setStrasse(result.getString("strasse"));
                adresse.setHausnummer(result.getString("hausnummer"));
                adresse.setPlz(result.getString("plz"));
                adresse.setOrt(result.getString("ort"));
                adresse.setLand(result.getString("land"));
                return adresse;
            }
        } catch(SQLException e) {
            System.out.println("SQLException: " + e.getMessage());
        }
        return null;
    }

    public Adresse save(Adresse adresse) {
        try (Connection con = getConnection();
            PreparedStatement ps = con.prepareStatement("INSERT INTO adresse (aktiv, strasse, hausnummer, plz, ort, land) VALUES (?,?,?,?,?,?)",
                    Statement.RETURN_GENERATED_KEYS)){
            ps.setBoolean(1, adresse.isAktiv());
            ps.setString(2, adresse.getStrasse());
            ps.setString(3, adresse.getHausnummer());
            ps.setString(4, adresse.getPlz());
            ps.setString(5, adresse.getOrt());
            ps.setString(6, adresse.getLand());
            ps.executeUpdate();

            try (ResultSet rs = ps.getGeneratedKeys()) {
                if (rs.next()) {
                    int generatedId = rs.getInt(1);
                    adresse.setAdresseId(generatedId);
                }
            }

        } catch(SQLException e) {
            System.out.println("SQLException: " + e.getMessage());
            return null;
        }
        return adresse;
    }


    public int updateAdresse(Adresse adresse) {
        int rows;
        try(Connection con = getConnection();
            PreparedStatement ps = con.prepareStatement("UPDATE adresse SET hausnummer = ?, land = ?, ort = ?, plz = ?, strasse = ?, aktiv = ? WHERE adresse_id = ?")) {

            ps.setString(1, adresse.getHausnummer());
            ps.setString(2, adresse.getLand());
            ps.setString(3, adresse.getOrt());
            ps.setString(4, adresse.getPlz());
            ps.setString(5, adresse.getStrasse());
            ps.setBoolean(6, adresse.isAktiv());
            ps.setInt(7, adresse.getAdresseId());
            rows = ps.executeUpdate();

        } catch(SQLException e) {
            System.out.println("SQLException: " + e.getMessage());
            rows = 0;
        }
        return rows;
    }
}

