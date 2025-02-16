package com.example.javaproject.fxmlControllers;

public class Patient {
    private int id;
    private String nom;
    private String prenom;
    private String email;
    private String telephone;
    private String motdepasse;
    private int age;          // New field
    private String sexe;      // New field
    private String ville;     // New field
    private String nomUtilisateur;  // New field

    // Updated constructor
    public Patient(int id, String nom, String prenom, String email, String telephone, String motdepasse, int age, String sexe, String ville, String nomUtilisateur) {
        this.id = id;
        this.nom = nom;
        this.prenom = prenom;
        this.email = email;
        this.telephone = telephone;
        this.motdepasse = motdepasse;
        this.age = age;
        this.sexe = sexe;
        this.ville = ville;
        this.nomUtilisateur = nomUtilisateur;
    }

    public int getId() {
        return id;
    }

    public String getNom() {
        return nom;
    }

    public String getPrenom() {
        return prenom;
    }

    public String getEmail() {
        return email;
    }

    public String getTelephone() {
        return telephone;
    }

    public String getMotdepasse() {
        return motdepasse;
    }

    public int getAge() {
        return age;
    }

    public String getSexe() {
        return sexe;
    }

    public String getVille() {
        return ville;
    }

    public String getNomUtilisateur() {
        return nomUtilisateur;
    }
}
