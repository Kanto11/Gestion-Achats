/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.gestion_achats.models;

public class Ligne_de_commande {
    private int idLc;
    private int numCm;
    private int idP;
    private int qteP;
    private double montant;
    
    public Ligne_de_commande(){
    }
    
    public Ligne_de_commande(int idLc , int numCm , int idP , int qteP , double montant){
        this.idLc = idLc;
        this.numCm = numCm;
        this.idP = idP;
        this.qteP = qteP;
        this.montant = montant;
    }
    
    public int getIdLc(){
        return idLc;
    }
    
    public int getNumCm(){
        return numCm;
    }
    
    public int getIdP(){
        return idP;
    }
    
    public int getQteP(){
        return qteP;
    }
    
    public double getMontant(){
        return montant;
    }
    
    public void setIdLc(int idLc){
        this.idLc = idLc;
    }
    
    public void setNumCm(int numCm){
        this.numCm = numCm;
    }
    
    public void setIdP(int idP){
        this.idP = idP;
    }
    
    public void setQteP(int qteP){
        this.qteP = qteP;
    }
    
    public void setMontant(double montant){
        this.montant = montant;
    }
}
