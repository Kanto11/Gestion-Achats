/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
    mvn javafx:run
 */
package com.mycompany.gestion_achats.dao;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class ConnexionDB {
     private static final String URL = "jdbc:mysql://localhost:3306/gestion_achats";
     private static final String USER = "root";
     private static final String PASSWORD = "";
     
     private static Connection connexion;
     
     public static Connection getConnexion(){
         try{
             if(connexion==null || connexion.isClosed()){
               connexion = DriverManager.getConnection(URL,USER,PASSWORD);          
         }
         }catch(SQLException e){
             System.err.println("Erreur de connexion a la base de donnée "+ e.getMessage());
             e.printStackTrace();
         }
         return connexion;
         
     }
     
}
