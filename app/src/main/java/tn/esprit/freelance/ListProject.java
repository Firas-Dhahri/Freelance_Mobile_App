package tn.esprit.freelance;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;
import android.content.Context;
import tn.esprit.freelance.SessionManager;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

import tn.esprit.freelance.database.ApplicationDatabase;
import tn.esprit.freelance.entities.Project;

public class ListProject extends AppCompatActivity {

    private RecyclerView projectRecyclerView;
    private ProjectAdapter projectAdapter;
    private List<Project> projectList;
    private List<Project> filteredList;
    private ApplicationDatabase db;
    private EditText inputField;

    // SessionManager
    private SessionManager sessionManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list_project);

        // Initialize SessionManager
        sessionManager = new SessionManager(this);

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

        // Set listeners for navigation
        findViewById(R.id.imageView).setOnClickListener(v -> navigateToAddProjectActivity());
        findViewById(R.id.signin).setOnClickListener(v -> navigateToSignInActivity());
        findViewById(R.id.postule).setOnClickListener(v -> navigateTopostule());
        // Set the search button click listener
        findViewById(R.id.searchButton).setOnClickListener(v -> performSearch());
    }

    private void loadProjectsFromDatabase() {
        new Thread(() -> {
            projectList = db.projectDao().getProjectByStatus(); // Fetch projects from Room database
            filteredList.addAll(projectList);  // Initially, show all projects
            runOnUiThread(() -> {
                if (filteredList.isEmpty()) {
                    Toast.makeText(ListProject.this, "No projects found", Toast.LENGTH_SHORT).show();
                } else {
                    // Set up the adapter with the filtered project list
                    projectAdapter = new ProjectAdapter(ListProject.this, filteredList);
                    projectRecyclerView.setAdapter(projectAdapter);
                }
            });
        }).start();
    }

    // Perform search based on the input field
    private void performSearch() {
        String searchQuery = inputField.getText().toString().toLowerCase().trim();

        if (searchQuery.isEmpty()) {
            // If search field is empty, show all projects
            loadProjectsFromDatabase();
        } else {
            // Search projects in the database using LIKE
            new Thread(() -> {
                List<Project> filteredProjects = db.projectDao().searchProjects("%" + searchQuery + "%"); // Using LIKE
                runOnUiThread(() -> {
                    if (filteredProjects.isEmpty()) {
                        Toast.makeText(ListProject.this, "No matching projects found", Toast.LENGTH_SHORT).show();
                    } else {
                        // Update filtered list
                        filteredList.clear();
                        filteredList.addAll(filteredProjects);
                        projectAdapter.notifyDataSetChanged();
                    }
                });
            }).start();
        }
    }

    private void navigateToAddProjectActivity() {
        // Check if the user is logged in and has the 'FREELANCER' role
        String fullName = sessionManager.getFullName();
        String email = sessionManager.getEmail();
        String role = sessionManager.getUserRole();  // Assume SessionManager has a method to get role
        if (fullName != null && email != null && "CLIENT".equals(role)) {
            // User is logged in and has 'FREELANCER' role
            Intent intent = new Intent(ListProject.this, AddprojectActivity.class);
            startActivity(intent);
        } else {
            // User is not logged in or does not have 'FREELANCER' role, redirect to MainActivity
            Intent intent = new Intent(ListProject.this, MainActivity.class);
            startActivity(intent);
            Toast.makeText(this, "You must be a Freelancer to add a project", Toast.LENGTH_SHORT).show();
        }
    }

    private void navigateToSignInActivity() {
        Intent intent = new Intent(ListProject.this, MainActivity.class);
        startActivity(intent);
    }
    private void navigateTopostule() {
        Intent intent = new Intent(ListProject.this, MainActivity.class);
        startActivity(intent);
    }
}
