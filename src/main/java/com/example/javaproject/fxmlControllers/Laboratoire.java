package com.example.javaproject.fxmlControllers;

public class Laboratoire {
    private int id;
    private String nom;
    private String adresse;

    public Laboratoire(int id, String nom, String adresse) {
        this.id = id;
        this.nom = nom;
        this.adresse = adresse;
    }

    public int getId() {
        return id;
    }

    public String getNom() {
        return nom;
    }

    public String getAdresse() {
        return adresse;
    }
}
