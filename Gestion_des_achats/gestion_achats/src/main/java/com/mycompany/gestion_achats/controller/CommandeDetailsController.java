package com.mycompany.gestion_achats.controller;

import com.mycompany.gestion_achats.dao.FournisseurDAO;
import com.mycompany.gestion_achats.dao.LigneDeCommandeDAO;
import com.mycompany.gestion_achats.dao.ProduitDAO;
import com.mycompany.gestion_achats.models.Commande;
import com.mycompany.gestion_achats.models.Fournisseur;
import com.mycompany.gestion_achats.models.Ligne_de_commande;
import com.mycompany.gestion_achats.models.Produits;
import java.util.List;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;

public class CommandeDetailsController {

    @FXML private Label lblNumCm;
    @FXML private Label lblFournisseur;
    @FXML private Label lblDateCm;
    @FXML private Label lblDateLivraison;
    @FXML private Label lblStatut;
    @FXML private Label lblTotal;

    @FXML private TableView<Ligne_de_commande> tableLignes;
    @FXML private TableColumn<Ligne_de_commande, String> colProduit;
    @FXML private TableColumn<Ligne_de_commande, Integer> colQte;
    @FXML private TableColumn<Ligne_de_commande, Double> colPrixUnitaire;
    @FXML private TableColumn<Ligne_de_commande, Double> colMontant;

    private final FournisseurDAO fournisseurDAO = new FournisseurDAO();
    private final LigneDeCommandeDAO ligneDAO = new LigneDeCommandeDAO();
    private final ProduitDAO produitDAO = new ProduitDAO();
    private List<Produits> produitsDisponibles;

    public void initDetails(Commande c) {
        produitsDisponibles = produitDAO.listerTous();

        lblNumCm.setText(String.valueOf(c.getNumCm()));
        lblFournisseur.setText(trouverNomFournisseur(c.getIdF()));
        lblDateCm.setText(c.getDateCm() != null ? c.getDateCm().toString() : "");
        lblDateLivraison.setText(c.getDatelivraisonCm() != null ? c.getDatelivraisonCm().toString() : "Non definie");
        lblStatut.setText(c.getStatutCm());

        configurerColonnes();

        List<Ligne_de_commande> lignes = ligneDAO.listerParCommande(c.getNumCm());
        tableLignes.setItems(FXCollections.observableArrayList(lignes));

        double total = 0;
        for (Ligne_de_commande l : lignes) {
            total += l.getMontant();
        }
        lblTotal.setText(String.format("Total : %.2f Ar", total));
    }

    private void configurerColonnes() {
        colProduit.setCellValueFactory(data -> {
            String nom = trouverNomProduit(data.getValue().getIdP());
            return new javafx.beans.property.SimpleStringProperty(nom);
        });

         colQte.setCellValueFactory(data -> {
            return new javafx.beans.property.SimpleIntegerProperty(data.getValue().getQteP()).asObject();
        });
        colPrixUnitaire.setCellValueFactory(data -> {
            Ligne_de_commande l = data.getValue();
            double prixUnitaire = l.getQteP() > 0 ? l.getMontant() / l.getQteP() : 0;
            return new javafx.beans.property.SimpleDoubleProperty(prixUnitaire).asObject();
        });
        colMontant.setCellValueFactory(new PropertyValueFactory<>("montant"));
    }

    private String trouverNomProduit(int idP) {
        for (Produits p : produitsDisponibles) {
            if (p.getIdP() == idP) {
                return p.getNomP();
            }
        }
        return "Inconnu";
    }

    private String trouverNomFournisseur(int idF) {
        List<Fournisseur> fournisseurs = fournisseurDAO.listerTous();
        for (Fournisseur f : fournisseurs) {
            if (f.getIdF() == idF) {
                return f.getNomF();
            }
        }
        return "Inconnu";
    }

    @FXML
    public void fermer() {
        Stage stage = (Stage) lblNumCm.getScene().getWindow();
        stage.close();
    }
}