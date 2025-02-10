package com.example.javaproject;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DatabaseConnection {
    private static final String URL = "jdbc:mysql://localhost:3306/db0"; // Remplace avec ton nom de base de données
    private static final String USER = "root"; // Ton nom d'utilisateur MySQL
    private static final String PASSWORD = "Kakashi123/"; // Ton mot de passe MySQL

    public static Connection connect() {
        try {
            Class.forName("com.mysql.cj.jdbc.Driver"); // Charger le driver MySQL
            Connection conn = DriverManager.getConnection(URL, USER, PASSWORD);
            System.out.println("✅ Connexion réussie à la base de données !");
            return conn;
        } catch (ClassNotFoundException e) {
            System.err.println("❌ Driver MySQL non trouvé !");
            e.printStackTrace();
            return null;
        } catch (SQLException e) {
            System.err.println("❌ Erreur de connexion à la base de données !");
            e.printStackTrace();
            return null;
        }
    }

    public static void main(String[] args) {
        Connection testConn = connect();
        if (testConn != null) {
            System.out.println("✅ Test de connexion réussi !");
        } else {
            System.out.println("❌ Test de connexion échoué !");
        }
    }
}
