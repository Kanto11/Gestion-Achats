package com.mycompany.gestion_achats.dao;

import com.mycompany.gestion_achats.models.Paiement;
import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class PaimentDAO {

    public boolean ajouter(Paiement p) {
        String SQL = "INSERT INTO Paiement (num_Cm, montant_paye, date_paiement, mode_paiement) VALUES (?, ?, ?, ?)";

        try (Connection conn = ConnexionDB.getConnexion();
             PreparedStatement ps = conn.prepareStatement(SQL)) {

            ps.setInt(1, p.getNumCm());
            ps.setDouble(2, p.getMontantPaye());
            ps.setDate(3, Date.valueOf(p.getDatePaiement()));
            ps.setString(4, p.getModePaiement());

            return ps.executeUpdate() > 0;

        } catch (SQLException e) {
            System.err.println("Erreur ajout paiement : " + e.getMessage());
            return false;
        }
    }

    public List<Paiement> listerParCommande(int numCm) {
        List<Paiement> liste = new ArrayList<>();
        String SQL = "SELECT * FROM Paiement WHERE num_Cm = ? ORDER BY date_paiement";

        try (Connection conn = ConnexionDB.getConnexion();
             PreparedStatement ps = conn.prepareStatement(SQL)) {

            ps.setInt(1, numCm);
            ResultSet rs = ps.executeQuery();

            while (rs.next()) {
                Paiement p = new Paiement(
                        rs.getInt("id_paiement"),
                        rs.getInt("num_Cm"),
                        rs.getDouble("montant_paye"),
                        rs.getDate("date_paiement").toLocalDate(),
                        rs.getString("mode_paiement")
                );
                liste.add(p);
            }
        } catch (SQLException e) {
            System.err.println("Erreur listage paiements par commande : " + e.getMessage());
        }
        return liste;
    }

    public List<Paiement> listerTous() {
        List<Paiement> liste = new ArrayList<>();
        String SQL = "SELECT * FROM Paiement ORDER BY date_paiement DESC";

        try (Connection conn = ConnexionDB.getConnexion();
             PreparedStatement ps = conn.prepareStatement(SQL);
             ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {
                Paiement p = new Paiement(
                        rs.getInt("id_paiement"),
                        rs.getInt("num_Cm"),
                        rs.getDouble("montant_paye"),
                        rs.getDate("date_paiement").toLocalDate(),
                        rs.getString("mode_paiement")
                );
                liste.add(p);
            }
        } catch (SQLException e) {
            System.err.println("Erreur listage de tous les paiements : " + e.getMessage());
        }
        return liste;
    }

    public double totalPayePourCommande(int numCm) {
        double total = 0;
        for (Paiement p : listerParCommande(numCm)) {
            total += p.getMontantPaye();
        }
        return total;
    }
}