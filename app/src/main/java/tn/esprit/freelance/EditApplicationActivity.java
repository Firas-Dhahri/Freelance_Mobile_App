package tn.esprit.freelance;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import tn.esprit.freelance.entities.Application;
import tn.esprit.freelance.DAO.ApplicationDao;
import tn.esprit.freelance.database.ApplicationDatabase;

public class EditApplicationActivity extends AppCompatActivity {

    private EditText etName, etEmail, etCoverLetter, etCVPath;
    private Button btnSave;
    private ApplicationDao applicationDao;
    private Application application;
    private int applicationId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.edit_applicationactivity);

        // Initialiser les vues
        etName = findViewById(R.id.etName);
        etEmail = findViewById(R.id.etEmail);
        etCoverLetter = findViewById(R.id.etCoverLetter);
        etCVPath = findViewById(R.id.etCVPath);
        btnSave = findViewById(R.id.btnSave);

        // Initialiser le DAO
        applicationDao = ApplicationDatabase.getInstance(this).applicationDao();

        // Récupérer l'ID de la candidature à modifier
        applicationId = getIntent().getIntExtra("applicationId", -1);

        // Charger la candidature depuis la base de données
        new Thread(() -> {
            application = applicationDao.getApplicationById(applicationId);
            runOnUiThread(() -> {
                if (application != null) {
                    etName.setText(application.getNom());
                    etEmail.setText(application.getEmail());
                    etCoverLetter.setText(application.getLettreMotivation());
                    etCVPath.setText(application.getCvPath());
                }
            });
        }).start();

        // Enregistrer les modifications
        btnSave.setOnClickListener(v -> {
            String updatedName = etName.getText().toString();
            String updatedEmail = etEmail.getText().toString();
            String updatedCoverLetter = etCoverLetter.getText().toString();
            String updatedCVPath = etCVPath.getText().toString();

            // Mettre à jour l'objet Application
            application.setNom(updatedName);
            application.setEmail(updatedEmail);
            application.setLettreMotivation(updatedCoverLetter);
            application.setCvPath(updatedCVPath);

            // Enregistrer les modifications dans la base de données
            new Thread(() -> {
                applicationDao.update(application);
                runOnUiThread(() -> {
                    Toast.makeText(EditApplicationActivity.this, "Application updated", Toast.LENGTH_SHORT).show();
                    finish(); // Fermer l'activité après la mise à jour
                });
            }).start();
        });
    }
}
