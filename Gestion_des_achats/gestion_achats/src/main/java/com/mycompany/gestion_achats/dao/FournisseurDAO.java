/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.gestion_achats.dao;

import com.mycompany.gestion_achats.models.Fournisseur;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.sql.Statement;

public class FournisseurDAO {
   public int Ajouter(Fournisseur f) {
    String SQL = "INSERT INTO Fournisseur (nom_F, tel_F, adr_F, email_F) VALUES (?, ?, ?, ?)";

    try (Connection conn = ConnexionDB.getConnexion();
         PreparedStatement ps = conn.prepareStatement(SQL, Statement.RETURN_GENERATED_KEYS)) {

        ps.setString(1, f.getNomF());
        ps.setString(2, f.getTelF());
        ps.setString(3, f.getAdrF());
        ps.setString(4, f.getEmailF());

        int lignesAffectees = ps.executeUpdate();
        if (lignesAffectees == 0) return -1;

        ResultSet cles = ps.getGeneratedKeys();
        if (cles.next()) {
            return cles.getInt(1);
        }
        return -1;

    } catch (SQLException e) {
        System.err.println("Erreur ajout fournisseur : " + e.getMessage());
        return -1;
    }
}
    
    public boolean Modifier(Fournisseur f){
        String SQL = "UPDATE fournisseur SET nom_F = ?,tel_F =?,adr_F=?,email_F =? WHERE id_F=? ";
         try (Connection conn = ConnexionDB.getConnexion();
             PreparedStatement ps = conn.prepareStatement(SQL)){
            ps.setString(1, f.getNomF());
            ps.setString(2, f.getTelF());
            ps.setString(3, f.getAdrF());
            ps.setString(4, f.getEmailF());
            ps.setInt(5, f.getIdF());
            
            return ps.executeUpdate() > 0;
         }catch(SQLException e){
            System.err.println("Erreur modification fournisseur : " + e.getMessage());
            return false;
         }
    }
    
    
    public boolean Supprimer(int idF){
        String SQL = "DELETE FROM fournisseur WHERE id_f = ?";
        
        try (Connection conn= ConnexionDB.getConnexion();
            PreparedStatement ps = conn.prepareStatement(SQL)){     
            ps.setInt(1,idF);
            return ps.executeUpdate()>0;
        }catch (SQLException e){
            System.err.print("Erreur suppression fournisseur :"+e.getMessage());
            return false;
        }
    }
    
    public List<Fournisseur>listerTous(){
        List<Fournisseur> liste = new ArrayList<>();
        String SQL = "SELECT * FROM Fournisseur";
        
        try (Connection conn = ConnexionDB.getConnexion();
            PreparedStatement ps = conn.prepareStatement(SQL);
            ResultSet rs = ps.executeQuery()){
                
                while (rs.next()){
                    Fournisseur f = new Fournisseur(
                            rs.getInt("id_F"),
                            rs.getString("nom_F"),
                            rs.getString("tel_F"),
                            rs.getString("adr_F"),
                            rs.getString("email_F")
                    );
                    liste.add(f);
                }
        }catch (SQLException e){
                System.err.println("Erreur listage fournisseur :"+e.getMessage());
        }
            return liste;
    }
    
    public List<Fournisseur> rechercherParNom(String nom){
        List<Fournisseur> liste = new ArrayList<>();
        String SQL = "SELECT * FROM Fournisseur Where nom_F LIKE ? ";
        
        try (Connection conn = ConnexionDB.getConnexion();
             PreparedStatement ps = conn.prepareStatement(SQL)){
            
            ps.setString(1,"%" + nom + "%");
            ResultSet rs = ps.executeQuery();
            
            while (rs.next()){
                 Fournisseur f= new Fournisseur(
                        rs.getInt("id_F"),
                        rs.getString("nom_F"),
                        rs.getString("tel_F"),
                        rs.getString("adr_F"),
                        rs.getString("email_F")
                 );
                 liste.add(f);
             }
        }catch(SQLException e){
            System.err.println("Erreur recherche fournisseur :"+ e.getMessage());
        }
        return liste;
    }
    
}
        
 
