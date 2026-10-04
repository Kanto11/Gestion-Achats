package com.mycompany.gestion_achats.controller;

import java.io.IOException;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.control.Label;
import javafx.scene.layout.StackPane;
import javafx.scene.control.Button;
import java.util.List;

public class Main_Layout_Controller {
    @FXML
    private StackPane zoneContenu;

    @FXML
    private Button btnDashboard;
    @FXML
    private Button btnFournisseurs;
    @FXML
    private Button btnProduits;
    @FXML
    private Button btnCommandes;
    @FXML
    private Button btnEspaceFournisseur;
    private List<Button> menuButtons;
    
    @FXML
    private Label TitreLink;
   

    @FXML
    public void initialize() {
        menuButtons = List.of(btnDashboard, btnFournisseurs, btnProduits, btnCommandes,btnEspaceFournisseur);
        afficherDashboard(); 
    }
    
    
    @FXML
    public void afficherDashboard() {
        setActiveButton(btnDashboard);
        chargerVue("/com/mycompany/gestion_achats/view/dashboard_view.fxml");
        TitreLink.setText("Tableau de bord");
    }

    
    @FXML
    public void afficherFournisseurs() {
        setActiveButton(btnFournisseurs);
        chargerVue("/com/mycompany/gestion_achats/view/fournisseur_view.fxml");
        TitreLink.setText("Fournisseur");
    }

    @FXML
    public void afficherProduits() {
        setActiveButton(btnProduits);
        chargerVue("/com/mycompany/gestion_achats/view/produits_view.fxml");
        TitreLink.setText("Produits");
    }

    @FXML
    public void afficherCommandes() {
        setActiveButton(btnCommandes);
        chargerVue("/com/mycompany/gestion_achats/view/commande_view.fxml");
        TitreLink.setText("Commande");
    }
    @FXML
    public void afficherEspaceFournisseur() {
        setActiveButton(btnEspaceFournisseur);
        chargerVue("/com/mycompany/gestion_achats/view/espace_fournisseur_view.fxml");
        TitreLink.setText("Espace fournisseur");
    }
    
    private void setActiveButton(Button btnActif) {
        for (Button btn : menuButtons) {
            btn.getStyleClass().remove("active");
        }
        btnActif.getStyleClass().add("active");
    }
    
    private void chargerVue(String cheminFxml) {
        try {
            Parent vue = FXMLLoader.load(getClass().getResource(cheminFxml));
            zoneContenu.getChildren().setAll(vue);
        } catch (IOException e) {
            zoneContenu.getChildren().setAll(new Label("Erreur de chargement : " + e.getMessage()));
            e.printStackTrace();
        }
    }
}