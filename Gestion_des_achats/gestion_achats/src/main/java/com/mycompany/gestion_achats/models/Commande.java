/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.gestion_achats.models;
import java.time.LocalDate;

public class Commande {
    private int numCm;
    private LocalDate dateCm;
    private LocalDate datelivraisonCm;
    private String statutCm;
    private int idF;
    
    public Commande(){
    }
    
    public Commande(int numCm , LocalDate dateCm ,LocalDate datelivraisonCm, String statutCm,int idF){
        this.numCm = numCm;
        this.dateCm = dateCm;
        this.datelivraisonCm = datelivraisonCm;
        this.statutCm = statutCm;
        this.idF = idF;
    }
    
    public int getNumCm(){
        return numCm;
    }
    
    public LocalDate getDateCm(){
        return dateCm;
    }
    
    public LocalDate getDatelivraisonCm(){
        return datelivraisonCm;
    }
    
    public String getStatutCm(){
        return statutCm;
    }
    
    public int getIdF(){
        return idF;
    }
    
    
    public void setNumCm(int numCm){
        this.numCm = numCm;
    }
    
    public void setDateCm(LocalDate dateCm){
        this.dateCm = dateCm;
    }
    
    public void setDatelivraisonCm(LocalDate datelivraisonCm){
        this.datelivraisonCm = datelivraisonCm;
    }
    
    public void setStatutCm(String statutCm){
        this.statutCm = statutCm;
    }
    
    public void setIdF(int idF){
        this.idF = idF;
    }
}
