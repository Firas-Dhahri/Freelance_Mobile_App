package tn.esprit.freelance;

import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Switch;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import tn.esprit.freelance.DAO.UserDao;
import tn.esprit.freelance.database.ApplicationDatabase;
import tn.esprit.freelance.entities.User;

public class UserDetailActivity extends AppCompatActivity {

    private EditText fullName, email, phoneNumber;
    private Spinner roleSpinner;
    private Button updateButton;
    private UserDao userDao;
    private User user;
    private Switch banSwitch;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_detail);

        fullName = findViewById(R.id.fullNameEditText);
        email = findViewById(R.id.emailEditText);
        phoneNumber = findViewById(R.id.phoneEditText);
        roleSpinner = findViewById(R.id.roleSpinner);
        banSwitch = findViewById(R.id.banSwitch);  // New switch for banning/unbanning
        updateButton = findViewById(R.id.updateButton);

        long userId = getIntent().getLongExtra("userId", -1);
        userDao = ApplicationDatabase.getInstance(this).userDao(); // Initialize UserDao

        loadUserDetails(userId);

        // Populate the role spinner
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this,
                R.array.user_roles, R.layout.spinner_item); // Use the custom item layout
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        roleSpinner.setAdapter(adapter);

        updateButton.setOnClickListener(v -> updateUserRole());
        banSwitch.setOnCheckedChangeListener((buttonView, isChecked) -> toggleBanStatus(isChecked));
    }

    private void loadUserDetails(long userId) {
        new Thread(() -> {
            user = userDao.getUserById(userId); // Fetch user details from the database
            runOnUiThread(() -> {
                if (user != null) {
                    fullName.setText(user.getFullName());
                    email.setText(user.getEmail());
                    phoneNumber.setText(user.getPhoneNumber());

                    // Dynamically set the role spinner based on the user's role
                    ArrayAdapter<CharSequence> adapter = (ArrayAdapter<CharSequence>) roleSpinner.getAdapter();
                    int position = adapter.getPosition(user.getRole());
                    if (position >= 0) {
                        roleSpinner.setSelection(position);
                    }

                    // Set the switch based on user ban status
                    banSwitch.setChecked(user.isBanned());
                } else {
                    Toast.makeText(UserDetailActivity.this, "User not found", Toast.LENGTH_SHORT).show();
                }
            });
        }).start();
    }

    private void updateUserRole() {
        // Get selected role from spinner
        String selectedRole = roleSpinner.getSelectedItem().toString();

        // Update user role in the database
        new Thread(() -> {
            user.setRole(selectedRole); // Update user object
            userDao.updateUser(user); // Update user in the database
            runOnUiThread(() -> {
                Toast.makeText(UserDetailActivity.this, "User role updated successfully!", Toast.LENGTH_SHORT).show();
                finish(); // Close the activity
            });
        }).start();
    }

    private void toggleBanStatus(boolean isBanned) {
        new Thread(() -> {
            user.setBanned(isBanned);
            userDao.updateUser(user);
            runOnUiThread(() -> {
                String message = isBanned ? "User banned successfully!" : "User unbanned successfully!";
                Toast.makeText(UserDetailActivity.this, message, Toast.LENGTH_SHORT).show();
            });
        }).start();
    }
}
