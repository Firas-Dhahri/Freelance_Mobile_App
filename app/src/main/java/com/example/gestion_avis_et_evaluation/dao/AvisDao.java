package com.example.gestion_avis_et_evaluation.dao;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import com.example.gestion_avis_et_evaluation.entity.Avis;

import java.util.List;

@Dao
public interface AvisDao {
    @Insert
    long insert(Avis avis); // Insert a new review

    @Update
    void update(Avis avis); // Update an existing review

    @Delete
    void delete(Avis avis); // Delete a review
    @Query("SELECT * FROM avis WHERE freelancerId = :freelancerId")
    List<Avis> getAllAvisByFreelancerId(Long freelancerId); // Get all reviews for a specific freelancer

    @Query("SELECT * FROM avis WHERE clientId = :clientId")
    List<Avis> getAllAvisByClientId(Long clientId); // Get all reviews by a specific client

    @Query("SELECT * FROM avis")
    List<Avis> getAllAvis(); // Get all reviews
}
