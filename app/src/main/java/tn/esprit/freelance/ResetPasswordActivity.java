package tn.esprit.freelance;

import android.os.Bundle;
import android.text.TextUtils;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import tn.esprit.freelance.DAO.UserDao;
import tn.esprit.freelance.database.ApplicationDatabase;
import tn.esprit.freelance.entities.User;

public class ResetPasswordActivity extends AppCompatActivity {

    private EditText newPasswordField, confirmPasswordField;
    private Button resetButton;
    private ApplicationDatabase database;
    private UserDao userDao;
    private Long userId; // Change to Long to match database

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.resetpasswordactivity);

        newPasswordField = findViewById(R.id.et_new_password);
        confirmPasswordField = findViewById(R.id.et_confirm_password);
        resetButton = findViewById(R.id.btn_reset_password);

        database = ApplicationDatabase.getInstance(this);
        userDao = database.userDao();

        // Retrieve user ID from intent
        userId = getIntent().getLongExtra("userId", -1);

        resetButton.setOnClickListener(v -> {
            String newPassword = newPasswordField.getText().toString().trim();
            String confirmPassword = confirmPasswordField.getText().toString().trim();

            if (TextUtils.isEmpty(newPassword)) {
                newPasswordField.setError("New password is required");
                return;
            }

            if (TextUtils.isEmpty(confirmPassword)) {
                confirmPasswordField.setError("Confirm password is required");
                return;
            }

            if (!newPassword.equals(confirmPassword)) {
                Toast.makeText(this, "Passwords do not match", Toast.LENGTH_SHORT).show();
                return;
            }

            // Update password in the database
            new Thread(() -> {
                User user = userDao.getUserById(userId);
                if (user != null) {
                    user.setPassword(newPassword); // Consider hashing the password here
                    userDao.updateUser(user); // Update user in the database

                    runOnUiThread(() -> {
                        Toast.makeText(ResetPasswordActivity.this, "Password reset successful", Toast.LENGTH_SHORT).show();
                        finish(); // Close the activity
                    });
                }
            }).start();
        });
    }
}
