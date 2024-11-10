package tn.esprit.freelance;

import android.os.Bundle;
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

        recyclerView = findViewById(R.id.recyclerViewApplications);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        // Initialize the DAO and fetch applications
        applicationDao = ApplicationDatabase.getInstance(this).applicationDao();
        new Thread(() -> {
            List<Application> applications = applicationDao.getAllApplications();
            runOnUiThread(() -> {
                adapter = new ApplicationAdapter(applications);
                recyclerView.setAdapter(adapter);
            });
        }).start();
    }
}
