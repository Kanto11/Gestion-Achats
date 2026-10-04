package com.mycompany.gestion_achats.models;

public class Fournir {

    private int idF;
    private int idP;
    private double prixFournisseur;

    public Fournir() {
    }

    public Fournir(int idF, int idP, double prixFournisseur) {
        this.idF = idF;
        this.idP = idP;
        this.prixFournisseur = prixFournisseur;
    }

    public int getIdF() {
        return idF;
    }

    public void setIdF(int idF) {
        this.idF = idF;
    }

    public int getIdP() {
        return idP;
    }

    public void setIdP(int idP) {
        this.idP = idP;
    }

    public double getPrixFournisseur() {
        return prixFournisseur;
    }

    public void setPrixFournisseur(double prixFournisseur) {
        this.prixFournisseur = prixFournisseur;
    }
}
