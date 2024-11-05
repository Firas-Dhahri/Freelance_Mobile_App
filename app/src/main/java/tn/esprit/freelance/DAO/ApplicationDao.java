package tn.esprit.freelance.DAO;
import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;

import java.util.List;

import tn.esprit.freelance.entities.Application;

@Dao
public interface ApplicationDao {
    @Insert
    void insert(Application application);

    @Query("SELECT * FROM Applications")
    List<Application> getAllApplications();
}

