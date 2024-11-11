package tn.esprit.freelance;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import tn.esprit.freelance.R;
import tn.esprit.freelance.entities.Application;
import tn.esprit.freelance.DAO.ApplicationDao;
import tn.esprit.freelance.database.ApplicationDatabase;

import java.util.List;
public class CandidatListActivity extends AppCompatActivity {
    private RecyclerView recyclerView;
    private ApplicationAdapter adapter;
    private ApplicationDao applicationDao;
    private EmailSender emailSender; // Instance de EmailSender

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_candidat_list);

        // Initialiser l'instance d'EmailSender
        emailSender = new EmailSender();

        // Initialiser RecyclerView
        recyclerView = findViewById(R.id.recyclerViewApplications);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        // Initialiser le DAO et récupérer toutes les candidatures
        applicationDao = ApplicationDatabase.getInstance(this).applicationDao();

        new Thread(() -> {
            // Récupérer toutes les candidatures depuis la base de données
            List<Application> allApplications = applicationDao.getAllApplications();

            // Mettre à jour l'UI avec toutes les candidatures
            runOnUiThread(() -> {
                adapter = new ApplicationAdapter(this, allApplications);
                recyclerView.setAdapter(adapter);

                // Configurer un écouteur pour les clics sur les éléments de la liste
                adapter.setOnItemClickListener(application -> {
                    Intent intent = new Intent(CandidatListActivity.this, DetailActivity.class);
                    intent.putExtra("applicationId", application.getId());
                    startActivity(intent);
                });

                // Gérer le clic sur le bouton "Accepter" dans l'adaptateur
                adapter.setOnAcceptClickListener(application -> {
                    // Mettre à jour le statut de la candidature à "accepter"
                    application.setStatus("accepter");

                    // Mettre à jour la candidature dans la base de données
                    new Thread(() -> {
                        applicationDao.update(application);
                        runOnUiThread(() -> {
                            Toast.makeText(CandidatListActivity.this, "Application accepted", Toast.LENGTH_SHORT).show();

                            // Envoyer l'email dans un thread séparé pour ne pas bloquer l'UI
                            new Thread(() -> {
                                emailSender.sendEmail(
                                        application.getEmail(),
                                        "Candidature acceptée",
                                        "Bonjour, votre candidature a été acceptée. Félicitations !"
                                );
                            }).start();
                        });
                    }).start();
                });
            });
        }).start();
    }
}
