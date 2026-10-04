package com.mycompany.gestion_achats.dao;

import com.mycompany.gestion_achats.models.Fournir;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class FournirDAO {

    public boolean ajouter(Fournir f) {
        String SQL = "INSERT INTO Fournir (id_F, id_P, prix_fournisseur) VALUES (?, ?, ?)";

        try (Connection conn = ConnexionDB.getConnexion();
             PreparedStatement ps = conn.prepareStatement(SQL)) {
            ps.setInt(1, f.getIdF());
            ps.setInt(2, f.getIdP());
            ps.setDouble(3, f.getPrixFournisseur());
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            System.err.println("Erreur ajout fournir : " + e.getMessage());
            return false;
        }
    }

    public boolean supprimerParFournisseur(int idF) {
        String SQL = "DELETE FROM Fournir WHERE id_F = ?";

        try (Connection conn = ConnexionDB.getConnexion();
             PreparedStatement ps = conn.prepareStatement(SQL)) {
            ps.setInt(1, idF);
            return ps.executeUpdate() >= 0;
        } catch (SQLException e) {
            System.err.println("Erreur suppression fournir : " + e.getMessage());
            return false;
        }
    }

    public List<Fournir> listerParFournisseur(int idF) {
        List<Fournir> liste = new ArrayList<>();
        String SQL = "SELECT * FROM Fournir WHERE id_F = ?";

        try (Connection conn = ConnexionDB.getConnexion();
             PreparedStatement ps = conn.prepareStatement(SQL)) {
            ps.setInt(1, idF);
            ResultSet rs = ps.executeQuery();

            while (rs.next()) {
                Fournir f = new Fournir(
                        rs.getInt("id_F"),
                        rs.getInt("id_P"),
                        rs.getDouble("prix_fournisseur")
                );
                liste.add(f);
            }
        } catch (SQLException e) {
            System.err.println("Erreur listage fournir : " + e.getMessage());
        }
        return liste;
    }
    
    public double obtenirPrix(int idF, int idP) {
    String SQL = "SELECT prix_fournisseur FROM Fournir WHERE id_F = ? AND id_P = ?";

    try (Connection conn = ConnexionDB.getConnexion();
         PreparedStatement ps = conn.prepareStatement(SQL)) {
        ps.setInt(1, idF);
        ps.setInt(2, idP);
        ResultSet rs = ps.executeQuery();

        if (rs.next()) {
            return rs.getDouble("prix_fournisseur");
        }
    } catch (SQLException e) {
        System.err.println("Erreur recherche prix fournir : " + e.getMessage());
    }
    return -1;
}
}
