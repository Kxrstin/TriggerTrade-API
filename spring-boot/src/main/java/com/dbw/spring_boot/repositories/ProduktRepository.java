package com.dbw.spring_boot.repositories;

import com.dbw.spring_boot.model.Produkt;
import com.dbw.spring_boot.model.ViewKundeBestellungBestellposition;
import com.dbw.spring_boot.model.ViewProduktVerkaufszahlen;

import java.sql.*;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

public class ProduktRepository {
    public static Connection getConnection() throws SQLException {
        return DriverManager.getConnection("jdbc:postgresql://localhost:5432/abschlussprojekt", "lordoftherows", "rows123");
    }

    public List<Produkt> findAll() {
        LinkedList<Produkt> produkte = new LinkedList<>();
        try (Connection con = getConnection();
             PreparedStatement ps = con.prepareStatement("SELECT * FROM produkt ORDER BY sku")) {
            try(ResultSet result = ps.executeQuery()) {
                while (result.next()) {
                    Produkt produkt = new Produkt();
                    produkt.setSku(result.getString("sku"));
                    produkt.setName(result.getString("name"));
                    produkt.setPreis(result.getDouble("preis"));
                    produkt.setLagerbestand(result.getInt("lagerbestand"));
                    produkt.setAngelegtVon(result.getInt("angelegt_von"));
                    produkte.add(produkt);
                }
            }
        } catch(SQLException e) {
            System.out.println("SQLException: " + e.getMessage());
            return null;
        }
        return produkte;
    }

    public Produkt findBySku(String sku) {
        try (Connection con = getConnection();
             PreparedStatement ps = con.prepareStatement("SELECT * FROM produkt WHERE sku = ?")) {

            ps.setString(1, sku);
            try (ResultSet result = ps.executeQuery()) {
                if (result.next()) {
                    Produkt produkt = new Produkt();
                    produkt.setSku(result.getString("sku"));
                    produkt.setName(result.getString("name"));
                    produkt.setPreis(result.getDouble("preis"));
                    produkt.setLagerbestand(result.getInt("lagerbestand"));
                    produkt.setAngelegtVon(result.getInt("angelegt_von"));
                    return produkt;
                }
            }
        } catch (SQLException e) {
            System.out.println("SQLException: " + e.getMessage());
        }
        return null;
    }

    public Produkt save(Produkt produkt) {
        try(Connection con = getConnection();
            PreparedStatement ps = con.prepareStatement("INSERT INTO produkt (sku, name, preis, lagerbestand, angelegt_von) VALUES (?,?,?,?,?)")) {
            ps.setString(1, produkt.getSku());
            ps.setString(2, produkt.getName());
            ps.setDouble(3, produkt.getPreis());
            ps.setInt(4, produkt.getLagerbestand());
            ps.setInt(5, produkt.getAngelegtVon());
            ps.executeUpdate();
        } catch(SQLException e) {
            System.out.println("SQLException: " + e.getMessage());
            return null;
        }
        return produkt;
    }

    public int deleteBySku(String sku) {
        try(Connection con = getConnection();
            PreparedStatement ps = con.prepareStatement("DELETE FROM produkt WHERE sku = ?")) {
            ps.setString(1, sku);
            return ps.executeUpdate();

        } catch(SQLException e) {
            System.out.println("SQLException: " + e.getMessage());
        }
        return 0;
    }

    public int changeLagerbestandBySku(String sku, int lagerbestand) {
        try(Connection con = getConnection();
            PreparedStatement ps = con.prepareStatement("UPDATE produkt SET lagerbestand = ? WHERE sku = ?")) {
            ps.setInt(1, lagerbestand);
            ps.setString(2, sku);
            return ps.executeUpdate();

        } catch(SQLException e) {
            System.out.println("SQLException: " + e.getMessage());
        }
        return 0;
    }

    public List<ViewProduktVerkaufszahlen> getViewProduktVerkaufszahlen() {
        List<ViewProduktVerkaufszahlen> ergebnisse = new ArrayList<>();
        try(Connection con = getConnection();
            PreparedStatement ps = con.prepareStatement("SELECT * FROM v_produkt_verkaufszahlen");
            ResultSet rs = ps.executeQuery()) {
            while(rs.next()) {
                ViewProduktVerkaufszahlen view = new ViewProduktVerkaufszahlen();
                view.setSku(rs.getString("sku"));
                view.setName(rs.getString("name"));
                view.setGesamtVerkaufteMenge(rs.getInt("gesamt_verkaufte_menge"));
                view.setUmsatz(rs.getDouble("umsatz"));
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

