package tn.esprit.freelance;

import static tn.esprit.freelance.R.id.listApplyButton;

import android.content.Intent;
import android.os.Bundle;
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
        // Set OnClickListener to open the application form
        applyButton.setOnClickListener(v -> {
            Intent intent = new Intent(DashboardActivity.this, ApplyJobActivity.class);
            startActivity(intent);
        });
        listApplyButton.setOnClickListener(v -> {
            Intent intent1 = new Intent(DashboardActivity.this, ApplicationListActivity.class);
            startActivity(intent1);
        });
        listCandidatButton.setOnClickListener(v -> {
            Intent intent2 = new Intent(DashboardActivity.this, CandidatListActivity.class);
            startActivity(intent2);
        });

    }

}
