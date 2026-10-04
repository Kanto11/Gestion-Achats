package com.mycompany.gestion_achats.controller;

import com.mycompany.gestion_achats.dao.StatistiqueDAO;
import java.util.Map;
import javafx.fxml.FXML;
import javafx.scene.chart.BarChart;
import javafx.scene.chart.CategoryAxis;
import javafx.scene.chart.LineChart;
import javafx.scene.chart.NumberAxis;
import javafx.scene.chart.PieChart;
import javafx.scene.chart.XYChart;
import javafx.scene.control.Label;
import javafx.scene.layout.StackPane;

public class DashboardController {

    @FXML private Label lblTotalFournisseurs;
    @FXML private Label lblTotalProduits;
    @FXML private Label lblTotalCommandes;
    @FXML private Label lblTotalAchats;
    @FXML private Label lblResteAPayer;
    @FXML private Label lblAlerteRetard;

    @FXML private StackPane conteneurChartStatut;
    @FXML private StackPane conteneurChartMois;
    @FXML private StackPane conteneurChartFournisseurs;
    @FXML private StackPane conteneurChartProduits;

    private final StatistiqueDAO statDAO = new StatistiqueDAO();

    @FXML
    public void initialize() {
        chargerChiffresClefs();
        chargerAlerteRetard();
        chargerChartStatut();
        chargerChartDepensesMois();
        chargerChartFournisseurs();
        chargerChartTopProduits();
    }

    private void chargerChiffresClefs() {
        lblTotalFournisseurs.setText(String.valueOf(statDAO.totalFournisseurs()));
        lblTotalProduits.setText(String.valueOf(statDAO.totalProduits()));
        lblTotalCommandes.setText(String.valueOf(statDAO.totalCommandes()));

        double totalAchats = statDAO.totalAchats();
        double totalPaye = statDAO.totalPaye();
        double reste = totalAchats - totalPaye;

        lblTotalAchats.setText(String.format("%.2f Ar", totalAchats));
        lblResteAPayer.setText(String.format("%.2f Ar", reste));
    }

    private void chargerAlerteRetard() {
        int nb = statDAO.nombreCommandesEnRetard();
        if (nb > 0) {
            lblAlerteRetard.setText("⚠ " + nb + " commande(s) en retard de livraison !");
        } else {
            lblAlerteRetard.setText("Aucune commande en retard.");
            lblAlerteRetard.setStyle("-fx-text-fill: #2e7d32; -fx-font-weight: bold;");
        }
    }

    private void chargerChartStatut() {
        PieChart chart = new PieChart();
        chart.setId("chartStatut");
        chart.setTitle("Commandes par statut");

        Map<String, Integer> repartition = statDAO.repartitionParStatut();
        for (Map.Entry<String, Integer> entree : repartition.entrySet()) {
            chart.getData().add(new PieChart.Data(entree.getKey() + " (" + entree.getValue() + ")", entree.getValue()));
        }

        conteneurChartStatut.getChildren().setAll(chart);
    }

    private void chargerChartDepensesMois() {
        CategoryAxis axeX = new CategoryAxis();
        axeX.setLabel("Mois");
        NumberAxis axeY = new NumberAxis();
        axeY.setLabel("Montant (Ar)");

        LineChart<String, Number> chart = new LineChart<>(axeX, axeY);
        chart.setTitle("Depenses par mois");

        XYChart.Series<String, Number> serie = new XYChart.Series<>();
        serie.setName("Depenses");

        Map<String, Double> depenses = statDAO.depensesParMois();
        for (Map.Entry<String, Double> entree : depenses.entrySet()) {
            serie.getData().add(new XYChart.Data<>(entree.getKey(), entree.getValue()));
        }

        chart.getData().add(serie);
        conteneurChartMois.getChildren().setAll(chart);
    }

    private void chargerChartFournisseurs() {
    CategoryAxis axeX = new CategoryAxis();
    axeX.setLabel("Fournisseur");
    NumberAxis axeY = new NumberAxis();
    axeY.setLabel("Montant (Ar)");

    BarChart<String, Number> chart = new BarChart<>(axeX, axeY);
    chart.setId("chartFournisseurs");
    chart.setTitle("Dépenses par fournisseur");

    XYChart.Series<String, Number> serie = new XYChart.Series<>();
    serie.setName("Montant");

    Map<String, Double> depenses = statDAO.depensesParFournisseur();
    for (Map.Entry<String, Double> entree : depenses.entrySet()) {
        serie.getData().add(new XYChart.Data<>(entree.getKey(), entree.getValue()));
    }

    chart.getData().add(serie);

    conteneurChartFournisseurs.getChildren().setAll(chart);
}

    private void chargerChartTopProduits() {
        CategoryAxis axeX = new CategoryAxis();
        axeX.setLabel("Produit");
        NumberAxis axeY = new NumberAxis();
        axeY.setLabel("Quantite");

        BarChart<String, Number> chart = new BarChart<>(axeX, axeY);
        chart.setId("monDeuxiemeChart");
        chart.setTitle("Top produits commandes");

        XYChart.Series<String, Number> serie = new XYChart.Series<>();
        serie.setName("Quantite");

        Map<String, Integer> topProduits = statDAO.topProduits(5);
        for (Map.Entry<String, Integer> entree : topProduits.entrySet()) {
            serie.getData().add(new XYChart.Data<>(entree.getKey(), entree.getValue()));
        }

        chart.getData().add(serie);
        conteneurChartProduits.getChildren().setAll(chart);
    }
}