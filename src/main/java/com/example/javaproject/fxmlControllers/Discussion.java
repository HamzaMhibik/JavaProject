package com.example.javaproject.fxmlControllers;

public class Discussion {
    private int id;
    private String nom;
    private String contenu;

    public Discussion(int id, String nom, String contenu) {
        this.id = id;
        this.nom = nom;
        this.contenu = contenu;
    }

    public int getId() {
        return id;
    }

    public String getNom() {
        return nom;
    }

    public String getContenu() {
        return contenu;
    }
}
