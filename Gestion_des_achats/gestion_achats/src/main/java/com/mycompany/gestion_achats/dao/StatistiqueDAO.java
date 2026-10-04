package com.mycompany.gestion_achats.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.LinkedHashMap;
import java.util.Map;

public class StatistiqueDAO {

    public int totalFournisseurs() {
        return compter("SELECT COUNT(*) FROM Fournisseur");
    }

    public int totalProduits() {
        return compter("SELECT COUNT(*) FROM Produit");
    }

    public int totalCommandes() {
        return compter("SELECT COUNT(*) FROM Commande");
    }

    private int compter(String sql) {
        try (Connection conn = ConnexionDB.getConnexion();
             PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            if (rs.next()) return rs.getInt(1);
        } catch (SQLException e) {
            System.err.println("Erreur comptage : " + e.getMessage());
        }
        return 0;
    }

    public double totalAchats() {
        String sql = "SELECT SUM(l.montant) FROM Ligne_commande l "
                + "JOIN Commande c ON l.num_Cm = c.num_Cm "
                + "WHERE c.statut_Cm != 'Annulee'";
        return sommer(sql);
    }

    public double totalPaye() {
        return sommer("SELECT SUM(montant_paye) FROM Paiement");
    }

    private double sommer(String sql) {
        try (Connection conn = ConnexionDB.getConnexion();
             PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            if (rs.next()) {
                double val = rs.getDouble(1);
                return rs.wasNull() ? 0 : val;
            }
        } catch (SQLException e) {
            System.err.println("Erreur somme : " + e.getMessage());
        }
        return 0;
    }

    public Map<String, Integer> repartitionParStatut() {
        Map<String, Integer> resultat = new LinkedHashMap<>();
        String sql = "SELECT statut_Cm, COUNT(*) AS nb FROM Commande GROUP BY statut_Cm";

        try (Connection conn = ConnexionDB.getConnexion();
             PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            while (rs.next()) {
                resultat.put(rs.getString("statut_Cm"), rs.getInt("nb"));
            }
        } catch (SQLException e) {
            System.err.println("Erreur repartition statut : " + e.getMessage());
        }
        return resultat;
    }

    public Map<String, Double> depensesParMois() {
        Map<String, Double> resultat = new LinkedHashMap<>();
        String sql = "SELECT DATE_FORMAT(c.date_Cm, '%Y-%m') AS mois, SUM(l.montant) AS total "
                + "FROM Ligne_commande l "
                + "JOIN Commande c ON l.num_Cm = c.num_Cm "
                + "WHERE c.statut_Cm != 'Annulee' "
                + "GROUP BY mois ORDER BY mois";

        try (Connection conn = ConnexionDB.getConnexion();
             PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            while (rs.next()) {
                resultat.put(rs.getString("mois"), rs.getDouble("total"));
            }
        } catch (SQLException e) {
            System.err.println("Erreur depenses par mois : " + e.getMessage());
        }
        return resultat;
    }

    public Map<String, Double> depensesParFournisseur() {
        Map<String, Double> resultat = new LinkedHashMap<>();
        String sql = "SELECT f.nom_F AS nom, SUM(l.montant) AS total "
                + "FROM Ligne_commande l "
                + "JOIN Commande c ON l.num_Cm = c.num_Cm "
                + "JOIN Fournisseur f ON c.id_F = f.id_F "
                + "WHERE c.statut_Cm != 'Annulee' "
                + "GROUP BY f.nom_F ORDER BY total DESC";

        try (Connection conn = ConnexionDB.getConnexion();
             PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            while (rs.next()) {
                resultat.put(rs.getString("nom"), rs.getDouble("total"));
            }
        } catch (SQLException e) {
            System.err.println("Erreur depenses par fournisseur : " + e.getMessage());
        }
        return resultat;
    }

    public Map<String, Integer> topProduits(int limite) {
        Map<String, Integer> resultat = new LinkedHashMap<>();
        String sql = "SELECT p.nom_P AS nom, SUM(l.qte) AS totalQte "
                + "FROM Ligne_commande l "
                + "JOIN Commande c ON l.num_Cm = c.num_Cm "
                + "JOIN Produit p ON l.id_P = p.id_P "
                + "WHERE c.statut_Cm != 'Annulee' "
                + "GROUP BY p.nom_P ORDER BY totalQte DESC LIMIT ?";

        try (Connection conn = ConnexionDB.getConnexion();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, limite);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                resultat.put(rs.getString("nom"), rs.getInt("totalQte"));
            }
        } catch (SQLException e) {
            System.err.println("Erreur top produits : " + e.getMessage());
        }
        return resultat;
    }

    public int nombreCommandesEnRetard() {
        String sql = "SELECT COUNT(*) FROM Commande "
                + "WHERE statut_Cm NOT IN ('Livree', 'Annulee') "
                + "AND date_prevue IS NOT NULL "
                + "AND date_prevue < CURDATE()";
        return compter(sql);
    }
}