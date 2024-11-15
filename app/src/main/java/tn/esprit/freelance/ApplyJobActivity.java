package tn.esprit.freelance;

import android.os.Bundle;
import android.text.TextUtils;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import android.net.Uri;
import android.os.Build;
import android.provider.OpenableColumns;
import androidx.annotation.Nullable;
import android.content.Intent;
import androidx.appcompat.app.AppCompatActivity;
import android.database.Cursor;
import java.util.Date;

import tn.esprit.freelance.DAO.ApplicationDao;
import tn.esprit.freelance.database.ApplicationDatabase;
import tn.esprit.freelance.entities.Application;

public class ApplyJobActivity extends AppCompatActivity {
    private int projectId;
    private EditText etFreelancerName, etFreelancerEmail, etCoverLetter;
    private Button btnSubmit,btnSelectCV;
    private TextView tvSelectedCV;
    private String cvPath; // Assume you will set this when the user selects their CV
    private static final int PICK_PDF_REQUEST = 1;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_apply_job);
        // Get the project ID from intent
        projectId = getIntent().getIntExtra("project_id", -1);

        // Initialize UI components
        etFreelancerName = findViewById(R.id.etFreelancerName);
        etFreelancerEmail = findViewById(R.id.etFreelancerEmail);
        etCoverLetter = findViewById(R.id.etCoverLetter);
        btnSubmit = findViewById(R.id.btnSubmit);
        // Autres initialisations
        btnSelectCV = findViewById(R.id.btnSelectCV);
        tvSelectedCV = findViewById(R.id.tvSelectedCV);
        // Récupérer les informations de l'utilisateur connecté
        SessionManager sessionManager = new SessionManager(this);
        String fullName = sessionManager.getFullName();
        String email = sessionManager.getEmail();
        etFreelancerName.setText(fullName);
        etFreelancerEmail.setText(email);
        // Action pour le bouton de sélection de fichier
        btnSelectCV.setOnClickListener(v -> openFileChooser());
        // Handle submit button click
        btnSubmit.setOnClickListener(v -> {
            if (validateInputs()) {
                submitApplication();
            }
        });
    }
    private void openFileChooser() {
        Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
        intent.setType("application/pdf");
        startActivityForResult(intent, PICK_PDF_REQUEST);
    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == PICK_PDF_REQUEST && resultCode == RESULT_OK && data != null && data.getData() != null) {
            Uri cvUri = data.getData();
            cvPath = cvUri.toString();
            tvSelectedCV.setText(getFileName(cvUri));
        }
    }

    private String getFileName(Uri uri) {
        String result = null;
        if (uri.getScheme().equals("content")) {
            try (Cursor cursor = getContentResolver().query(uri, null, null, null, null)) {
                if (cursor != null && cursor.moveToFirst()) {
                    int nameIndex = cursor.getColumnIndex(OpenableColumns.DISPLAY_NAME);
                    if (nameIndex >= 0) {  // Vérifie si l'index est valide
                        result = cursor.getString(nameIndex);
                    }
                }
            }
        }
        // Utiliser le dernier segment de chemin si le nom du fichier n'a pas été obtenu
        if (result == null) {
            result = uri.getLastPathSegment();
        }
        return result;
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
        if (TextUtils.isEmpty(cvPath)) {
            Toast.makeText(this, "Veuillez sélectionner un fichier CV", Toast.LENGTH_SHORT).show();
            return false;
        }
        return true;
    }

    // Submit the application
    private void submitApplication() {
        String nom = etFreelancerName.getText().toString();
        String email = etFreelancerEmail.getText().toString();
        String lettreMotivation = etCoverLetter.getText().toString();
        String date = "11-11-2024"; // Current date
        String status = "Pending";
        // Assume cvPath is set earlier when the user selects their CV
        Application candidature = new Application(nom, email, lettreMotivation, cvPath, date, status);
        // Set project ID in the application
        candidature.setProjectId(projectId);
        ApplicationDao candidatureDao = ApplicationDatabase.getInstance(this).applicationDao();

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