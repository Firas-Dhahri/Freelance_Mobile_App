package tn.esprit.freelance;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import org.mindrot.jbcrypt.BCrypt;

import tn.esprit.freelance.DAO.UserDao;
import tn.esprit.freelance.database.ApplicationDatabase;
import tn.esprit.freelance.entities.User;

public class MainActivity extends AppCompatActivity {

    private EditText usernameField, passwordField;
    private Button loginButton;
    private TextView signupText, forgotPasswordText; // Added forgotPasswordText here
    private ApplicationDatabase database;
    private UserDao userDao;
    private TextView tvForgotPassword; // Variable for Forgot Password TextView

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Initialize UI components
        usernameField = findViewById(R.id.username);
        passwordField = findViewById(R.id.password);
        loginButton = findViewById(R.id.loginButton);
        signupText = findViewById(R.id.signupText);
        tvForgotPassword = findViewById(R.id.forgotPasswordText); // Initialize the new TextView

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

        // Handle click event for forgot password
        tvForgotPassword.setOnClickListener(v -> {
            Intent intent = new Intent(MainActivity.this, EmailInputActivity.class);
            startActivity(intent);
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
    private void authenticateUser() {
        String username = usernameField.getText().toString().trim();
        String password = passwordField.getText().toString().trim();

        new Thread(() -> {
            // Fetch user by username only
            User user = userDao.getUserByEmail(username);

            runOnUiThread(() -> {
                if (user != null) {
                    // Verify password using bcrypt
                    if (BCrypt.checkpw(password, user.getPassword())) {
                        if (user.isBanned()) {
                            Toast.makeText(MainActivity.this, "Your account has been banned. Access denied.", Toast.LENGTH_LONG).show();
                            return;
                        }

                        // Authentication success: check user role
                        Toast.makeText(MainActivity.this, "Login successful!", Toast.LENGTH_SHORT).show();
                        SessionManager sessionManager = new SessionManager(this);
                        sessionManager.saveUser(user.getFullName(), user.getEmail(),user.getRole());
                        // Redirect based on user role
                        Intent intent;
                        if ("ADMIN".equals(user.getRole())) {
                            intent = new Intent(MainActivity.this, AdminUserManagementActivity.class);
                        } else if ("CLIENT".equals(user.getRole())){
                            intent = new Intent(MainActivity.this, ListProjectOwner.class);
                        } else {
                            intent = new Intent(MainActivity.this, AddprojectActivity.class);
                        }
                        startActivity(intent);
                        finish();
                    } else {
                        // Incorrect password
                        Toast.makeText(MainActivity.this, "Invalid username or password", Toast.LENGTH_SHORT).show();
                    }
                } else {
                    // User not found
                    Toast.makeText(MainActivity.this, "Invalid username or password", Toast.LENGTH_SHORT).show();
                }
            });
        }).start();
    }
}