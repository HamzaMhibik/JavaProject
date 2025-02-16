package com.example.javaproject.fxmlControllers;

public class Medecin {
    private int id;
    private String nom;
    private String prenom;
    private String email;
    private String telephone;
    private String motdepasse;
    private int age;           // Nouveau champ
    private String sexe;      // Nouveau champ
    private String ville;     // Nouveau champ
    private String nomUtilisateur;  // Nouveau champ

    public Medecin(int id, String nom, String prenom, String email, String telephone, String motdepasse, int age, String sexe, String ville, String nomUtilisateur) {
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
