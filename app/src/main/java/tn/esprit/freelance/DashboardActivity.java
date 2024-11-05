package tn.esprit.freelance;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

public class DashboardActivity extends AppCompatActivity {

    private TextView welcomeText;
    private Button applyButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dashboard);

        // Find the welcome text view by ID
        welcomeText = findViewById(R.id.welcomeText);
        applyButton = findViewById(R.id.applyButton);
        // Set the welcome message
        welcomeText.setText("Hello, you are now in the platform!");
        // Set OnClickListener to open the application form
        applyButton.setOnClickListener(v -> {
            Intent intent = new Intent(DashboardActivity.this, ApplyJobActivity.class);
            startActivity(intent);
        });
    }

}
