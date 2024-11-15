package tn.esprit.freelance;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;
import android.content.Context;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

import tn.esprit.freelance.database.ApplicationDatabase;
import tn.esprit.freelance.entities.Project;

public class ListProjectUser extends AppCompatActivity {

    private RecyclerView projectRecyclerView;
    private ProjectAdapter projectAdapter;
    private List<Project> projectList;
    private List<Project> filteredList;
    private ApplicationDatabase db;
    private EditText inputField;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list_project_user);
        // Initialize RecyclerView
        projectRecyclerView = findViewById(R.id.projectRecyclerView);
        projectRecyclerView.setLayoutManager(new LinearLayoutManager(this));

        // Initialize EditText for search
        inputField = findViewById(R.id.inputField);

        // Initialize the database
        db = ApplicationDatabase.getInstance(this);

        // Initialize the list for filtered projects
        filteredList = new ArrayList<>();

        // Load projects from the database
        loadProjectsFromDatabase();

        // Set the search button click listener
        findViewById(R.id.searchButton).setOnClickListener(v -> performSearch());
    }

    private void loadProjectsFromDatabase() {
        new Thread(() -> {
            projectList = db.projectDao().getAllProjects(); // Fetch projects from Room database
            filteredList.addAll(projectList);  // Initially, show all projects
            runOnUiThread(() -> {
                if (filteredList.isEmpty()) {
                    Toast.makeText(ListProjectUser.this, "No projects found", Toast.LENGTH_SHORT).show();
                } else {
                    // Set up the adapter with the filtered project list
                    projectAdapter = new ProjectAdapter(ListProjectUser.this, filteredList, this::navigateToPostule);
                    projectRecyclerView.setAdapter(projectAdapter);
                }
            });
        }).start();
    }
    private void navigateToPostule(Project project) {
        // Implement the navigation or functionality when the apply button is clicked
        Intent intent = new Intent(ListProjectUser.this, MainActivity.class);  // Adjust based on your requirement
        startActivity(intent);
    }
    // Perform search based on the input field
    private void performSearch() {
        String searchQuery = inputField.getText().toString().toLowerCase().trim();

        if (searchQuery.isEmpty()) {
            // Si le champ de recherche est vide, afficher tous les projets
            loadProjectsFromDatabase();
        } else {
            // Recherche dans la base de données en utilisant la nouvelle méthode du DAO
            new Thread(() -> {
                List<Project> filteredProjects = db.projectDao().searchProjects("%" + searchQuery + "%"); // Utilisation de LIKE
                runOnUiThread(() -> {
                    if (filteredProjects.isEmpty()) {
                        Toast.makeText(ListProjectUser.this, "No matching projects found", Toast.LENGTH_SHORT).show();
                    } else {
                        // Mettre à jour la liste filtrée
                        filteredList.clear();
                        filteredList.addAll(filteredProjects);
                        projectAdapter.notifyDataSetChanged();
                    }
                });
            }).start();
        }
    }

}
