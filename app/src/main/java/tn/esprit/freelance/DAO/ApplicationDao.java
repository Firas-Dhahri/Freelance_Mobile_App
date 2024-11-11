package tn.esprit.freelance.DAO;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import java.util.List;

import tn.esprit.freelance.entities.Application;

@Dao
public interface ApplicationDao {
    @Insert
    void insert(Application application);
    @Update
    void update(Application application);
    @Query("DELETE FROM applications WHERE id = :id")
    void deleteById(int id);
    @Query("SELECT * FROM Applications")
    List<Application> getAllApplications();
    @Delete
    void delete(Application application);
    @Query("SELECT * FROM applications WHERE email = :email")
    List<Application> getApplicationsByUserEmail(String email);
    @Query("SELECT * FROM applications WHERE id = :id")
    Application getApplicationById(int id);
}

