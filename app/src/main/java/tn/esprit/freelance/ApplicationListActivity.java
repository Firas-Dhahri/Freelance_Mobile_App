package tn.esprit.freelance;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

import tn.esprit.freelance.DAO.ApplicationDao;
import tn.esprit.freelance.database.ApplicationDatabase;
import tn.esprit.freelance.entities.Application;

public class ApplicationListActivity extends AppCompatActivity {
    private RecyclerView recyclerView;
    private ApplicationAdapter adapter;
    private ApplicationDao applicationDao;
    private SessionManager sessionManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_application_list);
        // Initialize SessionManager
        sessionManager = new SessionManager(this);

        Button btnAddApplication = findViewById(R.id.btnAddApplication);
        btnAddApplication.setOnClickListener(v -> {
            // When the Back button is clicked, navigate back to the Application List Activity
            Intent intent = new Intent(ApplicationListActivity.this, ApplyJobActivity.class);
            startActivity(intent);
            finish(); // Optional: finishes the current activity, removing it from the back stack
        });

        recyclerView = findViewById(R.id.recyclerViewApplications);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        // Initialize the DAO and fetch applications
        applicationDao = ApplicationDatabase.getInstance(this).applicationDao();
        new Thread(() -> {
            // Retrieve the logged-in user's email from SessionManager
            String currentUserEmail = sessionManager.getEmail();
            // Fetch all applications from the database
            List<Application> allApplications = applicationDao.getAllApplications();

            // Filter applications based on the logged-in user's email
            List<Application> userApplications = new ArrayList<>();
            for (Application application : allApplications) {
                if (application.getEmail().equals(currentUserEmail)) {
                    userApplications.add(application);
                }
            }
            runOnUiThread(() -> {
                adapter = new ApplicationAdapter(this,userApplications);
                recyclerView.setAdapter(adapter);
            });
        }).start();
    }
}
