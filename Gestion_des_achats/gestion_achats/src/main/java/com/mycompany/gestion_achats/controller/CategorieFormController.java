/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.gestion_achats.controller;
import com.mycompany.gestion_achats.dao.CategorieDAO;
import com.mycompany.gestion_achats.models.Categorie;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

public class CategorieFormController {
    @FXML private Label lblTitre;
    @FXML private TextField txtNom;
    private Categorie CategorieModifier;
    @FXML private Label lblErreur;
    
    private final CategorieDAO categoriedao = new CategorieDAO();
    private boolean succes = false;
    public void initModeAjout() {
        lblTitre.setText("Ajouter un categorie");
        CategorieModifier = null;
    }
    
    public void initModeModification(Categorie c) {
        lblTitre.setText("Modifier un Categorie");
        CategorieModifier = c;
        txtNom.setText(c.getNomC());
    }
    
    public boolean isSucces() {
        return succes;
    }
    
     @FXML
    public void enregistrer() {
        if (!validerChamps()) return;
        boolean ok;
        if (CategorieModifier == null) {
            Categorie c = new Categorie(0, txtNom.getText());
            ok = categoriedao.Ajouter(c);
        } else {
            CategorieModifier.setNomC(txtNom.getText());
            ok = categoriedao.Modifier(CategorieModifier);
        }
        if (ok) {
            succes = true;
            fermerFenetre();
        } else {
            lblErreur.setText("Erreur lors de l'enregistrement.");
        }
    }
    
    private boolean validerChamps() {
        if (txtNom.getText().trim().isEmpty()){
            lblErreur.setText("Nom obligatoire.");
            return false;
        }
        return true;
    }
    
    @FXML
    public void annuler() {
        succes = false;
        fermerFenetre();
        }
   

     private void fermerFenetre() {
        Stage stage = (Stage) txtNom.getScene().getWindow();
        stage.close();
    }
   
}
