/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.gestion_achats.controller;
import com.mycompany.gestion_achats.dao.CategorieDAO;
import com.mycompany.gestion_achats.dao.ProduitDAO;
import com.mycompany.gestion_achats.models.Categorie;
import com.mycompany.gestion_achats.models.Produits;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextField;
import javafx.collections.FXCollections;
import java.util.List;
import javafx.stage.Stage;

public class ProduitsFormController {
    @FXML private Label lblTitre;
    @FXML private TextField txtNom;
    @FXML private ComboBox<Categorie> combocategorie;
    @FXML private Label lblErreur;
    
    private final ProduitDAO produitDAO = new ProduitDAO();
    private final CategorieDAO categorieDAO = new CategorieDAO();
    private Produits produitAModifier;
    private boolean succes = false;
    
    @FXML
    public void initialize(){
        chargerCategorie();
    }
    
    private void chargerCategorie(){
        List<Categorie> liste = categorieDAO.listertous();
        combocategorie.setItems(FXCollections.observableArrayList(liste));
    }
    
    public void initModeAjout(){
        lblTitre.setText("Ajouter un produit");
        produitAModifier = null;
    
    }
    
    public void initModeModification(Produits p){
        lblTitre.setText("Ajouter un produit");
        produitAModifier = p;
        txtNom.setText(p.getNomP());

        for (Categorie c : combocategorie.getItems()) {
            if (c.getIdC() == p.getIdC()) {
                combocategorie.setValue(c);
                break;
           }
        }
    }

    public boolean isSucces() {
        return succes;
    } 
    
   
     @FXML
    public void enregistrer() {
        if (!validerChamps()) return;
        int idC = combocategorie.getValue().getIdC();
        boolean ok;

        if (produitAModifier == null) {
            Produits p = new Produits(0, txtNom.getText(), idC);
            ok = produitDAO.Ajouter(p);
        } else {
            produitAModifier.setNomP(txtNom.getText());
            produitAModifier.setIdC(idC);
            ok = produitDAO.Modifier(produitAModifier);
        }

        if (ok) {
            succes = true;
            fermerFenetre();
        } else {
            lblErreur.setText("Erreur lors de l'enregistrement.");
        }
    }
    
        @FXML
    public void annuler() {
        succes = false;
        fermerFenetre();
    }

    private boolean validerChamps() {
        if (txtNom.getText().trim().isEmpty()) {
            lblErreur.setText("Le nom est obligatoire.");
            return false;
        }
        if (combocategorie.getValue() == null) {
            lblErreur.setText("Veuillez choisir une categorie.");
            return false;
        }
        return true;
    }

    private void fermerFenetre() {
        Stage stage = (Stage) txtNom.getScene().getWindow();
        stage.close();
    }
}
    

