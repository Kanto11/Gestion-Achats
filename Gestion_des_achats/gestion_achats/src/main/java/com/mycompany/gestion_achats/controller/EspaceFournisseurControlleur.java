package com.mycompany.gestion_achats.controller;

import com.mycompany.gestion_achats.dao.CommandeDAO;
import com.mycompany.gestion_achats.dao.FournisseurDAO;
import com.mycompany.gestion_achats.dao.LigneDeCommandeDAO;
import com.mycompany.gestion_achats.models.Commande;
import com.mycompany.gestion_achats.models.Fournisseur;
import com.mycompany.gestion_achats.models.Ligne_de_commande;
import java.util.List;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.HBox;
import javafx.stage.Modality;
import javafx.stage.Stage;

public class EspaceFournisseurControlleur {

    @FXML private Label lblMessage;

    @FXML private TableView<Commande> tableCommandes;
    @FXML private TableColumn<Commande, Integer> colNumCm;
    @FXML private TableColumn<Commande, String> colDateCm;
    @FXML private TableColumn<Commande, String> colDateLivraison;
    @FXML private TableColumn<Commande, String> colFournisseur;
    @FXML private TableColumn<Commande, String> colMontantTotal;
    @FXML private TableColumn<Commande, Void> colActions;

    private final CommandeDAO commandeDAO = new CommandeDAO();
    private final FournisseurDAO fournisseurDAO = new FournisseurDAO();
    private final LigneDeCommandeDAO ligneDAO = new LigneDeCommandeDAO();

    @FXML
    public void initialize() {
        colNumCm.setCellValueFactory(new PropertyValueFactory<>("numCm"));

        colDateCm.setCellValueFactory(data -> {
            String date = data.getValue().getDateCm() != null ? data.getValue().getDateCm().toString() : "";
            return new javafx.beans.property.SimpleStringProperty(date);
        });

        colDateLivraison.setCellValueFactory(data -> {
            String date = data.getValue().getDatelivraisonCm() != null
                    ? data.getValue().getDatelivraisonCm().toString() : "Non definie";
            return new javafx.beans.property.SimpleStringProperty(date);
        });

        colFournisseur.setCellValueFactory(data -> {
            String nom = trouverNomFournisseur(data.getValue().getIdF());
            return new javafx.beans.property.SimpleStringProperty(nom);
        });

        colMontantTotal.setCellValueFactory(data -> {
            double total = calculerTotalCommande(data.getValue().getNumCm());
            return new javafx.beans.property.SimpleStringProperty(String.format("%.2f Ar", total));
        });

        configurerColonneActions();
        chargerTableau();
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

    private double calculerTotalCommande(int numCm) {
        List<Ligne_de_commande> lignes = ligneDAO.listerParCommande(numCm);
        double total = 0;
        for (Ligne_de_commande l : lignes) {
            total += l.getMontant();
        }
        return total;
    }

    private void chargerTableau() {
        List<Commande> liste = commandeDAO.listerParStatut("En attente");
        ObservableList<Commande> data = FXCollections.observableArrayList(liste);
        tableCommandes.setItems(data);
    }

    private void configurerColonneActions() {
        colActions.setCellFactory(colonne -> new TableCell<Commande, Void>() {

            private final Button btnVoir = new Button("Voir");
            private final Button btnValider = new Button("Valider");
            private final HBox conteneur = new HBox(5, btnVoir, btnValider);

            {{
    btnVoir.setStyle(
        "-fx-background-color: #131B2E; " +
        "-fx-text-fill: white; " +
        "-fx-background-radius: 4; " +
        "-fx-cursor: hand;"
    );

    btnValider.setStyle(
        "-fx-background-color: #515F74; " +
        "-fx-text-fill: #EAEDFF; " +
        "-fx-background-radius: 4; " +
        "-fx-cursor: hand;"
    );

    btnVoir.setOnAction(event -> {
        Commande c = getTableView().getItems().get(getIndex());
        ouvrirDetails(c);
    });

    btnValider.setOnAction(event -> {
        Commande c = getTableView().getItems().get(getIndex());
        validerCommande(c);
    });
}
            }

            @Override
            protected void updateItem(Void item, boolean empty) {
                super.updateItem(item, empty);
                setGraphic(empty ? null : conteneur);
            }
        });
    }

    private void validerCommande(Commande c) {
        if (commandeDAO.changerStatut(c.getNumCm(), "Validee")) {
            lblMessage.setText("Commande N° " + c.getNumCm() + " validee avec succes.");
            chargerTableau();
        } else {
            lblMessage.setText("Erreur lors de la validation.");
        }
    }

    private void ouvrirDetails(Commande c) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/mycompany/gestion_achats/view/commande_details.fxml"));
            Parent root = loader.load();

            CommandeDetailsController controller = loader.getController();
            controller.initDetails(c);

            Stage stage = new Stage();
            stage.setTitle("Details Commande");
            stage.initModality(Modality.APPLICATION_MODAL);
            stage.setScene(new Scene(root));
            stage.showAndWait();

        } catch (Exception e) {
            e.printStackTrace();
            lblMessage.setText("Erreur d'ouverture des details.");
        }
    }

    @FXML
    public void actualiserTableau() {
        chargerTableau();
        lblMessage.setText("");
    }
}