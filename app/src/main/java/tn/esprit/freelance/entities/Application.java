package tn.esprit.freelance.entities;

import androidx.room.Entity;
import androidx.room.PrimaryKey;
import java.util.Date;
@Entity(tableName = "Applications")
public class Application {
    @PrimaryKey(autoGenerate = true)
    private int id;
    private String nom;
    private String email;
    private String lettreMotivation;
    private String cvPath; // Chemin du fichier CV
    private String date;    // Date de soumission
    private String status;  // Statut de l'application (e.g., "Pending", "Accepted", "Rejected")

    // Constructeur
    public Application(String nom, String email, String lettreMotivation, String cvPath, String date, String status) {
        this.nom = nom;
        this.email = email;
        this.lettreMotivation = lettreMotivation;
        this.cvPath = cvPath;
        this.date = date;
        this.status = status;
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
    public void setNom(String nom) {
        this.nom = nom;
    }
    public String getEmail() {
        return email;
    }
    public void setEmail(String email) {
        this.email = email;
    }
    public String getLettreMotivation() {
        return lettreMotivation;
    }
    public void setLettreMotivation(String lettreMotivation) {
        this.lettreMotivation = lettreMotivation;
    }
    public String getCvPath() {
        return cvPath;
    }
    public void setCvPath(String cvPath) {
        this.cvPath = cvPath;
    }
    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

}
