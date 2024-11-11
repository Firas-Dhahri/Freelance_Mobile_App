package tn.esprit.freelance;

import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.FileProvider;

import java.io.File;
import android.content.Context;
import android.net.Uri;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import tn.esprit.freelance.DAO.ApplicationDao;
import tn.esprit.freelance.database.ApplicationDatabase;
import tn.esprit.freelance.entities.Application;

public class DetailActivity extends AppCompatActivity {
    private static final int REQUEST_CODE = 1001;
    private TextView tvName, tvEmail, tvCoverLetter, tvCVPath;
    private ApplicationDao applicationDao;
    private Application application; // This is the application object that will hold the data
    private void downloadFile(Uri uri, Context context) {
        try {
            // Open the input stream from the file URI using ContentResolver
            InputStream inputStream = context.getContentResolver().openInputStream(uri);

            // Create an output file in the app's external storage directory
            File outputFile = new File(context.getExternalFilesDir(null), "cv_downloaded.pdf");

            // Create output stream to write to the output file
            OutputStream outputStream = new FileOutputStream(outputFile);

            byte[] buffer = new byte[1024];
            int length;

            // Read from input stream and write to output file
            while ((length = inputStream.read(buffer)) > 0) {
                outputStream.write(buffer, 0, length);
            }

            // Close streams once done
            inputStream.close();
            outputStream.close();

            // Show success message
            Toast.makeText(context, "Fichier téléchargé avec succès", Toast.LENGTH_SHORT).show();
        } catch (IOException e) {
            e.printStackTrace();
            // Show error message
            Toast.makeText(context, "Erreur lors du téléchargement", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);
        Button openFileButton = findViewById(R.id.openFileButton);
        openFileButton.setOnClickListener(view -> {
            // Créer l'Intent pour ouvrir un document
            Intent intent = new Intent(Intent.ACTION_OPEN_DOCUMENT);
            intent.setType("*/*"); // Ou un type MIME spécifique, comme "application/pdf"
            startActivityForResult(intent, REQUEST_CODE);
        });
        // Initialize views
        tvName = findViewById(R.id.tvName);
        tvEmail = findViewById(R.id.tvEmail);
        tvCoverLetter = findViewById(R.id.tvCoverLetter);
        tvCVPath = findViewById(R.id.tvCVPath);

        // Initialize the DAO
        applicationDao = ApplicationDatabase.getInstance(this).applicationDao();

        // Récupérer l'ID de l'application passé dans l'Intent
        int applicationId = getIntent().getIntExtra("applicationId", -1);

        // Vérifier si l'ID est valide
        if (applicationId != -1) {
            // Charger l'application depuis la base de données
            new Thread(() -> {
                // Fetch application from the database
                application = applicationDao.getApplicationById(applicationId); // Make sure this works

                runOnUiThread(() -> {
                    if (application != null) {
                        // If application is found, display details
                        tvName.setText(application.getNom());
                        tvEmail.setText(application.getEmail());
                        tvCoverLetter.setText(application.getLettreMotivation());
                        tvCVPath.setText(application.getCvPath());
                    } else {
                        // Handle the case where the application is not found
                        Toast.makeText(DetailActivity.this, "Application not found", Toast.LENGTH_SHORT).show();
                    }
                });
            }).start();
        } else {
            // If the application ID is invalid
            Toast.makeText(this, "Invalid application ID", Toast.LENGTH_SHORT).show();
        }

        Button btnBack = findViewById(R.id.btnBack);
        Button btnEdit = findViewById(R.id.btnEdit);
        ImageView btnDelete = findViewById(R.id.btnDelete);
        tvCVPath.setOnClickListener(v -> {
            // Récupérer le chemin du CV
            String cvPath = application.getCvPath();

            // Vérifier si le chemin du CV est valide
            if (cvPath != null && !cvPath.isEmpty()) {
                File cvFile = new File(cvPath);

                // Vérifier si le fichier existe
                if (cvFile.exists()) {
                    // Créer une URI à partir du chemin du fichier
                    Uri uri = FileProvider.getUriForFile(DetailActivity.this,
                            "tn.esprit.freelance.fileprovider", cvFile);

                    // Intent pour ouvrir le PDF
                    Intent intent = new Intent(Intent.ACTION_VIEW);
                    intent.setDataAndType(uri, "application/pdf");
                    intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION); // Nécessaire pour donner l'accès à l'URI

                    // Vérifier si une application peut ouvrir ce fichier
                    try {
                        startActivity(intent);
                    } catch (ActivityNotFoundException e) {
                        // Si aucune application pour ouvrir le PDF
                        Toast.makeText(DetailActivity.this, "Aucune application pour ouvrir le PDF", Toast.LENGTH_SHORT).show();
                    }
                } else {
                    // Si le fichier n'existe pas
                    Toast.makeText(DetailActivity.this, "Le CV n'a pas été trouvé", Toast.LENGTH_SHORT).show();
                }
            } else {
                // Si le chemin du CV est invalide
                Toast.makeText(DetailActivity.this, "Chemin du CV invalide", Toast.LENGTH_SHORT).show();
            }
        });
        

        // Back button click listener
        btnBack.setOnClickListener(v -> {
            Intent intent = new Intent(DetailActivity.this, CandidatListActivity.class);
            startActivity(intent);
            finish(); // Finish the current activity
        });

        // Edit button functionality
        btnEdit.setOnClickListener(v -> {
            if (application != null) {
                Intent intent = new Intent(DetailActivity.this, EditApplicationActivity.class);
                intent.putExtra("applicationId", application.getId()); // Pass the application ID
                startActivity(intent);
            }
        });

        // Delete button functionality
        btnDelete.setOnClickListener(v -> {
            if (application != null) {
                // Delete the application from the database
                new Thread(() -> {
                    applicationDao.delete(application); // Ensure application is not null
                    runOnUiThread(() -> {
                        Intent intent = new Intent(DetailActivity.this, CandidatListActivity.class);
                        startActivity(intent);
                        finish(); // Finish the current activity
                    });
                }).start();
            }
        });
    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == REQUEST_CODE && resultCode == RESULT_OK) {
            Uri uri = data.getData();
            if (uri != null) {
                // Ouvrir le fichier avec ContentResolver
                try (InputStream inputStream = getContentResolver().openInputStream(uri)) {
                    // Traitez le fichier ici
                    // Par exemple, vous pouvez enregistrer ou afficher le contenu du fichier
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }

}
