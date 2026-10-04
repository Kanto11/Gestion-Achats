/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.gestion_achats.dao;
import com.mycompany.gestion_achats.models.Categorie;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.List;
import java.util.ArrayList;
import java.sql.ResultSet;


public class CategorieDAO {
    public boolean Ajouter(Categorie c) {
        String SQL = "INSERT INTO Categorie (nom_C) VALUES (?)";

        try (Connection conn = ConnexionDB.getConnexion();
             PreparedStatement ps = conn.prepareStatement(SQL)) {
            ps.setString(1, c.getNomC());
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            System.err.println("Erreur ajout categorie : " + e.getMessage());
            return false;
        }
    }
    
      public boolean Modifier(Categorie c){
        String SQL = "UPDATE categorie SET nom_C = ? WHERE id_C = ? ";
         try (Connection conn = ConnexionDB.getConnexion();
             PreparedStatement ps = conn.prepareStatement(SQL)){
                ps.setString(1, c.getNomC());
                ps.setInt(2, c.getIdC());
            
            return ps.executeUpdate() > 0;
         }catch(SQLException e){
            System.err.println("Erreur modification categorie : " + e.getMessage());
            return false;
         }
    }
    
    public List<Categorie> listertous(){
        List<Categorie> liste = new ArrayList<>();
        String SQL = "SELECT * FROM categorie";
          try (Connection conn = ConnexionDB.getConnexion();
            PreparedStatement ps = conn.prepareStatement(SQL);
            ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    Categorie c = new Categorie(rs.getInt("id_C"), rs.getString("nom_C"));
                   liste.add(c);
                }

          } catch (SQLException e) {
            System.err.println("Erreur listage categories : " + e.getMessage());
        }
        return liste;
    }
}
