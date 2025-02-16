package com.example.javaproject.fxmlControllers;

public class Historique {
    private int id;
    private String utilisateur;
    private String date; // Changement de LocalDate à String

    public Historique(int id, String utilisateur, String date) {
        this.id = id;
        this.utilisateur = utilisateur;
        this.date = date;
    }

    public int getId() {
        return id;
    }

    public String getUtilisateur() {
        return utilisateur;
    }

    public String getDate() {
        return date;
    }
}
