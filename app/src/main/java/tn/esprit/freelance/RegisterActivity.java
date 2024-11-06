    package tn.esprit.freelance;

    import android.content.Intent;
    import android.graphics.Bitmap;
    import android.net.Uri;
    import android.os.Bundle;
    import android.provider.MediaStore;
    import android.text.TextUtils;
    import android.widget.Button;
    import android.widget.CheckBox;
    import android.widget.EditText;
    import android.widget.ImageView;
    import android.widget.Toast;
    import androidx.appcompat.app.AppCompatActivity;
    import org.mindrot.jbcrypt.BCrypt;

    import java.io.IOException;

    import tn.esprit.freelance.database.ApplicationDatabase;
    import tn.esprit.freelance.entities.User;
    import tn.esprit.freelance.DAO.UserDao;

    public class RegisterActivity extends AppCompatActivity {

        private EditText fullName, email, phoneNumber, password, confirmPassword;
        private Button registerButton,uploadImageButton;
        private CheckBox clientCheckBox, freelancerCheckBox;
        private ApplicationDatabase database;
        private UserDao userDao;
        private Uri imageUri; // Store the image URI
        private ImageView profileImageView;
        private static final int PICK_IMAGE_REQUEST = 1; // Request code for picking an image


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
            uploadImageButton = findViewById(R.id.uploadImageButton);
            profileImageView = findViewById(R.id.profileImageView);

            // Open image chooser when button is clicked
            uploadImageButton.setOnClickListener(v -> openImageChooser());

            // Set the listener for the register button
            registerButton.setOnClickListener(v -> {
                if (validateInputs()) {
                    // Proceed with registration (add the user to the database)
                    registerUser();
                }
            });
        }
        private void openImageChooser() {
            // Open the image gallery
            Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
            intent.setType("image/*");
            startActivityForResult(intent, PICK_IMAGE_REQUEST);
        }
        @Override
        protected void onActivityResult(int requestCode, int resultCode, Intent data) {
            super.onActivityResult(requestCode, resultCode, data);

            if (requestCode == PICK_IMAGE_REQUEST && resultCode == RESULT_OK && data != null && data.getData() != null) {
                imageUri = data.getData(); // Get the image URI
                try {
                    // Set the selected image in ImageView
                    Bitmap bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), imageUri);
                    profileImageView.setImageBitmap(bitmap);
                } catch (IOException e) {
                    e.printStackTrace();
                    Toast.makeText(this, "Failed to load image", Toast.LENGTH_SHORT).show();
                }
            }
        }
        private boolean validateInputs() {
            String fullNameStr = fullName.getText().toString().trim();
            String emailStr = email.getText().toString().trim();
            String phoneStr = phoneNumber.getText().toString().trim();
            String passwordStr = password.getText().toString();
            String confirmPasswordStr = confirmPassword.getText().toString();

            // Full name validation
            if (TextUtils.isEmpty(fullNameStr)) {
                fullName.setError("Full name is required");
                fullName.requestFocus();
                return false;
            }

            // Email validation with regex
            if (TextUtils.isEmpty(emailStr)) {
                email.setError("Email is required");
                email.requestFocus();
                return false;
            } else if (!emailStr.matches("^[\\w-\\.]+@([\\w-]+\\.)+[\\w-]{2,4}$")) {
                email.setError("Invalid email format (expected format: xxx@xxx.xxx)");
                email.requestFocus();
                return false;
            }

            // Phone number validation
            if (TextUtils.isEmpty(phoneStr)) {
                phoneNumber.setError("Phone number is required");
                phoneNumber.requestFocus();
                return false;
            } else if (phoneStr.length() != 8 || !phoneStr.matches("\\d{8}")) {
                phoneNumber.setError("Phone number must be exactly 8 digits");
                phoneNumber.requestFocus();
                return false;
            }

            // Password validation
            if (TextUtils.isEmpty(passwordStr)) {
                password.setError("Password is required");
                password.requestFocus();
                return false;
            }

            // Confirm password validation
            if (TextUtils.isEmpty(confirmPasswordStr)) {
                confirmPassword.setError("Please confirm your password");
                confirmPassword.requestFocus();
                return false;
            } else if (!passwordStr.equals(confirmPasswordStr)) {
                confirmPassword.setError("Passwords do not match");
                confirmPassword.requestFocus();
                return false;
            }

            // Client/Freelancer selection validation
            if (!clientCheckBox.isChecked() && !freelancerCheckBox.isChecked()) {
                Toast.makeText(this, "Please select either Client or Freelancer", Toast.LENGTH_SHORT).show();
                return false;
            } else if (clientCheckBox.isChecked() && freelancerCheckBox.isChecked()) {
                Toast.makeText(this, "Select only one role: Client or Freelancer", Toast.LENGTH_SHORT).show();
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
                role = "USER";
            }
            String hashedPassword = BCrypt.hashpw(passwordStr, BCrypt.gensalt(12));


            // Create a new User object with the required arguments, including role
            User newUser = new User(fullNameStr, emailStr, phoneStr, hashedPassword, hashedPassword, role);

            // Insert the user into the database
            new Thread(() -> {
                userDao.createUser(newUser);

                // Show success message on the main thread
                runOnUiThread(() -> Toast.makeText(RegisterActivity.this, "User registered successfully!", Toast.LENGTH_SHORT).show());
                Intent intent = new Intent(RegisterActivity.this, MainActivity.class);
                startActivity(intent);
                // Redirect to login or another activity if needed
            }).start();
        }

    }
