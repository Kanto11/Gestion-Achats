package com.mycompany.gestion_achats.controller;

import com.mycompany.gestion_achats.dao.LigneDeCommandeDAO;
import com.mycompany.gestion_achats.dao.PaimentDAO;
import com.mycompany.gestion_achats.models.Commande;
import com.mycompany.gestion_achats.models.Ligne_de_commande;
import com.mycompany.gestion_achats.models.Paiement;
import java.time.LocalDate;
import java.util.List;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.scene.control.ComboBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;

public class PaiementFormController {

    @FXML private Label lblInfoCommande;
    @FXML private Label lblResteAPayer;
    @FXML private TextField txtMontant;
    @FXML private DatePicker dateP;
    @FXML private ComboBox<String> comboMode;
    @FXML private Label lblErreur;

    @FXML private TableView<Paiement> tableHistorique;
    @FXML private TableColumn<Paiement, String> colDate;
    @FXML private TableColumn<Paiement, Double> colMontant;
    @FXML private TableColumn<Paiement, String> colMode;

    private final PaimentDAO paiementDAO = new PaimentDAO();
    private final LigneDeCommandeDAO ligneDAO = new LigneDeCommandeDAO();

    private Commande commande;
    private double montantTotalCommande;
    private boolean succes = false;

    @FXML
    public void initialize() {
        comboMode.setItems(FXCollections.observableArrayList("Especes", "Cheque", "Virement", "Mobile Money"));
        dateP.setValue(LocalDate.now());

        colDate.setCellValueFactory(data -> new javafx.beans.property.SimpleStringProperty(
                data.getValue().getDatePaiement().toString()));
        colMontant.setCellValueFactory(new PropertyValueFactory<>("montantPaye"));
        colMode.setCellValueFactory(new PropertyValueFactory<>("modePaiement"));
    }

    public void initPourCommande(Commande c) {
        this.commande = c;

        List<Ligne_de_commande> lignes = ligneDAO.listerParCommande(c.getNumCm());
        montantTotalCommande = 0;
        for (Ligne_de_commande l : lignes) {
            montantTotalCommande += l.getMontant();
        }

        lblInfoCommande.setText("Commande N° " + c.getNumCm() + " - Total : " + String.format("%.2f Ar", montantTotalCommande));

        rafraichirHistoriqueEtReste();
    }

    private void rafraichirHistoriqueEtReste() {
        List<Paiement> historique = paiementDAO.listerParCommande(commande.getNumCm());
        tableHistorique.setItems(FXCollections.observableArrayList(historique));

        double dejaPayel = paiementDAO.totalPayePourCommande(commande.getNumCm());
        double reste = montantTotalCommande - dejaPayel;
        lblResteAPayer.setText(String.format("Deja paye : %.2f Ar   |   Reste a payer : %.2f Ar", dejaPayel, reste));
    }

    public boolean isSucces() {
        return succes;
    }

    @FXML
    public void enregistrer() {
        lblErreur.setText("");

        double montant;
        try {
            montant = Double.parseDouble(txtMontant.getText().trim().replace(",", "."));
            if (montant <= 0) {
                lblErreur.setText("Le montant doit être positif.");
                return;
            }
        } catch (NumberFormatException e) {
            lblErreur.setText("Le montant doit être un nombre valide.");
            return;
        }

        if (dateP.getValue() == null) {
            lblErreur.setText("La date est obligatoire.");
            return;
        }
        if (comboMode.getValue() == null) {
            lblErreur.setText("Choisissez un mode de paiement.");
            return;
        }

        Paiement p = new Paiement(0, commande.getNumCm(), montant, dateP.getValue(), comboMode.getValue());

        if (paiementDAO.ajouter(p)) {
            succes = true;
            txtMontant.clear();
            comboMode.setValue(null);
            rafraichirHistoriqueEtReste();
        } else {
            lblErreur.setText("Erreur lors de l'enregistrement du paiement.");
        }
    }

    @FXML
    public void annuler() {
        fermerFenetre();
    }

    private void fermerFenetre() {
        Stage stage = (Stage) txtMontant.getScene().getWindow();
        stage.close();
    }
}