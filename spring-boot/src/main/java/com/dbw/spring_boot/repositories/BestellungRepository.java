package com.dbw.spring_boot.repositories;

import com.dbw.spring_boot.model.*;

import java.sql.*;
import java.time.OffsetDateTime;
import java.util.*;

public class BestellungRepository {
    public static Connection getConnection() throws SQLException {
        return DriverManager.getConnection("jdbc:postgresql://localhost:5432/abschlussprojekt", "lordoftherows", "rows123");
    }

    public List<Bestellung> findAllOrById(Integer id) {
        String sql = "SELECT b.bestellung_id, b.kunde_id, b.mitarbeiterzuweis AS personalnr, b.datum, b.status, " +
                "bp.position_id as positions_id, bp.bestellung_id, p.sku, p.name, p.preis, p.lagerbestand, p.angelegt_von, "+
                "bp.menge, bp.menge * p.preis as gesamtpreis FROM bestellung b LEFT JOIN bestellposition bp " +
                "ON b.bestellung_id = bp.bestellung_id LEFT JOIN produkt p ON bp.sku = p.sku " +
                (id != null ? "WHERE b.bestellung_id = ?" : "ORDER BY b.bestellung_id");


        Map<Integer, Bestellung> bestellungen = new LinkedHashMap<>();
        try (Connection con = getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {
            if (id != null) {
                ps.setInt(1, id);
            }
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    int bestellungId = rs.getInt("bestellung_id");

                    Bestellung bestellung = bestellungen.get(bestellungId);
                    if (bestellung == null) {
                        bestellung = new Bestellung();
                        bestellung.setBestellungId(rs.getInt("bestellung_id"));
                        bestellung.setKundeId(rs.getInt("kunde_id"));
                        bestellung.setPersonalnr(rs.getInt("personalnr"));
                        bestellung.setDatum(rs.getObject("datum", OffsetDateTime.class));
                        bestellung.setStatus(rs.getString("status"));
                        bestellungen.put(bestellungId, bestellung);
                    }

                    int bestellpositionId = rs.getInt("positions_id");
                    if (!rs.wasNull()) {
                        BestellpositionF bp = new BestellpositionF();
                        bp.setPositionsId(bestellpositionId);
                        bp.setBestellungId(rs.getInt("bestellung_id"));
                        Produkt p = new Produkt();
                        p.setSku(rs.getString("sku"));
                        p.setName(rs.getString("name"));
                        p.setPreis(rs.getDouble("preis"));
                        p.setLagerbestand(rs.getInt("lagerbestand"));
                        p.setAngelegtVon(rs.getInt("angelegt_von"));
                        bp.setProdukt(p);
                        bp.setMenge(rs.getInt("menge"));
                        bp.setGesamtpreis(rs.getDouble("gesamtpreis"));

                        bestellung.addPositionen(bp);
                    }
                }
            }

        } catch (SQLException e) {
            System.out.println("SQLException: " + e.getMessage());
            return null;
        }
        return new ArrayList<>(bestellungen.values());
    }

    public Bestellung save(Bestellung bestellung) {
        try (Connection con = getConnection();
             PreparedStatement ps = con.prepareStatement("INSERT INTO bestellung (datum, status, mitarbeiterzuweis, kunde_id) VALUES (?,?,?,?)",
                     Statement.RETURN_GENERATED_KEYS)){
            ps.setObject(1, bestellung.getDatum());
            ps.setString(2, bestellung.getStatus());
            ps.setInt(3, bestellung.getPersonalnr());
            ps.setInt(4, bestellung.getKundeId());
            ps.executeUpdate();

            try (ResultSet rs = ps.getGeneratedKeys()) {
                if (rs.next()) {
                    int generatedId = rs.getInt(1);
                    bestellung.setBestellungId(generatedId);
                }
            }

        } catch(SQLException e) {
            System.out.println("SQLException: " + e.getMessage());
            return null;
        }
        return bestellung;
    }


    public int deleteBestellung(int bestellungId) {
        try(Connection con = getConnection();
            PreparedStatement ps = con.prepareStatement("DELETE FROM bestellung WHERE bestellung_id = ?")) {
            ps.setInt(1, bestellungId);
            return ps.executeUpdate();
        } catch(SQLException e) {
            System.out.println("SQLException: " + e.getMessage());
        }
        return 0;
    }

    public int setStorniert(int bestellungId) {
        try(Connection con = getConnection();
            PreparedStatement ps = con.prepareStatement("UPDATE bestellung SET status = 'storniert' WHERE bestellung_id = ?")) {
            ps.setInt(1, bestellungId);
            return ps.executeUpdate();
        } catch(SQLException e) {
            System.out.println("SQLException: " + e.getMessage());
        }
        return 0;
    }
}
