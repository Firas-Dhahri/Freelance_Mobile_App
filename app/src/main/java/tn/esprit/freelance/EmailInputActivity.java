package tn.esprit.freelance;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import tn.esprit.freelance.DAO.UserDao;
import tn.esprit.freelance.database.ApplicationDatabase;
import tn.esprit.freelance.entities.User;

public class EmailInputActivity extends AppCompatActivity {

    private EditText emailField;
    private Button sendButton;
    private ApplicationDatabase database;
    private UserDao userDao;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.emailinputactivity);

        emailField = findViewById(R.id.et_email);
        sendButton = findViewById(R.id.btn_submit_email);

        database = ApplicationDatabase.getInstance(this);
        userDao = database.userDao();

        sendButton.setOnClickListener(v -> {
            String email = emailField.getText().toString().trim();
            if (TextUtils.isEmpty(email)) {
                emailField.setError("Email is required");
                return;
            }

            // Verify email and proceed to reset password
            new Thread(() -> {
                User user = userDao.getUserByEmail(email);
                runOnUiThread(() -> {
                    if (user != null) {
                        // Email exists, proceed to the password reset screen
                        Intent intent = new Intent(EmailInputActivity.this, ResetPasswordActivity.class);
                        intent.putExtra("userId", user.getId());
                        startActivity(intent);
                        finish();
                    } else {
                        Toast.makeText(EmailInputActivity.this, "Email not found", Toast.LENGTH_SHORT).show();
                    }
                });
            }).start();
        });
    }
}
