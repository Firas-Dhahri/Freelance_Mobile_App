package tn.esprit.freelance;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import java.util.Date;
import java.text.SimpleDateFormat;
import java.util.Locale;
import android.widget.Spinner;
import android.widget.ArrayAdapter;

import androidx.appcompat.app.AppCompatActivity;
import androidx.room.Room;

import tn.esprit.freelance.database.ApplicationDatabase;
import tn.esprit.freelance.entities.Project;

public class ModifyProject extends AppCompatActivity {
    private ApplicationDatabase db;
    private Spinner statusSpinner; // Spinner pour le statut du projet
    private EditText projectNameEditText, projectDescriptionEditText, projectStartDateEditText, projectEndDateEditText, projectOwnerEditText, projectBudgetEditText;
    private Button updateButton;
    private int projectId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_modify_project);

        // Initialisation du Spinner pour le statut
        statusSpinner = findViewById(R.id.status_spinner);

        // Initialisation de la base de données
        db = Room.databaseBuilder(getApplicationContext(), ApplicationDatabase.class, "freelance").allowMainThreadQueries().build();

        // Initialisation des vues
        projectNameEditText = findViewById(R.id.project_name);
        projectDescriptionEditText = findViewById(R.id.project_description);
        projectStartDateEditText = findViewById(R.id.project_start_date);
        projectEndDateEditText = findViewById(R.id.project_end_date);
        projectOwnerEditText = findViewById(R.id.project_owner);
        projectBudgetEditText = findViewById(R.id.project_budget);
        updateButton = findViewById(R.id.update_button);

        // Remplir le Spinner avec les options "En cours" et "Terminé"
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this,
                R.array.status_array, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        statusSpinner.setAdapter(adapter);

        // Obtenir l'ID du projet à partir de l'Intent
        Intent intent = getIntent();
        projectId = intent.getIntExtra("PROJECT_ID", -1);

        if (projectId != -1) {
            // Charger les détails du projet
            loadProjectDetails(projectId);
        } else {
            Toast.makeText(this, "Project ID not found", Toast.LENGTH_SHORT).show();
        }

        // Configurer le listener pour le bouton de mise à jour
        updateButton.setOnClickListener(v -> updateProject());
    }

    private void loadProjectDetails(int projectId) {
        Project project = db.projectDao().getProjectById(projectId);

        if (project != null) {
            // Pré-remplir le formulaire avec les détails du projet existant
            projectNameEditText.setText(project.getName());
            projectDescriptionEditText.setText(project.getDescription());
            projectStartDateEditText.setText(formatDate(project.getStartDate()));
            projectEndDateEditText.setText(formatDate(project.getEndDate()));
            projectOwnerEditText.setText(project.getProjectOwner());
            projectBudgetEditText.setText(String.valueOf(project.getBudget()));

            // Pré-sélectionner le statut du projet dans le Spinner
            String status = project.getStatus();
            if (status != null) {
                ArrayAdapter<CharSequence> adapter = (ArrayAdapter<CharSequence>) statusSpinner.getAdapter();
                int position = adapter.getPosition(status);
                statusSpinner.setSelection(position); // Sélectionner l'élément du Spinner
            }
        }
    }

    private String formatDate(Date date) {
        SimpleDateFormat newFormat = new SimpleDateFormat("dd/MM/yyyy", Locale.getDefault());
        return newFormat.format(date);
    }

    private void updateProject() {
        String name = projectNameEditText.getText().toString();
        String description = projectDescriptionEditText.getText().toString();
        String startDate = projectStartDateEditText.getText().toString();
        String endDate = projectEndDateEditText.getText().toString();
        String owner = projectOwnerEditText.getText().toString();
        double budget = Double.parseDouble(projectBudgetEditText.getText().toString());

        // Récupérer le statut sélectionné dans le Spinner
        String status = statusSpinner.getSelectedItem().toString();

        // Créer l'objet Project avec les valeurs mises à jour
        Project updatedProject = new Project(name, description, parseDate(startDate), parseDate(endDate), status, owner, budget);
        updatedProject.setId(projectId);

        // Mettre à jour le projet dans la base de données
        db.projectDao().update(updatedProject);
        Toast.makeText(this, "Project updated successfully", Toast.LENGTH_SHORT).show();
        finish();  // Fermer l'activité
    }

    private Date parseDate(String dateString) {
        try {
            SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy", Locale.getDefault());
            return dateFormat.parse(dateString);
        } catch (Exception e) {
            return null;
        }
    }
}
