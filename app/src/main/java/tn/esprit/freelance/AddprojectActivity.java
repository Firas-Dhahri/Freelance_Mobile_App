package tn.esprit.freelance;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatImageButton;

import tn.esprit.freelance.database.ApplicationDatabase;
import tn.esprit.freelance.entities.Project;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class AddprojectActivity extends AppCompatActivity {

    private EditText projectName;
    private EditText projectDescription;
    private EditText projectStartDate;
    private EditText projectEndDate;
    private EditText projectStatus;
    private EditText projectBudget;
    private Button signupButton;
    private AppCompatImageButton backButton;
    private String fullName;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_addproject);

        // Initialize SessionManager
        SessionManager sessionManager = new SessionManager(this);

        // Get user details from session
        String email = sessionManager.getEmail();
        String role = sessionManager.getUserRole();  // Assuming `getRole()` method exists in `SessionManager`
        fullName = sessionManager.getFullName();
        // Initialize views
        projectName = findViewById(R.id.project_name);
        projectDescription = findViewById(R.id.project_description);
        projectStartDate = findViewById(R.id.project_start_date);
        projectEndDate = findViewById(R.id.project_end_date);
        projectBudget = findViewById(R.id.project_budget);
        signupButton = findViewById(R.id.signup_button);
        backButton = findViewById(R.id.backButton);

        // Set onClick listener for signup button
        signupButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                createProject();
            }
        });

        // Set onClick listener for back button
        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
    }

    private void createProject() {
        String name = projectName.getText().toString().trim();
        String description = projectDescription.getText().toString().trim();
        String startDateString = projectStartDate.getText().toString().trim();
        String endDateString = projectEndDate.getText().toString().trim();
        String status = "En attente";
     // Assuming you have this method
        String budgetString = projectBudget.getText().toString().trim();

        if (name.isEmpty() || description.isEmpty() || startDateString.isEmpty() ||
                endDateString.isEmpty()|| budgetString.isEmpty()) {
            Toast.makeText(this, "Please fill all fields", Toast.LENGTH_SHORT).show();
            return;
        }

        double budget;
        try {
            budget = Double.parseDouble(budgetString);
        } catch (NumberFormatException e) {
            Toast.makeText(this, "Invalid budget format", Toast.LENGTH_SHORT).show();
            return;
        }

        Date startDate = parseDate(startDateString);
        Date endDate = parseDate(endDateString);

        if (startDate == null || endDate == null) {
            Toast.makeText(this, "Invalid date format. Use dd/MM/yyyy", Toast.LENGTH_SHORT).show();
            return;
        }

        if (endDate.before(startDate)) {
            Toast.makeText(this, "End date must be after start date", Toast.LENGTH_SHORT).show();
            return;
        }

        // Create a new Project object
        Project project = new Project(description, name, startDate, endDate, status,fullName , budget);

        // Save the project to Room Database
        saveProjectToDatabase(project);
    }

    private Date parseDate(String dateString) {
        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy", Locale.getDefault());
        try {
            return sdf.parse(dateString);
        } catch (ParseException e) {
            e.printStackTrace();
            return null;
        }
    }

    private void saveProjectToDatabase(Project project) {
        // Execute database operations asynchronously
        new AsyncTask<Project, Void, Void>() {
            @Override
            protected Void doInBackground(Project... projects) {
                ApplicationDatabase db = ApplicationDatabase.getInstance(AddprojectActivity.this);
                db.projectDao().create(projects[0]);
                return null;
            }

            @Override
            protected void onPostExecute(Void aVoid) {
                super.onPostExecute(aVoid);
                Toast.makeText(AddprojectActivity.this, "Project created successfully", Toast.LENGTH_SHORT).show();

                // Redirect to ListProjectActivity
                Intent intent = new Intent(AddprojectActivity.this, ListProject.class);

                // Optionally, you can clear the activity stack to avoid returning to AddprojectActivity
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);

                // Start the ListProjectActivity
                startActivity(intent);

                // Finish AddprojectActivity
                finish();
            }

        }.execute(project);
    }
}
