package tn.esprit.freelance;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import tn.esprit.freelance.DAO.UserDao;
import tn.esprit.freelance.database.ApplicationDatabase;
import tn.esprit.freelance.entities.User;

public class MainActivity extends AppCompatActivity {

    private EditText usernameField, passwordField;
    private Button loginButton;
    private TextView signupText;
    private ApplicationDatabase database;
    private UserDao userDao;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Initialize UI components
        usernameField = findViewById(R.id.username);
        passwordField = findViewById(R.id.password);
        loginButton = findViewById(R.id.loginButton);
        signupText = findViewById(R.id.signupText);

        // Initialize Room database and UserDao
        database = ApplicationDatabase.getInstance(this);
        userDao = database.userDao();

        // Redirect to RegisterActivity if "Sign Up" is clicked
        signupText.setOnClickListener(v -> {
            Intent intent = new Intent(MainActivity.this, RegisterActivity.class);
            startActivity(intent);
        });

        // Handle login button click: Authenticate user
        loginButton.setOnClickListener(v -> {
            if (validateInputs()) {
                authenticateUser();
            }
        });


    }

    // Validate the inputs (username and password)
    private boolean validateInputs() {
        String username = usernameField.getText().toString().trim();
        String password = passwordField.getText().toString().trim();

        if (TextUtils.isEmpty(username)) {
            usernameField.setError("Username is required");
            return false;
        }

        if (TextUtils.isEmpty(password)) {
            passwordField.setError("Password is required");
            return false;
        }

        return true;
    }

    // Authenticate the user by checking credentials in the database
    // Authenticate the user by checking credentials in the database
    private void authenticateUser() {
        String username = usernameField.getText().toString().trim();
        String password = passwordField.getText().toString().trim();

        new Thread(() -> {
            // Query the user by username and password
            User user = userDao.getUserByEmailAndPassword(username, password);

            runOnUiThread(() -> {
                if (user != null) {
                    // Check if the user is banned
                    if (user.isBanned()) {
                        // If banned, show a message and prevent access
                        Toast.makeText(MainActivity.this, "Your account has been banned. Access denied.", Toast.LENGTH_LONG).show();
                        return;
                    }

                    // Authentication success: check user role
                    Toast.makeText(MainActivity.this, "Login successful!", Toast.LENGTH_SHORT).show();

                    // Redirect based on user role
                    Intent intent;
                    if ("ADMIN".equals(user.getRole())) {
                        intent = new Intent(MainActivity.this, AdminUserManagementActivity.class);  // Redirect to admin management for admins
                    } else {
                        intent = new Intent(MainActivity.this, DashboardActivity.class);  // Redirect to dashboard for regular users
                    }
                    startActivity(intent);
                    finish();
                } else {
                    // Authentication failed
                    Toast.makeText(MainActivity.this, "Invalid username or password", Toast.LENGTH_SHORT).show();
                }
            });
        }).start();
    }


}
