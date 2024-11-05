package tn.esprit.freelance;

import android.os.Bundle;
import android.text.TextUtils;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import tn.esprit.freelance.DAO.ApplicationDao;
import tn.esprit.freelance.database.ApplicationDatabase;
import tn.esprit.freelance.entities.Application;

public class ApplyJobActivity extends AppCompatActivity {

    private EditText etFreelancerName, etFreelancerEmail, etCoverLetter;
    private Button btnSubmit;
    private String cvPath; // Assume you will set this when the user selects their CV

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_application_form);

        // Initialize UI components
        etFreelancerName = findViewById(R.id.etFreelancerName);
        etFreelancerEmail = findViewById(R.id.etFreelancerEmail);
        etCoverLetter = findViewById(R.id.etCoverLetter);
        btnSubmit = findViewById(R.id.btnSubmit);

        // Handle submit button click
        btnSubmit.setOnClickListener(v -> {
            if (validateInputs()) {
                submitApplication();
            }
        });
    }

    // Validate inputs
    private boolean validateInputs() {
        if (TextUtils.isEmpty(etFreelancerName.getText())) {
            etFreelancerName.setError("Name is required");
            return false;
        }
        if (TextUtils.isEmpty(etFreelancerEmail.getText())) {
            etFreelancerEmail.setError("Email is required");
            return false;
        }
        if (TextUtils.isEmpty(etCoverLetter.getText())) {
            etCoverLetter.setError("Cover letter is required");
            return false;
        }
        return true;
    }

    // Submit the application
    private void submitApplication() {
        String nom = etFreelancerName.getText().toString();
        String email = etFreelancerEmail.getText().toString();
        String lettreMotivation = etCoverLetter.getText().toString();
        // Assume cvPath is set earlier when the user selects their CV
        Application candidature = new Application(nom, email, lettreMotivation, cvPath);
        ApplicationDao candidatureDao = ApplicationDatabase.getInstance(this).candidatureDao();

        // Insert in a separate thread
        new Thread(() -> {
            candidatureDao.insert(candidature);
            runOnUiThread(() -> {
                Toast.makeText(ApplyJobActivity.this, "Application submitted successfully!", Toast.LENGTH_SHORT).show();
                finish(); // Close this activity and return to the previous one
            });
        }).start();
    }
}
