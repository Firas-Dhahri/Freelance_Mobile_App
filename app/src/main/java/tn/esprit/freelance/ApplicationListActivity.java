package tn.esprit.freelance;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import tn.esprit.freelance.R;
import tn.esprit.freelance.entities.Application;
import tn.esprit.freelance.DAO.ApplicationDao;
import tn.esprit.freelance.database.ApplicationDatabase;

import java.util.List;

public class ApplicationListActivity extends AppCompatActivity {
    private RecyclerView recyclerView;
    private ApplicationAdapter adapter;
    private ApplicationDao applicationDao;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_application_list);
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
            List<Application> applications = applicationDao.getAllApplications();
            runOnUiThread(() -> {
                adapter = new ApplicationAdapter(this,applications);
                recyclerView.setAdapter(adapter);
            });
        }).start();
    }
}
