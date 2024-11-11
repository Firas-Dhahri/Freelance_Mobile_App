package tn.esprit.freelance;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

public class DashboardActivity extends AppCompatActivity {

    private TextView welcomeText;
    private Button applyButton;
    private Button listApplyButton;
    private Button listCandidatButton;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dashboard);

        // Find the welcome text view by ID
        welcomeText = findViewById(R.id.welcomeText);
        applyButton = findViewById(R.id.applyButton);
        listApplyButton = findViewById(R.id.listApplyButton);
        listCandidatButton = findViewById(R.id.listCandidatButton);
        // Set the welcome message
        welcomeText.setText("Hello, you are now in the platform!");
        // Get the user's role from SessionManager
        SessionManager sessionManager = new SessionManager(this);
        String role = sessionManager.getRole();

        // Display/Hide buttons based on user role
        if ("FREELANCER".equals(role)) {
            applyButton.setVisibility(View.VISIBLE);  // Show "Apply" button for Freelancer
            listApplyButton.setVisibility(View.VISIBLE);  // Show "Application List" button for Freelancer
            listCandidatButton.setVisibility(View.GONE);  // Hide "Candidat List" button for Freelancer
        } else if ("CLIENT".equals(role)) {
            applyButton.setVisibility(View.GONE);  // Hide "Apply" button for Client
            listApplyButton.setVisibility(View.GONE);  // Hide "Application List" button for Client
            listCandidatButton.setVisibility(View.VISIBLE);  // Show "Candidat List" button for Client
        }

        // Set button click listeners
        applyButton.setOnClickListener(v -> {
            Intent intent = new Intent(DashboardActivity.this, ApplyJobActivity.class);
            startActivity(intent);
        });

        listApplyButton.setOnClickListener(v -> {
            Intent intent1 = new Intent(DashboardActivity.this, ApplicationListActivity.class);
            startActivity(intent1);
        });


    }
}
