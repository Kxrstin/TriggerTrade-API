package com.dbw.spring_boot.repositories;

import com.dbw.spring_boot.model.Bestellposition;

import java.sql.*;
import java.util.LinkedList;
import java.util.List;

public class BestellpositionRepository {
    public static Connection getConnection() throws SQLException {
        return DriverManager.getConnection("jdbc:postgresql://localhost:5432/abschlussprojekt", "lordoftherows", "rows123");
    }

    public List<Bestellposition> findAll() {
        LinkedList<Bestellposition> bps = new LinkedList<>();
        String sql = "SELECT position_id as positions_id, bestellung_id, bp.sku as produkt_sku, menge, preis*menge as gesamtpreis"
                + " FROM bestellposition bp LEFT join produkt p on bp.sku = p.sku";
        try (Connection con = getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            try(ResultSet result = ps.executeQuery()) {
                while (result.next()) {
                    Bestellposition bp = new Bestellposition();
                    bp.setPositionsId(result.getInt("positions_id"));
                    bp.setBestellungId(result.getInt("bestellung_id"));
                    bp.setProduktSku(result.getString("produkt_sku"));
                    bp.setMenge(result.getInt("menge"));
                    bp.setGesamtpreis(result.getDouble("gesamtpreis"));
                    bps.add(bp);
                }
            }
        } catch(SQLException e) {
            System.out.println("SQLException: " + e.getMessage());
            return null;
        }

        return bps;
    }

    public Bestellposition findByPositionId(int id) {
        String sql = "SELECT position_id as positions_id, bestellung_id, bp.sku as produkt_sku, menge, preis*menge as gesamtpreis"
                + " FROM bestellposition bp LEFT join produkt p on bp.sku = p.sku WHERE position_id = ?";

        try (Connection con = getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setInt(1, id);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    Bestellposition bp = new Bestellposition();
                    bp.setPositionsId(rs.getInt("positions_id"));
                    bp.setBestellungId(rs.getInt("bestellung_id"));
                    bp.setProduktSku(rs.getString("produkt_sku"));
                    bp.setMenge(rs.getInt("menge"));
                    bp.setGesamtpreis(rs.getDouble("gesamtpreis"));
                    return bp;
                }
            }
        } catch (SQLException e) {
            System.out.println("SQLException: " + e.getMessage());
        }
        return null;
    }

    public Bestellposition save(Bestellposition bp) {
        try(Connection con = getConnection();
            PreparedStatement ps = con.prepareStatement("INSERT INTO bestellposition (bestellung_id, sku, menge) VALUES (?,?,?)",
                    Statement.RETURN_GENERATED_KEYS)) {
            ps.setInt(1, bp.getBestellungId());
            ps.setString(2, bp.getProduktSku());
            ps.setInt(3, bp.getMenge());
            ps.executeUpdate();

            try (ResultSet rs = ps.getGeneratedKeys()) {
                if (rs.next()) {
                    int generatedId = rs.getInt(1);
                    bp.setPositionsId(generatedId);
                }
            }
        } catch(SQLException e) {
            System.out.println("SQLException: " + e.getMessage());
            return null;
        }
        return bp;
    }

    public int deleteByPositionId(int id) {
        int rows = 0;
        try(Connection con = getConnection();
            PreparedStatement ps = con.prepareStatement("DELETE FROM bestellposition WHERE position_id = ?")) {
            ps.setInt(1, id);
            rows = ps.executeUpdate();

        } catch(SQLException e) {
            System.out.println("SQLException: " + e.getMessage());
        }
        return rows;
    }
}

