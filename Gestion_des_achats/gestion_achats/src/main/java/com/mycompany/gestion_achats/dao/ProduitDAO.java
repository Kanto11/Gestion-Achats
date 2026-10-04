/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.gestion_achats.dao;
import com.mycompany.gestion_achats.models.Produits;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class ProduitDAO {
    public boolean Ajouter(Produits p){
        String SQL = "INSERT INTO produit (nom_P,id_C) VALUES (?,?)";
        try (Connection conn= ConnexionDB.getConnexion();
            PreparedStatement ps = conn.prepareStatement(SQL)){
                ps.setString(1, p.getNomP());
                ps.setInt(2, p.getIdC());
                return ps.executeUpdate() > 0;

        }catch(SQLException e){
            System.err.println("Erreur ajout produit :"+ e.getMessage());
            return false;
        }
        
    }
    
    public boolean Modifier(Produits p){
        String SQL = "UPDATE produit SET nom_P = ?,id_C=? WHERE id_P=? ";
         try (Connection conn = ConnexionDB.getConnexion();
             PreparedStatement ps = conn.prepareStatement(SQL)){
                ps.setString(1, p.getNomP());
                ps.setInt(2, p.getIdC());
                ps.setInt(3, p.getIdP());
            
            return ps.executeUpdate() > 0;
         }catch(SQLException e){
            System.err.println("Erreur modification produit : " + e.getMessage());
            return false;
         }
    }
      
    public boolean Supprimer(int idP){
        String SQL = "DELETE FROM produit WHERE id_P = ?";
        
        try (Connection conn= ConnexionDB.getConnexion();
            PreparedStatement ps = conn.prepareStatement(SQL)){     
            ps.setInt(1,idP);
            return ps.executeUpdate()>0;
        }catch (SQLException e){
            System.err.print("Erreur suppression produit :"+e.getMessage());
            return false;
        }
    }
    
        public List<Produits>listerTous(){
        List<Produits> liste = new ArrayList<>();
        String SQL = "SELECT * FROM produit";
        
        try (Connection conn = ConnexionDB.getConnexion();
            PreparedStatement ps = conn.prepareStatement(SQL);
            ResultSet rs = ps.executeQuery()){
                
                while (rs.next()){
                    Produits p = new Produits(
                            rs.getInt("id_P"),
                            rs.getString("nom_P"),
                            rs.getInt("id_C")
                            
                    );
                    liste.add(p);
                }
        }catch (SQLException e){
                System.err.println("Erreur listage produit :"+e.getMessage());
        }
            return liste;
    }
        
        public List<Produits> rechercherParNom(String nom){
        List<Produits> liste = new ArrayList<>();
        String SQL = "SELECT * FROM produit Where nom_P LIKE ? ";
        
        try (Connection conn = ConnexionDB.getConnexion();
             PreparedStatement ps = conn.prepareStatement(SQL)){
            
            ps.setString(1,"%" + nom + "%");
            ResultSet rs = ps.executeQuery();
            
            while (rs.next()){
                        Produits p = new Produits(
                            rs.getInt("id_P"),
                            rs.getString("nom_P"),
                            rs.getInt("id_C")
                 );
                 liste.add(p);
             }
        }catch(SQLException e){
            System.err.println("Erreur recherche produit :"+ e.getMessage());
        }
        return liste;
    }
        
    public List<Produits> listerParCategorie(int idC) {
    List<Produits> liste = new ArrayList<>();
    String SQL = "SELECT * FROM Produit WHERE id_C = ?";

    try (Connection conn = ConnexionDB.getConnexion();
         PreparedStatement ps = conn.prepareStatement(SQL)) {
        ps.setInt(1, idC);
        ResultSet rs = ps.executeQuery();

        while (rs.next()) {
            Produits p = new Produits(
                    rs.getInt("id_P"),
                    rs.getString("nom_P"),
                    rs.getInt("id_C")
            );
            liste.add(p);
        }
    } catch (SQLException e) {
        System.err.println("Erreur filtrage produits par categorie : " + e.getMessage());
    }
    return liste;
}
    
}
