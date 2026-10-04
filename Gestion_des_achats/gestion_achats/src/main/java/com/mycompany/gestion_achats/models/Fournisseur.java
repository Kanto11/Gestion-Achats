package com.mycompany.gestion_achats.models;

/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */

public class Fournisseur {
    private int idF;
    private String nomF;
    private String telF;
    private String adrF;
    private String emailF;
    
    public Fournisseur(){
        
    }
    
    public Fournisseur(int idF, String nomF ,String telF, String adrF,String emailF){
        this.idF = idF;
        this.nomF = nomF;
        this.telF = telF;
        this.adrF = adrF;
        this.emailF = emailF;
    }
    
    public int getIdF(){
        return idF;
    }
    public void setIdF(int idF){
        this.idF = idF;
    }
    
    public String getNomF(){
        return nomF;
    }
    
    public void setNomF(String nomF){
        this.nomF = nomF;
    }
    
    public String getTelF(){
        return telF;
    }
    
    public void setTelF(String telF){
        this.telF = telF;
    }
    
    public String getEmailF(){
        return emailF;
    }
    
    public void setEmailF(String emailF){
        this.emailF = emailF;
    }
    
    public String getAdrF(){
        return adrF;
    }
    
    public void setAdrF(String adrF){
        this.adrF = adrF;
    }
    
     @Override
    public String toString() {
        return nomF;
    }
}
