package tn.esprit.freelance.DAO;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;
import tn.esprit.freelance.entities.Project;
import java.util.List;

@Dao
public interface ProjectDao {
    @Insert
    void create(Project project);

    @Delete
    void delete(Project project);

    @Query("SELECT * FROM project")
    List<Project> getAllProjects();
    @Query("SELECT * FROM project WHERE status = 'En attente' ")
    List<Project> getProjectByStatus();
    @Update
    void update(Project project);
    @Query("SELECT * FROM project WHERE id = :id")
    Project getProjectById(int id);
    @Query("SELECT * FROM project WHERE name LIKE :query OR description LIKE :query")
    List<Project> searchProjects(String query);
}
