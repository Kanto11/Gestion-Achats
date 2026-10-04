/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.gestion_achats.dao;
import com.mycompany.gestion_achats.models.Ligne_de_commande;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.sql.ResultSet;

public class LigneDeCommandeDAO {
    public boolean ajouter(Ligne_de_commande l) {
        String SQL = "INSERT INTO ligne_commande (id_P, num_Cm, qte,  montant) VALUES (?, ?, ?, ?)";

        try (Connection conn = ConnexionDB.getConnexion();
             PreparedStatement ps = conn.prepareStatement(SQL)) {

            ps.setInt(1, l.getIdP());
            ps.setInt(2, l.getNumCm());
            ps.setInt(3, l.getQteP());
            ps.setDouble(4, l.getMontant());

            return ps.executeUpdate() > 0;

        } catch (SQLException e) {
            System.err.println("Erreur ajout ligne de commande : " + e.getMessage());
            return false;
        }
    }
    
        public boolean supprimerParCommande(int numCm) {
        String SQL = "DELETE FROM ligne_commande WHERE num_Cm = ?";

        try (Connection conn = ConnexionDB.getConnexion();
             PreparedStatement ps = conn.prepareStatement(SQL)) {
            ps.setInt(1, numCm);
            return ps.executeUpdate() >= 0;
        } catch (SQLException e) {
            System.err.println("Erreur suppression lignes de commande : " + e.getMessage());
            return false;
        }
    }
        
    public List<Ligne_de_commande> listerParCommande(int numCm) {
        List<Ligne_de_commande> liste = new ArrayList<>();
        String SQL = "SELECT * FROM ligne_commande WHERE num_Cm = ?";

        try (Connection conn = ConnexionDB.getConnexion();
             PreparedStatement ps = conn.prepareStatement(SQL)) {

            ps.setInt(1, numCm);
            ResultSet rs = ps.executeQuery();

            while (rs.next()) {
            Ligne_de_commande l = new Ligne_de_commande(
                    rs.getInt("id_LC"),
                    rs.getInt("num_Cm"),
                    rs.getInt("id_P"),
                    rs.getInt("qte"),
                    rs.getDouble("montant")
            );
            liste.add(l);
        }
        } catch (SQLException e) {
            System.err.println("Erreur listage lignes de commande : " + e.getMessage());
        }
        return liste;
    }
}
