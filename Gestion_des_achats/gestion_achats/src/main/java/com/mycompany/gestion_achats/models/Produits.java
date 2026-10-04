/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.gestion_achats.models;

public class Produits {
    private int idP;
    private String nomP;
    private int idC;
    
    public Produits(){
    
    }
    
    public Produits(int idP, String nomP ,int idC){
        this.idP=idP;
        this.nomP =nomP;
        this.idC = idC;
    }
    
    public int getIdP(){
        return idP;
    }
    
    public String getNomP(){
        return nomP;
    }
    
    public int getIdC(){
        return idC;
    }
    
    public void setIdP(int idP){
        this.idP = idP;
    }
    
    public void setNomP(String nomP){
        this.nomP = nomP;
    }
    
    public void setIdC(int idC){
        this.idC =  idC;
    }
    
    @Override
    public String toString() {
        return nomP;
    }
}
