package com.mycompany.gestion_achats.controller;

import com.mycompany.gestion_achats.dao.FournirDAO;
import com.mycompany.gestion_achats.models.Fournir;
import com.mycompany.gestion_achats.dao.CommandeDAO;
import com.mycompany.gestion_achats.dao.FournisseurDAO;
import com.mycompany.gestion_achats.dao.LigneDeCommandeDAO;
import com.mycompany.gestion_achats.dao.ProduitDAO;
import com.mycompany.gestion_achats.models.Commande;
import com.mycompany.gestion_achats.models.Fournisseur;
import com.mycompany.gestion_achats.models.Ligne_de_commande;
import com.mycompany.gestion_achats.models.Produits;
import java.time.LocalDate;
import java.util.List;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Label;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.layout.HBox;
import javafx.stage.Stage;
import java.time.format.DateTimeFormatter;
import javafx.scene.control.DateCell;

public class CommandeFormController {

    @FXML private ComboBox<Fournisseur> comboFournisseur;
    @FXML private DatePicker dateCommande;
    @FXML private Label dateCommandes;
    @FXML private DatePicker dateLivraison;

    @FXML private ComboBox<Produits> comboProduit;
    @FXML private TextField txtQte;

    @FXML private TableView<Ligne_de_commande> tableLignes;
    @FXML private TableColumn<Ligne_de_commande, String> colLigneProduit;
    @FXML private TableColumn<Ligne_de_commande, Integer> colLigneQte;
    @FXML private TableColumn<Ligne_de_commande, Double> colLignePrixUnitaire;
    @FXML private TableColumn<Ligne_de_commande, Double> colLigneMontant;
    @FXML private TableColumn<Ligne_de_commande, Void> colLigneActions;

    @FXML private Label lblTotal;
    @FXML private Label lblErreur;

    private final CommandeDAO commandeDAO = new CommandeDAO();
    private final LigneDeCommandeDAO ligneDAO = new LigneDeCommandeDAO();
    private final FournisseurDAO fournisseurDAO = new FournisseurDAO();
    private final ProduitDAO produitDAO = new ProduitDAO();

    private final ObservableList<Ligne_de_commande> lignesEnCours = FXCollections.observableArrayList();
    private List<Produits> produitsDisponibles;
    private final FournirDAO fournirDAO = new FournirDAO();
    private List<Fournir> produitsDuFournisseurChoisi;
    
    private boolean succes = false;

    @FXML
    public void initialize() {
        comboFournisseur.setItems(FXCollections.observableArrayList(fournisseurDAO.listerTous()));

        produitsDisponibles = produitDAO.listerTous();
        comboProduit.setItems(FXCollections.observableArrayList(produitsDisponibles));
        comboFournisseur.valueProperty().addListener((obs, ancien, nouveau) -> {
        if (nouveau != null) {
            chargerProduitsDuFournisseur(nouveau.getIdF());
        } else {
            comboProduit.setItems(FXCollections.observableArrayList());
        }
        });
        configurerColonnesLignes();
        tableLignes.setItems(lignesEnCours);

        lignesEnCours.addListener((javafx.collections.ListChangeListener<Ligne_de_commande>) change -> recalculerTotal());

        dateCommande.setValue(LocalDate.now());
        dateCommande.setVisible(false);
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");
        String dateDuJour = LocalDate.now().format(formatter);
        dateCommandes.setText(dateDuJour);
        bloquerDate(dateLivraison);
        dateLivraison.setEditable(false);
    }

    private void configurerColonnesLignes() {
        colLigneProduit.setCellValueFactory(data -> {
            String nom = trouverNomProduit(data.getValue().getIdP());
            return new javafx.beans.property.SimpleStringProperty(nom);
        });
  
        colLigneQte.setCellValueFactory(data -> {
            return new javafx.beans.property.SimpleIntegerProperty(data.getValue().getQteP()).asObject();
        });
        colLignePrixUnitaire.setCellValueFactory(data -> {
            Ligne_de_commande l = data.getValue();
            double prixUnitaire = l.getQteP() > 0 ? l.getMontant() / l.getQteP() : 0;
            return new javafx.beans.property.SimpleDoubleProperty(prixUnitaire).asObject();
        });
        colLigneMontant.setCellValueFactory(new javafx.scene.control.cell.PropertyValueFactory<>("montant"));

        colLigneActions.setCellFactory(colonne -> new TableCell<Ligne_de_commande, Void>() {
            private final Button btnSupprimer = new Button("Retirer");
            private final HBox conteneur = new HBox(btnSupprimer);

            {
                btnSupprimer.getStyleClass().addAll("btn", "btn-danger");
                btnSupprimer.setOnAction(event -> {
                    Ligne_de_commande l = getTableView().getItems().get(getIndex());
                    lignesEnCours.remove(l);
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

    private void recalculerTotal() {
        double total = 0;
        for (Ligne_de_commande l : lignesEnCours) {
            total += l.getMontant();
        }
        lblTotal.setText(String.format("Total : %.2f Ar", total));
    }

    public boolean isSucces() {
        return succes;
    }

    @FXML
    public void ajouterLigne() {
        lblErreur.setText("");

        Produits produitChoisi = comboProduit.getValue();
        if (produitChoisi == null) {
            lblErreur.setText("Choisissez un produit.");
            return;
        }

        
        int qte;
        try {
            qte = Integer.parseInt(txtQte.getText().trim());
            if (qte <= 0) {
                lblErreur.setText("La quantité doit être un nombre positif.");
                return;
            }
        } catch (NumberFormatException e) {
            lblErreur.setText("La quantité doit être un nombre entier valide.");
            return;
        }

       
        double prixUnitaire = fournirDAO.obtenirPrix(comboFournisseur.getValue().getIdF(), produitChoisi.getIdP());
        if (prixUnitaire == -1) {
        lblErreur.setText("Ce produit n'est plus fourni par ce fournisseur.");
        return;
        }
double montant = qte * prixUnitaire;

        Ligne_de_commande ligne = new Ligne_de_commande(0,0,produitChoisi.getIdP(), qte, montant);
        lignesEnCours.add(ligne);

        comboProduit.setValue(null);
        txtQte.clear();
    }

    @FXML
    public void enregistrer() {
        if (!validerCommande()) return;

        Commande c = new Commande();
        c.setDateCm(dateCommande.getValue());
        c.setDatelivraisonCm(dateLivraison.getValue());
        c.setStatutCm("En attente");
        c.setIdF(comboFournisseur.getValue().getIdF());

        int numCm = commandeDAO.ajouter(c);
        if (numCm == -1) {
            lblErreur.setText("Erreur lors de l'enregistrement de la commande.");
            return;
        }

        for (Ligne_de_commande l : lignesEnCours) {
            l.setNumCm(numCm);
            ligneDAO.ajouter(l);
        }

        succes = true;
        fermerFenetre();
    }

    @FXML
    public void annuler() {
        succes = false;
        fermerFenetre();
    }

    private boolean validerCommande() {
        if (comboFournisseur.getValue() == null) {
            lblErreur.setText("Choisissez un fournisseur.");
            return false;
        }
        if (dateCommande.getValue() == null) {
            lblErreur.setText("La date de commande est obligatoire.");
            return false;
        }
        if (lignesEnCours.isEmpty()) {
            lblErreur.setText("Ajoutez au moins une ligne de produit.");
            return false;
        }
        return true;
    }
    

    private void fermerFenetre() {
        Stage stage = (Stage) lblTotal.getScene().getWindow();
        stage.close();
    }
    
    private void chargerProduitsDuFournisseur(int idF) {
    produitsDuFournisseurChoisi = fournirDAO.listerParFournisseur(idF);

    List<Produits> produitsFiltres = new java.util.ArrayList<>();
    for (Fournir lien : produitsDuFournisseurChoisi) {
        for (Produits p : produitsDisponibles) {
            if (p.getIdP() == lien.getIdP()) {
                produitsFiltres.add(p);
                break;
            }
        }
    }

    comboProduit.setItems(FXCollections.observableArrayList(produitsFiltres));
}
       
    public void bloquerDate(DatePicker picker){
        picker.setDayCellFactory(p -> new DateCell(){
            @Override
            public void updateItem(LocalDate date , boolean empty){
                super.updateItem(date,empty);
                LocalDate demain = LocalDate.now().plusDays(1);
                if(date != null && date.isBefore(demain)){
                    setDisable(true);
                    setStyle("-fx-background-color: #eeeeee;");
                }
            }
        });
    }
}
