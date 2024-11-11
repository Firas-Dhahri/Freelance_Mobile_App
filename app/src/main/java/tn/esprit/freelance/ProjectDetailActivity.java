// ProjectDetailActivity.java
package tn.esprit.freelance;
import androidx.appcompat.widget.AppCompatImageButton;
import android.graphics.ImageDecoder;
import android.graphics.drawable.Drawable;
import java.io.IOException;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import androidx.room.Room;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import tn.esprit.freelance.database.ApplicationDatabase;
import tn.esprit.freelance.entities.Project;

public class ProjectDetailActivity extends AppCompatActivity {
    private ApplicationDatabase db;

    private TextView projectNameTextView,projectStatusTextView, projectDescriptionTextView, projectOwnerTextView, projectStartDateTextView, projectEndDateTextView, projectBudgetTextView, projectDetailsSubtitle;
    private ImageView projectImageView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_project_detail);

        // Initialize views
        projectNameTextView = findViewById(R.id.projectNameTextView);
        projectDescriptionTextView = findViewById(R.id.projectDescriptionTextView);
        projectOwnerTextView = findViewById(R.id.projectOwnerTextView);
        projectStartDateTextView = findViewById(R.id.projectStartDateTextView);
        projectEndDateTextView = findViewById(R.id.projectEndDateTextView);
        projectBudgetTextView = findViewById(R.id.projectBudgetTextView);
        projectStatusTextView = findViewById(R.id.projectStatusTextView);
        projectDetailsSubtitle = findViewById(R.id.projectDetailsSubtitle);
        // Initialize the database
        db = Room.databaseBuilder(getApplicationContext(), ApplicationDatabase.class, "freelance").allowMainThreadQueries().build();

        // Get the project ID from the intent
        Intent intent = getIntent();
        int projectId = intent.getIntExtra("PROJECT_ID", -1);

        if (projectId != -1) {
            // Fetch and display project details from the database
            fetchProjectDetails(projectId);
        } else {
            projectDetailsSubtitle.setText("Project not found");
            Toast.makeText(this, "Project ID not found", Toast.LENGTH_SHORT).show();
        }
        // Set OnClickListener for the back button
        AppCompatImageButton backButton = findViewById(R.id.backButton);
        backButton.setOnClickListener(v -> onBackPressed());
    }

    private void fetchProjectDetails(int projectId) {
        Project project = db.projectDao().getProjectById(projectId);

        if (project != null) {
            // Display project details
            projectNameTextView.setText(project.getName());
            projectDescriptionTextView.setText(project.getDescription());
            projectOwnerTextView.setText(project.getProjectOwner());
            projectStartDateTextView.setText(formatDate(project.getStartDate()));
            projectEndDateTextView.setText(formatDate(project.getEndDate()));
            projectStatusTextView.setText(project.getStatus());
            projectBudgetTextView.setText(String.format(Locale.getDefault(), "%.2f", project.getBudget()));
            projectDetailsSubtitle.setText("Project Details");
        }
    }

    private String formatDate(Date date) {
        SimpleDateFormat newFormat = new SimpleDateFormat("dd MMM yyyy", Locale.getDefault());
        return newFormat.format(date);
    }
}
