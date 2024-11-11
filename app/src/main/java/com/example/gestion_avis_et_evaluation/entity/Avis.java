package com.example.gestion_avis_et_evaluation.entity;


import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "avis")
public class Avis {

    @PrimaryKey(autoGenerate = true)
    private Long id;           // Unique identifier for each review

    private Long clientId;     // ID of the client giving the review
    private Long freelancerId; // ID of the freelancer receiving the review
    private String comment;     // The review comment provided by the client
    private int rating;         // Rating given by the client (e.g., from 1 to 5)

    // Constructor
    public Avis(Long clientId, Long freelancerId, String comment, int rating) {
        this.clientId = clientId;
        this.freelancerId = freelancerId;
        this.comment = comment;
        this.rating = rating;
    }
public Avis (){}
    // Getters and Setters
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getClientId() {
        return clientId;
    }

    public void setClientId(Long clientId) {
        this.clientId = clientId;
    }

    public Long getFreelancerId() {
        return freelancerId;
    }

    public void setFreelancerId(Long freelancerId) {
        this.freelancerId = freelancerId;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    public int getRating() {
        return rating;
    }

    public void setRating(int rating) {
        this.rating = rating;
    }
}
