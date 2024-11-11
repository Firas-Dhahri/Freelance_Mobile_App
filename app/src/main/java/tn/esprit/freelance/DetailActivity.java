package tn.esprit.freelance;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;

public class DetailActivity extends AppCompatActivity {
    private TextView tvName, tvEmail, tvCoverLetter, tvCVPath;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);
        Button btnBack = findViewById(R.id.btnBack);
        btnBack.setOnClickListener(v -> {
            // When the Back button is clicked, navigate back to the Application List Activity
            Intent intent = new Intent(DetailActivity.this, ApplicationListActivity.class);
            startActivity(intent);
            finish(); // Optional: finishes the current activity, removing it from the back stack
        });
        // Initialize views
        tvName = findViewById(R.id.tvName);
        tvEmail = findViewById(R.id.tvEmail);
        tvCoverLetter = findViewById(R.id.tvCoverLetter);
        tvCVPath = findViewById(R.id.tvCVPath);

        // Retrieve application details passed with Intent
        String name = getIntent().getStringExtra("name");
        String email = getIntent().getStringExtra("email");
        String coverLetter = getIntent().getStringExtra("coverLetter");
        String cvPath = getIntent().getStringExtra("cvPath");

        // Display details
        tvName.setText(name);
        tvEmail.setText(email);
        tvCoverLetter.setText(coverLetter);
        tvCVPath.setText(cvPath);
    }
}
