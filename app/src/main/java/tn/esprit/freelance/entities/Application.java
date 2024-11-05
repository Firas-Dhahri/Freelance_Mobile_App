package tn.esprit.freelance.entities;

import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "Applications")
public class Application {
    @PrimaryKey(autoGenerate = true)
    private int id;
    private String nom;
    private String email;
    private String lettreMotivation;
    private String cvPath; // Chemin du fichier CV

    // Constructeur
    public Application(String nom, String email, String lettreMotivation, String cvPath) {
        this.nom = nom;
        this.email = email;
        this.lettreMotivation = lettreMotivation;
        this.cvPath = cvPath;
    }

    // Getters et setters
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getNom() {
        return nom;
    }

    public String getEmail() {
        return email;
    }

    public String getLettreMotivation() {
        return lettreMotivation;
    }

    public String getCvPath() {
        return cvPath;
    }
}
