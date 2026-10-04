/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.gestion_achats.models;
import java.time.LocalDate;
/**
 *
 * @author Andriana
 */

public class Paiement {

    private int idPaiement;
    private int numCm;
    private double montantPaye;
    private LocalDate datePaiement;
    private String modePaiement;

    public Paiement() {
    }

    public Paiement(int idPaiement, int numCm, double montantPaye, LocalDate datePaiement, String modePaiement) {
        this.idPaiement = idPaiement;
        this.numCm = numCm;
        this.montantPaye = montantPaye;
        this.datePaiement = datePaiement;
        this.modePaiement = modePaiement;
    }

    public int getIdPaiement() {
        return idPaiement;
    }

    public void setIdPaiement(int idPaiement) {
        this.idPaiement = idPaiement;
    }

    public int getNumCm() {
        return numCm;
    }

    public void setNumCm(int numCm) {
        this.numCm = numCm;
    }

    public double getMontantPaye() {
        return montantPaye;
    }

    public void setMontantPaye(double montantPaye) {
        this.montantPaye = montantPaye;
    }

    public LocalDate getDatePaiement() {
        return datePaiement;
    }

    public void setDatePaiement(LocalDate datePaiement) {
        this.datePaiement = datePaiement;
    }

    public String getModePaiement() {
        return modePaiement;
    }

    public void setModePaiement(String modePaiement) {
        this.modePaiement = modePaiement;
    }
}