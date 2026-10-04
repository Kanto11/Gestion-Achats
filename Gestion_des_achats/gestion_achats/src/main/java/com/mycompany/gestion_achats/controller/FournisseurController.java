/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.gestion_achats.controller;
import com.mycompany.gestion_achats.models.Fournir;
import com.mycompany.gestion_achats.models.Produits;
import com.mycompany.gestion_achats.dao.FournirDAO;
import com.mycompany.gestion_achats.dao.ProduitDAO;
import com.mycompany.gestion_achats.models.Fournisseur;
import com.mycompany.gestion_achats.dao.FournisseurDAO;
import java.util.List;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.collections.ObservableList;
import javafx.collections.FXCollections;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.scene.control.TableCell;
import javafx.scene.control.Button;
import javafx.scene.layout.HBox;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.ButtonType;
import java.util.Optional;





public class FournisseurController {
    @FXML private TextField txtRecherche;
    @FXML private Label lblMessage;
    
    @FXML private TableView<Fournisseur> tableFournisseurs;
    @FXML private TableColumn<Fournisseur ,Integer> colId;
    @FXML private TableColumn<Fournisseur ,String> colNom;
    @FXML private TableColumn<Fournisseur ,String> colTel;
    @FXML private TableColumn<Fournisseur ,String> colAdresse;
    @FXML private TableColumn<Fournisseur , String> colEmail;
    @FXML private TableColumn<Fournisseur , Void> colActions;
    @FXML private Label lblNomFournisseurSelectionne;
    @FXML private TableView<Fournir> tableProduitsFournisseur;
    @FXML private TableColumn<Fournir, String> colPF;
    @FXML private TableColumn<Fournir, Double> colprixF;
    
    private final FournisseurDAO dao = new FournisseurDAO();
    private Fournisseur fournisseurSelectionne;
    private final FournirDAO fournirDAO = new FournirDAO();
    private final ProduitDAO produitDAO = new ProduitDAO();
    

    @FXML 
    public void ouvrirFormulaireAjout(){
        ouvrirFormulaire(null);
    }
    
    private void ouvrirFormulaire(Fournisseur fournisseurAModifier){
       try{
           FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/mycompany/gestion_achats/view/fournisseur_form.fxml"));
            Parent root = loader.load();
            
            FournisseurFormController controller = loader.getController();
            if (fournisseurAModifier == null) {
                controller.initModeAjout();
            } else {
                controller.initModeModification(fournisseurAModifier);
            }
            Stage stage = new Stage();
            stage.setTitle("Formulaire Fournisseur");
            stage.initModality(Modality.APPLICATION_MODAL);
            stage.setScene(new Scene(root));
            stage.showAndWait();

            if (controller.isSucces()) {
                chargerTableau();
                lblMessage.setText("Opération effectuée avec succès.");
            }
       }catch (Exception e) {
            e.printStackTrace();
            lblMessage.setText("Erreur d'ouverture du formulaire.");
    }
   }
   
   @FXML
    public void actualiserTableau() {
        chargerTableau();
        lblMessage.setText("");
}
    
    @FXML
    public void initialize(){
        colId.setCellValueFactory(new PropertyValueFactory<>("idF"));
        colNom.setCellValueFactory(new PropertyValueFactory<>("nomF"));
        colTel.setCellValueFactory(new PropertyValueFactory<>("telF"));
        colAdresse.setCellValueFactory(new PropertyValueFactory<>("adrF"));
        colEmail.setCellValueFactory(new PropertyValueFactory<>("emailF"));
        colPF.setCellValueFactory(data -> {
        String nom = trouverNomProduit(data.getValue().getIdP());
        return new javafx.beans.property.SimpleStringProperty(nom);
        });
        colprixF.setCellValueFactory(new PropertyValueFactory<>("prixFournisseur"));

        tableFournisseurs.getSelectionModel().selectedItemProperty().addListener((obs, ancien, nouveau) -> {
        if (nouveau != null) {
            afficherProduitsDuFournisseur(nouveau);
        }
    });
        configurerColonneActions();
        
        chargerTableau();
    }
    
    
    
    private void configurerColonneActions() {
        colActions.setCellFactory(colonne -> new TableCell<Fournisseur, Void>() {

            private final Button btnModifier = new Button("");
            private final Button btnSupprimer = new Button("");
            private final HBox conteneur = new HBox(5, btnModifier, btnSupprimer);

        {
            try {
               
                javafx.scene.image.Image imgModifier = new javafx.scene.image.Image(getClass().getResourceAsStream("/com/mycompany/gestion_achats/icons/edit.png"));
                javafx.scene.image.ImageView viewModifier = new javafx.scene.image.ImageView(imgModifier);
                viewModifier.setFitHeight(16);
                viewModifier.setFitWidth(16);  
                btnModifier.setGraphic(viewModifier); 

                javafx.scene.image.Image imgSupprimer = new javafx.scene.image.Image(getClass().getResourceAsStream("/com/mycompany/gestion_achats/icons/supprimer.png"));
                javafx.scene.image.ImageView viewSupprimer = new javafx.scene.image.ImageView(imgSupprimer);
                viewSupprimer.setFitHeight(16);
                viewSupprimer.setFitWidth(16);
                btnSupprimer.setGraphic(viewSupprimer);
                
            } catch (Exception e) {
                System.err.println("Impossible de charger les icônes de boutons : " + e.getMessage());
            }
            btnModifier.setStyle(
                "-fx-background-color: #131B2E; " +
                "-fx-background-radius: 4; " +
                "-fx-cursor: hand;"
            );

            
            btnSupprimer.setStyle(
                "-fx-background-color: #BA1A1A; " +
                
                "-fx-background-radius: 4; " +
                "-fx-cursor: hand;"
            );

            conteneur.setAlignment(javafx.geometry.Pos.CENTER);
            btnModifier.setOnAction(event -> {
                Fournisseur f = getTableView().getItems().get(getIndex());
                ouvrirFormulaire(f);
            });

            btnSupprimer.setOnAction(event -> {
                Fournisseur f = getTableView().getItems().get(getIndex());
                supprimerFournisseur(f);
            });
        }


        @Override
        protected void updateItem(Void item, boolean empty) {
            super.updateItem(item, empty);
            setGraphic(empty ? null : conteneur);
        }
    });
}
    
    private void chargerTableau(){
        List<Fournisseur> liste = dao.listerTous();
        ObservableList<Fournisseur> data = FXCollections.observableArrayList(liste);
        tableFournisseurs.setItems(data);
    }
     
    private void afficherErreur(String message){
        lblMessage.setTextFill(javafx.scene.paint.Color.RED);
        lblMessage.setText(message);
    }
    
    private void afficherSucces(String message){
        lblMessage.setTextFill(javafx.scene.paint.Color.GREEN);
        lblMessage.setText(message);
    }
      
    public void supprimerFournisseur(Fournisseur f) {
        if (f == null) {
            afficherErreur("Impossible de récupérer les données du fournisseur.");
            return;
        }
        Alert alert = new Alert(AlertType.CONFIRMATION);
        alert.setTitle("Confirmation de suppression");
        alert.setHeaderText(null);
        alert.setContentText("Voulez-vous vraiment supprimer le fournisseur : " + f.getNomF() + " ?");
    
        Optional<ButtonType> result = alert.showAndWait();
        
        if (result.isPresent() && result.get() == ButtonType.OK) {
            if (dao.Supprimer(f.getIdF())) {
                afficherSucces("Fournisseur supprimé avec succès.");
                chargerTableau(); 
            } else {
                afficherErreur("Erreur lors de la suppression dans la base de données.");
            }
        } else {
            System.out.println("Suppression annulée pour : " + f.getNomF());
        }
}
    @FXML

    public void rechercherFournisseur(){
        String nom = txtRecherche.getText().trim();
        if(nom.isEmpty()){
            chargerTableau();
            return;
        }
        List<Fournisseur> resultats = dao.rechercherParNom(nom);
        tableFournisseurs.setItems(FXCollections.observableArrayList(resultats));
    }
    
    private void afficherProduitsDuFournisseur(Fournisseur f) {
    lblNomFournisseurSelectionne.setText(" Fournisseur : " + f.getNomF());
    List<Fournir> liste = fournirDAO.listerParFournisseur(f.getIdF());
    tableProduitsFournisseur.setItems(FXCollections.observableArrayList(liste));
}

    private String trouverNomProduit(int idP) {
        List<Produits> produits = produitDAO.listerTous();
        for (Produits p : produits) {
            if (p.getIdP() == idP) {
             return p.getNomP();
        }
        }
        return "Inconnu";
}
    
}
