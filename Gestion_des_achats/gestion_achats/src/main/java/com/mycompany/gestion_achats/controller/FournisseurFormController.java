package com.mycompany.gestion_achats.controller;

import com.mycompany.gestion_achats.dao.FournisseurDAO;
import com.mycompany.gestion_achats.dao.FournirDAO;
import com.mycompany.gestion_achats.dao.ProduitDAO;
import com.mycompany.gestion_achats.models.Fournir;
import com.mycompany.gestion_achats.models.Fournisseur;
import com.mycompany.gestion_achats.models.Produits;
import java.util.List;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.HBox;
import javafx.stage.Stage;

public class FournisseurFormController {

    @FXML private Label lblTitre;
    @FXML private TextField txtNom;
    @FXML private TextField txtTel;
    @FXML private TextField txtAdresse;
    @FXML private TextField txtEmail;
    @FXML private Label lblErreur;

    @FXML private ComboBox<Produits> comboProduit;
    @FXML private TextField txtPrix;
    @FXML private TableView<Fournir> tableProduitsFournis;
    @FXML private TableColumn<Fournir, String> colProduitNom;
    @FXML private TableColumn<Fournir, Double> colProduitPrix;
    @FXML private TableColumn<Fournir, Void> colProduitActions;

    private final FournisseurDAO fournisseurDAO = new FournisseurDAO();
    private final FournirDAO fournirDAO = new FournirDAO();
    private final ProduitDAO produitDAO = new ProduitDAO();

    private final ObservableList<Fournir> produitsFournisEnCours = FXCollections.observableArrayList();
    private List<Produits> produitsDisponibles;

    private Fournisseur fournisseurAModifier;
    private boolean succes = false;

    @FXML
    public void initialize() {
        produitsDisponibles = produitDAO.listerTous();
        comboProduit.setItems(FXCollections.observableArrayList(produitsDisponibles));

        configurerColonnesProduits();
        tableProduitsFournis.setItems(produitsFournisEnCours);
    }

    private void configurerColonnesProduits() {
        colProduitNom.setCellValueFactory(data -> {
            String nom = trouverNomProduit(data.getValue().getIdP());
            return new javafx.beans.property.SimpleStringProperty(nom);
        });
        colProduitPrix.setCellValueFactory(new PropertyValueFactory<>("prixFournisseur"));

        colProduitActions.setCellFactory(colonne -> new TableCell<Fournir, Void>() {
            private final Button btnRetirer = new Button("Retirer");
            private final HBox conteneur = new HBox(btnRetirer);

            {
                btnRetirer.getStyleClass().addAll("btn", "btn-danger");
                btnRetirer.setOnAction(event -> {
                    Fournir f = getTableView().getItems().get(getIndex());
                    produitsFournisEnCours.remove(f);
                });
            }

            @Override
            protected void updateItem(Void item, boolean empty) {
                super.updateItem(item, empty);
                setGraphic(empty ? null : conteneur);
            }
        });
    }

    private String trouverNomProduit(int idP) {
        for (Produits p : produitsDisponibles) {
            if (p.getIdP() == idP) {
                return p.getNomP();
            }
        }
        return "Inconnu";
    }

    public void initModeAjout() {
        lblTitre.setText("Ajouter un Fournisseur");
        fournisseurAModifier = null;
    }

    public void initModeModification(Fournisseur f) {
        lblTitre.setText("Modifier un Fournisseur");
        fournisseurAModifier = f;
        txtNom.setText(f.getNomF());
        txtTel.setText(f.getTelF());
        txtAdresse.setText(f.getAdrF());
        txtEmail.setText(f.getEmailF());

        List<Fournir> liensExistants = fournirDAO.listerParFournisseur(f.getIdF());
        produitsFournisEnCours.setAll(liensExistants);
    }

    public boolean isSucces() {
        return succes;
    }

    @FXML
    public void ajouterProduitFourni() {
        lblErreur.setText("");

        Produits produitChoisi = comboProduit.getValue();
        if (produitChoisi == null) {
            lblErreur.setText("Choisissez un produit.");
            return;
        }

        double prix;
        try {
            prix = Double.parseDouble(txtPrix.getText().trim().replace(",", "."));
            if (prix <= 0) {
                lblErreur.setText("Le prix doit être un nombre positif.");
                return;
            }
        } catch (NumberFormatException e) {
            lblErreur.setText("Le prix doit être un nombre valide.");
            return;
        }

        for (Fournir f : produitsFournisEnCours) {
            if (f.getIdP() == produitChoisi.getIdP()) {
                lblErreur.setText("Ce produit est déjà dans la liste.");
                return;
            }
        }

        Fournir lien = new Fournir(0, produitChoisi.getIdP(), prix);
        produitsFournisEnCours.add(lien);

        comboProduit.setValue(null);
        txtPrix.clear();
    }

    @FXML
    public void enregistrer() {
        if (!validerChamps()) return;

        boolean ok;
        int idF;

        if (fournisseurAModifier == null) {
            Fournisseur f = new Fournisseur(0, txtNom.getText(), txtTel.getText(), txtAdresse.getText(), txtEmail.getText());
            idF = fournisseurDAO.Ajouter(f);
            if (idF == -1) {
            lblErreur.setText("Erreur lors de l'enregistrement du fournisseur.");
            return;
            }

        } else {
            fournisseurAModifier.setNomF(txtNom.getText());
            fournisseurAModifier.setTelF(txtTel.getText());
            fournisseurAModifier.setAdrF(txtAdresse.getText());
            fournisseurAModifier.setEmailF(txtEmail.getText());
            ok = fournisseurDAO.Modifier(fournisseurAModifier);
            if (!ok) {
                lblErreur.setText("Erreur lors de la modification du fournisseur.");
                return;
            }
            idF = fournisseurAModifier.getIdF();
            fournirDAO.supprimerParFournisseur(idF);
        }

        for (Fournir lien : produitsFournisEnCours) {
            lien.setIdF(idF);
            fournirDAO.ajouter(lien);
        }

        succes = true;
        fermerFenetre();
    }

    @FXML
    public void annuler() {
        succes = false;
        fermerFenetre();
    }

    private boolean validerChamps() {
        if (txtNom.getText().trim().isEmpty()
                || txtTel.getText().trim().isEmpty()
                || txtEmail.getText().trim().isEmpty()) {
            lblErreur.setText("Nom, téléphone et email sont obligatoires.");
            return false;
        }
        if (!txtTel.getText().matches("[0-9]{10}")) {
            lblErreur.setText("Le téléphone doit contenir exactement 10 chiffres.");
            return false;
        }
        if (!txtEmail.getText().contains("@")) {
            lblErreur.setText("L'email doit contenir un @.");
            return false;
        }
        return true;
    }

    private void fermerFenetre() {
        Stage stage = (Stage) txtNom.getScene().getWindow();
        stage.close();
    }
}