package tn.esprit.freelance;

import android.os.Bundle;
import android.text.TextUtils;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import tn.esprit.freelance.database.ApplicationDatabase;
import tn.esprit.freelance.entities.User;
import tn.esprit.freelance.DAO.UserDao;

public class RegisterActivity extends AppCompatActivity {

    private EditText fullName, email, phoneNumber, password, confirmPassword;
    private Button registerButton;
    private CheckBox clientCheckBox, freelancerCheckBox;
    private ApplicationDatabase database;
    private UserDao userDao;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        // Initialize the database and DAO
        database = ApplicationDatabase.getInstance(this);
        userDao = database.userDao();

        // Find views by ID
        fullName = findViewById(R.id.fullName);
        email = findViewById(R.id.email);
        phoneNumber = findViewById(R.id.phoneNumber);
        password = findViewById(R.id.password);
        confirmPassword = findViewById(R.id.confirmPassword);
        registerButton = findViewById(R.id.registerButton);
        clientCheckBox = findViewById(R.id.checkbox_client);
        freelancerCheckBox = findViewById(R.id.checkbox_freelancer);

        registerButton.setOnClickListener(v -> {
            if (validateInputs()) {
                // Proceed with registration (add the user to the database)
                registerUser();
            }
        });
    }

    private boolean validateInputs() {
        String fullNameStr = fullName.getText().toString().trim();
        String emailStr = email.getText().toString().trim();
        String phoneStr = phoneNumber.getText().toString().trim();
        String passwordStr = password.getText().toString();
        String confirmPasswordStr = confirmPassword.getText().toString();

        if (TextUtils.isEmpty(fullNameStr)) {
            fullName.setError("Full name is required");
            fullName.requestFocus();
            return false;
        }

        if (TextUtils.isEmpty(emailStr)) {
            email.setError("Email is required");
            email.requestFocus();
            return false;
        }

        if (TextUtils.isEmpty(phoneStr)) {
            phoneNumber.setError("Phone number is required");
            phoneNumber.requestFocus();
            return false;
        } else if (phoneStr.length() != 8) {
            phoneNumber.setError("Phone number must be 8 digits");
            phoneNumber.requestFocus();
            return false;
        }

        if (TextUtils.isEmpty(passwordStr)) {
            password.setError("Password is required");
            password.requestFocus();
            return false;
        }

        if (TextUtils.isEmpty(confirmPasswordStr)) {
            confirmPassword.setError("Please confirm your password");
            confirmPassword.requestFocus();
            return false;
        }

        if (!passwordStr.equals(confirmPasswordStr)) {
            confirmPassword.setError("Passwords do not match");
            confirmPassword.requestFocus();
            return false;
        }

        if (!clientCheckBox.isChecked() && !freelancerCheckBox.isChecked()) {
            Toast.makeText(this, "Please select either Client or Freelancer", Toast.LENGTH_SHORT).show();
            return false;
        }

        return true;
    }

    private void registerUser() {
        // Get the input data
        String fullNameStr = fullName.getText().toString().trim();
        String emailStr = email.getText().toString().trim();
        String phoneStr = phoneNumber.getText().toString().trim();
        String passwordStr = password.getText().toString();
        boolean isClient = clientCheckBox.isChecked();
        boolean isFreelancer = freelancerCheckBox.isChecked();

        // Determine the role based on checkboxes
        String role;
        if (isClient) {
            role = "CLIENT";
        } else if (isFreelancer) {
            role = "FREELANCER";
        } else {
            role = "USER"; // Default role if neither is selected, adjust as needed
        }

        // Create a new User object with the required arguments, including role
        User newUser = new User(fullNameStr, emailStr, phoneStr, passwordStr, passwordStr, role);

        // Insert the user into the database
        new Thread(() -> {
            userDao.createUser(newUser);

            // Show success message on the main thread
            runOnUiThread(() -> Toast.makeText(RegisterActivity.this, "User registered successfully!", Toast.LENGTH_SHORT).show());

            // Redirect to login or another activity if needed
        }).start();
    }

}
