package com.mycompany.gestion_achats.controller;

import com.mycompany.gestion_achats.dao.CommandeDAO;
import com.mycompany.gestion_achats.dao.FournisseurDAO;
import com.mycompany.gestion_achats.dao.LigneDeCommandeDAO;
import com.mycompany.gestion_achats.dao.PaimentDAO;
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
import javafx.scene.control.DatePicker;
import javafx.scene.control.ComboBox;
import java.time.LocalDate;

public class CommandeController {

    @FXML private Label lblMessage;

    @FXML private TableView<Commande> tableCommandes;
    @FXML private TableColumn<Commande, Integer> colNumCm;
    @FXML private TableColumn<Commande, String> colDateCm;
    @FXML private TableColumn<Commande, String> colDateLivraison;
    @FXML private TableColumn<Commande, String> colStatut;
    @FXML private TableColumn<Commande, String> colFournisseur;
    @FXML private TableColumn<Commande, String> colMontantTotal;
    @FXML private TableColumn<Commande, String> colPaiement;
    @FXML private TableColumn<Commande, Void> colActions;
    
    @FXML private DatePicker dateDebut;
    @FXML private DatePicker dateFin;
    @FXML private ComboBox<Fournisseur> comboFiltreFournisseur;
    @FXML private ComboBox<String> comboFiltreStatut;
    @FXML private ComboBox<String> comboFiltrePaiement;

    private final CommandeDAO commandeDAO = new CommandeDAO();
    private final FournisseurDAO fournisseurDAO = new FournisseurDAO();
    private final LigneDeCommandeDAO ligneDAO = new LigneDeCommandeDAO();
    private final PaimentDAO paiementDAO = new PaimentDAO();

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

        colStatut.setCellValueFactory(new PropertyValueFactory<>("statutCm"));

        colFournisseur.setCellValueFactory(data -> {
            String nom = trouverNomFournisseur(data.getValue().getIdF());
            return new javafx.beans.property.SimpleStringProperty(nom);
        });

        colMontantTotal.setCellValueFactory(data -> {
            double total = calculerTotalCommande(data.getValue().getNumCm());
            return new javafx.beans.property.SimpleStringProperty(String.format("%.2f Ar", total));
        });

        colPaiement.setCellValueFactory(data -> {
            String statutPaiement = calculerStatutPaiement(data.getValue().getNumCm());
            return new javafx.beans.property.SimpleStringProperty(statutPaiement);
        });
        chargerFiltres();

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

    private String calculerStatutPaiement(int numCm) {
        double total = calculerTotalCommande(numCm);
        double paye = paiementDAO.totalPayePourCommande(numCm);

        if (paye <= 0) {
            return "Non paye";
        } else if (paye < total) {
            return "Partiellement paye";
        } else {
            return "Paye";
        }
    }

    private void chargerTableau() {
        List<Commande> liste = commandeDAO.listerTous();
        ObservableList<Commande> data = FXCollections.observableArrayList(liste);
        tableCommandes.setItems(data);
    }

    private void configurerColonneActions() {
        colActions.setCellFactory(colonne -> new TableCell<Commande, Void>() {

            private final Button btnVoir = new Button("Voir");
            private final Button btnAnnuler = new Button("Annuler");
            private final Button btnLivrer = new Button("Marquer Livree");
            private final Button btnPaiement = new Button("Paiement");
            private final HBox conteneur = new HBox(5);

            {
              {
    btnVoir.setStyle(
        "-fx-background-color: #131B2E; " +
        "-fx-text-fill: white; " +
        "-fx-background-radius: 4; " +
        "-fx-cursor: hand;"
    );

    btnAnnuler.setStyle(
        "-fx-background-color: #BA1A1A; " +
        "-fx-text-fill: white; " +
        "-fx-background-radius: 4; " +
        "-fx-cursor: hand;"
    );

    btnLivrer.getStyleClass().addAll("btn", "btn-primary");

    btnPaiement.setStyle(
        "-fx-background-color: #515F74; " +
        "-fx-text-fill: #EAEDFF; " +
        "-fx-background-radius: 4; " +
        "-fx-cursor: hand;"
    );

    btnVoir.setOnAction(event -> {
        Commande c = getTableView().getItems().get(getIndex());
        ouvrirDetails(c);
    });

    btnAnnuler.setOnAction(event -> {
        Commande c = getTableView().getItems().get(getIndex());
        changerStatut(c, "Annulee");
    });

    btnLivrer.setOnAction(event -> {
        Commande c = getTableView().getItems().get(getIndex());
        changerStatut(c, "Livree");
    });

    btnPaiement.setOnAction(event -> {
        Commande c = getTableView().getItems().get(getIndex());
        ouvrirPaiement(c);
    });
}
            }

            @Override
protected void updateItem(Void item, boolean empty) {
    super.updateItem(item, empty);

    if (empty) {
        setGraphic(null);
        return;
    }

    Commande c = getTableView().getItems().get(getIndex());
    conteneur.getChildren().clear();
    conteneur.getChildren().add(btnVoir);

    switch (c.getStatutCm()) {
        case "En attente":
            conteneur.getChildren().add(btnAnnuler);
            conteneur.getChildren().add(btnPaiement);
            break;
        case "Validee":
            conteneur.getChildren().add(btnLivrer);
            conteneur.getChildren().add(btnPaiement);
            break;
        case "Livree":
            conteneur.getChildren().add(btnPaiement);
            break;
        case "Annulee":
            
            break;
        default:
            break;
    }

        setGraphic(conteneur);
    }
        });
    }

    private void changerStatut(Commande c, String nouveauStatut) {
        if (commandeDAO.changerStatut(c.getNumCm(), nouveauStatut)) {
            lblMessage.setText("Statut mis a jour : " + nouveauStatut);
            chargerTableau();
        } else {
            lblMessage.setText("Erreur lors du changement de statut.");
        }
    }

    @FXML
    public void ouvrirFormulaireAjout() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/mycompany/gestion_achats/view/commande_form.fxml"));
            Parent root = loader.load();

            CommandeFormController controller = loader.getController();

            Stage stage = new Stage();
            stage.setTitle("Nouvelle Commande");
            stage.initModality(Modality.APPLICATION_MODAL);
            stage.setScene(new Scene(root));
            stage.showAndWait();

            if (controller.isSucces()) {
                chargerTableau();
                lblMessage.setText("Commande ajoutee avec succes.");
            }

        } catch (Exception e) {
            e.printStackTrace();
            lblMessage.setText("Erreur d'ouverture du formulaire.");
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

    private void ouvrirPaiement(Commande c) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/mycompany/gestion_achats/view/paiement_form.fxml"));
            Parent root = loader.load();

            PaiementFormController controller = loader.getController();
            controller.initPourCommande(c);

            Stage stage = new Stage();
            stage.setTitle("Paiement Commande N° " + c.getNumCm());
            stage.initModality(Modality.APPLICATION_MODAL);
            stage.setScene(new Scene(root));
            stage.showAndWait();

            chargerTableau();

        } catch (Exception e) {
            e.printStackTrace();
            lblMessage.setText("Erreur d'ouverture du paiement.");
        }
    }

    @FXML
    public void actualiserTableau() {
        chargerTableau();
        lblMessage.setText("");
    }
    
    private void chargerFiltres() {
    comboFiltreFournisseur.setItems(FXCollections.observableArrayList(fournisseurDAO.listerTous()));
    comboFiltreStatut.setItems(FXCollections.observableArrayList("En attente", "Validee", "Livree", "Annulee"));
    comboFiltrePaiement.setItems(FXCollections.observableArrayList("Non paye", "Partiellement paye", "Paye"));
}

@FXML
public void rechercherCommandes() {
    List<Commande> toutes = commandeDAO.listerTous();
    List<Commande> resultats = new java.util.ArrayList<>();

    for (Commande c : toutes) {
        
        if (dateDebut.getValue() != null && c.getDateCm().isBefore(dateDebut.getValue())) {
            continue;
        }
        if (dateFin.getValue() != null && c.getDateCm().isAfter(dateFin.getValue())) {
            continue;
        }

        
        if (comboFiltreFournisseur.getValue() != null
                && c.getIdF() != comboFiltreFournisseur.getValue().getIdF()) {
            continue;
        }

     
        if (comboFiltreStatut.getValue() != null
                && !c.getStatutCm().equals(comboFiltreStatut.getValue())) {
            continue;
        }


        if (comboFiltrePaiement.getValue() != null
                && !calculerStatutPaiement(c.getNumCm()).equals(comboFiltrePaiement.getValue())) {
            continue;
        }

        resultats.add(c);
    }

    tableCommandes.setItems(FXCollections.observableArrayList(resultats));
    lblMessage.setText(resultats.size() + " commande(s) trouvee(s).");
}

@FXML
public void reinitialiserFiltres() {
    dateDebut.setValue(null);
    dateFin.setValue(null);
    comboFiltreFournisseur.setValue(null);
    comboFiltreStatut.setValue(null);
    comboFiltrePaiement.setValue(null);
    chargerTableau();
    lblMessage.setText("");
}
}