package tn.esprit.freelance;

import android.os.Bundle;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

public class DashboardActivity extends AppCompatActivity {

    private TextView welcomeText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dashboard);

        // Find the welcome text view by ID
        welcomeText = findViewById(R.id.welcomeText);

        // Set the welcome message
        welcomeText.setText("Hello, you are now in the platform!");
    }
}
