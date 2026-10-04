package com.mycompany.gestion_achats.dao;

import com.mycompany.gestion_achats.models.Commande;
import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

public class CommandeDAO {

    public int ajouter(Commande c) {
        String SQL = "INSERT INTO Commande (date_Cm, date_prevue, statut_Cm, id_F) VALUES (?, ?, ?, ?)";

        try (Connection conn = ConnexionDB.getConnexion();
             PreparedStatement ps = conn.prepareStatement(SQL, Statement.RETURN_GENERATED_KEYS)) {

            ps.setDate(1, Date.valueOf(c.getDateCm()));
            if (c.getDatelivraisonCm() != null) {
                ps.setDate(2, Date.valueOf(c.getDatelivraisonCm()));
            } else {
                ps.setNull(2, java.sql.Types.DATE);
            }
            ps.setString(3, c.getStatutCm());
            ps.setInt(4, c.getIdF());

            int lignesAffectees = ps.executeUpdate();
            if (lignesAffectees == 0) return -1;

            ResultSet cles = ps.getGeneratedKeys();
            if (cles.next()) {
                return cles.getInt(1);
            }
            return -1;

        } catch (SQLException e) {
            System.err.println("Erreur ajout commande : " + e.getMessage());
            return -1;
        }
    }

    public boolean changerStatut(int numCm, String nouveauStatut) {
        String SQL = "UPDATE Commande SET statut_Cm = ? WHERE num_Cm = ?";

        try (Connection conn = ConnexionDB.getConnexion();
             PreparedStatement ps = conn.prepareStatement(SQL)) {

            ps.setString(1, nouveauStatut);
            ps.setInt(2, numCm);
            return ps.executeUpdate() > 0;

        } catch (SQLException e) {
            System.err.println("Erreur changement statut commande : " + e.getMessage());
            return false;
        }
    }

    public List<Commande> listerTous() {
        List<Commande> liste = new ArrayList<>();
        String SQL = "SELECT * FROM Commande ORDER BY num_Cm DESC";

        try (Connection conn = ConnexionDB.getConnexion();
             PreparedStatement ps = conn.prepareStatement(SQL);
             ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {
                Date dateLivraison = rs.getDate("date_prevue");
                Commande c = new Commande(
                        rs.getInt("num_Cm"),
                        rs.getDate("date_Cm").toLocalDate(),
                        dateLivraison != null ? dateLivraison.toLocalDate() : null,
                        rs.getString("statut_Cm"),
                        rs.getInt("id_F")
                );
                liste.add(c);
            }
        } catch (SQLException e) {
            System.err.println("Erreur listage commandes : " + e.getMessage());
        }
        return liste;
    }

    public List<Commande> listerParStatut(String statut) {
        List<Commande> liste = new ArrayList<>();
        String SQL = "SELECT * FROM Commande WHERE statut_Cm = ? ORDER BY num_Cm DESC";

        try (Connection conn = ConnexionDB.getConnexion();
             PreparedStatement ps = conn.prepareStatement(SQL)) {

            ps.setString(1, statut);
            ResultSet rs = ps.executeQuery();

            while (rs.next()) {
                Date dateLivraison = rs.getDate("date_prevue");
                Commande c = new Commande(
                        rs.getInt("num_Cm"),
                        rs.getDate("date_Cm").toLocalDate(),
                        dateLivraison != null ? dateLivraison.toLocalDate() : null,
                        rs.getString("statut_Cm"),
                        rs.getInt("id_F")
                );
                liste.add(c);
            }
        } catch (SQLException e) {
            System.err.println("Erreur listage commandes par statut : " + e.getMessage());
        }
        return liste;
    }
}