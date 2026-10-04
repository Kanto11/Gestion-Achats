package com.mycompany.gestion_achats.controller;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.fxml.FXMLLoader;
import javafx.stage.Stage;
import javafx.collections.ObservableList;
import javafx.collections.FXCollections;
import java.util.List;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.layout.HBox;
import javafx.scene.control.TableCell;
import javafx.scene.control.cell.PropertyValueFactory;
import com.mycompany.gestion_achats.models.Produits;
import com.mycompany.gestion_achats.dao.ProduitDAO;
import com.mycompany.gestion_achats.models.Categorie;
import com.mycompany.gestion_achats.dao.CategorieDAO;
public class ProduitsController{
    
    @FXML private TextField txtRecherche;
    @FXML private Label lblMessage;
    
    @FXML private TableView<Produits> tableProduits;
    @FXML private TableColumn<Produits ,Integer> colIdP;
    @FXML private TableColumn<Produits ,String> colNomP;
    @FXML private TableColumn<Produits ,String> colCategorieP;
    @FXML private TableColumn<Produits , Void> colActionP;
    @FXML private ComboBox<Categorie> comboFiltreCategorie;
    private final ProduitDAO produitdao = new ProduitDAO();
    
    @FXML private TableView<Categorie> TCategorie;
    @FXML private TableColumn<Categorie ,Integer> colIdC;
    @FXML private TableColumn<Categorie ,String> colNomC;
    @FXML private TableColumn<Categorie , Void> colActionC;
    private final CategorieDAO categoriedao = new CategorieDAO();
    
    @FXML 
    
    private void ouvrirFormulairePparent() {
        ouvrirFormulairePparent(null); 
    }
    
    private void ouvrirFormulairePparent(Produits produitsAModifier) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/mycompany/gestion_achats/view/produits_form_view.fxml"));
            javafx.scene.Parent root = loader.load();
        
            ProduitsFormController controller = loader.getController();
            if (produitsAModifier == null) {
                controller.initModeAjout();
            } else {
                controller.initModeModification(produitsAModifier);
            }

            javafx.stage.Stage stage = new Stage();
            stage.setTitle("Formulaire produits");
            stage.setScene(new javafx.scene.Scene(root));
            stage.showAndWait(); 

        
            if (controller.isSucces()) {
                chargerDonneesTableauP(); 
            }
    
        } catch (Exception e) {
            e.printStackTrace();
            System.out.println("Erreur lors de l'ouverture du formulaire catégorie.");
        }
    }
    
     
    @FXML 
    public void initialize(){
        colIdP.setCellValueFactory(new PropertyValueFactory<>("IdP"));
        colNomP.setCellValueFactory(new PropertyValueFactory<>("NomP"));
        colCategorieP.setCellValueFactory(cellData -> {
        int idC = cellData.getValue().getIdC(); 
        String nomCategorie = trouverNomCategorie(idC);
        return new javafx.beans.property.SimpleStringProperty(nomCategorie);
        });
        colIdC.setCellValueFactory(new PropertyValueFactory<>("idC"));
        colNomC.setCellValueFactory(new PropertyValueFactory<>("nomC"));
        configurerColonneActions();
        chargerDonneesTableau();
        configurerColonneActionsP();
        chargerFiltreCategories();
        chargerDonneesTableauP();
    }
    
    private String trouverNomCategorie(int idC) {
    List<Categorie> categories = categoriedao.listertous();
    for (Categorie c : categories) {
        if (c.getIdC() == idC) {
            return c.getNomC();
        }
    }
    return "Inconnue";
}

    private void chargerDonneesTableauP() {
        List<Produits> listeBrute = produitdao.listerTous(); 
        ObservableList<Produits> listeObservable = FXCollections.observableArrayList(listeBrute);
        tableProduits.setItems(listeObservable);
    }
    

     private void configurerColonneActionsP() {
        colActionP.setCellFactory(colonne -> new TableCell<Produits, Void>() {

            private final Button btnModifier = new Button("");
            private final HBox conteneur = new HBox(5, btnModifier);

        {
            try {
               
                javafx.scene.image.Image imgModifier = new javafx.scene.image.Image(getClass().getResourceAsStream("/com/mycompany/gestion_achats/icons/edit.png"));
                javafx.scene.image.ImageView viewModifier = new javafx.scene.image.ImageView(imgModifier);
                viewModifier.setFitHeight(16);
                viewModifier.setFitWidth(16);  
                btnModifier.setGraphic(viewModifier); 
                
            } catch (Exception e) {
                System.err.println("Impossible de charger les icônes de boutons : " + e.getMessage());
            }
            btnModifier.setStyle(
                "-fx-background-color: #131B2E; " +
                "-fx-background-radius: 4; " +
                "-fx-cursor: hand;"
            );


            conteneur.setAlignment(javafx.geometry.Pos.CENTER);
            btnModifier.setOnAction(event -> {
                Produits f = getTableView().getItems().get(getIndex());
                ouvrirFormulairePparent(f);
            });

        }


        @Override
        protected void updateItem(Void item, boolean empty) {
            super.updateItem(item, empty);
            setGraphic(empty ? null : conteneur);
        }
    });
}
     
    @FXML
    public void rechercherProduit() {
        String nom = txtRecherche.getText().trim();
        if (nom.isEmpty()) {
            chargerDonneesTableauP();
            return;
        }
        List<Produits> resultats = produitdao.rechercherParNom(nom);
        tableProduits.setItems(FXCollections.observableArrayList(resultats));
    }
    
    
    
   

    private void chargerFiltreCategories() {
        List<Categorie> categories = categoriedao.listertous();
        comboFiltreCategorie.setItems(FXCollections.observableArrayList(categories));

        comboFiltreCategorie.valueProperty().addListener((obs, ancienne, nouvelle) -> {
            if (nouvelle != null) {
                List<Produits> resultats = produitdao.listerParCategorie(nouvelle.getIdC());
                tableProduits.setItems(FXCollections.observableArrayList(resultats));
            } else {
            chargerDonneesTableauP();
            }
        });
}

    @FXML
    public void actualiserTableau() {
        comboFiltreCategorie.setValue(null);
        txtRecherche.clear();
        chargerDonneesTableauP();
        lblMessage.setText("");
    }
    
    //CATEGORIE
    @FXML 
    
    private void ouvrirFormulaireCparent() {
        ouvrirFormulaireCparent(null); 
}

    private void ouvrirFormulaireCparent(Categorie categorieAModifier) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/mycompany/gestion_achats/view/categorie_form.fxml"));
            javafx.scene.Parent root = loader.load();
        
            CategorieFormController controller = loader.getController();
            if (categorieAModifier == null) {
                controller.initModeAjout();
            } else {
                controller.initModeModification(categorieAModifier);
            }

            javafx.stage.Stage stage = new Stage();
            stage.setTitle("Formulaire Catégorie");
            stage.setScene(new javafx.scene.Scene(root));
            stage.showAndWait(); 

        
            if (controller.isSucces()) {
                chargerDonneesTableau(); 
            }
    
        } catch (Exception e) {
            e.printStackTrace();
            System.out.println("Erreur lors de l'ouverture du formulaire catégorie.");
        }
    }

    
    
    private void chargerDonneesTableau() {
        List<Categorie> listeBrute = categoriedao.listertous(); 
        ObservableList<Categorie> listeObservable = FXCollections.observableArrayList(listeBrute);
       TCategorie.setItems(listeObservable);
    }
    
    private void configurerColonneActions() {
    colActionC.setCellFactory(colonne -> new TableCell<Categorie, Void>() {

            private final Button btnModifier = new Button("");
            private final HBox conteneur = new HBox(5, btnModifier);

        {
            try {
               
                javafx.scene.image.Image imgModifier = new javafx.scene.image.Image(getClass().getResourceAsStream("/com/mycompany/gestion_achats/icons/edit.png"));
                javafx.scene.image.ImageView viewModifier = new javafx.scene.image.ImageView(imgModifier);
                viewModifier.setFitHeight(16);
                viewModifier.setFitWidth(16);  
                btnModifier.setGraphic(viewModifier); 
                
            } catch (Exception e) {
                System.err.println("Impossible de charger les icônes de boutons : " + e.getMessage());
            }
            btnModifier.setStyle(
                "-fx-background-color: #131B2E; " +
                "-fx-background-radius: 4; " +
                "-fx-cursor: hand;"
            );


            conteneur.setAlignment(javafx.geometry.Pos.CENTER);
            btnModifier.setOnAction(event -> {
                Categorie f = getTableView().getItems().get(getIndex());
                ouvrirFormulaireCparent(f);
            });

        }


        @Override
        protected void updateItem(Void item, boolean empty) {
            super.updateItem(item, empty);
            setGraphic(empty ? null : conteneur);
        }
    });
    }
    
    
  
}