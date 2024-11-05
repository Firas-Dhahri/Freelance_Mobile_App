package tn.esprit.freelance;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import androidx.appcompat.app.AppCompatActivity;

import java.util.List;

import tn.esprit.freelance.DAO.UserDao;
import tn.esprit.freelance.database.ApplicationDatabase;
import tn.esprit.freelance.entities.User;

public class AdminUserManagementActivity extends AppCompatActivity {

    private ListView userListView;
    private UserDao userDao;
    private List<User> userList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_user_management);

        userListView = findViewById(R.id.userListView);
        userDao = ApplicationDatabase.getInstance(this).userDao(); // Initialize UserDao

        loadUserList();
    }

    private void loadUserList() {
        new Thread(() -> {
            userList = userDao.getAllUsers(); // Fetch all users from the database
            runOnUiThread(() -> {
                UserAdapter adapter = new UserAdapter(AdminUserManagementActivity.this, userList);
                userListView.setAdapter(adapter);
            });
        }).start();

        userListView.setOnItemClickListener((parent, view, position, id) -> {
            User selectedUser = userList.get(position);
            Intent intent = new Intent(AdminUserManagementActivity.this, UserDetailActivity.class);
            intent.putExtra("userId", selectedUser.getId()); // Pass user ID to the details activity
            startActivity(intent);
        });
    }

}
