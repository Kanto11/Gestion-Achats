/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.gestion_achats.models;

public class Categorie {
    private int idC;
    private String nomC;
    
    public Categorie(){
    }
    
    public Categorie(int idC , String nomC){
        this.idC = idC;
        this.nomC = nomC;
    }
    
    public int getIdC(){
        return idC;
    }
    
    public String getNomC(){
        return nomC;
    }
    
    public void setIdC(int idC){
        this.idC=idC;
    }
    
    public void setNomC(String nomC){
        this.nomC = nomC;
    }
    
     @Override
    public String toString() {
        return nomC;
    }
    
}
