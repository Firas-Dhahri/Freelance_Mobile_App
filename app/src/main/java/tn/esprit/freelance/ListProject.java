package tn.esprit.freelance;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;
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

        sessionManager = new SessionManager(this);
        projectRecyclerView = findViewById(R.id.projectRecyclerView);
        projectRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        inputField = findViewById(R.id.inputField);
        db = ApplicationDatabase.getInstance(this);
        filteredList = new ArrayList<>();

        // Load projects from the database
        loadProjectsFromDatabase();

        findViewById(R.id.signin).setOnClickListener(v -> navigateToSignInActivity());

        // Set the search button click listener
        findViewById(R.id.searchButton).setOnClickListener(v -> performSearch());
    }

    private void loadProjectsFromDatabase() {
        new Thread(() -> {
            projectList = db.projectDao().getProjectByStatus(); // Fetch projects from Room database
            filteredList.addAll(projectList);
            runOnUiThread(() -> {
                if (filteredList.isEmpty()) {
                    Toast.makeText(ListProject.this, "No projects found", Toast.LENGTH_SHORT).show();
                } else {
                    projectAdapter = new ProjectAdapter(ListProject.this, filteredList, this::navigateToPostule);
                    projectRecyclerView.setAdapter(projectAdapter);
                }
            });
        }).start();
    }

    private void navigateToPostule(Project project) {
        // Implement the navigation or functionality when the apply button is clicked
        Intent intent = new Intent(ListProject.this, MainActivity.class);  // Adjust based on your requirement
        startActivity(intent);
    }

    private void performSearch() {
        String searchQuery = inputField.getText().toString().toLowerCase().trim();
        if (searchQuery.isEmpty()) {
            loadProjectsFromDatabase();
        } else {
            new Thread(() -> {
                List<Project> filteredProjects = db.projectDao().searchProjects("%" + searchQuery + "%");
                runOnUiThread(() -> {
                    if (filteredProjects.isEmpty()) {
                        Toast.makeText(ListProject.this, "No matching projects found", Toast.LENGTH_SHORT).show();
                    } else {
                        filteredList.clear();
                        filteredList.addAll(filteredProjects);
                        projectAdapter.notifyDataSetChanged();
                    }
                });
            }).start();
        }
    }
    private void navigateToApplyJobActivity() {
        Intent intent = new Intent(ListProject.this, AddprojectActivity.class);
        startActivity(intent);
    }

    private void navigateToSignInActivity() {
        Intent intent = new Intent(ListProject.this, MainActivity.class);
        startActivity(intent);
    }
}
