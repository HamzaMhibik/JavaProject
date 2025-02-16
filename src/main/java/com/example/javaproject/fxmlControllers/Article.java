package com.example.javaproject.fxmlControllers;

public class Article {
    private int id;
    private String titre;
    private String description;

    public Article(int id, String titre, String description) {
        this.id = id;
        this.titre = titre;
        this.description = description;
    }

    public int getId() {
        return id;
    }

    public String getTitre() {
        return titre;
    }

    public String getDescription() {
        return description;
    }
}
